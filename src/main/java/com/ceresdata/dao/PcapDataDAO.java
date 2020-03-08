package com.ceresdata.dao;


import com.ceresdata.pojo.PcapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * created by hsk on 2020/3/4
 */
@Repository
public class PcapDataDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(PcapData pcapData){
        String sql="insert into pkt_info (`Header`,`SatPos`,`Frequence`,`ModeCode`,``Check`,`Length`,`Time0`,`Time1`,user_id`,`intf_type`)" +
                " values('"+ pcapData.getSatPos()
                +"','"+pcapData.getFrequence()
                +"','"+pcapData.getModeCode()
                +"','"+pcapData.getCheck()
                +"','"+ pcapData.getLength()
                +"','"+pcapData.getTime0()
                +"','"+pcapData.getTime1()
                +"','"+pcapData.getUserId()
                +"','"+pcapData.getReveType()+"')";
        jdbcTemplate.update(sql);
    }
}