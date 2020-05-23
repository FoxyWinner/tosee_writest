package com.tosee.tosee_writest.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChildQuestionBankForm
{
    private String childQbId;

    @NotEmpty(message = "主题库必选")
    private String parentQbId;

    @NotEmpty(message = "子题库名称必填")
    private String cqbTitle;

    @NotNull(message = "模拟时长必填")
    private Integer simulationTime;

    @NotEmpty(message = "发布时间必填")
    private String relaseTime;

    @NotNull(message = "热度必填")
    private Integer cqbHeat;

    // 默认是不推荐
    private Boolean isRecommended = false;
}
