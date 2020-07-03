package com.github.seckillsystem.queue.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import javax.jms.Destination;

/**
 * activemq消息队列
 */
@Component
public class ActiveMqSender {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public void sendChannelMessage(Destination destination,final String message){
        jmsTemplate.convertAndSend(destination,message);
    }
}
