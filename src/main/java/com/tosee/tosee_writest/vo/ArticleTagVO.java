package com.tosee.tosee_writest.vo;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/22 11:07 下午
 */

@Data
public class ArticleTagVO
{
    private Integer tagId;

    private String tagName;
}
