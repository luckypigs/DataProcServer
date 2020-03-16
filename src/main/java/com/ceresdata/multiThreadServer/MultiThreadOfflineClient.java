package com.ceresdata.multiThreadServer;

import com.ceresdata.pojo.LogInfo;
import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.pojo.UserInfo;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.tools.Trans;
import com.ceresdata.util.PcapFileUtil;
import com.ceresdata.util.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by hsk on 2020/3/11
 */
public class MultiThreadOfflineClient  implements Runnable{
    private DataProcessServer dataProcessServer;
    private ServerConfig serverConfig;
    private PcapDataService service;

    private String descDir;
    private String srcDir;
    private String R_DIR;
    private int type=0;
    private int position;
    private static Map<Short,Long> id=new HashMap<>();
    // 是否可以接收数据
    private boolean isRunning = false;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss"); //设置格式 数据库的

    static long countByte=0;//已读字节数
    static int countPa=0;//已读包数
    private int readFiles;


    @Override
    public void run() {
        this.isRunning = true;
        int curPt=0;//假设一个pcap文件没有保存完整数据
        int fileHeadLen=24;
        long now;
        int PackageLen;


        try {
            File fileRoot = new File(srcDir);
            File files[]=fileRoot.listFiles();
            for (int j=readFiles;j<files.length;j++) {
                File file=files[j];
                if(isRunning) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    readFiles++;
                    int headLen;
                    if (type == 0) {
                        headLen = 24;
                    } else {
                        headLen = 32;
                    }
                    byte[] fileHead = new byte[fileHeadLen];
                    fileInputStream.read(fileHead);
                    countByte+=fileHeadLen;

                    byte[] data = new byte[headLen];
                    int length;
                    length = fileInputStream.read(data);
                    countByte+=length;
                    do {
                        now = System.currentTimeMillis()/1000;
                        PcapData pcapdata = getPcapData(data, type);
                        PackageLen = pcapdata.getLength();
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUser_id(pcapdata.getUserId());
                        userInfo.setPosition(position);
                        //初始化
                        PcapFileUtil pcapFileUtil = new PcapFileUtil();
                        pcapFileUtil.setFileMaxMinute(serverConfig.getFileMaxMinute());
                        pcapFileUtil.setFileMaxSize(serverConfig.getFileMaxSize());

                        long datatime = 0;
                        if (service.getFilePath(pcapdata.getUserId()) == null) {
                            id.put(pcapdata.getUserId(), now);
                            service.save_userInfo(userInfo);
                        }else if(service.getFilePath(pcapdata.getUserId()).length()!=0){
                            String prePath=service.getFilePath(pcapdata.getUserId());
                            String []sp=prePath.split("_");
                            id.put(pcapdata.getUserId(),format.parse(sp[sp.length-2]).getTime());
                        }
                        if ( id.get(pcapdata.getUserId())==null||now - id.get(pcapdata.getUserId()) >= (pcapFileUtil.getFileMaxMinute() * 60 )) {
                            id.put(pcapdata.getUserId(), now);
                        }
                        if (type == 0) {
                            String pathname = this.descDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                    "_" + format.format(id.get(pcapdata.getUserId())) + "_f.pcap";
                            pcapdata.setPath(pathname);
                            userInfo.setFile_path(pathname);
                        } else {
                            String pathname = this.descDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                    "_" + format.format(id.get(pcapdata.getUserId())) + "_r.pcap";
                            pcapdata.setPath(pathname);
                            userInfo.setFile_path(pathname);
                        }
                        // 保存到 pcap 文件中 (根据规则，如果文件超过 30 M 或 时间大于30分钟，重新生成新文件)
                        for (int i = 0; i * 20000 < PackageLen - headLen; i++) {
                            int len = Math.min(20000, PackageLen - headLen - i * 20000);
                            byte[] dt = new byte[len];
                            fileInputStream.read(dt);
							countByte += len;
                            datatime = pcapFileUtil.writeFile(pcapdata.getPath(), dt);
                        }


                        if (datatime != id.get(pcapdata.getUserId())) {
                            id.put(pcapdata.getUserId(), datatime);
                            //修改userinfo表
                            if (type == 0) {
                                String pathname = this.descDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                        "_" + format.format(id.get(pcapdata.getUserId())) + "_f.pcap";
                                userInfo.setFile_path(pathname);
                            } else {
                                String pathname = this.descDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                        "_" + format.format(id.get(pcapdata.getUserId())) + "_r.pcap";
                                userInfo.setFile_path(pathname);
                            }
                        }

                        // 存放到数据库中
                        service.save(pcapdata);
                        service.update_userInfo(userInfo);
                        countPa++;

                        length = fileInputStream.read(data);
						countByte += length;
						serverConfig.setReadFiles(readFiles);
						dataProcessServer.saveToFile();
						


                    } while (length > 0);
                    fileInputStream.close();
                }

            }


        }catch (Exception e){

        }
    }
    public MultiThreadOfflineClient(DataProcessServer dataProcessServer){
        this.dataProcessServer=dataProcessServer;
        this.serverConfig=this.dataProcessServer.getConfig();
        this.service=dataProcessServer.getPcapDataService();
        descDir=serverConfig.getDescRootDir();
        srcDir=serverConfig.getSrcRootDir();
        position=serverConfig.getPosition();
        R_DIR=serverConfig.getR_dir();

        if(srcDir.equals(R_DIR)){
            type=0;
        }else{
            type=1;
        }
    }
    public PcapData getPcapData(byte[]arrMsgBuff, int type){
        PcapData pcapData=new PcapData();
        byte[]head =new byte[4];
        System.arraycopy(arrMsgBuff, 0, head, 0, 4);
        pcapData.setHeader(new String(head));
        byte[] pos = new byte[2];
        System.arraycopy(arrMsgBuff, 4, pos, 0, 2);
        pcapData.setSatPos(Trans.byte2short(pos));
        byte[] fre = new byte[4];
        System.arraycopy(arrMsgBuff, 6, fre, 0, 4);
        pcapData.setFrequence(Trans.byte2int(fre));
        byte[]s_PackageLen =new byte[2];
        System.arraycopy(arrMsgBuff, 12, s_PackageLen, 0, 2);
        short PackageLen=Trans.byte2short(s_PackageLen);
        pcapData.setLength(PackageLen);
        if (type == 0) {
            pcapData.setModeCode(String.valueOf(arrMsgBuff[10]));
            pcapData.setCheck(String.valueOf(arrMsgBuff[11]));

            byte[] time = new byte[8];
            System.arraycopy(arrMsgBuff, 14, time, 0, 8);
            pcapData.setTime0(Trans.bytes2Long(time));

            byte[] id = new byte[2];
            System.arraycopy(arrMsgBuff, 22, id, 0, 2);
            pcapData.setUserId(Trans.byte2short(id));

        }else{
            pcapData.setModeCode(String.valueOf(arrMsgBuff[10]));
            byte[] time0 = new byte[8];
            byte[] time1 = new byte[8];
            System.arraycopy(arrMsgBuff, 14, time0, 0, 8);
            System.arraycopy(arrMsgBuff, 22, time1, 0, 8);

            pcapData.setTime0(Trans.bytes2Long(time0));
            pcapData.setTime1(Trans.bytes2Long(time1));

            byte[] id = new byte[2];
            System.arraycopy(arrMsgBuff, 30, id, 0, 2);
            pcapData.setUserId(Trans.byte2short(id));

        }
        pcapData.setReveType(type);
        return pcapData;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public static ResultMsg getLog(){
        List<LogInfo> res= new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
        String timeText = format.format(System.currentTimeMillis());
        LogInfo logInfo=new LogInfo(timeText,countByte,countPa);
        res.add(logInfo);
        return new ResultMsg(res);
    }
}
