package com.ceresdata.dao;


import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.UserInfo;
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
        String sql;
        if(pcapData.getReveType()==0){
            sql="insert into pkt_info (`SatPos`,`Frequence`,`ModeCode`,`Check`,`Length`,`Time0`,`user_id`,`intf_type`,`path`) values (?,?,?,?,?,FROM_UNIXTIME(?),?,?,?)";
            jdbcTemplate.update(sql,new Object[]{pcapData.getSatPos(),pcapData.getFrequence(),pcapData.getModeCode(),pcapData.getCheck(),pcapData.getLength(),pcapData.getTime0()/1000,pcapData.getUserId()
            ,pcapData.getReveType(),pcapData.getPath()});
        }else{
            sql="insert into pkt_info (`SatPos`,`Frequence`,`ModeCode`,`Length`,`Time0`,`Time1`,`user_id`,`intf_type`,`path`) values (?,?,?,?,FROM_UNIXTIME(?),FROM_UNIXTIME(?),?,?,?)";
            jdbcTemplate.update(sql,new Object[]{pcapData.getSatPos(),pcapData.getFrequence(),pcapData.getModeCode(),pcapData.getLength(),pcapData.getTime0()/1000,pcapData.getTime1()/1000,pcapData.getUserId()
                    ,pcapData.getReveType(),pcapData.getPath()});
        }

    }
/*    public int count(int user_id){
        String sql="select count(*) from pkt_info where user_id = "+ user_id;
        List<Integer> list = jdbcTemplate.query(sql, new Object[]{user_id}, new BeanPropertyRowMapper(Integer.class));
        if(null != list && list.size()>0){
            int res= list.get(0);
            return res;
        }else{
            return 0;
        }

    }*/
    public String getFilePath(int user_id){
        String sql="select file_path from user_info where user_id = ?";
        List<String> list = jdbcTemplate.query(sql, new Object[]{user_id}, new BeanPropertyRowMapper(String.class));
        if(null != list && list.size()>0){
            String res= list.get(0);
            return res;
        }else{
            return null;
        }
    }
    public void save_userInfo(UserInfo userInfo){
        String sql="insert into user_info (`user_id`,`position`,`file_path`)" +
                " values('" + userInfo.getUser_id()
                +"','"+userInfo.getPosition()
                +"','"+userInfo.getFile_path()+"')";
        jdbcTemplate.update(sql);
    }
    public void update_userInfo(UserInfo userInfo){
        String sql="update user_info set `file_path`= ?  ,`position`= ? where `user_id` = ?";
        jdbcTemplate.update(sql,userInfo.getFile_path(),userInfo.getPosition(),userInfo.getUser_id());
    }
}
/*
CREATE TABLE IF NOT EXISTS `user_info` (
  `user_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `file_path` varchar(128) DEFAULT NULL,
  PRIMARY KEY ( user_id )
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户保存用户id信息，及用户下保存的数据文件路径';

CREATE TABLE IF NOT EXISTS `pkt_info` (
  `user_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `Header` int(11) DEFAULT NULL,
  `SatPos` int(11) DEFAULT NULL,
  `Frequence` int(11) DEFAULT NULL,
  `ModeCode` int(11) DEFAULT NULL,
  `Length` int(11) DEFAULT NULL,
  `Time0` datetime DEFAULT NULL,
  `intf_type` int(11) DEFAULT NULL,
  `Check` int(11) DEFAULT NULL,
  `Time1` datetime DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户收集端口输入的所有的报文信息';
 */