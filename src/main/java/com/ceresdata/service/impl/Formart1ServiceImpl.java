package com.ceresdata.service.impl;

import com.ceresdata.dao.Formart1DAO;
import com.ceresdata.pojo.Formart1;
import com.ceresdata.service.Formart1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Formart1ServiceImpl implements Formart1Service {

    @Autowired
    private Formart1DAO formart1DAO;

    @Override
    public void save(Formart1 formart1){
        formart1DAO.save((formart1));
    }

}
