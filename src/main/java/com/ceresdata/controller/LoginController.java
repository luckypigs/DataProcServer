package com.ceresdata.controller;

import com.ceresdata.multiThreadServer.MultiThreadOfflineClient;
import com.ceresdata.multiThreadServer.MultiThreadSocketClient;
import com.ceresdata.pojo.FileSize;
import com.ceresdata.pojo.ServerConfig;
import com.ceresdata.service.PcapDataService;
import com.ceresdata.util.ResultMsg;
import com.ceresdata.config.DataProcessConfig;
import com.ceresdata.multiThreadServer.DataProcessServer;
import com.ceresdata.pojo.Connect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // 数据处理服务端
    @Autowired
    DataProcessServer dataProcessServer;
    @Autowired
    PcapDataService pcapDataService;
    @Autowired
    DataProcessConfig dataProcessConfig;

    @PostConstruct
    public void initServer(){
        // 设置数据启动
        dataProcessServer.setInitConfigFile(dataProcessConfig.getInitFile());
        dataProcessServer.initServer();
    }

    /**
     * 启动socketServer
     */
    @CrossOrigin
    @PostMapping(value = "/api/startSocketServer")
    @ResponseBody
    public ResultMsg startSocketServer(@RequestBody FileSize fileSize){
        ServerConfig serverConfig=dataProcessServer.getConfig();
        serverConfig.setFileMaxMinute(fileSize.getFileMaxMinute());
        serverConfig.setFileMaxSize(fileSize.getFileMaxSize());
        serverConfig.setFilePath(fileSize.getFilePath());

        dataProcessServer.startServer();
        return ResultMsg.success("启动服务成功");
    }

    /**
     * 停止socket
     */
    @CrossOrigin
    @RequestMapping(value = "/api/stopSocketServer")
    @ResponseBody
    public ResultMsg stopSocketServer(){
        dataProcessServer.stopServer();
        return ResultMsg.success("停止服务成功");
    }

    /**
     * 断开连接
     */
    @CrossOrigin
    @PostMapping(value = "/api/disconnect")
    @ResponseBody
    public ResultMsg disconnect(@RequestBody Connect connect){
       boolean flag =  this.dataProcessServer.disconnect(connect);
       if(flag){
           return ResultMsg.success("断开连接成功");
       }else{
           return ResultMsg.error("断开连接失败");
       }
    }

    /**
     * 开始建立连接
     */
    @CrossOrigin
    @PostMapping(value = "/api/connect" )
    @ResponseBody
    public ResultMsg connect(@RequestBody Connect connect){
        boolean flag =  this.dataProcessServer.connect(connect);
        if(flag){
            return ResultMsg.success("建立连接成功");
        }else{
            return ResultMsg.error("建立连接失败");
        }
    }

    /**
     * 获取在线日志
     */
    @CrossOrigin
    @GetMapping(value = "/api/onlineLog")
    @ResponseBody
    public ResultMsg getOnlineLog(){
        return MultiThreadSocketClient.getLog();
    }

    /**
     * 开始离线传输
     */
    @CrossOrigin
    @PostMapping(value = "/api/startOfflineServer")
    @ResponseBody
    public ResultMsg startOfflineServer(@RequestBody FileSize fileSize){
        dataProcessServer.startOfflineServer(fileSize);
        return ResultMsg.success("启动服务成功");
    }

    /**
     * 停止离线传输
     */
    @CrossOrigin
    @RequestMapping(value = "/api/stopOfflineServer")
    @ResponseBody
    public ResultMsg stopOfflineServer(){
        dataProcessServer.stopOfflineServer();
        return ResultMsg.success("停止服务成功");
    }

    /**
     * 获取离线日志
     */
    @GetMapping(value = "/api/offlineLog")
    @ResponseBody
    public ResultMsg getOfflineLog(){
        return MultiThreadOfflineClient.getLog();
    }

    /**
     * 启动获取列表
     */
    @CrossOrigin
    @GetMapping(value = "/api/getClients")
    @ResponseBody
    public ResultMsg getClients(){
        return new ResultMsg(dataProcessServer.getConfig().getConnectList());

    }

    /**
     * 获取路径
     */
    @CrossOrigin
    @GetMapping(value = "/api/getPath")
    @ResponseBody
    public ResultMsg getPath(){
        return new ResultMsg(dataProcessServer.getFileSize());

    }
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "hello";
    }


}