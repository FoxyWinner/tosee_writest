package com.tosee.tosee_writest.dto;

import lombok.Data;

import java.util.List;

/**
 * 该类把PracticeRecord类中的userAnswerList变为了链表存储
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:04 下午
 */

@Data
public class PracticeRecordDTO
{
    private String recordId;

    private String openid;

    private String childQbId;

    private Integer completeNumber;

    private List<String> userAnswerList;

    private Integer spentTime;

    private Integer complete;

    private Integer lastMode;

    private Integer correctRatio;

    private Integer surpassRatio;
}
