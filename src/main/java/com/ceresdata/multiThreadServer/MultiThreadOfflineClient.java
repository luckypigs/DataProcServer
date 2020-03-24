package com.ceresdata.multiThreadServer;

import com.ceresdata.pojo.LogInfo;
import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.tools.Trans;
import com.ceresdata.util.ParseUtil;
import com.ceresdata.util.PcapFileUtil;
import com.ceresdata.util.ResultMsg;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * created by hsk on 2020/3/11
 */
public class MultiThreadOfflineClient  implements Runnable{
    private DataProcessServer dataProcessServer;
    private ServerConfig serverConfig;
    private PcapDataService service;

    private String descDir;
    private String srcDir;
    private String backupDir;
    private int type=0;
    private int position;
    private static Map<Short,Long> id=new HashMap<>();
    // 是否可以接收数据
    private boolean isRunning = false;

    static long countByte=0;//已读字节数
    static int countPa=0;//已读包数

    public MultiThreadOfflineClient(DataProcessServer dataProcessServer){
        this.dataProcessServer=dataProcessServer;
        this.serverConfig=this.dataProcessServer.getConfig();
        this.service=dataProcessServer.getPcapDataService();
        descDir=serverConfig.getDescRootDir();
        srcDir=serverConfig.getSrcRootDir();
        backupDir=srcDir+File.separator+"backup";
        position=serverConfig.getPosition();
    }

    @Override
    public void run() {
        this.isRunning = true;
        int fileHeadLen=24;
        int PackageLen;

        try {
            File fileRoot = new File(srcDir);
            checkBackupFile(backupDir);
            File files[]=fileRoot.listFiles();
            for (File file:files) {
                File dest=new File(backupDir+File.separator+file.getName());
                if(isRunning) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    int headLen;
                    byte[] fileHead = new byte[fileHeadLen];
                    fileInputStream.read(fileHead);
                    countByte+=fileHeadLen;

                    byte[]head=new byte[34];
                    int length;
                    length = fileInputStream.read(head,0,12);
                    countByte+=length;
                    do {
                        if(ParseUtil.isRType(head)){
                            type=0;
                            headLen=26;
                        }else {
                            type=1;
                            headLen=34;
                        }
                        length=fileInputStream.read(head,12,headLen-12);
                        countByte+=length;

                        long now = System.currentTimeMillis();
                        PcapData pcapdata = ParseUtil.createPcapData(head, type);
                        PackageLen = pcapdata.getLength();
						pcapdata.setPosition(position);
                        //初始化
                        PcapFileUtil pcapFileUtil = createPcapFileUtil();

                        long datatime = 0;
                        update_IdMap(pcapdata,now);

                        String pathname=getPathname(pcapdata);
                        pcapdata.setPath(pathname);
                        service.save(pcapdata);

                        // 保存到 pcap 文件中 (根据规则，如果文件超过 30 M 或 时间大于30分钟，重新生成新文件)
                        for (int i = 0; i * 20000 < PackageLen - headLen; i++) {
                            int len = Math.min(20000, PackageLen - headLen - i * 20000);
                            byte[] dt = new byte[len];
                            int r=fileInputStream.read(dt);
							countByte += r;
                            datatime = pcapFileUtil.writeFile(pcapdata.getPath(), dt,0,r,id.get(pcapdata.getUserId()));
                        }

                        if (datatime != id.get(pcapdata.getUserId())) {
                            id.put(pcapdata.getUserId(), datatime);
                            //修改userinfo表
                            pathname=getPathname(pcapdata);
                            pcapdata.setPath(pathname);
                        }

                        // 存放到数据库中
                        service.update_userInfo(pcapdata);
                        countPa++;
                        length = fileInputStream.read(head,0,12);
						countByte += length;

                    } while (length > 0);
                    fileInputStream.close();
                    file.renameTo(dest);
                }

            }


        }catch (Exception e){

        }
    }

    public PcapFileUtil createPcapFileUtil(){
        PcapFileUtil pcapFileUtil = new PcapFileUtil();
        pcapFileUtil.setFileMaxMinute(serverConfig.getFileMaxMinute());
        pcapFileUtil.setFileMaxSize(serverConfig.getFileMaxSize());
        return pcapFileUtil;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public static long getCountByte() {
        return countByte;
    }

    public static void setCountByte(long countByte) {
        MultiThreadOfflineClient.countByte = countByte;
    }

    public static int getCountPa() {
        return countPa;
    }

    public static void setCountPa(int countPa) {
        MultiThreadOfflineClient.countPa = countPa;
    }

    public static ResultMsg getLog(){
        List<LogInfo> res= new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
        String timeText = format.format(System.currentTimeMillis());
        LogInfo logInfo=new LogInfo(timeText,countByte,countPa);
        res.add(logInfo);
        return new ResultMsg(res);
    }

    /**
     * 更新idMap
     */
    public void update_IdMap(PcapData pcapdata,long now) throws ParseException {
        String prePath=service.getFilePath(pcapdata);
        if (prePath == null) {
            id.put(pcapdata.getUserId(), now);
            service.save_userInfo(pcapdata,now);
        }else if(prePath.length()!=0){
            String []sp=prePath.split("_");
            id.put(pcapdata.getUserId(),Trans.dateToStamp(sp[sp.length-2]));
        }
        if ( id.get(pcapdata.getUserId())==null||now - id.get(pcapdata.getUserId()) >= (serverConfig.getFileMaxMinute() * 60 * 1000 )) {
            id.put(pcapdata.getUserId(), now);
        }
    }

    public String getPathname(PcapData pcapData){
        String path=this.descDir + File.separator + "" + pcapData.getUserId() + "_" + position +
                "_" + Trans.stampToDate(id.get(pcapData.getUserId()));
        if(type==0){
            path=path+"_r.pcap";
        }else {
            path=path+"_f.pcap";
        }
        return path;
    }

    public boolean checkBackupFile(String dir){
        File file=new File(dir);
        if(!file.exists()){
            return file.mkdirs();
        }
        return true;
    }
}
