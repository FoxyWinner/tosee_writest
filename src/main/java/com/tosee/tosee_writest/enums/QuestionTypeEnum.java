package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:17 上午
 */

@Getter
public enum QuestionTypeEnum implements CodeEnum
{
    SINGLE_CHOICE(1,"单选题"),
    MULTIPLE_CHOICE(2,"多选题"),
    ESSAY_QUESTION(3,"问答题")
    ;

    private Integer code;
    private String message;

    QuestionTypeEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
