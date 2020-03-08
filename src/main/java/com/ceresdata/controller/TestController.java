package com.ceresdata.controller;

import com.ceresdata.util.ResultMsg;
import com.ceresdata.kafka.KfkaProducer;
import com.ceresdata.pojo.DataCenterMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    KfkaProducer producer;

    @GetMapping("/test")
    public ResultMsg testSendMsg(){
        DataCenterMessage dataCenterMessage = new DataCenterMessage();
        dataCenterMessage.addDispatchId(1);
        dataCenterMessage.addSubscribeId(2);

        producer.send(dataCenterMessage);
        return ResultMsg.success("测试发送消息成功");
    }
}
