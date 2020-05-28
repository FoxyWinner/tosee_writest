package com.tosee.tosee_writest.dto;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */
@Data
public class ExperienceArticleDTO
{
    private String articleId;

    private Integer isRecommended;

    // 预留字段，暂时用不着
    private Integer articleType;

    private Integer articleTagId;

    private String articleTagName;

    private String secondTag;

    private String articleIcon;

    private String articleTitle;

    private Integer readerNumber;

    private String author;

    private String content;

    private String relaseTime;
}
