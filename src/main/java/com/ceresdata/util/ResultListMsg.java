package com.ceresdata.util;

import java.util.List;

/**
 * 返回列表消息格式
 */
public class ResultListMsg extends ResultMsg {
    private Integer totalCount;

    public ResultListMsg(List data, Integer totalCount){
         super(data);
         this.totalCount = totalCount;
    }

    public ResultListMsg(int code,String msg,List data, Integer totalCount){
        super(code,msg,data);
        this.totalCount = totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }


}
