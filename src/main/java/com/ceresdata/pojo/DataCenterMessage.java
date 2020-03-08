package com.ceresdata.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布订阅实体类
 */
public class DataCenterMessage {
    private String position;//阵地编号
    //  订阅数据的用户ID号
    private List<Integer> subscribeIds = new ArrayList<Integer>();
    // 发布数据的ID列表
    private List<Integer> dispatchIds =  new ArrayList<Integer>();

    public List<Integer> getSubscribeIds() {
        return subscribeIds;
    }

    public List<Integer> getDispatchIds() {
        return dispatchIds;
    }

    public void setDispatchIds(List<Integer> dispatchIds) {
        this.dispatchIds = dispatchIds;
    }

    public void setSubscribeIds(List<Integer> subscribeIds) {
        this.subscribeIds = subscribeIds;
    }

    public void addSubscribeId(Integer id){
        this.subscribeIds.add(id);
    }

    public void addDispatchId(Integer id){
        this.dispatchIds.add(id);
    }
}
