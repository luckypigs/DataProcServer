package com.ceresdata.dao;

import com.ceresdata.pojo.Formart1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class Formart1DAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Formart1 formart1){
        String sql="insert into formart1 (`pos`,`fre`,`mode`,`check`,`len`,`time`,`id`,`path`) values('"+ formart1.getPos()
                +"','"+formart1.getFre()
                +"','"+formart1.getMode()
                +"','"+formart1.getCheck()
                +"','"+ formart1.getLen()
                +"','"+formart1.getTime()
                +"','"+formart1.getId()
                +"','"+ formart1.getPath()+"')";
        jdbcTemplate.update(sql);
    }
}
