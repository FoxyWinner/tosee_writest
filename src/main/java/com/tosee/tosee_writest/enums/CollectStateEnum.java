package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 8:45 下午
 */
@Getter
public enum CollectStateEnum implements CodeEnum
{
    IS_NOT_COLLECTED(0,"未收藏"),
    IS_COLLECTED(1,"已收藏")
    ;

    private Integer code;
    private String message;

    CollectStateEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
