package com.ceresdata.pojo;

/**
 * 连接对象
 */
public class Connect {
    private String ip;
    private int port;
    private boolean socktState=true;
    private boolean ismain;
    private int position;

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

    public boolean isSocktState() {
        return socktState;
    }

    public void setSocktState(boolean socktState) {
        this.socktState = socktState;
    }

}
