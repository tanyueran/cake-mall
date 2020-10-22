package com.github.tanyueran.entity;


// 响应消息的响应码
public enum ResponseResultCode {
    SUCCESS("成功", 200),
    UNAUTHORIZED("暂未登录或token已经过期", 401),
    FORBIDDEN("没有相关权限", 403),
    FAILED("服务器错误", 500);

    private String codeMsg;
    private Integer code;

    public String getCodeMsg() {
        return this.codeMsg;
    }

    public Integer getCode() {
        return this.code;
    }

    private ResponseResultCode(String codeMsg, Integer code) {
        this.codeMsg = codeMsg;
        this.code = code;
    }
}
