package com.github.seckillsystem.util;


import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁工具类
 * @author bu
 */
public class RedisLockUtil {
    private static RedissonClient redissonClient;
    public void setRedissonClient(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    /**
     * 对key加锁后返回锁
     */
    public static RLock lock(String lockKey){
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 加锁指定时间，超过时间自动释放
     * @param lockKey
     * @param time
     * @return
     */
    public static RLock lock(String lockKey, long time){
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(time,TimeUnit.SECONDS);
        return lock;
    }

    public static RLock lock(String lockKey, long time,TimeUnit unit){
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(time,unit);
        return lock;
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @return
     */
    public static Boolean tryLock(String lockKey,int waitTime, int leaseTime){
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime,leaseTime,TimeUnit.SECONDS);
        }catch (Exception e){
            return false;
        }
    }

    /**
     *
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     */
    public static Boolean tryLock(String lockKey,int waitTime,int leaseTime,TimeUnit unit){
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.tryLock(waitTime,leaseTime,unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey){
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     * @param lock
     */
    public static void unlock(RLock lock){
        lock.unlock();
    }

//    /**
//     * 递增
//     * @param key
//     * @param delta 要增加几(大于0)
//     * @return
//     */
//    public int incr(String key, int delta) {
//        RMapCache<String, Integer> mapCache = redissonClient.getMapCache("skill");
//        if (delta < 0) {
//            throw new RuntimeException("递增因子必须大于0");
//        }
//        return  mapCache.addAndGet(key, 1);//加1并获取计算后的值
//    }
//
//    /**
//     * 递减
//     * @param key 键
//     * @param delta 要减少几(小于0)
//     * @return
//     */
//    public int decr(String key, int delta) {
//        RMapCache<String, Integer> mapCache = redissonClient.getMapCache("skill");
//        if (delta < 0) {
//            throw new RuntimeException("递减因子必须大于0");
//        }
//        return mapCache.addAndGet(key, -delta);//加1并获取计算后的值
//    }
}
