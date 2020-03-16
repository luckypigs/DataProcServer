//package com.ceresdata.kafka;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ceresdata.pojo.DataCenterMessage;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author xielijun
// * @version V1.0
// * @description: kafka消费者
// * @date 2018/4/2 0002 下午 3:31
// */
//@Component
//public class KafkaConsumer {
//    Logger logger = LoggerFactory.getLogger(getClass());
//    /**
//     * topics: 配置消费topic，以数组的形式可以配置多个
//     * groupId: 配置消费组为”xiaofeng1“
//     *
//     * @param message
//     */
//    @KafkaListener(topics = {"PositionTopic"},groupId= "protision-group-1")
//    public void consumer(String message) {
//        DataCenterMessage entity = JSONObject.parseObject(message, DataCenterMessage.class);
//        logger.info("客户端收到消息：= " + message);
//    }
//}