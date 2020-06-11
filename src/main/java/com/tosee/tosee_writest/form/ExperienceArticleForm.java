package com.tosee.tosee_writest.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ExperienceArticleForm
{
    private String articleId;

    @NotEmpty(message = "文章名必填")
    private String articleTitle;

    @NotEmpty(message = "图片ICONURL必填")
    private String articleIcon;

    @NotNull(message = "第一标签必选")
    private Integer articleTagId;

    private String secondTag;

    @NotEmpty(message = "作者必填")
    private String author;


    private String content;

    @NotNull(message = "阅读数必填")
    private Integer readerNumber;

    @NotNull(message = "发布时间必填")
    private String relaseTime;

    // 默认是不推荐
    private Boolean isRecommended = false;
}
