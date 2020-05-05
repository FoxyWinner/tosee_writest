package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:07 下午
 */

@Getter
public enum RecordLastModeEnum implements CodeEnum
{
    UNKOWN_MODE(0,"未做过"),
    SIMULATE_MODE(1,"模拟模式"),
    PRACTICE_MODE(2,"练习模式"),

    ;

    private Integer code;
    private String message;

    RecordLastModeEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
