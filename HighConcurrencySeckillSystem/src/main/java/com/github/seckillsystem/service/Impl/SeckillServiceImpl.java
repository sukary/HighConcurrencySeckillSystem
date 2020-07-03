package com.github.seckillsystem.service.Impl;


import com.github.seckillsystem.common.CommonResult;
import com.github.seckillsystem.dao.SeckillMapper;
import com.github.seckillsystem.entity.Order;
import com.github.seckillsystem.entity.Seckill;
import com.github.seckillsystem.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 会出现超卖
     * @param skillId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public CommonResult start(long skillId,long userId) {
        //查询秒杀商品数量
        int num = seckillMapper.selectNumBySeckillId(skillId);
        if(num > 0){
            // 库存减一
            int count = seckillMapper.deleteOneBySeckillId(skillId);
            //创建订单
            if(count >0){
                Order order = new Order();
                order.setSeckillId(skillId);
                order.setUserId(userId);
                order.setState(0);
                Timestamp createTime = new Timestamp(new Date().getTime());
                order.setCreateTime(createTime);
                //订单入库
                seckillMapper.insertOrder(order);
                //用户付款,事务
                return CommonResult.success();
            }
        }
        return CommonResult.fail();
    }

    /**
     * 悲观锁，正如其名，具有强烈的独占和排他特性。它指的是对数据被外界（包括本系统当前的其他事务，
     * 以及来自外部系统的事务处理）修改持保守态度。因此，在整个数据处理过程中，将数据处于锁定状态。
     * 悲观锁的实现，往往依靠数据库提供的锁机制（也只有数据库层提供的锁机制才能真正保证数据访问的排他性，
     * 否则，即使在本系统中实现了加锁机制，也无法保证外部系统不会修改数据）。
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    public CommonResult startTow(long seckillId, long userId) {
        int num = seckillMapper.selectNumForUpdate(seckillId);
        if(num >0){
            seckillMapper.deleteOneBySeckillId(seckillId);
            Order order = new Order();
            order.setSeckillId(seckillId);
            order.setUserId(userId);
            order.setState(0);
            Timestamp createTime = new Timestamp(new Date().getTime());
            order.setCreateTime(createTime);
            //订单入库
            seckillMapper.insertOrder(order);
            //用户付款,事务
            return CommonResult.success();
        }
        return CommonResult.fail();
    }

    /**
     * 乐观锁机制采取了更加宽松的加锁机制。乐观锁是相对悲观锁而言，也是为了避免数据库幻读、
     * 业务处理时间过长等原因引起数据处理错误的一种机制，但乐观锁不会刻意使用数据库本身的锁机制，
     * 而是依据数据本身来保证数据的正确性。
     * 相对于悲观锁，在对数据库进行处理的时候，乐观锁并不会使用数据库提供的锁机制。一般的实现乐观锁
     * 的方式就是记录数据版本。时间戳也是可以的
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    public CommonResult startThree(long seckillId, long userId) {
        Seckill seckill = seckillMapper.selectNumByOpLock(seckillId);
        if(seckill.getNum()>0){
            // 通过版本号判断数据是否已被更改
            int count =  seckillMapper.updateByOpLock(seckillId,seckill.getVersion());
            if(count > 0){
                Order order = new Order();
                order.setSeckillId(seckillId);
                order.setUserId(userId);
                order.setState(0);
                Timestamp createTime = new Timestamp(new Date().getTime());
                order.setCreateTime(createTime);
                //订单入库
                seckillMapper.insertOrder(order);
                //用户付款,事务
                return CommonResult.success();
            }
        }
        return CommonResult.fail();
    }

    /**
     * lock锁
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    public CommonResult startFour(long seckillId, long userId) {
        try {
            lock.lock();
            int num = seckillMapper.selectNumBySeckillId(seckillId);
            if (num > 0) {
                int count = seckillMapper.deleteOneBySeckillId(seckillId);
                if (count > 0) {
                    Order order = new Order();
                    order.setSeckillId(seckillId);
                    order.setUserId(userId);
                    order.setState(0);
                    Timestamp createTime = new Timestamp(new Date().getTime());
                    order.setCreateTime(createTime);
                    //订单入库
                    seckillMapper.insertOrder(order);
                    //用户付款,事务
                    return CommonResult.success();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return CommonResult.fail();
    }

    @Override
    public int getNumBySeckillId(long skillId) {
        return seckillMapper.getSuccessNum(skillId);
    }
}
