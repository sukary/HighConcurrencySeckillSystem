package com.github.seckillsystem.queue.delay.redis;

import java.io.Serializable;

/**
 * 红包队列消息
 */
public class RedisRedPacketMessage implements Serializable {

    /**
     * 红包 ID
     */
    private  long redPacketId;

    /**
     * 创建时间戳
     */
    private  long timestamp;

    public RedisRedPacketMessage() {

    }

    public RedisRedPacketMessage(long redPacketId) {
        this.redPacketId = redPacketId;
        this.timestamp = System.currentTimeMillis();
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
