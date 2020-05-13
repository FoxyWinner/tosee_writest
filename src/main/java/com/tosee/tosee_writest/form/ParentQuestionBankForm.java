package com.tosee.tosee_writest.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ParentQuestionBankForm
{
    private String parentQbId;

    @NotNull(message = "题库类型必选")
    private Integer pqbType;

    @NotNull(message = "行业必选")
    private Integer fieldType;

    @NotNull(message = "岗位必选")
    private Integer positionType;

    @NotNull(message = "公司必选")
    private Integer companyId;

    @NotEmpty(message = "主题库名称必填")
    private String pqbTitle;

    @NotNull(message = "是否推荐必选")
    private Integer isRecommended;

    // 发布日期，默认显示当前

}
