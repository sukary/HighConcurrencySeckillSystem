package com.github.seckillsystem.common;

/**
 * 常用API操作码
 * Created by bu on 2019/5/23.
 */
public enum CommonResultCode implements Code{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    NotFound(404, "服务器无法根据客户端的请求找到资源"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");
    private int code;
    private String message;

    CommonResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }
}
