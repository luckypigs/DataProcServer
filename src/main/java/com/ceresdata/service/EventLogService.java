package com.ceresdata.service;


import com.ceresdata.pojo.EventLog;
import com.ceresdata.pojo.SystemStatusLog;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface EventLogService {

    public  void addEventLog(EventLog log);

    public  void addErrorLog(EventLog log);

    public  void addInfoLog(EventLog log);

    public  void addWarnLog(EventLog log);

    public  void deleteAll();

    public void addSystemStatusLog(SystemStatusLog log);

    public void deleteAllSystemStatusLog();
}
