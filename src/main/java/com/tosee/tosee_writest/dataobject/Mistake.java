package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 9:46 下午
 */
@Entity
@DynamicUpdate
@Data
/**
 * 这个表格实际上是一个指向真正题目的"虚指针"
 */
public class Mistake
{
    @Id
    private String mistakeId;

    private String openid;

    private String mistakeBookId;

    private String questionId;

    private Date createTime;

    private Date updateTime;
}
