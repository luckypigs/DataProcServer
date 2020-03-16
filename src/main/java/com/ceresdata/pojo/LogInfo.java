package com.ceresdata.pojo;

public class LogInfo {
    private String time;
    private long countByte;
    private int countPa;
    public LogInfo(String time,long countByte,int countPa){
        this.time=time;
        this.countByte=countByte;
        this.countPa=countPa;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getCountByte() {
        return countByte;
    }

    public void setCountByte(long countByte) {
        this.countByte = countByte;
    }

    public int getCountPa() {
        return countPa;
    }

    public void setCountPa(int countPa) {
        this.countPa = countPa;
    }
}
