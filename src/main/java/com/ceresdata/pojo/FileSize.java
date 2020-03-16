package com.ceresdata.pojo;

/**
 * created by hsk on 2020/3/12
 */
public class FileSize {
    private int fileMaxSize;
    private int fileMaxMinute;
    private String filePath;
    private String srcDir;
    private String descDir;

    public FileSize(int fileMaxSize, int fileMaxMinute, String filePath, String srcDir, String descDir) {
        this.fileMaxSize = fileMaxSize;
        this.fileMaxMinute = fileMaxMinute;
        this.filePath = filePath;
        this.srcDir = srcDir;
        this.descDir = descDir;
    }
    public FileSize(){

    }

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getDescDir() {
        return descDir;
    }

    public void setDescDir(String descDir) {
        this.descDir = descDir;
    }
}
