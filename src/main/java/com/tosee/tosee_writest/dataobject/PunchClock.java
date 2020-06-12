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
public class PunchClock
{
    @Id
    private String punchId;

    private String openid;

    private Date punchDate;

    private Integer punchState;

    private Integer completeNumber;

    private Integer solveNumber;

    private Integer exerciseTime;

    private Date createTime;

    private Date updateTime;
}
