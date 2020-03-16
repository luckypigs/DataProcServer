package com.ceresdata.multiThreadServer;

import com.ceresdata.pojo.LogInfo;
import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.pojo.UserInfo;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.tools.Trans;
import com.ceresdata.util.PcapFileUtil;
import com.ceresdata.util.ResultMsg;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
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

    private int R_PORT;
    private String ip;
    private int port;
    private Socket socket;
    private String rootDir;// 存放的根目录
    private int position;

    // 是否可以接收数据
    private boolean isRunning = false;
    // socket 退出标记
    private boolean exit = false;
    //dao
    private PcapDataService service;
    //记录端口类型
    private int type;
    //记录时间
    long now;
    private static Map<Short,Long> id=new HashMap<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss"); //设置格式 数据库的


    public MultiThreadSocketClient(String ip,int port ,Socket socket,DataProcessServer dataProcessServer){
        this.ip = ip;
        this.port = port;
        this.socket = socket;
        this.dataProcessServer=dataProcessServer;
        this.serverConfig=this.dataProcessServer.getConfig();
        this.service=dataProcessServer.getPcapDataService();
        R_PORT=serverConfig.getR_port();
        rootDir=serverConfig.getFilePath();
        position=serverConfig.getPosition();

        if(port == R_PORT){
            type=0;
        }else{
            type=1;
        }
    }

    public void run(){
        this.isRunning = true;
        int PackageLen = 0;//一条message（即帧头+pcap一行数据的头+内容）的全长
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        ) {

            while (!exit) {
                // 开始接收文件
                int headLen;
                if(type==0){
                    headLen=24;//tcp缓存
                }else{
                    headLen=32;
                }
                byte[]data=new byte[headLen];
                int length = -1;
                // 读取数据到内存中
                length = inputStream.read(data);
                countByte+=length;
                if(length==headLen) {
                    if (data[0] == 0x04 && data[1] == (byte) 0xCF && data[2] == 0x5F && data[3] == (byte) 0xFF) {
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
                            String pathname = this.rootDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                    "_" + format.format(id.get(pcapdata.getUserId())) + "_f.pcap";
                            pcapdata.setPath(pathname);
                            userInfo.setFile_path(pathname);
                        } else {
                            String pathname = this.rootDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                    "_" + format.format(id.get(pcapdata.getUserId())) + "_r.pcap";
                            pcapdata.setPath(pathname);
                            userInfo.setFile_path(pathname);
                        }
                        // 保存到 pcap 文件中 (根据规则，如果文件超过 30 M 或 时间大于30分钟，重新生成新文件)
                        for (int i = 0; i * 20000 < PackageLen - headLen; i++) {
                            int len = Math.min(20000, PackageLen - headLen - i * 20000);
                            byte[] dt = new byte[len];
                            inputStream.read(dt);
                            countByte += len;
                            datatime = pcapFileUtil.writeFile(pcapdata.getPath(), dt);
                        }


                        if (datatime != id.get(pcapdata.getUserId())) {
                            id.put(pcapdata.getUserId(), datatime);
                            //修改userinfo表
                            if (type == 0) {
                                String pathname = this.rootDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                        "_" + format.format(id.get(pcapdata.getUserId())) + "_f.pcap";
                                userInfo.setFile_path(pathname);
                            } else {
                                String pathname = this.rootDir + File.separator + "" + pcapdata.getUserId() + "_" + position +
                                        "_" + format.format(id.get(pcapdata.getUserId())) + "_r.pcap";
                                userInfo.setFile_path(pathname);
                            }
                        }

                        // 存放到数据库中
                        service.save(pcapdata);
                        service.update_userInfo(userInfo);
                        countPa++;
                    }else{
                        System.out.println("error");
                    }


                }else{
                    //没有读到完整帧头
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public long getLastTime(int user_id){
        String path=service.getFilePath(user_id);
        String []split=path.split("_");
        return Long.valueOf(split[split.length-2]);
    }

    public PcapData getPcapData(byte[]arrMsgBuff,int type){
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

    /**
     * 停止接收
     */
    public void exit(){
        this.exit = true;
        this.isRunning=false;
        this.close();
    }
    /**
     * 关闭socket 连接
     */
    private void close(){
        try {
            this.socket.shutdownInput();
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
