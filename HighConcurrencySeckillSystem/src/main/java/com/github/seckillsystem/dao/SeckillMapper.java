package com.github.seckillsystem.dao;

import com.github.seckillsystem.entity.Order;
import com.github.seckillsystem.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillMapper {
    int selectNumBySeckillId(long seckillId);
    int deleteOneBySeckillId(long seckillId);
    int insertOrder(Order order);
    int getSuccessNum(long seckillId);
    int selectNumForUpdate(long seckillId);
    Seckill selectNumByOpLock(long seckillId);
    int updateByOpLock(long seckillId,int version);
}
