package com.github.seckillsystem.service;

import com.github.seckillsystem.common.CommonResult;

public interface SeckillDistributedService {
    /**
     * 秒杀 一  单个商品
     * @param seckillId 秒杀商品ID
     * @param userId 用户ID
     * @return
     */
    CommonResult startSeckilRedisLock(long seckillId,long userId);
    /**
     * 秒杀 一  单个商品
     * @param seckillId 秒杀商品ID
     * @param userId 用户ID
     * @return
     */
    CommonResult startSeckilZksLock(long seckillId, long userId);

    /**
     * 秒杀 二 多个商品
     * @param seckillId 秒杀商品ID
     * @param userId 用户ID
     * @param number 秒杀商品数量
     * @return
     */
}
