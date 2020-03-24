package com.ceresdata.service;

import com.ceresdata.pojo.PcapData;

/**
 * created by hsk on 2020/3/4
 */
public interface PcapDataService {
    void save(PcapData pcapData);
    String getFilePath(PcapData pcapData);
    void save_userInfo(PcapData pcapData,long now);
    void update_userInfo(PcapData pcapData);
}
