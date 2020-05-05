package com.tosee.tosee_writest.dto;

import com.tosee.tosee_writest.dataobject.QuestionOption;
import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:15 下午
 */
@Data
public class QuestionDTO
{
    private String questionId;

    private String childQbId;

    private Integer questionType;

    private Integer questionSeq;

    private String questionStem;

    private List<QuestionOption> questionOptions;

    private String answer;

    private String explanation;
}
