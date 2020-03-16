//package com.ceresdata;
//
//import com.ceresdata.kafka.KfkaProducer;
//import com.ceresdata.pojo.DataCenterMessage;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class WjApplicationTests {
//    @Autowired
//    KfkaProducer  kfkaProducer;
//
//    @Test
//    void testSendMsg() {
//        System.out.println("发送消息开始");
//        DataCenterMessage entity = new DataCenterMessage();
//        entity.addSubscribeId(1);
//        entity.addDispatchId(2);
//
//        kfkaProducer.send(entity);
//        System.out.println("发送消息结束");
//    }
//
//}
