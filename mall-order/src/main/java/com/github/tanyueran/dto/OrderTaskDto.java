package com.github.tanyueran.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderTaskDto {
    // 订单编号
    private String orderId;
    
    // 订单开始判断时间
    private Date startTime;
}
