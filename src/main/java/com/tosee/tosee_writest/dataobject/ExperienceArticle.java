package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */
@Entity
@DynamicUpdate
@Data
public class ExperienceArticle
{
    @Id
    private String articleId;

    private Integer isRecommended;

    // 预留字段，暂时用不着
    private Integer articleType;

    private Integer articleTagId;

    private String secondTag;

    private String articleIcon;

    private String articleTitle;

    private Integer readerNumber;

    private String author;

    private String content;

    private Date relaseTime;

    private Date createTime;

    private Date updateTime;

}
