package com.ceresdata.multiThreadServer;

import com.ceresdata.pojo.Formart1;
import com.ceresdata.service.impl.Formart1ServiceImpl;
import com.ceresdata.tools.Trans;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;


public class MultiThreadServer implements Runnable {

    Socket csocket;
    private boolean isOff = true;
    public static Formart1ServiceImpl formart1Service;
    MultiThreadServer(Socket csocket) {
        this.csocket = csocket;
    }


    //Formart1ServiceImpl formart1Service=new Formart1ServiceImpl();



    public static void StartServer() throws Exception {
        ServerSocket serverSocket = new ServerSocket(6664);
        System.out.println("Listening");
        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Connected");
            new Thread(new MultiThreadServer(sock)).start();

            sock.close();

        }
    }

    @Override
    public void run() {
        int CurrPt = 0;
        int searchDataState = 0;
        byte[] Head = new byte[4];
        int HeadPtr = 0;
        int dwFrameLen = 0;
        byte[] arrMsgBuff = new byte[20000];//一条message的缓存
        short PackageLen = 0;//一条message（即帧头+pcap一行数据的头+内容）的全长
        boolean haveSearchCompleteMessage=true;
        int searchState = -1;
        try (
                DataInputStream inputStream = new DataInputStream(csocket.getInputStream());
        ) {
            while (isOff) {
                byte[] data = new byte[20000];//tcp缓存
                int length = -1;
                length = inputStream.read(data);
                if(length>0) {
                    do {
                        searchState=0;
                        for (byte ch; CurrPt < length; CurrPt++) {
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


                        Formart1 formart1 = new Formart1();
                        byte[] pos = new byte[2];
                        System.arraycopy(arrMsgBuff, 4, pos, 0, 2);
                        formart1.setPos(Trans.byte2short(pos));
                        byte[] fre = new byte[4];
                        System.arraycopy(arrMsgBuff, 6, fre, 0, 4);
                        formart1.setFre(Trans.byte2int(fre));
                        formart1.setMode(arrMsgBuff[10]);
                        formart1.setCheck(arrMsgBuff[11]);
                        formart1.setLen(PackageLen);
                        byte[] time = new byte[8];
                        System.arraycopy(arrMsgBuff, 14, time, 0, 8);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
                        String timeText = format.format(Trans.bytes2Long(time));
                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd"); //设置格式
                        String timePath = format2.format(Trans.bytes2Long(time));
                        formart1.setTime(timeText);
                        byte[] id = new byte[2];
                        System.arraycopy(arrMsgBuff, 22, id, 0, 2);
                        formart1.setId(Trans.byte2short(id));
                        byte[] dt = new byte[PackageLen - 24];
                        System.arraycopy(arrMsgBuff, 24, dt, 0, PackageLen - 24);
                        String path="D:\\\\" + timePath;
                        File Path = new File(path);
                        if (!Path.exists()) {
                            Path.mkdirs();
                        }


                        String pathname = path+"\\\\"+ formart1.getId() + ".pcap";
                        formart1.setPath(pathname);
                        File file = new File(pathname);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                byte[] head = new byte[]{-44, -61, -78, -95, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 0};
                                FileOutputStream outHeadStream = new FileOutputStream(file);
                                outHeadStream.write(head);
                                outHeadStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        FileOutputStream outStream = new FileOutputStream(file, true);
                        outStream.write(dt);
                        outStream.close();
                        formart1Service.save(formart1);
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
}
