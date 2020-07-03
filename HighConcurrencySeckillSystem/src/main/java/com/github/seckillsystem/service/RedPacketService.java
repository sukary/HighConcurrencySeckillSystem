package com.github.seckillsystem.service;

import com.github.seckillsystem.common.CommonResult;

public interface RedPacketService {
    CommonResult start(long redPacketId,String username);
    CommonResult startTow(long redPacketId,String username);
}
