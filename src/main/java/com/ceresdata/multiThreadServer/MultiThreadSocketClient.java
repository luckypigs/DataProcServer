package com.ceresdata.multiThreadServer;

import com.ceresdata.pojo.LogInfo;
import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.tools.Trans;
import com.ceresdata.util.ParseUtil;
import com.ceresdata.util.PcapFileUtil;
import com.ceresdata.util.ResultMsg;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiThreadSocketClient implements Runnable{

    private DataProcessServer dataProcessServer;
    private ServerConfig serverConfig;
    static long countByte=0;//已读字节数
    static int countPa=0;//已读包数

    private String ip;
    private int port;
    private String rootDir;// 存放的根目录
    private int position;
    private ZMQ.Context context;
    private ZMQ.Socket socket;

    // 是否可以接收数据
    private boolean isRunning = false;
    // socket 退出标记
    private boolean exit = false;
    //dao
    private PcapDataService service;
    //记录端口类型
    private int type;
    private static Map<Short,Long> id=new HashMap<>();


    public MultiThreadSocketClient(String ip,int port ,DataProcessServer dataProcessServer){
        this.ip = ip;
        this.port = port;
        this.dataProcessServer=dataProcessServer;
        this.serverConfig=this.dataProcessServer.getConfig();
        this.service=dataProcessServer.getPcapDataService();
        rootDir=serverConfig.getFilePath();
        position=serverConfig.getPosition();

    }

    public void run(){
        this.isRunning = true;
        this.context=ZMQ.context(1);
        this.socket=context.socket(SocketType.SUB);
        socket.connect("tcp://"+ip+":"+port);
        byte[]filer=new byte[]{0x04,(byte) 0xCF ,0x5F ,(byte) 0xFF};
        socket.subscribe(filer);
        try  {
            while (!exit) {
                // 开始接收文件
                int headLen;
                byte[]head= socket.recv(0);
                int length = head.length;
                // 读取数据到内存中

                if(length>0) {
                    countByte += length;
                    if (ParseUtil.isRType(head[10])) {
                        type = 0;
                        headLen = 26;
                    } else {
                        type = 1;
                        headLen = 34;
                    }

                    long now = System.currentTimeMillis();
                    PcapData pcapdata = ParseUtil.createPcapData(head, type);
                    pcapdata.setPosition(position);
                    //初始化
                    PcapFileUtil pcapFileUtil = createPcapFileUtil();

                    long datatime = 0;
                    update_IdMap(pcapdata, now);

                    String pathname = getPathname(pcapdata);
                    pcapdata.setPath(pathname);
                    pcapdata.setDatetime(now);
                    service.save(pcapdata);

                    // 保存到 pcap 文件中 (根据规则，如果文件超过 30 M 或 时间大于30分钟，重新生成新文件)
                    datatime = pcapFileUtil.writeFile(pcapdata.getPath(), head, headLen,length-headLen, id.get(pcapdata.getUserId()));

                    if (datatime != id.get(pcapdata.getUserId())) {
                        id.put(pcapdata.getUserId(), datatime);
                        //修改userinfo表
                        pathname = getPathname(pcapdata);
                        pcapdata.setPath(pathname);
                    }

                    // 存放到数据库中

                    service.update_userInfo(pcapdata);

                    countPa++;


                }else{
                    //没有数据了
                    break;
                }
            }
            socket.close();
            context.term();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public PcapFileUtil createPcapFileUtil(){
        PcapFileUtil pcapFileUtil = new PcapFileUtil();
        pcapFileUtil.setFileMaxMinute(serverConfig.getFileMaxMinute());
        pcapFileUtil.setFileMaxSize(serverConfig.getFileMaxSize());
        return pcapFileUtil;
    }

    /**
     * 停止接收
     */
    public void exit(){
        this.exit = true;
        this.isRunning=false;
        //this.close();
    }
    /**
     * 关闭socket 连接
     */
    private void close(){
        this.socket.close();
        this.context.term();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static long getCountByte() {
        return countByte;
    }

    public static void setCountByte(long countByte) {
        MultiThreadSocketClient.countByte = countByte;
    }

    public static int getCountPa() {
        return countPa;
    }

    public static void setCountPa(int countPa) {
        MultiThreadSocketClient.countPa = countPa;
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
     * 验证是否当前线程已经启动
     * @return
     */
    public boolean isRunning() {
        return isRunning;
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
        if ( id.get(pcapdata.getUserId())==null||now - id.get(pcapdata.getUserId()) >= (serverConfig.getFileMaxMinute() * 60 * 1000)) {
            id.put(pcapdata.getUserId(), now);
        }
    }

    public String getPathname(PcapData pcapData){
        String path=this.rootDir + File.separator + "" + pcapData.getUserId() + "_" + position +
                "_" + Trans.stampToDate(id.get(pcapData.getUserId()));
        if(type==0){
            path=path+"_r.pcap";
        }else {
            path=path+"_f.pcap";
        }
        return path;
    }
}
