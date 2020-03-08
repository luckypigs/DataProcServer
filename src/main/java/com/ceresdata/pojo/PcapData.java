package com.ceresdata.pojo;

/**
 * 接收数据实体
 */
public class PcapData {
    private String header;
    private int satPos;
    private int frequence;
    private String modeCode;
    private String check;
    private int length;
    private long time0;// 接收时间
    private long time1;// 接收时间
    private short userId;//用户id
    private String path;// 文件路径
    private int reveType;// 接收方式（0,1）

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getSatPos() {
        return satPos;
    }

    public void setSatPos(int satPos) {
        this.satPos = satPos;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public String getModeCode() {
        return modeCode;
    }

    public void setModeCode(String modeCode) {
        this.modeCode = modeCode;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getTime0() {
        return time0;
    }

    public void setTime0(long time0) {
        this.time0 = time0;
    }

    public long getTime1() {
        return time1;
    }

    public void setTime1(long time1) {
        this.time1 = time1;
    }

    public short getUserId() {
        return userId;
    }

    public void setUserId(short userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getReveType() {
        return reveType;
    }

    public void setReveType(int reveType) {
        this.reveType = reveType;
    }
}
