package com.jqmk.examsystem.dto;

import com.jqmk.examsystem.errors.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;
/**
 * @ClassName WebResult
 * @Author tian
 * @Date 2024/6/17 9:34
 * @Description 自定义返回体
 */
@Data
public class WebResult implements Serializable {

    /**
     * 是否返回成功
     */
    private Boolean success;

    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 状态消息
     */
    private String message;

    /**
     * 实际响应消息的载体
     */
    private Object data;

    // 构造器私有
    private WebResult() {
    }

    /**
     * 通用返回成功
     * 用法：
     * (1)当返回带有 date 部分的响应数据时，调用方法如下：
     * return WebResult.ok().date(_data), _data 为需要返回的数据
     * (2)当返回无 date 部分的响应数据时，调用方法如下：
     * return WebResult.ok().
     *
     * @return
     */
    public static WebResult ok() {
        WebResult r = new WebResult();
        r.setSuccess(ErrorCodeEnum.SUCCESS.getSuccess());
        r.setCode(ErrorCodeEnum.SUCCESS.getCode());
        r.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        return r;
    }

    /**
     * 通用返回失败，未知错误
     *
     * @return
     */
    public static WebResult fail() {
        WebResult r = new WebResult();
        r.setSuccess(ErrorCodeEnum.UNKNOWN_ERROR.getSuccess());
        r.setCode(ErrorCodeEnum.UNKNOWN_ERROR.getCode());
        r.setMessage(ErrorCodeEnum.UNKNOWN_ERROR.getMessage());
        return r;
    }

    // 设置失败结果，形参为结果枚举
    public static WebResult fail(ErrorCodeEnum result) {
        WebResult r = new WebResult();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    /**
     * 创建一个空的 WebResult
     *
     * @return
     */
    public static WebResult empty() {
        WebResult r = new WebResult();
        return r;
    }


    /**------------支持使用链式编程-----------**/

    /**
     * 自定义返回数据
     *
     * @param data
     * @return
     */
    public WebResult data(Object data) {
        this.setData(data);
        return this;
    }

    /**
     * 自定义状态信息
     *
     * @param message
     * @return
     */
    public WebResult message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 自定义状态码
     *
     * @param code
     * @return
     */
    public WebResult code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 自定义返回结果
     *
     * @param success
     * @return
     */
    public WebResult success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

}
