package com.tosee.tosee_writest.vo;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 11:02 下午
 */
@Data
public class MistakeBookVO
{
    @Id
    private String mistakeBookId;

    private String title;

    private Integer mistakeNumber;

    private String updateTime;
}
