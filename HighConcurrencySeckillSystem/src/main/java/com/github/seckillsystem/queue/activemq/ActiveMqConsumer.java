package com.github.seckillsystem.queue.activemq;

import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.common.redis.RedisUtil;
import com.github.seckillsystem.common.webSocket.WebSocketServer;
import com.github.seckillsystem.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ActiveMqConsumer {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisUtil redisUtil;
    @JmsListener(destination = "seckill.activemq")
    public void receiveQueue(String message){
        //收到通道的消息之后执行秒杀操作(超卖)
        String[] array = message.split(";");
        CommonResult result = seckillService.startThree(Long.parseLong(array[0]),Long.parseLong(array[1]));
        if(result.getCode() == 200){
            WebSocketServer.sendInfo(array[0].toString() + " " + "秒杀成功","");
        }else{
            WebSocketServer.sendInfo(array[0].toString() + " " + "秒杀失败","");
        }
    }
}
