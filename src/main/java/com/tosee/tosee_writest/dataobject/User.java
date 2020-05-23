package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */
@Entity
@DynamicUpdate
@Data
public class User
{
    @Id
    private String userId;

    private String openid;

    private String wechatId;

    private String userName;

    private Integer gender; //genderEnum

    private String profilePhotoUrl;

    private Integer authorised;

    /* 教育背景 */

    private Integer eduDegree;

    private String university;

    private String major;

    private String graduationTime; //yyyy/MM

    private String targetFields;

    private String targetPositions;
}
