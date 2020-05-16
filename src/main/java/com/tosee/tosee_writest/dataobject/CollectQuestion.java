package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/15 11:32 上午
 */
@Entity
@DynamicUpdate
@Data
/**
 * 这个表格实际上是一个指向真正题目的"虚指针"
 */
public class CollectQuestion
{
    @Id
    private String collectQuestionId;

    private String openid;

    private String collectBookId;

    private String questionId;

    private Date createTime;

    private Date updateTime;
}
