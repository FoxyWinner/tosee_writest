package com.tosee.tosee_writest.enums;

import lombok.Getter;

@Getter
public enum ResultEnum
{
    SUCCESS(0,"成功"),

    PARAM_ERROR(1, "参数不正确"),


    PQB_NOT_EXIST(10,"主题库不存在"),

    COMPANY_NOT_EXIST(11,"公司不存在"),

    OPTIONS_NOT_EXIST(21,"题目的选项不存在"),

    RECORD_NOT_EXIST(22,"无历史作答记录"),

    SPECIAL_PRACTICE_NOT_EXIST(23,"该行业专业知识题库暂为空"),

    MISTAKE_NOT_EXIST(24,"用户错题本中没有该条错题"),

    MISTAKE_BOOK_NOT_EXIST(25,"用户错题本中没有该错题本"),

    COLLECT_BOOK_NOT_EXIST(26,"用户收藏集中没有该题目收藏集"),

    COLLECT_QUESTION_NOT_EXIST(27,"用户收藏集中没有该题目"),

    OPTIONS_IS_EMPTY(28,"单选题和多选题选项不能为空"),


    WECHAT_MA_EMPTY_JSCODE(30, "JSCODE为空"),

    WECHAT_MA_ERROR(31,"微信小程序端相关错误"),

    USERINFO_NOT_EXIST(32,"查询不到该用户信息"),

    ARTICLE_NOT_EXIST(33,"查询文章失败"),


    EXCEED_AUTHORITY(40,"横向越权错误"),

    ;
    private Integer code;

    private String message;

    ResultEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
