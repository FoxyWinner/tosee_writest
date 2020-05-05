package com.tosee.tosee_writest.enums;

import lombok.Getter;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:07 下午
 */

@Getter
public enum FavoriteTypeEnum implements CodeEnum
{
    //收藏类型, 1子题库2题目3文章
    CHILD_QUESTIONBANK(1,"子题库"),
    QUESTION(2,"题目"),
    EXPERIENCE_ARTICLE(3,"笔经干货"),
    ;

    private Integer code;
    private String message;

    FavoriteTypeEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
