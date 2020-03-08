package com.ceresdata.pojo;

/**
 * 阵地站点推送到数据中心的消息实体
 */
public class PositionMessage {
    private String position;// 阵地编号
    private String type;// 消息类型（原始数据表还是业务数据表）
    private String filePath;// 文件路径或上传到ftp的路径
    private String userid;// 用户id

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
