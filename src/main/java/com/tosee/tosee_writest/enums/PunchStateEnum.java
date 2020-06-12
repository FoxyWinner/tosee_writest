package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 8:45 下午
 */
@Getter
public enum PunchStateEnum implements CodeEnum
{
    NOT_UP_TO_STANDARD_AND_NOT_CLOCKIN(0,"未达打卡标准"),
    UP_TO_STANDARD_BUT_NOT_CLOCKIN(1,"已达标但未打卡"),
    ALREADY_CLOCKIN(2,"已打卡")
    ;

    private Integer code;
    private String message;

    PunchStateEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
