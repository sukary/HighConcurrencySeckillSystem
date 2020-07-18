package com.github.seckillsystem.controller;


import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.common.redis.RedisUtil;
import com.github.seckillsystem.queue.activemq.ActiveMQSender;
import com.github.seckillsystem.queue.kafka.KafKaSender;
import com.github.seckillsystem.service.SeckillDistributedService;
import com.github.seckillsystem.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Api(tags ="分布式秒杀")
@RestController
@RequestMapping("/distributed")
public class DistributedController {
	private final static Logger LOGGER = LoggerFactory.getLogger(DistributedController.class);
	
	private static int corePoolSize = Runtime.getRuntime().availableProcessors();
	//调整队列数 拒绝服务
	private static ThreadPoolExecutor threadPoolExecutor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(1000));
	
	@Autowired
	private SeckillService seckillService;
	@Autowired
	private SeckillDistributedService seckillDistributedService;
	@Autowired
	private KafKaSender kafkaSender;
	@Autowired
	private ActiveMQSender activeMQSender;

	@Autowired
	private RedisUtil redisUtil;
	
	@ApiOperation(value="秒杀(Rediss分布式锁)")
	@PostMapping("/startRedisLock")
	public CommonResult startRedisLock(long seckillId){

		final long killId =  seckillId;
		LOGGER.info("开始秒杀");
		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = () -> {
				CommonResult result = seckillDistributedService.startSeckilRedisLock(killId, userId);
				LOGGER.info("用户:{}{}",userId,result.getData());
			};
			threadPoolExecutor.execute(task);
		}
		try {
			Thread.sleep(15000);
			int seckillCount = seckillService.getNumBySeckillId(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return CommonResult.success();
	}
	@ApiOperation(value="秒杀(zookeeper分布式锁)")
	@PostMapping("/startZkLock")
	public CommonResult startZkLock(long seckillId){
		seckillService.getNumBySeckillId(seckillId);
		final long killId =  seckillId;
		LOGGER.info("开始秒杀");
		for(int i=0;i<10000;i++){
			final long userId = i;
			Runnable task = () -> {
				CommonResult result = seckillDistributedService.startSeckilZksLock(killId, userId);
				LOGGER.info("用户:{}{}",userId,result.getData());
			};
			threadPoolExecutor.execute(task);
		}
		try {
			Thread.sleep(10000);
			int seckillCount = seckillService.getNumBySeckillId(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return CommonResult.success();
	}
	@ApiOperation(value="秒杀(Kafka分布式队列)")
	@PostMapping("/startKafkaQueue")
	public CommonResult startKafkaQueue(long seckillId){

		final long killId =  seckillId;
		LOGGER.info("开始秒杀");
		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = () -> {
				if(redisUtil.get(killId+"")==null){
					//思考如何返回给用户信息ws
					kafkaSender.sendChannelMess("seckill",killId+";"+userId);
				}else{
					//秒杀结束
				}
			};
			threadPoolExecutor.execute(task);
		}
		try {
			Thread.sleep(10000);
			redisUtil.set(killId+"", null);
			int seckillCount = seckillService.getNumBySeckillId(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return CommonResult.success();
	}
	@ApiOperation(value="秒杀(ActiveMQ分布式队列)",nickname="科帮网")
	@PostMapping("/startActiveMQQueue")
	public CommonResult startActiveMQQueue(long seckillId){

		final long killId =  seckillId;
		LOGGER.info("开始秒杀");
		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = () -> {
				if(redisUtil.get(killId+"")==null){
					Destination destination = new ActiveMQQueue("seckill.queue");
					//思考如何返回给用户信息ws
					activeMQSender.sendChannelMessage(destination,killId+";"+userId);
				}else{
					//秒杀结束
				}
			};
			threadPoolExecutor.execute(task);
		}
		try {
			Thread.sleep(10000);
			redisUtil.set(killId+"", null);
			int seckillCount  = seckillService.getNumBySeckillId(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return CommonResult.success();
	}
}
