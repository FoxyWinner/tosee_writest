package com.tosee.tosee_writest.vo;

import com.tosee.tosee_writest.dataobject.WorkField;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/21 10:50 下午
 */

@Data
public class UserVO
{
    private String wechatId;

    private String userName;

    private String gender; //genderEnum

    private String profilePhotoUrl;

    private Integer authorised;

    /* 教育背景 */

    private String eduDegree;

    private String university;

    private String major;

    private String graduationTime; //yyyy/MM

    /* 求职背景 */
    private List<WorkFieldVO> targetFields;

    private List<WorkPostionVO> targetPositions;

}
