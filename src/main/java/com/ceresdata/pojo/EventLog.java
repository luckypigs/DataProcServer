package com.ceresdata.pojo;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * created by hsk on 2020/3/22
 */
@Component
public class EventLog {
    public static int LOG_LEVEL_INFO=1;
    public static int LOG_LEVEL_ERROR=2;
    public static int LOG_LEVEL_WARN=3;
    private int position;
    private String eventSource;
    private String eventName;
    private String eventDate;
    private String eventInfo;
    private int logLevel;
    private String eventParams;

    public EventLog(){
        try {
            InetAddress inetAddress=InetAddress.getLocalHost();
            this.eventSource=inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getEventParams() {
        return eventParams;
    }

    public void setEventParams(String eventParams) {
        this.eventParams = eventParams;
    }
}
