package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:42 下午
 */
@Data
public class ChildQuestionBankVO
{
    private String childQbId;

    private Integer questionNumber;

    @JsonProperty("title")
    private String cqbTitle;

    @JsonProperty("heat")
    private Integer cqbHeat;

    private Integer spentTime;

    private Integer simulationTime;

    //完成（单位个）
    private Integer completeNumber;

    //正确率
    @JsonProperty("correct")
    private Integer correctRatio;

    private Integer lastMode;

    private Integer collectState;

}
