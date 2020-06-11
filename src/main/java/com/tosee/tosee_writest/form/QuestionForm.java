package com.tosee.tosee_writest.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/13 11:30 下午
 */
@Data
public class QuestionForm
{
    private String questionId;

    @NotNull(message = "题目类型必选")
    private Integer questionType;

    @NotEmpty(message = "所属子题库必选")
    private String childQbId;

    @NotEmpty(message = "题干必填")
    private String questionStem;

    @NotEmpty(message = "答案必填")
    private String answer;

    @NotEmpty(message = "解析必填")
    private String explanation;
    
    private String questionOptions;
}
