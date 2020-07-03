package com.github.seckillsystem.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 * @author bu
 */
@Component
public class RedisUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
    @Resource
    private RedisTemplate<Serializable,Serializable> redisTemplate;

    /**
     * 前缀
     */
    private static final String KEY_PREFIX_VALUE = "learn:seckill:value";

    /**
     * 缓存value
     * @param k
     * @param v
     * @param time
     * @param unit
     * @return
     */
    public boolean set(String k, Serializable v, Long time, TimeUnit unit){
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
            operations.set(key,v);
            if(time > 0){
                redisTemplate.expire(key,time,unit);
            }
            return true;
        }catch (Throwable t){
            LOGGER.error("缓存失败:",key,v,time);
        }
        return false;
    }

    /**
     * 缓存value
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean set(String k,Serializable v,Long time){
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
            operations.set(key,v);
            if(time>0){
                redisTemplate.expire(k,time,TimeUnit.SECONDS);
            }
            return true;
        }catch (Throwable t){
            LOGGER.info("缓存失败:",key,v,time);
        }
        return false;
    }

    /**
     * 缓存value
     * @param k
     * @param v
     * @return
     */
    public boolean set(String k,Serializable v){
        return set(k,v,-1l);
    }

    /**
     * 获取缓存
     * @param k
     * @return
     */
    public Serializable get(String k){
        String key =KEY_PREFIX_VALUE +k;
        try {
            ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
            return operations.get(key);
        }catch (Throwable t) {
            LOGGER.error("获取缓存失败key[" + KEY_PREFIX_VALUE + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     * @param k
     * @return
     */
    public Boolean isContainsKey(String k){
        String key = KEY_PREFIX_VALUE + k;
        try {
            return redisTemplate.hasKey(key);
        }catch (Throwable t){
            LOGGER.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 移除缓存
     * @param k
     * @return
     */
    public  boolean remove(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            LOGGER.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }
    /**
     * 递增
     * @param k
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String k, long delta) {
        String key = KEY_PREFIX_VALUE + k;
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param k 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String k, long delta) {
        String key = KEY_PREFIX_VALUE + k;
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }
}
