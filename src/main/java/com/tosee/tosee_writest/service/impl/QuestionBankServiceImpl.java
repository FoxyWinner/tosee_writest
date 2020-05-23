package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.converter.ParentQB2ParentQBDTOConverter;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 1:47 下午
 */
@Service
@Slf4j
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
    public List<ParentQuestionBank> findEPQBListByPositionTypesAndPqbTypeOrderBy(List<Integer> positionTypes, QuestionBankSortEnum sortRule)
    {
        // 按热度排序
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return parentQuestionBankRepository.findByPositionTypeInAndPqbTypeOrderByPqbHeatDesc(positionTypes,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode());
        }
        else return parentQuestionBankRepository.findByPositionTypeInAndPqbTypeOrderByRelaseTimeDesc(positionTypes,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode());
    }

    @Override
    public List<ParentQuestionBank> findEPQBListRecommendedOrderBy(QuestionBankSortEnum sortRule)
    {
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return parentQuestionBankRepository.findByIsRecommendedAndPqbTypeOrderByPqbHeatDesc(1,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode());
        }
        else return parentQuestionBankRepository.findByIsRecommendedAndPqbTypeOrderByRelaseTimeDesc(1,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode());
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
    public ChildQuestionBank findCQBById(String childQbId)
    {
        return childQuestionBankRepository.findById(childQbId).orElse(null);
    }


    /**
     * 该函数应为按照openid查询给该用户推荐的子题库列表，先暂时传的被编辑点了推荐的子题库
     * @param openid
     * @return
     */
    @Override
    public List<ChildQuestionBank> findRecommendedCQB(String openid)
    {
        List<ChildQuestionBank> result = childQuestionBankRepository.findByIsRecommendedOrderByCqbHeatDesc(1);
        return result;
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
            ChildQuestionBank childQuestionBank = childQuestionBankRepository.findById(questionDTO.getChildQbId()).orElse(null);
            if (childQuestionBank !=null)  questionDTO.setChildQbTitle(childQuestionBank.getCqbTitle());

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
            answerList.add(questionDTO.getAnswer().trim());
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
    public Integer getSimulationTime(String childQbId)
    {
        ChildQuestionBank childQuestionBank = childQuestionBankRepository.findById(childQbId).orElse(null);
        if(childQuestionBank != null) return childQuestionBank.getSimulationTime();
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
            this.updateParentQuestionBankHeat(childQuestionBank.getParentQbId());
        }
    }

    @Override
    public void updateParentQuestionBankHeat(String parentQbId)
    {
        // 重新计算父题库热度
        ParentQuestionBank parentQuestionBank = parentQuestionBankRepository.findById(parentQbId).orElse(null);
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
    public Page<ChildQuestionBank> findCQBList(Pageable pageable)
    {
        Page<ChildQuestionBank> childQuestionBankPage =  childQuestionBankRepository.findAll(pageable);

        return childQuestionBankPage;
    }

    @Override
    public Page<ChildQuestionBank> findCQBList(Pageable pageable, String pqbId)
    {
        Page<ChildQuestionBank> childQuestionBankPage =  childQuestionBankRepository.findByParentQbIdOrderByRelaseTime(pageable,pqbId);

        return childQuestionBankPage;
    }

    @Override
    public List<ChildQuestionBank> findAllCQBs()
    {
        return childQuestionBankRepository.findAll();
    }

    @Override
    public Page<QuestionDTO> findQuestionsDTOList(Pageable pageable)
    {
        List<Question> questions = questionRepository.findAll();

        // 这里把questions转为questionDTOS
        List<QuestionDTO> questionDTOS = Question2QuestionDTOConverter.convert(questions);

        //为DTOList里每个QuestionDTO填充他的选项List，且填入子题库名
        for (QuestionDTO questionDTO : questionDTOS)
        {
            ChildQuestionBank childQuestionBank = childQuestionBankRepository.findById(questionDTO.getChildQbId()).orElse(null);
            if (childQuestionBank !=null)  questionDTO.setChildQbTitle(childQuestionBank.getCqbTitle());

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

        return new PageImpl<>(questionDTOS, pageable,questionDTOS.size());
    }

    @Override
    public Page<QuestionDTO> findQuestionsDTOList(Pageable pageable, String cqbId)
    {
        List<QuestionDTO> questionDTOS = this.findQuestionListByCQBId(cqbId);
        return new PageImpl<>(questionDTOS, pageable,questionDTOS.size());
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

    @Override
    public List<ParentQuestionBankDTO> findAllPQBDTOs()
    {
        List<ParentQuestionBank> parentQuestionBankList =parentQuestionBankRepository.findAll();
        List<ParentQuestionBankDTO> result = new ArrayList<>();

        for (ParentQuestionBank parentQuestionBank : parentQuestionBankList)
        {
            ParentQuestionBankDTO parentQuestionBankDTO = ParentQB2ParentQBDTOConverter.convert(parentQuestionBank);

            // converter中无法调用service或repository，需要在这填入实体公司 行业 职位
            if(parentQuestionBank.getCompanyId() != null)
            {
                Company company = companyRepository.findById(parentQuestionBank.getCompanyId()).orElse(null);
                parentQuestionBankDTO.setCompany(company);

            }

            parentQuestionBankDTO.setWorkField(workFieldRepository.findByFieldType(parentQuestionBank.getFieldType()));
            parentQuestionBankDTO.setWorkPosition(workPositionRepository.findByPositionType(parentQuestionBank.getPositionType()));

            result.add(parentQuestionBankDTO);
        }

        return result;
    }

    @Override
    public List<ParentQuestionBank> findAllPQBs()
    {
        return parentQuestionBankRepository.findAll();
    }

    @Transactional
    @Override
    public Question saveQuestionDTO(QuestionDTO questionDTO)
    {
        // 若是新增，要将CQB的questionNumber++
        Question question = questionRepository.findById(questionDTO.getQuestionId()).orElse(null);
        if (question == null) this.increaseChildQuestionBankQuestionNumber(questionDTO.getChildQbId());

        // 非问答题，存选项
        if(questionDTO.getQuestionType() != QuestionTypeEnum.ESSAY_QUESTION.getCode())
        {
            if(CollectionUtils.isEmpty(questionDTO.getQuestionOptions()))
            {
                log.error("【存储新题】题目不为问答题时，选项不许为空");
                throw new WritestException(ResultEnum.OPTIONS_IS_EMPTY);
            }
            this.saveQuestionOptions(questionDTO.getQuestionOptions());
        }

        // 存Question
        question = new Question();
        BeanUtils.copyProperties(questionDTO,question);
        // 答案去空格化处理
        question.setAnswer(question.getAnswer().trim());
        return questionRepository.save(question);
    }

    @Transactional
    @Override
    public ChildQuestionBank saveChildQuestionBank(ChildQuestionBank childQuestionBank)
    {
        // 若是新增，要将PQB的questionNumber++
        ChildQuestionBank existChildQuestionBank = childQuestionBankRepository.findById(childQuestionBank.getChildQbId()).orElse(null);
        if (existChildQuestionBank == null) this.increaseParentQuestionBankCQBNum(childQuestionBank.getParentQbId());

        // 存ChildQuestionBank
        return childQuestionBankRepository.save(childQuestionBank);
    }

    @Transactional
    @Override
    public String deleteQuestionById(String questionId)
    {
        // 暂存子题库ID，删除Question
        String childQbId = null;
        Question question = questionRepository.findById(questionId).orElse(null);
        if(question!=null)
        {
            childQbId = question.getChildQbId();
            questionRepository.delete(question);
        }

        // 子题库题目数量重数
        ChildQuestionBank childQuestionBank = this.findCQBById(childQbId);
        childQuestionBank.setQuestionNumber(this.findQuestionListByCQBId(childQbId).size());
        childQuestionBankRepository.save(childQuestionBank);

        // 题目序号重排列并保存
        List<QuestionDTO> questionDTOS = this.findQuestionListByCQBId(childQbId);
        int i = 0;
        for (QuestionDTO questionDTO : questionDTOS)
        {
            questionDTO.setQuestionSeq(i + 1);
            this.saveQuestionDTO(questionDTO);
            i++;
        }

        return childQbId;
    }

    /**
     * 私有方法，被saveQuestionDTO调用。
     * 将选项列表存入选项表中，以questionOptionId为基准判断到底是更改还是新增。
     * @param questionOptions
     */
    private void saveQuestionOptions(List<QuestionOption> questionOptions)
    {
        for (QuestionOption questionOption : questionOptions)
        {
            questionOptionRepository.save(questionOption);
        }
    }

    /**
     * 运营添加题目时，让对应的childQbNumber加1。
     * @param childQbId
     */
    private void increaseChildQuestionBankQuestionNumber(String childQbId)
    {
        ChildQuestionBank childQuestionBank = this.findCQBById(childQbId);
        childQuestionBank.setQuestionNumber(childQuestionBank.getQuestionNumber() + 1);

        childQuestionBankRepository.save(childQuestionBank);
    }

    /**
     * 运营添加子题库时，让对应的cqbNum加1。
     * @param parentQbId
     */
    private void increaseParentQuestionBankCQBNum(String parentQbId)
    {
        ParentQuestionBank parentQuestionBank = this.findOneParentQuestionBank(parentQbId);
        parentQuestionBank.setCqbNumber(parentQuestionBank.getCqbNumber() + 1);

        parentQuestionBankRepository.save(parentQuestionBank);
    }

}
