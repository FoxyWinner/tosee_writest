package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:17 上午
 */

@Getter
public enum PositionPQBTypeEnum implements CodeEnum
{
    ENTERPRISE_BANK(0,"企业真题"),
    PROFESSIONAL_BANK(1,"专业知识"),
    MINE_INFO(2,"我的"),
    ALL_PQBTYPE(3,"全部主题库"),
    ;

    private Integer code;
    private String message;

    PositionPQBTypeEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
