package com.github.seckillsystem.service;

import com.github.seckillsystem.common.CommonResult;

public interface SeckillService {
    CommonResult start(long seckillId,long userId);
    CommonResult startTow(long seckillId,long userId);
    CommonResult startThree(long seckillId,long userId);
    CommonResult startFour(long seckillId,long userId);
    int getNumBySeckillId(long skillId);
}
