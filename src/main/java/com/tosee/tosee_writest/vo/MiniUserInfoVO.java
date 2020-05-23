package com.tosee.tosee_writest.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/23 9:44 下午
 */
@Data
public class MiniUserInfoVO
{
    private String profilePhotoUrl;

    private String userName;

    private Integer authorised;

    private List<String> targetFields;

    private List<String> targetPositions;
}
