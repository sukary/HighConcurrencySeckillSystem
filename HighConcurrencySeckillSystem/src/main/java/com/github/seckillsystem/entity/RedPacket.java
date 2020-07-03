package com.github.seckillsystem.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Api("红包")
@Data
public class RedPacket {
    @ApiModelProperty(value = "红包个数")
    private Integer skillNum;
    @ApiModelProperty(value = "红包id")
    private Long redPacketId;
    @ApiModelProperty(value = "红包金额")
    private int money;
}
