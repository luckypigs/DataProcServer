package com.ceresdata.service.impl;


import com.ceresdata.dao.EventLogDao;
import com.ceresdata.dao.SystemStatusLogDao;
import com.ceresdata.pojo.EventLog;
import com.ceresdata.pojo.SystemStatusLog;
import com.ceresdata.service.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EventLogServiceImpl implements EventLogService{
    @Autowired
    EventLogDao eventLogDao;
    @Autowired
    SystemStatusLogDao systemStatusLogDao;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");

    public  void addEventLog(EventLog log){

        log.setEventDate(simpleDateFormat.format(System.currentTimeMillis()));
        eventLogDao.addEventLog(log);
    }

    public  void addErrorLog(EventLog log){
        log.setEventDate(simpleDateFormat.format(System.currentTimeMillis()));
        log.setLogLevel(EventLog.LOG_LEVEL_ERROR);
        this.addEventLog(log);
    }

    public  void addInfoLog(EventLog log){
        log.setEventDate(simpleDateFormat.format(System.currentTimeMillis()));
        log.setLogLevel(EventLog.LOG_LEVEL_INFO);
        this.addEventLog(log);
    }

    public  void addWarnLog(EventLog log){
        log.setEventDate(simpleDateFormat.format(System.currentTimeMillis()));
        log.setLogLevel(EventLog.LOG_LEVEL_WARN);
        this.addEventLog(log);
    }

    public  void deleteAll(){
        eventLogDao.deleteAll();
    }

    public void addSystemStatusLog(SystemStatusLog log){
        systemStatusLogDao.addSystemStatusLog(log);
    }

    public void deleteAllSystemStatusLog(){
        systemStatusLogDao.deleteAllSystemStatusLog();
    }
}
