package com.ceresdata.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * pcap 文件写入工具类
 */
public class PcapFileUtil {
    private int fileMaxSize = 30*1024*1024;
    private int fileMaxMinute;

    public int getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(int fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public int getFileMaxMinute() {
        return fileMaxMinute;
    }

    public void setFileMaxMinute(int fileMaxMinute) {
        this.fileMaxMinute = fileMaxMinute;
    }


    /**
     * 写入pcap的文件头
     */
    private static final byte[] HEADER_DATAS = new byte[]{-44, -61, -78, -95, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 0};


    /**
     * 追加到文件中
     * @param path
     * @param bytes
     */
    public long writeFile(String path,byte[] bytes){
        File file = new File(path);
        // 父目录不存在，创建目录
        File parentDirFile = file.getParentFile();
        if(!parentDirFile.exists()){
            parentDirFile.mkdirs();
        }
        // 文件不存在
        if (!file.exists()) {
           this.createFileAddHeader(path);
        }
        // 追加数据文件
        String []split=path.split("_");
        String newpath="";
        long pre=0;
        for(int i=0;i<split.length;i++){
            if(i!=split.length-2){
                newpath+=split[i];
            }else{
                pre=Long.valueOf(split[i]);
                newpath +=System.currentTimeMillis();
            }
        }
        if(file.length()>=fileMaxSize){
            return this.writeFile(newpath,bytes);
        }else if((file.length()+bytes.length)<=fileMaxSize){
            this.appendToFile(path,bytes);
            return pre;
        }else{
            byte[]byte1=new byte[(int) (fileMaxSize-file.length())];
            byte[]byte2=new byte[bytes.length-byte1.length];
            this.appendToFile(path,byte1);
            return this.writeFile(newpath,byte2);

        }

    }
    /**
     * 追加内容到pcap 文件包中
     * @param path
     * @param bytes
     */
    public void appendToFile(String path,byte[] bytes) {
        File file = new File(path);
        try {
            FileOutputStream outStream = new FileOutputStream(file, true);
            outStream.write(bytes);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  文件不存在，创建文件并且把pcap 的 文件头写入
     * @param path
     */
    private void createFileAddHeader(String path){
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream outHeadStream = new FileOutputStream(file);
                outHeadStream.write(HEADER_DATAS);
                outHeadStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
