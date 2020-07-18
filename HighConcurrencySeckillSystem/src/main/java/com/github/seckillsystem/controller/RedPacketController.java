package com.github.seckillsystem.controller;


import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.common.redis.RedisUtil;
import com.github.seckillsystem.entity.RedPacket;
import com.github.seckillsystem.service.RedPacketService;
import com.github.seckillsystem.util.DoubleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 高并发的情况下抢红包
 * @author bu
 */

@Api
@RestController
@RequestMapping("red/packet")
public class RedPacketController{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketController.class);
    /**
     * 自定义线程池管理
     */

    //虚拟机可用的最大处理器数量
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 不建议使用jdk自带线程池，建议自定义线程池
     * corePoolSize: 核心线程池数量 cpu密集性应用设置cpu核心数
     * maximumPoolSize ：最大线程池数量 cpu核心数加一
     *keepAliveTime 当线程池线程数量大于corePoolSize时候，多出来的空闲线程，多长时间会被销毁。
     * workQueue 任务队列
     * threadFactory 线程工厂，用于创建线程，一般可以用默认的
     * handler 拒绝策略，当任务过多时候，如何拒绝任务。 默认的
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,corePoolSize+1,100,
            TimeUnit.SECONDS,new  ArrayBlockingQueue(1000));
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedPacketService redPacketService;

    /**
     * 抢红包，抢到基本能拆到
     * @return
     */
    @ApiOperation(value = "抢红包")
    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public CommonResult redPacketStart(@RequestBody RedPacket redPacket){
        // 初始化红包数量
        redisUtil.set(redPacket.getRedPacketId() + "num",10);
        System.out.println(redisUtil.get(redPacket.getRedPacketId() + "num").toString());
        // 初始化红包金额
        redisUtil.set(redPacket.getRedPacketId() + "money",redPacket.getMoney());

        redisUtil.set(redPacket.getRedPacketId()+"restPeople",10);
        // 倒计时锁，模拟skillNum人同时抢红包
        CountDownLatch countDownLatch = new CountDownLatch(redPacket.getSkillNum());
        for (int i = 0;i < 100;i++){
            String username = "name:" + i;
            Runnable task = ()->{
                long count = (long) redisUtil.decr(redPacket.getRedPacketId() + "num",1);
                if(count >= 0){
                    CommonResult result = redPacketService.start(redPacket.getRedPacketId(),username);
                    Double amount = DoubleUtil.divide(Double.parseDouble(result.getData().toString()), (double) 100);
                    LOGGER.info("用户{}抢红包成功，金额：{},剩余人数:{}", username,amount,redisUtil.get(redPacket.getRedPacketId() + "restPeople"));
                }else {
                    LOGGER.info("用户{}抢红包失败",username);
                }
                countDownLatch.countDown();
            };
            executor.execute(task);
        }try {
            countDownLatch.await();
            Integer restMoney = Integer.parseInt(redisUtil.get(redPacket.getRedPacketId()+"money").toString());
            LOGGER.info("红包剩余金额:"+restMoney);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.success();
    }

    /**
     * 抢到不一定能拆到
     * @param redPacket
     * @return
     */
    @ApiOperation("抢红包2")
    @RequestMapping(value = "/start2",method = RequestMethod.POST)
    public CommonResult redPacketStartTow(@RequestBody RedPacket redPacket){

        redisUtil.set(redPacket.getRedPacketId()+"-restPeople", 10);
        redisUtil.set(redPacket.getRedPacketId()+"-money",redPacket.getMoney());
        CountDownLatch countDownLatch = new CountDownLatch(redPacket.getSkillNum());
        for (int i = 0;i < redPacket.getSkillNum();i++){
            String username = "name"+ i;
            Runnable task = ()->{
                Integer money = Integer.parseInt(redisUtil.get(redPacket.getRedPacketId()+"-money").toString());
                if(money > 0){
                    CommonResult result = redPacketService.startTow(redPacket.getRedPacketId(),username);
                    if(result.getCode() == 200){
                        Double amount = DoubleUtil.divide(Double.parseDouble(result.getData().toString()), (double) 100);
                        LOGGER.info("用户{}抢红包成功，金额：{}", username,amount);
                    }else {
                        LOGGER.info("手慢了，没抢到");
                    }
                }else {
                    LOGGER.info("手慢了，没抢到");
                }
                countDownLatch.countDown();
            };
            executor.execute(task);
        }
        try {
            countDownLatch.await();
            Integer restMoney = Integer.parseInt(redisUtil.get(redPacket.getRedPacketId()+"-money").toString());
            LOGGER.info("红包剩余金额:"+restMoney);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success();
    }
}
