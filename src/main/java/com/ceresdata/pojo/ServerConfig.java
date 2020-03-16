package com.ceresdata.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器配置
 */
public class ServerConfig {
    private int streamState;// 流服务的状态（0处于停止状态，1：运行状态）
    private int fileServerState;// 文件处理状态（0处于停止状态，1：运行状态）
    private String srcRootDir;// 离线源存储位置
    private String descRootDir;// 离线目标存储位置
    private String filePath; //在线目标存储位置
    private int fileMaxSize = 50;// 文件大小（M）
    private int fileMaxMinute = 30;// 接收文件（minute）
    private int r_port; //r类型数据的端口号
    private int position; //阵地信息
    private String r_dir; //r类型数据目录
    private int readFiles; //已读文件数
    // 连接站点列表
    private List<Connect> connectList = new ArrayList<Connect>();

    public int getStreamState() {
        return streamState;
    }

    public void setStreamState(int streamState) {
        this.streamState = streamState;
    }

    public int getFileServerState() {
        return fileServerState;
    }

    public void setFileServerState(int fileServerState) {
        this.fileServerState = fileServerState;
    }

    public String getSrcRootDir() {
        return srcRootDir;
    }

    public void setSrcRootDir(String srcRootDir) {
        this.srcRootDir = srcRootDir;
    }

    public String getDescRootDir() {
        return descRootDir;
    }

    public void setDescRootDir(String descRootDir) {
        this.descRootDir = descRootDir;
    }

    public int getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(int fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public int getFileMaxMinute() {
        return fileMaxMinute;
    }

    public void setFileMaxMinute(int fileMaxMinute) {
        this.fileMaxMinute = fileMaxMinute;
    }

    public int getR_port() {
        return r_port;
    }

    public void setR_port(int r_port) {
        this.r_port = r_port;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Connect> getConnectList() {
        return connectList;
    }

    public void setConnectList(List<Connect> connectList) {
        this.connectList = connectList;
    }

    public String getR_dir() {
        return r_dir;
    }

    public void setR_dir(String r_dir) {
        this.r_dir = r_dir;
    }

    public int getReadFiles() {
        return readFiles;
    }

    public void setReadFiles(int readFiles) {
        this.readFiles = readFiles;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 添加连接
     * @param connect
     */
    public void addConnect(Connect connect){
        this.connectList.add(connect);
    }

    /**
     * 移除连接
     * @param connect
     */
    public void removeConnect(Connect connect){
        for(int i = 0 ; i < this.connectList.size(); i++){
            Connect c = this.connectList.get(i);
            if(c.getIp().equals(connect.getIp()) && c.getPort()==connect.getPort()){
                this.connectList.remove(i);
                break;
            }
        }
    }

    /**
     * 根据配置文件重新加载
     *
     * @param json
     */
    public void reset(String json){
        //ServerConfig entity = JSONObject.parseObject(json, ServerConfig.class);

        JSONObject jsonObject = JSON.parseObject(json);
        this.srcRootDir = jsonObject.getString("srcRootDir");
        this.descRootDir = jsonObject.getString("descRootDir");
        this.fileMaxSize = jsonObject.getInteger("fileMaxSize");
        this.fileMaxMinute = jsonObject.getInteger("fileMaxMinute");
        this.streamState = jsonObject.getIntValue("streamState");
        this.fileServerState = jsonObject.getIntValue("fileServerState");
        this.position = jsonObject.getIntValue("position");
        this.r_port = jsonObject.getInteger("r_port");

        JSONArray jsonArray = jsonObject.getJSONArray("connectList");
        if(jsonArray != null){
            for(int  i = 0 ; i < jsonArray.size(); i++){
                JSONObject temp = jsonArray.getJSONObject(i);
                Connect connect = new Connect();
                connect.setIp(temp.getString("ip"));
                connect.setPort(temp.getIntValue("port"));
                connect.setSocktState(temp.getBoolean("state"));
                //continue;
            }
        }
    }
}
