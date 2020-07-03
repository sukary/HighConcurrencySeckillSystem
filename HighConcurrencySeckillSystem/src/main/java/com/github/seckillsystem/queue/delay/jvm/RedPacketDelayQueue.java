package com.github.seckillsystem.queue.delay.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 红包延迟过期失效
 * 延迟队列：第一他是个队列，所以具有对列功能第二就是延时，这就是延时对列，功能也就是将任务放在
 * 该延时对列中，只有到了延时时刻才能从该延时对列中获取任务否则获取不到……
 */
public class RedPacketDelayQueue {
    private static Logger LOGGER = LoggerFactory.getLogger(RedPacketDelayQueue.class);

    public static void main(String[] args) throws Exception {
        DelayQueue<RedPacketMessage> delayQueue = new DelayQueue();
        RedPacketMessage message = new RedPacketMessage(1);
        delayQueue.add(message);
        // 延迟5秒
        message = new RedPacketMessage(2, 5);
        delayQueue.add(message);
        // 延迟10秒
        message = new RedPacketMessage(3, 10);
        delayQueue.add(message);
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName("DelayWorker");
            thread.setDaemon(true);
            return thread;
        });
        LOGGER.info("开始执行调度线程...");
        executorService.execute(() -> {
            while (true) {
                System.out.println("111");
                try {
                    RedPacketMessage task = delayQueue.take();
                    System.out.println("111");
                    LOGGER.info("延迟处理红包消息,{}", task.getDescription());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });
        Thread.sleep(Integer.MAX_VALUE);
    }
}
