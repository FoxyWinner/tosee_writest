package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.converter.Question2QuestionDTOConverter;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.QuestionBankSortEnum;
import com.tosee.tosee_writest.enums.QuestionTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.*;
import com.tosee.tosee_writest.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 1:47 下午
 */
@Service
public class QuestionBankServiceImpl implements QuestionBankService
{
    @Autowired
    private ParentQuestionBankRepository parentQuestionBankRepository;

    @Autowired
    private ChildQuestionBankRepository childQuestionBankRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private PracticeRecordRepository practiceRecordRepository;

    @Override
    public List<ParentQuestionBank> findPQBListByPositionTypeAndPqbTypeOrderBy(Integer positionType, Integer pqbType, QuestionBankSortEnum sortRule)
    {
        // 按热度排序
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return parentQuestionBankRepository.findByPositionTypeAndPqbTypeOrderByPqbHeatDesc(positionType,pqbType);
        }
        else return parentQuestionBankRepository.findByPositionTypeAndPqbTypeOrderByRelaseTimeDesc(positionType,pqbType);
    }

    @Override
    public List<ChildQuestionBank> findCQBListByPQBIdOrderBy(String parentQbId, QuestionBankSortEnum sortRule)
    {
        // 按热度排序
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return childQuestionBankRepository.findByParentQbIdOrderByCqbHeatDesc(parentQbId);
        }
        else return childQuestionBankRepository.findByParentQbIdOrderByRelaseTimeDesc(parentQbId);
    }

    @Override
    public List<QuestionDTO> findQuestionListByCQBId(String childQbId)
    {
        List<Question> questions = questionRepository.findQuestionsByChildQbIdOrderByQuestionSeqAsc(childQbId);

        // 这里把questions转为questionDTOS
        List<QuestionDTO> questionDTOS = Question2QuestionDTOConverter.convert(questions);

        //为DTOList里每个QuestionDTO填充他的选项List
        for (QuestionDTO questionDTO : questionDTOS)
        {
            if(questionDTO.getQuestionType()!= QuestionTypeEnum.ESSAY_QUESTION.getCode())
            {
                List<QuestionOption> questionOptions = questionOptionRepository.findByQuestionIdOrderByOptionNameAsc(questionDTO.getQuestionId());

                if(CollectionUtils.isEmpty(questionOptions))
                {
                    throw new WritestException(ResultEnum.OPTIONS_NOT_EXIST);
                }

                questionDTO.setQuestionOptions(questionOptions);
            }
        }

        return questionDTOS;
    }

    @Override
    public PracticeRecord findPracticeRecord(String openid, String childQbId)
    {
        return practiceRecordRepository.findByOpenIdAndAndChildQbId(openid,childQbId);
    }

    @Override
    public List<String> findAnswerList(String childQbId)
    {
        List<String> answerList = new ArrayList<>();
        List<QuestionDTO> questionDTOS = this.findQuestionListByCQBId(childQbId);
        for (QuestionDTO questionDTO : questionDTOS)
        {
            answerList.add(questionDTO.getAnswer());
        }
        return answerList;
    }

    @Override
    public Integer getQuestionNumber(String childQbId)
    {
        ChildQuestionBank childQuestionBank = childQuestionBankRepository.findById(childQbId).orElse(null);
        if(childQuestionBank != null) return childQuestionBank.getQuestionNumber();
        else return 0;
    }
}
