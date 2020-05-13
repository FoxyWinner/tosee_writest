package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.converter.ParentQB2ParentQBDTOConverter;
import com.tosee.tosee_writest.converter.PracticeRecord2PracticeRecordDTOConverter;
import com.tosee.tosee_writest.converter.Question2QuestionDTOConverter;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.ParentQuestionBankDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.ParentQuestionBankTypeEnum;
import com.tosee.tosee_writest.enums.QuestionBankSortEnum;
import com.tosee.tosee_writest.enums.QuestionTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.*;
import com.tosee.tosee_writest.service.QuestionBankService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private WorkPositionRepository workPositionRepository;

    @Autowired
    private WorkFieldRepository workFieldRepository;
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
    public List<ParentQuestionBank> findAdministraPqb()
    {
        return parentQuestionBankRepository.findByPqbTypeOrderByPqbHeat(ParentQuestionBankTypeEnum.ADMINISTRATIVE_APTITUDE_BANK.getCode());
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
    public QuestionDTO findQuestionById(String questionId)
    {
        Question question = questionRepository.findById(questionId).orElse(null);
        QuestionDTO result = new QuestionDTO();
        if (question != null)
        {
            result = Question2QuestionDTOConverter.convert(question);

            if(result.getQuestionType()!= QuestionTypeEnum.ESSAY_QUESTION.getCode())
            {
                List<QuestionOption> questionOptions = questionOptionRepository.findByQuestionIdOrderByOptionNameAsc(result.getQuestionId());

                if(CollectionUtils.isEmpty(questionOptions))
                {
                    throw new WritestException(ResultEnum.OPTIONS_NOT_EXIST);
                }

                result.setQuestionOptions(questionOptions);
            }
        }

        return result;
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

    @Override
    public String getCQbTitle(String childQbId)
    {
        ChildQuestionBank childQuestionBank = childQuestionBankRepository.findById(childQbId).orElse(null);
        if(childQuestionBank != null) return childQuestionBank.getCqbTitle();
        else  return "";
    }

    //todo 这个方法感觉异步调用就可以
    @Override
    public void increaseChildQuestionBankHeat(String childQbId)
    {
        ChildQuestionBank childQuestionBank = childQuestionBankRepository.findById(childQbId).orElse(null);
        if(childQuestionBank != null)
        {
            Integer newHeat = childQuestionBank.getCqbHeat()+1;
            childQuestionBank.setCqbHeat(newHeat);

            childQuestionBankRepository.save(childQuestionBank);

            // 重新计算父题库热度
            ParentQuestionBank parentQuestionBank = parentQuestionBankRepository.findById(childQuestionBank.getParentQbId()).orElse(null);
            if(parentQuestionBank!=null)
            {
                Integer parentHeat = 0;
                List<ChildQuestionBank> childQuestionBanks = childQuestionBankRepository.findByParentQbIdOrderByCqbHeatDesc(parentQuestionBank.getParentQbId());
                for (ChildQuestionBank hisChildren : childQuestionBanks)
                {
                    parentHeat += hisChildren.getCqbHeat();
                }
                parentQuestionBank.setPqbHeat(parentHeat);
                parentQuestionBankRepository.save(parentQuestionBank);
            }

        }
    }

    @Override
    public Page<ParentQuestionBankDTO> findPQBList(Pageable pageable)
    {
        Page<ParentQuestionBank> parentQuestionBankPage =  parentQuestionBankRepository.findAll(pageable);

        List<ParentQuestionBank> questionBankList = parentQuestionBankPage.getContent();
        List<ParentQuestionBankDTO> dtoList = new ArrayList<>();

        for (ParentQuestionBank questionBank : questionBankList)
        {

            ParentQuestionBankDTO parentQuestionBankDTO = ParentQB2ParentQBDTOConverter.convert(questionBank);

            // converter中无法调用service或repository，需要在这填入实体公司 行业 职位
            if(questionBank.getCompanyId() != null)
            {
                Company company = companyRepository.findById(questionBank.getCompanyId()).orElse(null);
                parentQuestionBankDTO.setCompany(company);

            }

            parentQuestionBankDTO.setWorkField(workFieldRepository.findByFieldType(questionBank.getFieldType()));
            parentQuestionBankDTO.setWorkPosition(workPositionRepository.findByPositionType(questionBank.getPositionType()));
            dtoList.add(parentQuestionBankDTO);
        }
//        List<ParentQuestionBankDTO> dtoList = ParentQB2ParentQBDTOConverter.convert(parentQuestionBankPage.getContent());

        return new PageImpl<>(dtoList, pageable, parentQuestionBankPage.getTotalElements());
    }

    @Override
    public ParentQuestionBankDTO findOneParentQuestionBankDTO(String pqbId)
    {
        ParentQuestionBank parentQuestionBank = parentQuestionBankRepository.findById(pqbId).orElse(null);
        ParentQuestionBankDTO result = ParentQB2ParentQBDTOConverter.convert(parentQuestionBank);

        // converter中无法调用service或repository，需要在这填入实体公司 行业 职位
        if(parentQuestionBank.getCompanyId() != null)
        {
            Company company = companyRepository.findById(parentQuestionBank.getCompanyId()).orElse(null);
            result.setCompany(company);

        }

        result.setWorkField(workFieldRepository.findByFieldType(parentQuestionBank.getFieldType()));
        result.setWorkPosition(workPositionRepository.findByPositionType(parentQuestionBank.getPositionType()));

        return result;
    }

    @Override
    public ParentQuestionBank findOneParentQuestionBank(String pqbId)
    {
        ParentQuestionBank parentQuestionBank = parentQuestionBankRepository.findById(pqbId).orElse(null);
        return parentQuestionBank;
    }

    @Override
    public ParentQuestionBank saveParentQuestionBankDTO(ParentQuestionBankDTO parentQuestionBankDTO)
    {
        // 先将DTO转BEAN，然后存BEAN
        ParentQuestionBank parentQuestionBank = new ParentQuestionBank();
        BeanUtils.copyProperties(parentQuestionBankDTO,parentQuestionBank);
        parentQuestionBank.setCompanyId(parentQuestionBankDTO.getCompany().getCompanyId());
        parentQuestionBank.setFieldType(parentQuestionBankDTO.getWorkField().getFieldType());
        parentQuestionBank.setPositionType(parentQuestionBankDTO.getWorkPosition().getPositionType());

        return parentQuestionBankRepository.save(parentQuestionBank);
    }

    @Override
    public ParentQuestionBank saveParentQuestionBank(ParentQuestionBank parentQuestionBank)
    {
        return parentQuestionBankRepository.save(parentQuestionBank);
    }

    @Override
    public ChildQuestionBank findById(String childQbId)
    {
        return childQuestionBankRepository.findById(childQbId).orElse(null);
    }
}
