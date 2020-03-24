package com.ceresdata.dao;

import com.ceresdata.pojo.EventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventLogDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public  void addEventLog(EventLog log){
        String sql = "insert into config_event_log(position,eventSource,eventName,eventDate,eventInfo,loglevel,eventParam,datetime) values(?,?,?,?,?,?,?,?)";
        this.jdbcTemplate.update(sql,new Object[]{log.getPosition(),log.getEventSource(),log.getEventName(),log.getEventDate(),log.getEventInfo(),log.getLogLevel(),log.getEventParams(),log.getEventDate()});

    }
    public  void deleteAll(){
        String sql = "delete from config_event_log";
        this.jdbcTemplate.update(sql);
    }
}
