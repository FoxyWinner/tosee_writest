package com.tosee.tosee_writest.enums;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 6:20 下午
 */
import lombok.Getter;

@Getter
public enum UserEduDegreeEnum implements CodeEnum
{
    DOMESTIC_UNDERGRADUATE(1,"国内本科"),
    OVERSEAS_UNDERGRADUATE(2,"海外本科"),
    MASTER_DEGREE_CANDIDATE(3,"硕士研究生"),
    DOCTORAL_CANDIDATE(3,"博士研究生"),
    ELSE(4,"其他"),
            ;

    private Integer code;
    private String message;

    UserEduDegreeEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
