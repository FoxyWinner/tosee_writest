package com.tosee.tosee_writest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tosee.tosee_writest.dataobject.QuestionOption;
import com.tosee.tosee_writest.enums.ParentQuestionBankTypeEnum;
import com.tosee.tosee_writest.enums.QuestionTypeEnum;
import com.tosee.tosee_writest.utils.EnumUtil;
import com.tosee.tosee_writest.vo.QuestionOptionVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.json.GsonJsonParser;

import java.util.ArrayList;
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

    @JsonIgnore
    public QuestionTypeEnum getQuestionTypeEnum()
    {
        return EnumUtil.getByCode(questionType, QuestionTypeEnum.class);
    }

    @JsonIgnore
    public String getOptionsJson()
    {
        List<QuestionOptionVO> questionOptionVOList = new ArrayList<>();
        // 为空的话肯定是问答题啦，不为空才填选项
        if(questionOptions != null)
        {
            for (QuestionOption questionOption : questionOptions)
            {
                QuestionOptionVO questionOptionVO = new QuestionOptionVO();
                BeanUtils.copyProperties(questionOption,questionOptionVO);
                questionOptionVOList.add(questionOptionVO);
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(questionOptionVOList);
    }
}
