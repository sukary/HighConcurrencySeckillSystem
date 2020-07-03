package com.github.seckillsystem.service.Impl;

import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.common.redis.RedisUtil;
import com.github.seckillsystem.service.RedPacketService;
import com.github.seckillsystem.util.RedisLockUtil;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Random;


@Service("RedPacketService")
public class RedPacketServiceImpl implements RedPacketService {
    @Autowired
    private RedisUtil redisUtil;
//    private ReentrantLock lock = new ReentrantLock(); //互斥锁
    /**
     * 使用redis分布式锁来保证线程安全
     * @param redPacketId
     * @param username
     * @return
     */
    @Override
    public CommonResult start(long redPacketId,String username) {
        Integer money = 0;
        try {
            // 尝试获取锁，获取锁失败最大等待时间为3s,获取锁后超过10s自动释放
             if( RedisLockUtil.tryLock(redPacketId +"",3,10)){
                 Integer restMoney = Integer.parseInt(redisUtil.get(redPacketId +"money").toString());
                 long restPeople = redisUtil.decr(redPacketId+"restPeople",1)+1;
                 if(restPeople == 1){
                     money = restMoney;
                 }else {
                     Random random = new Random();
                     money = random.nextInt((int) (restMoney / (restPeople+1) * 2 - 1)) + 1;
                 }

                 redisUtil.decr(redPacketId+"money",money);
             }else {
                 //等待时间3秒，获取锁失败阻塞3秒，直到成功，不会走这里
                 System.out.println("---------没抢到锁---------");
                 redisUtil.incr(redPacketId+"num",1);
             }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
                RedisLockUtil.unlock(redPacketId+"");
        }
        return CommonResult.success(money);
    }

    @Override
    public CommonResult startTow(long redPacketId, String username) {
        Boolean res = false;
        Integer money = 0;
        try {
            res = RedisLockUtil.tryLock(redPacketId+"",3,10);
            if(res){
                Long resPeople = redisUtil.decr(redPacketId+"-restPeople",1)+1;
                if(resPeople>0){
                    if(resPeople == 1){
                        money = Integer.parseInt(redisUtil.get(redPacketId+"-money").toString());
                    }else {
                        Integer restMoney = Integer.parseInt(redisUtil.get(redPacketId+"-money").toString());
                        Random random = new Random();
                        money = random.nextInt((int) (restMoney / (resPeople) * 2 - 1)) + 1;
                    }
                    redisUtil.decr(redPacketId+"-money",money);
                }else {
                    return CommonResult.fail("手慢了，红包已被抢完");
                }
            }else {
                return CommonResult.fail("手慢了，红包已被抢完");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(res == true){
                RedisLockUtil.unlock(redPacketId+"");
            }
        }
        return CommonResult.success(money);
    }
}
