package com.tosee.tosee_writest.enums;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 6:20 下午
 */
//        `edu_degree` tinyint(3) comment '教育程度：1Domestic undergraduate2海外本科Overseas undergraduate3硕士研究生4博士研究生5其他',

import lombok.Getter;

@Getter
public enum UserGenderEnum implements CodeEnum
{
    MALE(1,"男"),
    FEMALE(2,"女"),
    ELSE(3,"其他"),
    ;

    private Integer code;
    private String message;

    UserGenderEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
