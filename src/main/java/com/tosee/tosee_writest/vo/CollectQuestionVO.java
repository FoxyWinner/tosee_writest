package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:42 下午
 */
@Data
public class CollectQuestionVO
{
    private String collectQuestionId;

    // 收藏时间排序
    private Integer collectQuestionSeq;

    private String questionId;

    private Integer questionType;

    private String questionStem;

    @JsonProperty("questionOptions")
    private List<QuestionOptionVO> questionOptionVOList;

    private String answer;

    private String explanation;

    private Integer collectState;
}
