package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/15 11:31 上午
 */
@Entity
@DynamicUpdate
@Data
public class CollectBook
{
    @Id
    private String collectBookId;

    private String openid;

    private String cqbId;

    private Integer collectNumber;

    private Date createTime;

    private Date updateTime;
}
