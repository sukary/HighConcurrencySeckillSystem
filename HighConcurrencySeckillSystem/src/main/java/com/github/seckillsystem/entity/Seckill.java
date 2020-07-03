package com.github.seckillsystem.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Seckill implements Serializable {
    private static final long serialVersionUID = 1L;
    private long seckillId;
    private String name;
    private int num;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createTime;
    @Version
    private int version;
}
