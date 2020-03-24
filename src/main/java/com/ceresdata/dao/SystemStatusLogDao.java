package com.ceresdata.dao;

import com.ceresdata.pojo.SystemStatusLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SystemStatusLogDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public  void addSystemStatusLog(SystemStatusLog log){
        String sql = "insert into config_systemstatus_log(position,eqpId,runStat,workStat,eqpIp,errDesc,reportTime,tcpUse,udpUse,softVer,workParam,datetime) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        this.jdbcTemplate.update(sql,new Object[]{log.getPosition(),log.getEqpID(),log.getRunStat(),log.getWorkStat(),log.getEqpIP(),log.getErrDesc(),log.getReportTime(),log.getTcpUse(),log.getUdpUse(),log.getSoftVer(),log.getWorkparam(),log.getReportTime()});
    }

    public  void deleteAllSystemStatusLog(){
        String sql = "delete from config_systemstatus_log";
        this.jdbcTemplate.update(sql);
    }
}
