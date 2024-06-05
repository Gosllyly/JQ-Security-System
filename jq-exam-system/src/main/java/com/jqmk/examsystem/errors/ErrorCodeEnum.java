package com.jqmk.examsystem.errors;

import lombok.Getter;

/**
 * @ClassName ErrorCodeEnum
 * @Author tian
 * @Date 2024/6/5 13:44
 * @Description 自定义枚举错误类型
 */
@Getter
public enum ErrorCodeEnum {
    SUCCESS(true, 20000, "成功"),
    UNKNOWN_ERROR(false, 20001, "未知错误"),
    PARAM_ERROR(false, 20002, "参数错误"),
    NULL_POINT(false, 20004, "空指针异常"),
    HTTP_CLIENT_ERROR(false, 20005, "http 请求失败，4xx||5xx"),
    NULL_PARAM(false, 20006, "空参数异常"),
    NULL_FORECAST_DATA(false, 20007,"暂无预测数据，请稍后再试"),

    HTTP_PARAM_IS_NULL(false, 20011, "http请求参数为空"),
    FORMAT_ERROR(false, 20010, "参数格式错误"),
    BUILD_REQUEST_ERROR(false, 20012, "构造请求异常"),

    TOKEN_NONE(false, 20014, "无token"),
    TOKEN_OVERDUE(false, 20015, "token过期"),
    TOKEN_FAIL_VERIFY(false, 20016, "token验证失败"),
    NULL_DATA(false, 20008, "未查询到指定数据，请确认后重试");

    // 响应是否成功
    private final Boolean success;

    // 响应状态码
    private final Integer code;

    // 响应信息
    private final String message;

    ErrorCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}

