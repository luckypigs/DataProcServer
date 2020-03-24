package com.ceresdata.dao;


import com.ceresdata.pojo.PcapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
            sql="insert into dataclean_pkt_info (`Header`,`SatPos`,`Frequence`,`ModeCode`,`Check`,`Length`,`Time0`,`user_id`,`intf_type`,`file_path`" +
                    ",`datetime`,`intf_nouse`) " +
                    "values (?,?,?,?,?,?,FROM_UNIXTIME(?),?,?,?,FROM_UNIXTIME(?),?)";
            jdbcTemplate.update(sql,new Object[]{pcapData.getHeader(),pcapData.getSatPos(),pcapData.getFrequence(),pcapData.getModeCode(),pcapData.getCheck(),pcapData.getLength(),pcapData.getTime0()/1000,pcapData.getUserId()
            ,2,pcapData.getPath(),pcapData.getDatetime()/1000,pcapData.getIntf_nouse()});
        }else{
            sql="insert into dataclean_pkt_info (`Header`,`SatPos`,`Frequence`,`ModeCode`,`Length`,`Time0`,`Time1`,`user_id`,`intf_type`,`file_path`" +
                    ",`datetime`,`intf_nouse`)" +
                    " values (?,?,?,?,?,FROM_UNIXTIME(?),FROM_UNIXTIME(?),?,?,?)";
            jdbcTemplate.update(sql,new Object[]{pcapData.getHeader(),pcapData.getSatPos(),pcapData.getFrequence(),pcapData.getModeCode(),pcapData.getLength(),pcapData.getTime0()/1000,pcapData.getTime1()/1000,pcapData.getUserId()
                    ,1,pcapData.getPath(),pcapData.getDatetime()/1000,pcapData.getIntf_nouse()});
        }

    }

    public String getFilePath(PcapData pcapData){
        String sql;
        if(pcapData.getReveType()==0){
            sql="select file_rpath from dataclean_user_info where user_id = ?";
        }else{
            sql="select file_fpath from dataclean_user_info where user_id = ?";
        }
        List<String> list = jdbcTemplate.queryForList(sql, new Object[]{pcapData.getUserId()},String.class);;
        if(null != list && list.size()>0){
            String res= list.get(0);
            return res;
        }else{
            return null;
        }
    }

    public void save_userInfo(PcapData pcapData,long now){
        String sql;
        if(pcapData.getReveType()==0){
            sql="insert into dataclean_user_info (`user_id`,`position`,`file_rpath`,`file_fpath`,`datetime`) values (?,?,?,?,FROM_UNIXTIME(?))";
            jdbcTemplate.update(sql,pcapData.getUserId(),pcapData.getPosition(),"","",now/1000);
        }else{
            sql="insert into dataclean_user_info (`user_id`,`position`,`file_rpath`,`file_fpath`,`datetime`) values (?,?,?,?,FROM_UNIXTIME(?))";
            jdbcTemplate.update(sql,pcapData.getUserId(),pcapData.getPosition(),"","",now/1000);
        }

    }

    public void update_userInfo(PcapData pcapData){
        String sql;
        if(pcapData.getReveType()==0){
            sql="update dataclean_user_info set `file_rpath`= ?  ,`position`= ? where `user_id` = ?";
        }else {
            sql="update dataclean_user_info set `file_fpath`= ?  ,`position`= ? where `user_id` = ?";
        }
        jdbcTemplate.update(sql,pcapData.getPath(),pcapData.getPosition(),pcapData.getUserId());
    }
}
/*
DROP TABLE IF EXISTS `dataclean_user_info`;
CREATE TABLE `dataclean_user_info` (
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `position` int(11) DEFAULT NULL COMMENT '阵地编号',
  `file_fpath` varchar(255) DEFAULT NULL,
  `file_rpath` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `datetime` datetime DEFAULT NULL COMMENT '增加时间'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户保存用户id信息，及用户下保存的数据文件路径';

DROP TABLE IF EXISTS `dataclean_pkt_info`;
CREATE TABLE `dataclean_pkt_info` (
  `user_id` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `Header` int(11) DEFAULT NULL,
  `SatPos` int(11) DEFAULT NULL,
  `Frequence` int(11) DEFAULT NULL,
  `ModeCode` int(11) DEFAULT NULL,
  `Length` int(11) DEFAULT NULL,
  `Time0` time DEFAULT NULL,
  `intf_type` int(11) DEFAULT NULL COMMENT '接口类型，1为F接口类型，2为R接口类型',
  `intf_nouse` int(11) DEFAULT NULL COMMENT '次列数据没有用',
  `Check` int(11) DEFAULT NULL,
  `Time1` time DEFAULT NULL,
  `datetime` datetime DEFAULT NULL COMMENT '次列数据同步用',
  `file_path` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '文件路径'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户收集端口输入的所有的报文信息，两种接口信息的每个报文均存储在这个表中';

 */