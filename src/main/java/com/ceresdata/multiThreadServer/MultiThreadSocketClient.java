package com.ceresdata.multiThreadServer;

import com.ceresdata.config.DataProcessConfig;
import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.pojo.UserInfo;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.service.impl.PcapDataServiceImpl;
import com.ceresdata.tools.Trans;
import com.ceresdata.util.PcapFileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MultiThreadSocketClient implements Runnable{
    @Autowired
    private DataProcessConfig dataProcessConfig;
    @Autowired
    private ServerConfig serverConfig;

    private final int R_PORT=serverConfig.getR_port();
    private String ip;
    private int port;
    private Socket socket=null;
    private String rootDir=serverConfig.getDescRootDir();// 存放的根目录
    private int position=serverConfig.getPosition();

    // 是否可以接收数据
    private boolean isRunning = false;
    // socket 退出标记
    private boolean exit = false;
    //dao
    @Autowired
    private PcapDataService service;
    //记录端口类型
    private int type=0;
    //记录时间
    long now;
    private static Map<Short,Long> id=new HashMap<>();


    public MultiThreadSocketClient(String ip,int port ,Socket socket){
        this.ip = ip;
        this.port = port;
        this.socket = socket;
        if(port == R_PORT){
            type=1;
        }else{
            type=0;
        }
    }

    public void run(){
        this.isRunning = true;
        int CurrPt = 0;
        int searchDataState = 0; //记录解析到数据了吗
        byte[] Head = new byte[4];
        int HeadPtr = 0;
        int dwFrameLen = 0;
        byte[] arrMsgBuff = new byte[20000];//一条message的缓存
        short PackageLen = 0;//一条message（即帧头+pcap一行数据的头+内容）的全长
        boolean haveSearchCompleteMessage=true;
        int searchState = -1;//记录解析完一条数据了吗
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());


        ) {
            now = System.currentTimeMillis();
            while (!exit) {
                // 开始接收文件
                byte[] data = new byte[20000];//tcp缓存
                int length = -1;
                // 读取数据到内存中
                length = inputStream.read(data);
                if(length>0) {
                    do {
                        searchState=0;
                        for (byte ch = 0; CurrPt < length; CurrPt++) {
                            ch = data[CurrPt];
                            switch (searchDataState) {
                                case 0: {
                                    Head[HeadPtr++] = ch;
                                    if (HeadPtr == 4) {
                                        arrMsgBuff[0] = data[0];
                                        arrMsgBuff[1] = data[1];
                                        arrMsgBuff[2] = data[2];
                                        arrMsgBuff[3] = data[3];
                                        searchDataState = 1;
                                        HeadPtr = 0;//包头完整读取，置0
                                        //检索header
                                        if (Head[0] == 0x04 && Head[1] == (byte) 0xCF && Head[2] == 0x5F && Head[3] == (byte) 0xFF) {
                                            dwFrameLen = 4;
                                            if (CurrPt==length-1){
                                                searchDataState=1;
                                                haveSearchCompleteMessage=false;
                                            }
                                        }

                                    }else if (HeadPtr<4&&CurrPt==length-1){
                                        //包头没有读完整，包头被分割到了两次tcp包
                                        searchDataState=0; //下一个包来时继续向Head中的第HeadPtr个元素追加
                                        haveSearchCompleteMessage=false;
                                    }
                                }
                                break;
                                case 1: {//1:已找到头，寻找剩余部分
                                    arrMsgBuff[dwFrameLen++] = ch;
                                    if (dwFrameLen<=13&&CurrPt==length-1){
                                        //arrMsgBuff[12]和arrMsgBuff[13]为单条message的长度，单条报文将两个字节分割
                                        //或还没读到12、13字节处
                                        haveSearchCompleteMessage=false;
                                        searchDataState = 1;
                                    }
                                    else if (dwFrameLen == 14) {
                                        byte[] s_PackageLen = new byte[]{arrMsgBuff[12], arrMsgBuff[13]};
                                        PackageLen = Trans.byte2short(s_PackageLen);//得到单挑message的全长
                                    }
                                    else if (dwFrameLen>14){
                                        if (dwFrameLen==PackageLen&&CurrPt==length-1){
                                            //仅包含完整的一帧数据
                                            searchState = 2;
                                            searchDataState = 0;

                                        }
                                        else if (dwFrameLen<PackageLen) {
                                            //一条message未读完整，for循环继续
                                            searchDataState = 1;
                                        }
                                        else if(dwFrameLen==PackageLen&&CurrPt<length-1){
                                            //一条数据接收完整，但还有数据，先跳出for循环处理此条message，更改do-while循环标识searchState=1
                                            searchState = 1;
                                            searchDataState = 0;
                                        }
                                        else if (dwFrameLen<PackageLen&&CurrPt==length-1){
                                            ////一条数据未接收完整
                                            haveSearchCompleteMessage=false;
                                        }
                                    }
                                }
                                break;
                                default:
                                    break;
                            }
                            if (searchState == 1 && searchDataState == 0) {
                                //一条数据接收完整，但还有数据，先跳出for循环处理此条message，更改do-while循环标识searchState=1
                                CurrPt++;
                                dwFrameLen=0;
                                break;
                            }else if (searchState==2&&searchDataState==0){
                                //仅包含完整的一帧数据
                                CurrPt=0;
                                dwFrameLen=0;
                                searchState=0;
                                break;
                            }else if (!haveSearchCompleteMessage){
                                //一条数据未接收完整
                                CurrPt=0;
                                //一条data不完整就跳出for循环
                                //判断是否一条数据没接收完整，更新do_while循环判断依据searchState的值
                                searchState = -1;//读空了缓存区数据
                                break;
                            }
                        }

                        if (searchState == -1){
                            haveSearchCompleteMessage=true;
                            break;
                        }

                        PcapData pcapdata = getPcapData(arrMsgBuff,type);
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUser_id(pcapdata.getUserId());
                        userInfo.setPosition(position);
                        byte[] dt =null;
                        if (type == 1) {
                            dt = new byte[PackageLen - 24];
                            System.arraycopy(arrMsgBuff, 24, dt, 0, PackageLen - 24);
                        }else{
                            dt = new byte[PackageLen - 32];
                            System.arraycopy(arrMsgBuff, 32, dt, 0, PackageLen - 32);
                        }
                        // 保存到 pcap 文件中 (根据规则，如果文件超过 30 M 或 时间大于30分钟，重新生成新文件)
                        PcapFileUtil pcapFileUtil = new PcapFileUtil();
                        if(id.get(pcapdata.getUserId())==null||now-id.get(pcapdata.getUserId())>=pcapFileUtil.getFileMaxMinute()){
                            id.put(pcapdata.getUserId(),now);
                        }
                        if(type==0){
                            String pathname=this.rootDir+File.separator+""+pcapdata.getUserId()+"_"+id.get(pcapdata.getUserId())+"_f.pcap";
                            pcapdata.setPath(pathname);
                            userInfo.setFile_path(pathname);
                        }else{
                            String pathname=this.rootDir+File.separator+""+pcapdata.getUserId()+"_"+id.get(pcapdata.getUserId())+"_r.pcap";
                            pcapdata.setPath(pathname);
                            userInfo.setFile_path(pathname);
                        }

                        long datatime = pcapFileUtil.writeFile(pcapdata.getPath(),dt);
                        if(datatime!=id.get(pcapdata.getUserId())){
                            id.put(pcapdata.getUserId(),datatime);
                            //修改userinfo表
                        }

                        // 存放到数据库中
                        service.save(pcapdata);
                        service.save_userInfo(userInfo);


                        arrMsgBuff = new byte[20000];
                        Head=new byte[4];
                    } while (searchState == 1);
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        byte[] dt =null;
        if (type == 1) {
            pcapData.setModeCode(String.valueOf(arrMsgBuff[10]));
            pcapData.setCheck(String.valueOf(arrMsgBuff[11]));

            byte[] time = new byte[8];
            System.arraycopy(arrMsgBuff, 14, time, 0, 8);
            pcapData.setTime0(Trans.bytes2Long(time));

            byte[] id = new byte[2];
            System.arraycopy(arrMsgBuff, 22, id, 0, 2);
            pcapData.setUserId(Trans.byte2short(id));

            dt = new byte[PackageLen - 24];
            System.arraycopy(arrMsgBuff, 24, dt, 0, PackageLen - 24);

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

            dt = new byte[PackageLen - 32];
            System.arraycopy(arrMsgBuff, 32, dt, 0, PackageLen - 32);

        }
        pcapData.setReveType(type);
        return pcapData;
    }

    /**
     * 停止接收
     */
    public void exit(){
        this.exit = true;
        this.close();
    }
    /**
     * 关闭socket 连接
     */
    private void close(){
        try {
            this.socket.close();
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

    /**
     * 验证是否当前线程已经启动
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }
}
