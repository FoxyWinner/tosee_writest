package com.tosee.tosee_writest.enums;

import lombok.Getter;

@Getter
public enum ResultEnum
{
    SUCCESS(0,"成功"),

    PARAM_ERROR(1, "参数不正确"),

    COMPANY_NOT_EXIST(11,"公司不存在"),

    OPTIONS_NOT_EXIST(21,"题目的选项不存在"),

    RECORD_NOT_EXIST(22,"无历史作答记录"),

    SPECIAL_PRACTICE_NOT_EXIST(23,"该行业专业知识题库暂为空"),

    MISTAKE_NOT_EXIST(24,"用户错题本中没有该条错题"),

    MISTAKE_BOOK_NOT_EXIST(25,"用户错题本中没有该错题本"),

    WECHAT_MA_EMPTY_JSCODE(30, "JSCODE为空"),

    WECHAT_MA_ERROR(31,"微信小程序端相关错误"),

    ;
    private Integer code;

    private String message;

    ResultEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
