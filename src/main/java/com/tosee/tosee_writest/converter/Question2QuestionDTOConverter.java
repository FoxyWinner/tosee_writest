package com.tosee.tosee_writest.converter;

import com.tosee.tosee_writest.dataobject.Question;
import com.tosee.tosee_writest.dto.QuestionDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:28 下午
 */
public class Question2QuestionDTOConverter
{
    public static QuestionDTO convert(Question question)
    {
        QuestionDTO questionDTO = new QuestionDTO();

        BeanUtils.copyProperties(question,questionDTO);
        return questionDTO;
    }

    public static List<QuestionDTO> convert(List<Question> questionList)
    {
        return questionList
                .stream()
                .map(e -> convert(e))
                .collect(Collectors.toList());
    }
}
