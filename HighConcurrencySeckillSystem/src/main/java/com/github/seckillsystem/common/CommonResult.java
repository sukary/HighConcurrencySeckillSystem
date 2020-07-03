package com.github.seckillsystem.common;

import lombok.Data;

/**
 * @author bu
 * Created by bu on 2019/5/23.
 * 封装返回的结果
 */
public class CommonResult<T>{
    private int code;        // 状态码
    private String message;  // 返回消息
    private T data;          // 返回数据
    public CommonResult(int code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public CommonResult(){
    }

    /**
     * 成功返回结果
     * @param <T>
     * @return
     */
    public static<T> CommonResult<T> success(T data){
        return new CommonResult<T>(CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(),data);
    }
    /**
     * 成功
     */
    public static<T> CommonResult<T> success(){
        return new CommonResult<T>(CommonResultCode.SUCCESS.getCode(),CommonResultCode.SUCCESS.getMessage(),null);
    }

    /**
     * @param errorCode
     * @param <T>
     * @return
     */
    public static<T> CommonResult<T> fail(Code errorCode){
        return new CommonResult<T>(errorCode.getCode(),errorCode.getMessage(),null);
    }

    /**
     * @param errorCode
     * @param message
     */
    public static<T> CommonResult<T> fail(Code errorCode,String message){
        return new CommonResult<T>(errorCode.getCode(),message,null);
    }

    /**
     * 失败返回结果
     * @param message
     */
    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<T>(CommonResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> fail() {
        return fail(CommonResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(CommonResultCode.NotFound.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(CommonResultCode.UNAUTHORIZED.getCode(), CommonResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(CommonResultCode.FORBIDDEN.getCode(), CommonResultCode.FORBIDDEN.getMessage(), data);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
