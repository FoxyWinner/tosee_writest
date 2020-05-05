package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:07 下午
 */

@Getter
public enum RecordStateEnum implements CodeEnum
{
    UNCOMPLETED(0,"未完成，中途退出"),
    COMPLETED(1,"完成，作答记录"),
    ;

    private Integer code;
    private String message;

    RecordStateEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
