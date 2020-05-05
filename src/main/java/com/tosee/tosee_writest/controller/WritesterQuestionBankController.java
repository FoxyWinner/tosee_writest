package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.converter.RecordForm2PracticeRecordDTOConverter;
import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import com.tosee.tosee_writest.dataobject.ParentQuestionBank;
import com.tosee.tosee_writest.dataobject.PracticeRecord;
import com.tosee.tosee_writest.dataobject.QuestionOption;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.QuestionBankSortEnum;
import com.tosee.tosee_writest.enums.RecordLastModeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.form.RecordForm;
import com.tosee.tosee_writest.service.CompanyService;
import com.tosee.tosee_writest.service.FavoriteService;
import com.tosee.tosee_writest.service.PracticeRecordService;
import com.tosee.tosee_writest.service.QuestionBankService;
import com.tosee.tosee_writest.utils.EnumUtil;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 1:52 下午
 */

@RestController
@RequestMapping("/writester/questionbank")
@Slf4j
public class WritesterQuestionBankController
{
    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PracticeRecordService practiceRecordService;

    @GetMapping("/parentqblist")
    public ResultVO parentQuestionBankList(@RequestParam("openid") String openid,
                                           @RequestParam("positionType") Integer positionType,
                                           @RequestParam("pqbType")Integer pqbType,
                                           @RequestParam(value = "collation", defaultValue = "1")Integer collation,

                                           @RequestParam(value = "page",defaultValue = "0") Integer page,
                                           @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询子题库列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(positionType))
        {
            log.error("【查询子题库列表】positionType为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(pqbType))
        {
            log.error("【查询子题库列表】pqbType为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<ParentQuesitonBankVO> questionBankVOList = new ArrayList<>();
        List<ParentQuestionBank> questionBankList = questionBankService.findPQBListByPositionTypeAndPqbTypeOrderBy(positionType,pqbType, EnumUtil.getByCode(collation, QuestionBankSortEnum.class));

        for (ParentQuestionBank questionBank : questionBankList)
        {
            ParentQuesitonBankVO parentQuesitonBankVO = new ParentQuesitonBankVO();
            parentQuesitonBankVO.setPqbId(questionBank.getParentQbId());
            parentQuesitonBankVO.setIconUrl(companyService.findCompanyIconUrlByID(questionBank.getCompanyId()));
            parentQuesitonBankVO.setCqbNumber(questionBank.getCqbNumber());
            parentQuesitonBankVO.setHeat(questionBank.getPqbHeat());
            parentQuesitonBankVO.setPqbTitle(questionBank.getPqbTitle());

            questionBankVOList.add(parentQuesitonBankVO);
        }

        return ResultVOUtil.success(questionBankVOList);
    }

    @GetMapping("/childqblist")
    public ResultVO childQuestionBankList(@RequestParam("openid") String openid,
                                          @RequestParam("pqbId") String pqbId,
                                          @RequestParam(value = "collation", defaultValue = "1")Integer collation,

                                          @RequestParam(value = "page",defaultValue = "0") Integer page,
                                          @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询子题库列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(pqbId))
        {
            log.error("【查询子题库列表】pqbId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        //1. 获取childQuesitonBanks并逐个赋值到VO
        // 按照排序规则排序
        List<ChildQuestionBank> childQuestionBanks = questionBankService.findCQBListByPQBIdOrderBy(pqbId,EnumUtil.getByCode(collation, QuestionBankSortEnum.class));
        List<ChildQuestionBankVO> result = new ArrayList<>();

        for (ChildQuestionBank childQuestionBank : childQuestionBanks)
        {
            ChildQuestionBankVO childQuestionBankVO = new ChildQuestionBankVO();
            BeanUtils.copyProperties(childQuestionBank,childQuestionBankVO);

            // 2.为每个VO写入"练习记录"关系值
            PracticeRecord practiceRecord = questionBankService.findPracticeRecord(openid,childQuestionBankVO.getChildQbId());
            if(practiceRecord == null)
            {
                childQuestionBankVO.setSpentTime(0);
                childQuestionBankVO.setCompleteNumber(0);
                childQuestionBankVO.setCorrectRatio(-1);
                childQuestionBankVO.setLastMode(RecordLastModeEnum.UNKOWN_MODE.getCode()); // 暂时传0
            }else
            {
                childQuestionBankVO.setSpentTime(practiceRecord.getSpentTime());
                childQuestionBankVO.setCompleteNumber(practiceRecord.getCompleteNumber());
                childQuestionBankVO.setCorrectRatio(practiceRecord.getCorrectRatio());
                childQuestionBankVO.setLastMode(practiceRecord.getLastMode());
            }


            // 3.为每个VO写入"收藏夹"关系值
            childQuestionBankVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid,FavoriteTypeEnum.CHILD_QUESTIONBANK.getCode(),childQuestionBank.getChildQbId()));

            result.add(childQuestionBankVO);
        }

        return ResultVOUtil.success(result);
    }


    @GetMapping("/questionslist")
    public ResultVO questionsList(@RequestParam("openid") String openid,
                                  @RequestParam("cqbId") String cqbId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询题目列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(cqbId))
        {
            log.error("【查询题目列表】cqbId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        //TODO 这套子题库热度++

        //1. 用Service查询questionDTOs转化为VO
        List<QuestionDTO> questionDTOList = questionBankService.findQuestionListByCQBId(cqbId);
        List<QuestionVO> result = new ArrayList<>();
        for (int i = 0 ; i < questionDTOList.size() ; i++)
        {
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(questionDTOList.get(i),questionVO);

            List<QuestionOptionVO> questionOptionVOList = new ArrayList<>();
            // 为空的话肯定是问答题啦
            if(questionDTOList.get(i).getQuestionOptions() != null)
            {
                for (QuestionOption questionOption : questionDTOList.get(i).getQuestionOptions())
                {
                    QuestionOptionVO questionOptionVO = new QuestionOptionVO();
                    BeanUtils.copyProperties(questionOption,questionOptionVO);
                    questionOptionVOList.add(questionOptionVO);
                }

                questionVO.setQuestionOptionVOList(questionOptionVOList);
            }


            // TODO 填入上次做题历史作答
            questionVO.setUserAnswer("");
            questionVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid, FavoriteTypeEnum.QUESTION.getCode(),questionVO.getQuestionId()));
            result.add(questionVO);
        }

        return ResultVOUtil.success(result);
    }

    @GetMapping("/report")
    public ResultVO report(@RequestParam("openid") String openid,
                           @RequestParam("cqbId") String cqbId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询作答报告】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(cqbId))
        {
            log.error("【查询作答报告】cqbId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        // 我发现不需要在数据库里存储answerList，用户需要报告的时候现查就是了
//         correctRatio; spentTime; completeNumber; questionNumber;surpassRatio;answerList; userAnswerList;
        // todo 一定要给数据库加questionSeq 而且查询list的时候按题号升序
        ReportVO reportVO = new ReportVO();

        PracticeRecordDTO practiceRecordDTO = practiceRecordService.findRecord(openid,cqbId);
        log.info("【查询作答报告】practiceRecordDTO{}",practiceRecordDTO);
        BeanUtils.copyProperties(practiceRecordDTO,reportVO);

        // 填入AnwserList
        List<String> answerList = questionBankService.findAnswerList(cqbId);
        reportVO.setAnswerList(answerList);
        //填入题库题数
        reportVO.setQuestionNumber(questionBankService.getQuestionNumber(cqbId));
        return ResultVOUtil.success(reportVO);
    }

    @PostMapping("/record")
    public ResultVO practicerecord(@Valid RecordForm recordForm,
                                   BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            log.error("【创建记录】参数不正确，recordForm={}",recordForm);
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        log.info("【创建记录】用户作答列表为{}",recordForm.getUserAnswerList());

        PracticeRecordDTO practiceRecordDTO = RecordForm2PracticeRecordDTOConverter.convert(recordForm);
        practiceRecordService.addOrUpdateRecord(practiceRecordDTO);

        Map<String, Integer> map = new HashMap<>();

        // todo 现在surpassRatio是空的，我想要上面addOrUpdateRecord返回surpassRatio
//        map.put("surpassRatio",practiceRecordDTO.getSurpassRatio());
        map.put("surpassRatio",100);
        return ResultVOUtil.success(map);
    }
}
