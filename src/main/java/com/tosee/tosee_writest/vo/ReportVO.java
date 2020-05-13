package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/5 8:33 下午
 */
@Data
public class ReportVO
{
    private String childQbTitle;

    private Integer correctRatio;

    private Integer spentTime;

    private Integer completeNumber;

    private Integer questionNumber;

    private Integer surpassRatio;

    private List<String> answerList;

    private List<String> userAnswerList;

    private Integer lastMode;
}
