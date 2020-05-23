package com.tosee.tosee_writest.dataobject;

import com.sun.javafx.beans.IDProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/22 11:07 下午
 */
@Entity
@DynamicUpdate
@Data
public class ArticleTag
{
    @Id
    private Integer tagId;

    private String tagName;

    private Integer tagHeat;
}
