package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 9:44 下午
 */
@Entity
@DynamicUpdate
@Data
public class MistakeBook
{
    @Id
    private String mistakeBookId;

    private String openid;

    private String cqbId;

    private Integer mistakeNumber;

    private Date createTime;

    private Date updateTime;
}
