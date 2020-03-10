package com.ceresdata.controller;

import com.ceresdata.service.impl.PcapDataServiceImpl;
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
    DataProcessServer dataProcessServer = new DataProcessServer();
    @Autowired
    PcapDataServiceImpl pcapDataService;
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
    @GetMapping(value = "/api/startSocketServer")
    @ResponseBody
    public ResultMsg startSocketServer(){
        dataProcessServer.startServer();
        return ResultMsg.success("启动服务成功");
    }

    /**
     * 停止socket
     */
    @CrossOrigin
    @GetMapping(value = "/api/stopSocketServer")
    @ResponseBody
    public ResultMsg stopSocketServer(){
        dataProcessServer.stopServer();
        return ResultMsg.success("停止服务成功");
    }

    /**
     * 断开连接
     */
    @GetMapping(value = "/api/disconnect")
    @ResponseBody
    public ResultMsg disconnect(Connect connect){
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
    @GetMapping(value = "/api/connect")
    @ResponseBody
    public ResultMsg connect(Connect connect){
        boolean flag =  this.dataProcessServer.connect(connect);
        if(flag){
            return ResultMsg.success("建立连接成功");
        }else{
            return ResultMsg.error("建立连接失败");
        }
    }


}