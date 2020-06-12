package com.tosee.tosee_writest.vo;

import lombok.Data;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/22 11:07 下午
 */

@Data
public class PunchResultVO
{
    private Integer isFirstTimePunch;

    private Integer punchState;

    private String profilePhoto;

    private Integer completeNumber;

    private Integer correctRatio;

    private String exerciseTime;

    private Integer insistDays;
}
