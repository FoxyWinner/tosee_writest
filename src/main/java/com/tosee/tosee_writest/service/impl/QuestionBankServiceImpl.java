package com.tosee.tosee_writest.service.impl;
import com.tosee.tosee_writest.converter.ParentQB2ParentQBDTOConverter;
import com.tosee.tosee_writest.converter.Question2QuestionDTOConverter;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.ChildQuestionBankDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<ChildQuestionBank> searchChildQuestionBank(String search)
    {
        // 覆盖主题库岗位 主题库名 子题库名
        // 思路是创建集合，通过这些方式将ID填写进去，然后拿到实体，按热度排名
        Set<String> cqbIdsSet = new HashSet<>();


        // 岗位
//        List<WorkPosition> workPositionsSearched = workPositionRepository.findByPositionNameLike("%"+search+"%");



        // 按主题库名
        List<ParentQuestionBank> parentQuestionBanksSearched = parentQuestionBankRepository.findByPqbTitleLike("%"+search+"%");
        for (ParentQuestionBank parentQuestionBank : parentQuestionBanksSearched)
        {
            List<ChildQuestionBank> childQuestionBanks = this.findCQBListByPQBIdOrderBy(parentQuestionBank.getParentQbId(),QuestionBankSortEnum.SORT_BY_HEAT_DESC);
            for (ChildQuestionBank childQuestionBank : childQuestionBanks)
                cqbIdsSet.add(childQuestionBank.getChildQbId());
        }



        // 子题库名搜索
        List<ChildQuestionBank> childQuestionBanksSearched = childQuestionBankRepository.findByCqbTitleLike("%"+search+"%");
        for (ChildQuestionBank childQuestionBank : childQuestionBanksSearched)
            cqbIdsSet.add(childQuestionBank.getChildQbId());

        // 题库题干搜索
        List<Question> questionsSearched = questionRepository.findByQuestionStemLike("%"+search+"%");
        for (Question question : questionsSearched)
            cqbIdsSet.add(question.getChildQbId());



        List<ChildQuestionBank> result = childQuestionBankRepository.findAllById(cqbIdsSet);
        return result;
    }

    @Override
    public List<ChildQuestionBank> findHotPoint6CQBs()
    {
        return childQuestionBankRepository.findTop6ByOrderByCqbHeatDesc();
    }

    @Override
    public List<ParentQuestionBank> findPQBListByPositionTypeAndPqbTypeOrderBy(Integer positionType, Integer pqbType, QuestionBankSortEnum sortRule)
    {
        // 按热度排序
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return parentQuestionBankRepository.findByPositionTypeAndPqbTypeAndIsRelaseOrderByPqbHeatDesc(positionType,pqbType,1);

        }
        else return parentQuestionBankRepository.findByPositionTypeAndPqbTypeAndIsRelaseOrderByRelaseTimeDesc(positionType,pqbType,1);
    }

    @Override
    public List<ParentQuestionBank> findEPQBListByPositionTypesAndPqbTypeOrderBy(List<Integer> positionTypes, QuestionBankSortEnum sortRule)
    {
        // 按热度排序
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return parentQuestionBankRepository.findByPositionTypeInAndPqbTypeAndIsRelaseOrderByPqbHeatDesc(positionTypes,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode(),1);
        }
        else return parentQuestionBankRepository.findByPositionTypeInAndPqbTypeAndIsRelaseOrderByRelaseTimeDesc(positionTypes,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode(),1);
    }

    @Override
    public List<ParentQuestionBank> findEPQBListRecommendedOrderBy(QuestionBankSortEnum sortRule)
    {
        if(sortRule == QuestionBankSortEnum.SORT_BY_HEAT_DESC)
        {
            return parentQuestionBankRepository.findByIsRecommendedAndPqbTypeAndIsRelaseOrderByPqbHeatDesc(1,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode(),1);
        }
        else return parentQuestionBankRepository.findByIsRecommendedAndPqbTypeAndIsRelaseOrderByRelaseTimeDesc(1,ParentQuestionBankTypeEnum.ENTERPRISE_BANK.getCode(),1);
    }

    @Override
    public List<ParentQuestionBank> findAdministraPqb()
    {
        return parentQuestionBankRepository.findByPqbTypeAndIsRelaseOrderByPqbHeat(ParentQuestionBankTypeEnum.ADMINISTRATIVE_APTITUDE_BANK.getCode(),1);
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

    @Override
    public String getPQBTitle(String parentQbId)
    {
        ParentQuestionBank parentQuestionBank = parentQuestionBankRepository.findById(parentQbId).orElse(null);
        if (parentQuestionBank != null) return parentQuestionBank.getPqbTitle();
        else return "";
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
        log.info("【重算热度】主题库{}",parentQuestionBank);
        if(parentQuestionBank!=null)
        {
            Integer parentHeat = 0;
            List<ChildQuestionBank> childQuestionBanks = childQuestionBankRepository.findByParentQbIdOrderByCqbHeatDesc(parentQuestionBank.getParentQbId());

            if (childQuestionBanks.size() > 0)
            {
                for (ChildQuestionBank hisChildren : childQuestionBanks)
                {
                    parentHeat += hisChildren.getCqbHeat();
                }
            }

            parentQuestionBank.setPqbHeat(parentHeat);
            log.info("【重算热度】{}",parentQuestionBank);
            parentQuestionBankRepository.save(parentQuestionBank);
        }
    }

    @Override
    public Page<ParentQuestionBankDTO> findPQBList4Admin(Pageable pageable)
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
    public void relaseParentQuestionBank(ParentQuestionBank parentQuestionBank)
    {
        if (parentQuestionBank == null)
        {
            throw new WritestException(ResultEnum.PQB_NOT_EXIST);
        }
        parentQuestionBank.setIsRelase(1);
        parentQuestionBankRepository.save(parentQuestionBank);
    }

    @Override
    public void cancelParentQuestionBank(ParentQuestionBank parentQuestionBank)
    {
        if (parentQuestionBank == null)
        {
            throw new WritestException(ResultEnum.PQB_NOT_EXIST);
        }

        parentQuestionBank.setIsRelase(0);
        parentQuestionBankRepository.save(parentQuestionBank);
    }

    @Override
    public Page<ChildQuestionBankDTO> findCQBList(Pageable pageable)
    {
        Page<ChildQuestionBank> childQuestionBankPage =  childQuestionBankRepository.findAll(pageable);

        List<ChildQuestionBankDTO> childQuestionBankDTOS = new ArrayList<>();
        for (ChildQuestionBank childQuestionBank : childQuestionBankPage.getContent())
        {
            ChildQuestionBankDTO childQuestionBankDTO = new ChildQuestionBankDTO();
            BeanUtils.copyProperties(childQuestionBank,childQuestionBankDTO);
            childQuestionBankDTO.setParentQbTitle(this.getPQBTitle(childQuestionBankDTO.getParentQbId()));

            childQuestionBankDTOS.add(childQuestionBankDTO);
        }
        return new PageImpl<>(childQuestionBankDTOS, pageable,childQuestionBankPage.getTotalElements());
    }

    @Override
    public Page<ChildQuestionBankDTO> findCQBList(Pageable pageable, String pqbId)
    {
        Page<ChildQuestionBank> childQuestionBankPage =  childQuestionBankRepository.findByParentQbIdOrderByRelaseTime(pageable,pqbId);


        List<ChildQuestionBankDTO> childQuestionBankDTOS = new ArrayList<>();
        for (ChildQuestionBank childQuestionBank : childQuestionBankPage.getContent())
        {
            ChildQuestionBankDTO childQuestionBankDTO = new ChildQuestionBankDTO();
            BeanUtils.copyProperties(childQuestionBank,childQuestionBankDTO);
            childQuestionBankDTO.setParentQbTitle(this.getPQBTitle(childQuestionBankDTO.getParentQbId()));

            childQuestionBankDTOS.add(childQuestionBankDTO);
        }
        return new PageImpl<>(childQuestionBankDTOS, pageable,childQuestionBankPage.getTotalElements());
    }

    @Override
    public List<ChildQuestionBank> findAllCQBs()
    {
        return childQuestionBankRepository.findAll();
    }

    @Override
    public Page<QuestionDTO> findQuestionsDTOList(Pageable pageable)
    {
        Page<Question> questionPage = questionRepository.findAll(pageable);

        // 这里把questions转为questionDTOS
        List<QuestionDTO> questionDTOS = Question2QuestionDTOConverter.convert(questionPage.getContent());

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

        return new PageImpl<>(questionDTOS, pageable,questionPage.getTotalElements());
    }

    @Override
    public Page<QuestionDTO> findQuestionsDTOList(Pageable pageable, String cqbId)
    {
        Page<Question> questionPage = questionRepository.findByChildQbIdOrderByQuestionSeqAsc(cqbId,pageable);
        // 这里把questions转为questionDTOS
        List<QuestionDTO> questionDTOS = Question2QuestionDTOConverter.convert(questionPage.getContent());

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

        return new PageImpl<>(questionDTOS, pageable,questionPage.getTotalElements());
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
        // 先给所填答案去空格化
        questionDTO.setAnswer(questionDTO.getAnswer().trim());

        // 若是新增，要将CQB的questionNumber++
        Question question = questionRepository.findById(questionDTO.getQuestionId()).orElse(null);
        Question result;
        if (question == null)
        {
            // 要新增
            question = new Question();
            BeanUtils.copyProperties(questionDTO,question);
            result = questionRepository.save(question);
            // 新增之后子题库题目数量 + 1
            this.recountChildQuestionBankQuestionNumber(result.getChildQbId());
        }
        else
        {
            // 若为更改
            String lastChildQbId = question.getChildQbId();
            // copy完之后更改后的子题库ID可能已经存入question中
            BeanUtils.copyProperties(questionDTO,question);

            result = questionRepository.save(question);

            // 如果这次更改更改了子题库所属，那么前后子题库都要更新计数，题目题号也要重新维护
            if (!lastChildQbId.equals(question.getChildQbId()))
            {
                this.recountChildQuestionBankQuestionNumber(lastChildQbId);
                this.recountChildQuestionBankQuestionNumber(result.getChildQbId());

                this.resortQuestionSeq(lastChildQbId);
                this.resortQuestionSeq(result.getChildQbId());

            }

        }


        // 无论更改还是新增，非问答题，新存选项，如果以前有则自动覆盖，没有则创建（以ID为基准的）
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
        return result;
    }

    @Transactional
    @Override
    public ChildQuestionBank saveChildQuestionBank(ChildQuestionBank childQuestionBank)
    {

        ChildQuestionBank existChildQuestionBank = childQuestionBankRepository.findById(childQuestionBank.getChildQbId()).orElse(null);


        // 存ChildQuestionBank
        ChildQuestionBank result = childQuestionBankRepository.save(childQuestionBank);

        // 新增和修改做出不同的对主题库的热度、子题库数的维护
        if (existChildQuestionBank == null)
        {
            log.info("【新增子题库】更新了计数和热度");
            this.increaseParentQuestionBankCQBNum(childQuestionBank.getParentQbId());
            this.updateParentQuestionBankHeat(childQuestionBank.getParentQbId());

        }else
        {
            log.info("【修改子题库】更新了计数和热度");
            this.recountParentQuestionBankCQBNum(existChildQuestionBank.getParentQbId());
            this.updateParentQuestionBankHeat(existChildQuestionBank.getParentQbId());
            this.recountParentQuestionBankCQBNum(result.getParentQbId());
            this.updateParentQuestionBankHeat(result.getParentQbId());
        }

        return result;
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
        this.recountChildQuestionBankQuestionNumber(childQbId);

        // 题目序号重排列并保存
        this.resortQuestionSeq(childQbId);
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
     * 运营添加或者更改题目所属子题库时，更新子题库题目数计数
     * @param childQbId
     */
    private void recountChildQuestionBankQuestionNumber(String childQbId)
    {
        ChildQuestionBank childQuestionBank = this.findCQBById(childQbId);
        childQuestionBank.setQuestionNumber(questionRepository.countByChildQbId(childQbId));
        childQuestionBankRepository.save(childQuestionBank);
    }

    /**
     * 重新为该题库排列题号
     * @param childQbId
     */
    private void resortQuestionSeq(String childQbId)
    {
        List<QuestionDTO> questionDTOS = this.findQuestionListByCQBId(childQbId);

        int i = 1;
        for (QuestionDTO questionDTO : questionDTOS)
        {
            questionDTO.setQuestionSeq(i);
            Question question = new Question();
            BeanUtils.copyProperties(questionDTO,question);
            questionRepository.save(question);
            i++;
        }
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

    private void recountParentQuestionBankCQBNum(String parentQbId)
    {
        ParentQuestionBank parentQuestionBank = this.findOneParentQuestionBank(parentQbId);
        parentQuestionBank.setCqbNumber(childQuestionBankRepository.countByParentQbId(parentQbId));

        parentQuestionBankRepository.save(parentQuestionBank);
    }

}
