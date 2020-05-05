package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 12:16 下午
 */

@Getter
public enum QuestionBankSortEnum implements CodeEnum
{
    SORT_BY_TIME_DESC(0,"按时间降序"),
    SORT_BY_HEAT_DESC(1,"按热度降序"),
    ;

    private Integer code;
    private String message;

    QuestionBankSortEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
