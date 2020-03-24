package com.ceresdata.pojo;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * created by hsk on 2020/3/22
 */
public class SystemStatusLog {
    private int position;
    private String eqpID;
    private int runStat; //0正常 1故障
    private int workStat;//0空闲 1在用
    private String eqpIP;
    private String errDesc;
    private String reportTime;
    private String tcpUse;
    private String udpUse;
    private String softVer;
    private String workparam;

    public SystemStatusLog(){
        try {
            InetAddress inetAddress=InetAddress.getLocalHost();
            this.eqpID=inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.runStat=0;
        this.workStat=0;
        this.errDesc="";
        this.udpUse="";
        this.softVer="1.0";

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEqpID() {
        return eqpID;
    }

    public void setEqpID(String eqpID) {
        this.eqpID = eqpID;
    }

    public int getRunStat() {
        return runStat;
    }

    public void setRunStat(int runStat) {
        this.runStat = runStat;
    }

    public int getWorkStat() {
        return workStat;
    }

    public void setWorkStat(int workStat) {
        this.workStat = workStat;
    }

    public String getEqpIP() {
        return eqpIP;
    }

    public void setEqpIP(String eqpIP) {
        this.eqpIP = eqpIP;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getTcpUse() {
        return tcpUse;
    }

    public void setTcpUse(String tcpUse) {
        this.tcpUse = tcpUse;
    }

    public String getUdpUse() {
        return udpUse;
    }

    public void setUdpUse(String udpUse) {
        this.udpUse = udpUse;
    }

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }

    public String getWorkparam() {
        return workparam;
    }

    public void setWorkparam(String workparam) {
        this.workparam = workparam;
    }
}
