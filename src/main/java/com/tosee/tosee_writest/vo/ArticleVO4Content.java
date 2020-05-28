package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */
@Data
public class ArticleVO4Content
{
    private String articleId;

    private Integer isRecommended;

    @JsonProperty("title")
    private String articleTitle;

    // 转换
    @JsonProperty("tag")
    private String articleTagName;

    private String author;

    //转换
    private Integer collectState;

    @JsonProperty("readNum")
    private Integer readerNumber;

    private String content;

    private String relaseTime;
}
