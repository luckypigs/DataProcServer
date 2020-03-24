package com.ceresdata.util;

import com.ceresdata.pojo.PcapData;
import com.ceresdata.tools.Trans;

import java.io.IOException;

public class ParseUtil {
    public static PcapData createPcapData(byte[]head,int type) throws IOException {
        PcapData pcapData=new PcapData();
        byte[]header =new byte[4];
        System.arraycopy(head, 0, header, 0, 4);
        pcapData.setHeader(Trans.byte2int(header)+"");
        byte[] pos = new byte[2];
        System.arraycopy(head, 4, pos, 0, 2);
        pcapData.setSatPos(Trans.byte2short(pos));
        byte[] fre = new byte[4];
        System.arraycopy(head, 6, fre, 0, 4);
        pcapData.setFrequence(Trans.byte2int(fre));
        pcapData.setIntf_nouse(head[11]);
        byte[]s_PackageLen =new byte[2];
        System.arraycopy(head, 14, s_PackageLen, 0, 2);
        short PackageLen=Trans.byte2short(s_PackageLen);
        pcapData.setLength(PackageLen);

        if (type==0) {
            pcapData.setModeCode(String.valueOf(head[12]));
            pcapData.setCheck(String.valueOf(head[13]));

            byte[] time = new byte[8];
            System.arraycopy(head, 16, time, 0, 8);
            pcapData.setTime0(Trans.bytes2Long(time));

            byte[] id = new byte[2];
            System.arraycopy(head, 24, id, 0, 2);
            pcapData.setUserId(Trans.byte2short(id));

            pcapData.setReveType(type);

        }else{
            pcapData.setModeCode(String.valueOf(head[10]));
            byte[] time0 = new byte[8];
            byte[] time1 = new byte[8];
            System.arraycopy(head, 16, time0, 0, 8);
            System.arraycopy(head, 24, time1, 0, 8);

            pcapData.setTime0(Trans.bytes2Long(time0));
            pcapData.setTime1(Trans.bytes2Long(time1));

            byte[] id = new byte[2];
            System.arraycopy(head, 32, id, 0, 2);
            pcapData.setUserId(Trans.byte2short(id));

            pcapData.setReveType(type);

        }

        return pcapData;
    }

    public static boolean isRType(byte b){
        if(b==2)return true;
        return false;
    }

    public static boolean isRType(byte[]b){
        return isRType(b[10]);
    }


}
