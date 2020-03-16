package com.ceresdata.util;

import com.ceresdata.pojo.PcapData;
import com.ceresdata.tools.Trans;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * created by hsk on 2020/3/4
 */
public class ParseUtil {
    public static PcapData parse(Socket socket){
        PcapData res=new PcapData();
        int searchState = -1;//记录解析完一条数据了吗
        int searchDataState=0;//记录解析到数据了吗

        byte[] Head = new byte[4];
        int HeadPtr = 0;




        int type=0;
        int currPt = 0;//记录缓存的
        int dwFrameLen = 0; //记录所有的
        byte[] arrMsgBuff = new byte[20000];//一条message的缓存
        boolean haveSearchCompleteMessage=true;
        short PackageLen = 0;//一条message（即帧头+pcap一行数据的头+内容）的全长
        short dataLen;

        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        ) {
            while (socket.isConnected()) {
                PcapData pcapData=new PcapData();
                pcapData.setReveType(type);
                // 开始接收文件
                if (type == 0) {
                    byte[] head = new byte[24];//tcp缓存
                    int length = -1;
                    // 读取数据到内存中
                    length = inputStream.read(head);
                    if (length==24) {
                        byte[] head1 =new byte[4];
                        System.arraycopy(arrMsgBuff, 0, head1, 0, 4);
                        pcapData.setHeader(new String(head1));

                        byte[] pos = new byte[2];
                        System.arraycopy(arrMsgBuff, 4, pos, 0, 2);
                        pcapData.setSatPos(Trans.byte2short(pos));

                        byte[] fre = new byte[4];
                        System.arraycopy(arrMsgBuff, 6, fre, 0, 4);
                        pcapData.setFrequence(Trans.byte2int(fre));

                        byte[]s_PackageLen =new byte[2];
                        System.arraycopy(arrMsgBuff, 12, s_PackageLen, 0, 2);
                        PackageLen=Trans.byte2short(s_PackageLen);
                        pcapData.setLength(PackageLen);
                        pcapData.setModeCode(String.valueOf(arrMsgBuff[10]));
                        pcapData.setCheck(String.valueOf(arrMsgBuff[11]));

                        byte[] time = new byte[8];
                        System.arraycopy(arrMsgBuff, 14, time, 0, 8);
                        pcapData.setTime0(Trans.bytes2Long(time));

                        byte[] id = new byte[2];
                        System.arraycopy(arrMsgBuff, 22, id, 0, 2);
                        pcapData.setUserId(Trans.byte2short(id));

                        byte []data=new byte[20000];
                        dataLen= (short) (PackageLen-24);
                        for(int i=0;i*data.length<dataLen;i++){
                            inputStream.read(data);
                        }

                    }




                    /*do {
                        searchState = 0;
                        //
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
                                            if (CurrPt == length - 1) {
                                                searchDataState = 1;
                                                haveSearchCompleteMessage = false;
                                            }
                                        }

                                    } else if (HeadPtr < 4 && CurrPt == length - 1) {
                                        //包头没有读完整，包头被分割到了两次tcp包
                                        searchDataState = 0; //下一个包来时继续向Head中的第HeadPtr个元素追加
                                        haveSearchCompleteMessage = false;
                                    }
                                }
                                break;
                                case 1: {//1:已找到头，寻找剩余部分
                                    arrMsgBuff[dwFrameLen++] = ch;
                                    if (dwFrameLen <= 13 && CurrPt == length - 1) {
                                        //arrMsgBuff[12]和arrMsgBuff[13]为单条message的长度，单条报文将两个字节分割
                                        //或还没读到12、13字节处
                                        haveSearchCompleteMessage = false;
                                        searchDataState = 1;
                                    } else if (dwFrameLen == 14) {
                                        byte[] s_PackageLen = new byte[]{arrMsgBuff[12], arrMsgBuff[13]};
                                        PackageLen = Trans.byte2short(s_PackageLen);//得到单挑message的全长
                                    } else if (dwFrameLen > 14) {
                                        if (dwFrameLen == PackageLen && CurrPt == length - 1) {
                                            //仅包含完整的一帧数据
                                            searchState = 2;
                                            searchDataState = 0;

                                        } else if (dwFrameLen < PackageLen) {
                                            //一条message未读完整，for循环继续
                                            searchDataState = 1;
                                        } else if (dwFrameLen == PackageLen && CurrPt < length - 1) {
                                            //一条数据接收完整，但还有数据，先跳出for循环处理此条message，更改do-while循环标识searchState=1
                                            searchState = 1;
                                            searchDataState = 0;
                                        } else if (dwFrameLen < PackageLen && CurrPt == length - 1) {
                                            ////一条数据未接收完整
                                            haveSearchCompleteMessage = false;
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
                                dwFrameLen = 0;
                                break;
                            } else if (searchState == 2 && searchDataState == 0) {
                                //仅包含完整的一帧数据
                                CurrPt = 0;
                                dwFrameLen = 0;
                                searchState = 0;
                                break;
                            } else if (!haveSearchCompleteMessage) {
                                //一条数据未接收完整
                                CurrPt = 0;
                                //一条data不完整就跳出for循环
                                //判断是否一条数据没接收完整，更新do_while循环判断依据searchState的值
                                searchState = -1;//读空了缓存区数据
                                break;
                            }
                        }

                        if (searchState == -1) {
                            haveSearchCompleteMessage = true;
                            break;
                        }




                        arrMsgBuff = new byte[20000];
                        Head = new byte[4];
                    } while (searchState == 1);*/
                } else {
                    break;
                }
            }
        } catch (Exception e) {

        }
        return res;
    }
}
