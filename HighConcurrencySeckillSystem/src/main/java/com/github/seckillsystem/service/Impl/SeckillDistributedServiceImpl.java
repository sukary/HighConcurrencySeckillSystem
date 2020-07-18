package com.github.seckillsystem.service.Impl;

import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.controller.DistributedController;
import com.github.seckillsystem.dao.SeckillMapper;
import com.github.seckillsystem.entity.Order;
import com.github.seckillsystem.service.SeckillDistributedService;
import com.github.seckillsystem.service.SeckillService;
import com.github.seckillsystem.util.RedisLockUtil;
import com.github.seckillsystem.util.ZkLockUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
@Service
public class SeckillDistributedServiceImpl implements SeckillDistributedService {

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private SeckillMapper seckillMapper;

    @Override
    @Transactional
    public CommonResult startSeckilRedisLock(long seckillId, long userId) {
        boolean res=false;
        try {
            //redis分布式锁
            res = RedisLockUtil.tryLock(seckillId+"", 3, 10, TimeUnit.SECONDS);
            if(res){
                int number = seckillService.getNumBySeckillId(seckillId);
                if(number>0){
                    Order order = new Order();
                    order.setSeckillId(seckillId);
                    order.setUserId(userId);
                    order.setState(0);
                    order.setCreateTime(new Timestamp(new Date().getTime()));
                    seckillMapper.insertOrder(order);
                    return CommonResult.success();
                }else{
                    return CommonResult.fail();
                }
            }else{
                return CommonResult.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(res){//释放锁
                RedisLockUtil.unlock(seckillId+"");
            }
        }
        return CommonResult.success();
    }
    @Override
    @Transactional
    public CommonResult startSeckilZksLock(long seckillId, long userId) {
        boolean res=false;
        try {
            //基于redis分布式锁
            res = ZkLockUtil.acquire(3,TimeUnit.SECONDS);
            if(res){
                int number =  seckillService.getNumBySeckillId(seckillId);
                if(number>0){
                    Order order = new Order();
                    order.setSeckillId(seckillId);
                    order.setUserId(userId);
                    order.setState(0);
                    order.setCreateTime(new Timestamp(new Date().getTime()));
                    seckillMapper.insertOrder(order);
                    return CommonResult.success();
                }else{
                    return CommonResult.fail();
                }
            }else{
                return CommonResult.fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(res){//释放锁
                ZkLockUtil.release();
            }
        }
        return CommonResult.success();
    }
}
