package com.tosee.tosee_writest.enums;

import lombok.Getter;

@Getter
public enum ResultEnum
{
    SUCCESS(0,"成功"),

    PARAM_ERROR(1, "参数不正确"),

    COMPANY_NOT_EXIST(11,"公司不存在"),

    OPTIONS_NOT_EXIST(21,"题目的选项不存在"),

    WECHAT_MA_EMPTY_JSCODE(30, "JSCODE为空"),

    WECHAT_MA_ERROR(20, "微信小程序方面错误"),

    WECHAT_MP_ERROR(21,"微信扫码登录方面错误"),


    LOGIN_FAIL(25,"登录失败，登录信息不正确"),
    LOGOUT_SUCCESS(26, "登出成功"),
    ;
    private Integer code;

    private String message;

    ResultEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
