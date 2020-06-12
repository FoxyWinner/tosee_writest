package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */
@Entity
@DynamicUpdate
@Data
public class Feedback
{
    @Id
    private String feedbackId;

    private String openid;

    private String content;

    private Date createTime;

    private Date updateTime;
}
