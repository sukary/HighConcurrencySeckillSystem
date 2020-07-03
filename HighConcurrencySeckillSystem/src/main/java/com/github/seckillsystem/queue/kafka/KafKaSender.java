package com.github.seckillsystem.queue.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * KafKa消息队列生产者
 */
@Component
public class KafKaSender {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    public void sendChannelMess(String channel,String message){
        kafkaTemplate.send(channel,message);
    }
}
