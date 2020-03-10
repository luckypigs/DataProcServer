package com.ceresdata.dao;


import com.ceresdata.pojo.PcapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public String getFilePath(int user_id){
        String sql="select file_path from user_info";
        List<String> list = jdbcTemplate.query("select * from learn_resource where id = ?", new Object[]{user_id}, new BeanPropertyRowMapper(String.class));
        if(null != list && list.size()>0){
            String res= list.get(0);
            return res;
        }else{
            return null;
        }
    }
}
/*
CREATE TABLE IF NOT EXISTS `user_info` (
  `user_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `file_path` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户保存用户id信息，及用户下保存的数据文件路径';
 */