package com.ceresdata.kafka;

import com.ceresdata.pojo.DataCenterMessage;
import com.ceresdata.util.FastJSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KfkaProducer {
    private static Logger logger = LoggerFactory.getLogger(KfkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //发送消息方法
    public void send(DataCenterMessage entity ) {
        String jsonString = FastJSONUtil.toJSONString(entity);
        logger.info("发送消息 ----->>>>>  message = {}", jsonString);
        kafkaTemplate.send("PositionTopic", jsonString);
    }
}