package com.tosee.tosee_writest.vo;

import lombok.Data;

import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/15 11:03 上午
 */
@Data
public class CollectBookVO
{
    @Id
    private String CollectBookId;

    private String title;

    private Integer collectNumber;

    private String updateTime;
}
