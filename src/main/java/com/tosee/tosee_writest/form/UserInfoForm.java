package com.tosee.tosee_writest.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/20 10:07 下午
 */
@Data
public class UserInfoForm
{
    @NotEmpty(message = "openid不可为空")
    private String openid;

    private String wechatId;

    private String userName;

    private Integer gender; //genderEnum

    private String profilePhotoUrl;

    @NotNull(message = "是否已授权不能为空")
    private Integer authorised;


    /* 教育背景 */

    private Integer eduDegree;

    private String university;

    private String graduationTime; //yyyy/MM

    private String major;


    /* 求职背景 */
    private List<Integer> targetFields;

    private List<Integer> targetPositions;
}
