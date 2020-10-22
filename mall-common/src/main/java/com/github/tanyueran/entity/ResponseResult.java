package com.github.tanyueran.entity;

import lombok.Data;

@Data
public class ResponseResult {
    // 状态码
    private Integer code;
    // 提示消息
    private String msg;
    // 具体的数据体
    private Object data;
}
