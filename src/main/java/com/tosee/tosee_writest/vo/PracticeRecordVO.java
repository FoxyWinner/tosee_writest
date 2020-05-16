package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/14 8:31 下午
 */
@Data
public class PracticeRecordVO
{

    private String recordId;

    private String childQbId;

    @JsonProperty("title")
    private String childQbTitle;

    private Integer complete;

    private Integer completeNumber;

    private Integer spentTime;

    private Integer lastMode;

    @JsonProperty("correct")
    private Integer correctRatio;

    private String updateTime;

    // 额外属性
    private Integer questionNumber;

    private Integer simulationTime;
}
