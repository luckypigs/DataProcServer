package com.ceresdata.util;

import com.ceresdata.tools.Trans;

import java.io.File;
import java.io.FileOutputStream;

/**
 * pcap 文件写入工具类
 */
public class PcapFileUtil {
    private int fileMaxSize;
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
    public long writeFile(String path,byte[] bytes,int offset,int len,long l){
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
        String newpath;
        long size=file.length();
        if(size>=fileMaxSize*1024*1024){
            String olddatetime= Trans.stampToDate(l);
            long newTime = System.currentTimeMillis();
            String newdatetime=Trans.stampToDate(newTime);
            newpath=path.replace(olddatetime,newdatetime);
            return this.writeFile(newpath,bytes,offset,len,newTime);
        }else if((size+len)<=(fileMaxSize*1024*1024)){
            this.appendToFile(path,bytes,offset,len);
            return l;
        }else{
            String olddatetime= Trans.stampToDate(l);
            long newTime = System.currentTimeMillis();
            String newdatetime=Trans.stampToDate(newTime);
            newpath=path.replace(olddatetime,newdatetime);
            this.appendToFile(path,bytes,offset,(int)(fileMaxSize*1024*1024-size));
            return this.writeFile(newpath,bytes,(int)(offset+fileMaxSize*1024*1024-size),
                    (int)(len-fileMaxSize*1024*1024+size),newTime);

        }

    }
    /**
     * 追加内容到pcap 文件包中
     * @param path
     * @param bytes
     */
    public void appendToFile(String path,byte[] bytes,int offset,int len) {
        File file = new File(path);
        try {
            FileOutputStream outStream = new FileOutputStream(file, true);
            outStream.write(bytes,offset,len);
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
