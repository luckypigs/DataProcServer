package com.ceresdata.service.impl;

import com.ceresdata.dao.PcapDataDAO;
import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.UserInfo;
import com.ceresdata.service.PcapDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by hsk on 2020/3/4
 */
@Service
public class PcapDataServiceImpl implements PcapDataService {

    @Autowired
    private PcapDataDAO pcapDataDAO;

    @Override
    public void save(PcapData pcapData) {
        pcapDataDAO.save(pcapData);
    }

    @Override
    public String getFilePath(int user_id) {
        return pcapDataDAO.getFilePath(user_id);
    }

    @Override
    public void save_userInfo(UserInfo userInfo) {
        pcapDataDAO.save_userInfo(userInfo);
    }

    @Override
    public void update_userInfo(UserInfo userInfo) {
        pcapDataDAO.update_userInfo(userInfo);
    }
}
