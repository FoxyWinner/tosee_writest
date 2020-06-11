package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tosee.tosee_writest.dataobject.QuestionOption;
import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:42 下午
 */
@Data
public class QuestionVO
{

    private String questionId;

    private Integer questionSeq;

    private Integer questionType;

    private String questionStem;

    @JsonProperty("questionOptions")
    private List<QuestionOptionVO> questionOptionVOList;

    /** 该属性表明了用户上次用途退出或完成时该题的答案 */
    private String userAnswer="";

    private String answer;

    private String explanation;

    private Integer collectState;

    private Integer isInMistake;
}
