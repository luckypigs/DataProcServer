package com.ceresdata.service.impl;

import com.ceresdata.dao.PcapDataDAO;
import com.ceresdata.pojo.PcapData;
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
    public String getFilePath(PcapData pcapData) {
        return pcapDataDAO.getFilePath(pcapData);
    }

    @Override
    public void save_userInfo(PcapData pcapData,long now) {
        pcapDataDAO.save_userInfo(pcapData,now);
    }

    @Override
    public void update_userInfo(PcapData pcapData) {
        pcapDataDAO.update_userInfo(pcapData);
    }
}
