package com.github.seckillsystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;   //?
    private Long seckillId;
    private Long userId;
    private Timestamp createTime;
    private Integer state;  // -1 表示无效,0表示付款，1表示成功
}
