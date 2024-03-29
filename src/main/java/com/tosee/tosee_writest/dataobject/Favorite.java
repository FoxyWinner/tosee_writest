package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 4:57 下午
 */

@Entity
@DynamicUpdate
@Data
public class Favorite
{
    @Id
    private String favoriteId;

    private String openid;

    private Integer favoriteType;

    private String targetId;

    private Date createTime;

    private Date updateTime;
}
