package com.ceresdata.util;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class FileUtil {

    /**
     *  将内容写入文件中
     * @param content
     * @param file
     * @throws Exception
     */
    public static  void writeFile(String file,String content) throws Exception {
        FileUtils.writeStringToFile(new File(file),content,"utf-8");
    }

    /**
     * 将文本内容写入文件中并指定编码
     * @param file
     * @param content
     * @param charset
     * @throws Exception
     */
    public static  void writeFile(String file,String content,String charset) throws Exception {
        try{
            OutputStream out=new FileOutputStream(file);
            BufferedWriter rd=new BufferedWriter(new OutputStreamWriter(out,charset));
            rd.write(content);
            rd.close();
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件内容
     *
     * @param path
     * @return
     */
    public static String readStringByFile(String path) throws Exception {
        return FileUtils.readFileToString(new File(path),"utf-8");
    }
}
