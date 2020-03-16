package com.ceresdata.service;

import com.ceresdata.pojo.PcapData;
import com.ceresdata.pojo.UserInfo;

/**
 * created by hsk on 2020/3/4
 */
public interface PcapDataService {
    void save(PcapData pcapData);
    String getFilePath(int user_id);
    void save_userInfo(UserInfo userInfo);
    void update_userInfo(UserInfo userInfo);
}
