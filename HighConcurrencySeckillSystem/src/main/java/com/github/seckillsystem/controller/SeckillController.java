package com.github.seckillsystem.controller;

import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.service.SeckillService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillController.class);

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 100, TimeUnit.SECONDS
            , new LinkedBlockingDeque<>(1000));
    @Autowired
    private SeckillService seckillService;

    /**
     * 会出现超卖
     * @param skillId
     * @return
     */
    @ApiOperation("秒杀one")
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public CommonResult start(long skillId) {
        int skillNum = 100;
        final CountDownLatch downLatch = new CountDownLatch(skillNum);
        final long killId = skillId;
        for (int i = 0; i < skillNum; i++) {
            long userId = i;
            Runnable task = () -> {
                try {
                    CommonResult result = seckillService.start(skillId, userId);
                    if (result.getCode() == 200) {
                        LOGGER.info("用户秒杀成功");
                    } else {
                        LOGGER.info("抱歉，抢光了");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
            };
            executor.execute(task);
        }
        try {
            downLatch.await();
            int count = seckillService.getNumBySeckillId(skillId);
            LOGGER.info("一共秒杀商品{}件", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success();
    }

    /**
     *
     * @param skillId
     * @return
     */
    @ApiOperation("数据库悲观锁")
    @RequestMapping(value = "/start2" ,method = RequestMethod.POST)
    public CommonResult startTow(long skillId){
        int skillNum = 100;
        final CountDownLatch downLatch = new CountDownLatch(skillNum);
        final long killId = skillId;
        for (int i = 0; i < skillNum; i++) {
            long userId = i;
            Runnable task = () -> {
                try {
                    CommonResult result = seckillService.startTow(skillId, userId);
                    if (result.getCode() == 200) {
                        LOGGER.info("用户秒杀成功");
                    } else {
                        LOGGER.info("抱歉，抢光了");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
            };
            executor.execute(task);
        }
        try {
            downLatch.await();
            int count = seckillService.getNumBySeckillId(skillId);
            LOGGER.info("一共秒杀商品{}件", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success();
    }

    @ApiOperation("数据库乐观锁")
    @RequestMapping(value = "/start/three" ,method = RequestMethod.POST)
    public CommonResult startThree(long seckillId){
        int skillNum = 100;
        final CountDownLatch downLatch = new CountDownLatch(skillNum);
        final long killId = seckillId;
        for (int i = 0; i < skillNum; i++) {
            long userId = i;
            Runnable task = () -> {
                try {
                    CommonResult result = seckillService.startThree(seckillId, userId);
                    if (result.getCode() == 200) {
                        LOGGER.info("用户秒杀成功");
                    } else {
                        LOGGER.info("抱歉，抢光了");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
            };
            executor.execute(task);
        }
        try {
            downLatch.await();
            int count = seckillService.getNumBySeckillId(seckillId);
            LOGGER.info("一共秒杀商品{}件", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success();
    }

    @ApiOperation("lock锁")
    @RequestMapping(value = "/start/four" ,method = RequestMethod.POST)
    public CommonResult startFour(long seckillId){
        int skillNum = 100;
        final CountDownLatch downLatch = new CountDownLatch(skillNum);
        final long killId = seckillId;
        for (int i = 0; i < skillNum; i++) {
            long userId = i;
            Runnable task = () -> {
                try {
                    CommonResult result = seckillService.startFour(seckillId, userId);
                    if (result.getCode() == 200) {
                        LOGGER.info("用户秒杀成功");
                    } else {
                        LOGGER.info("抱歉，抢光了");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
            };
            executor.execute(task);
        }
        try {
            downLatch.await();
            int count = seckillService.getNumBySeckillId(seckillId);
            LOGGER.info("一共秒杀商品{}件", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CommonResult.success();
    }
}
