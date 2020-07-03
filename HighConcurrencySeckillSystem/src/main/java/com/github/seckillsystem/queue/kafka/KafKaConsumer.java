package com.github.seckillsystem.queue.kafka;

import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.common.redis.RedisUtil;
import com.github.seckillsystem.common.webSocket.WebSocketServer;
import com.github.seckillsystem.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafKaConsumer {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisUtil redisUtil;

    @KafkaListener(topics = {"seckill"})
    public void receiveMessage(String message){
        //收到通道的消息之后执行秒杀操作
        String[] array = message.split(";");
        /**
         * 这里可以做重复消费的检查，防止重复消费。
         *  根据业务要求，是否要求强一致性，如果要求强一致性，则可以根据订单唯一id用流水表记录，持久化到磁盘
         *  非强一致性，可以用redis缓存暂时存取，设置失效时间。
         */
        CommonResult result = seckillService.startThree(Long.parseLong(array[0]),Long.parseLong(array[1]));
        if(result.getCode() == 200){
            WebSocketServer.sendInfo(array[0].toString() + " " + "秒杀成功","");
        }else{
            WebSocketServer.sendInfo(array[0].toString() + " " + "秒杀失败","");
        }
    }
}
