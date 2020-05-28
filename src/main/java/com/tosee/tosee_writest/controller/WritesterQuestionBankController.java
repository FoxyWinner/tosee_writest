package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.converter.PracticeRecord2PracticeRecordDTOConverter;
import com.tosee.tosee_writest.converter.RecordForm2PracticeRecordDTOConverter;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.*;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.form.RecordForm;
import com.tosee.tosee_writest.service.*;
import com.tosee.tosee_writest.utils.EnumUtil;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private MistakeBookService mistakeBookService;

    @PostMapping("/enterpriseqblist")
    public ResultVO parentQuestionBankListTest(@RequestParam("openid") String openid,
                                           @RequestParam(value = "positionTypes" ,required=false) List<Integer> positionTypes,
                                           @RequestParam(value = "collation", defaultValue = "1")Integer collation,

                                           @RequestParam(value = "page",defaultValue = "0") Integer page,
                                           @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【企业真题子题库列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<ParentQuesitonBankVO> questionBankVOList = new ArrayList<>();
        List<ParentQuestionBank> questionBankList;
        if(CollectionUtils.isEmpty(positionTypes))
        {
            // 不传positionTypes的时候应该返回推荐列表
            questionBankList = questionBankService.findEPQBListRecommendedOrderBy(EnumUtil.getByCode(collation, QuestionBankSortEnum.class));
        }
        else
        {
            // 传了的话按照选择的岗位返回主题库列表
            questionBankList = questionBankService.findEPQBListByPositionTypesAndPqbTypeOrderBy(positionTypes, EnumUtil.getByCode(collation, QuestionBankSortEnum.class));
        }

        // 转VO
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

        //获取childQuesitonBanks并填入记录相关信息到VO
        List<ChildQuestionBank> childQuestionBanks = questionBankService.findCQBListByPQBIdOrderBy(pqbId,EnumUtil.getByCode(collation, QuestionBankSortEnum.class));
        List<ChildQuestionBankVO> result = this.convertToChildQuestionBankVOList(childQuestionBanks,openid);

        return ResultVOUtil.success(result);
    }



    @GetMapping("/specialpractice")
    public ResultVO specialpracticeList(@RequestParam("openid") String openid,
                                           @RequestParam("positionType") Integer positionType,
                                           @RequestParam(value = "collation", defaultValue = "1")Integer collation,

                                           @RequestParam(value = "page",defaultValue = "0") Integer page,
                                           @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        List<ChildQuestionBankVO> result = new ArrayList<>();

        // 1. 先根据positionType拿到专项练习父题库ID(一个行业只对应一个专业知识父题库，根据这个父题库ID查找子题库列表)
        ParentQuestionBank parentQuestionBank = new ParentQuestionBank();
        List<ParentQuestionBank> parentQuestionBanks = questionBankService.findPQBListByPositionTypeAndPqbTypeOrderBy(positionType,ParentQuestionBankTypeEnum.PROFESSIONAL_BANK.getCode(),QuestionBankSortEnum.SORT_BY_HEAT_DESC);
        if(parentQuestionBanks.size() != 1)
        {
            log.error("【查专业知识子题库列表】未上传该专业知识题库，positionType为{}",positionType);
            return ResultVOUtil.success(result);
        }
        parentQuestionBank = parentQuestionBanks.get(0);

        // 2. 再根据父题库ID拿到专项练习子题库列表
        List<ChildQuestionBank> childQuestionBanks = new ArrayList<>();
        childQuestionBanks = questionBankService.findCQBListByPQBIdOrderBy(parentQuestionBank.getParentQbId(),QuestionBankSortEnum.SORT_BY_HEAT_DESC);

        // 3. VO转换
        result = this.convertToChildQuestionBankVOList(childQuestionBanks,openid);

        return ResultVOUtil.success(result);
    }

    //administrativeqblist
    @GetMapping("/administrativeqblist")
    public ResultVO administrativeqblist(@RequestParam("openid") String openid,
                                        @RequestParam(value = "collation", defaultValue = "1")Integer collation,

                                        @RequestParam(value = "page",defaultValue = "0") Integer page,
                                        @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        List<AdministraQuestionBankVO> result = new ArrayList<>();

        // 1. 拿到行测练习父题库列表
        List<ParentQuestionBank> questionBankList = questionBankService.findAdministraPqb();
        for (ParentQuestionBank parentQuestionBank : questionBankList)
        {
            AdministraQuestionBankVO administraQuestionBankVO = new AdministraQuestionBankVO();
            administraQuestionBankVO.setPqbId(parentQuestionBank.getParentQbId());
            administraQuestionBankVO.setPqbTitle(parentQuestionBank.getPqbTitle());
            administraQuestionBankVO.setCqbNumber(parentQuestionBank.getCqbNumber());
            administraQuestionBankVO.setHeat(parentQuestionBank.getPqbHeat());

            // 2. 查询其中的子题库列表并填入administrativeqblist中（这个list的元素为AdministraQbVO）
            List<ChildQuestionBank> childQuestionBanks = questionBankService.findCQBListByPQBIdOrderBy(parentQuestionBank.getParentQbId(),QuestionBankSortEnum.SORT_BY_HEAT_DESC);

            List<ChildQuestionBankVO> childQuestionBankVOS = this.convertToChildQuestionBankVOList(childQuestionBanks,openid);
            administraQuestionBankVO.setCqbList(childQuestionBankVOS);

            result.add(administraQuestionBankVO);
        }



        // 3. 返回
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

        // 这套子题库热度++
        questionBankService.increaseChildQuestionBankHeat(cqbId);

        // 查看记录
        PracticeRecord practiceRecord = questionBankService.findPracticeRecord(openid,cqbId);
        PracticeRecordDTO practiceRecordDTO = null;
        if(practiceRecord!=null)
        {
            practiceRecordDTO = PracticeRecord2PracticeRecordDTOConverter.convert(practiceRecord);
        }

        // 用Service查询questionDTOs转化为VO
        List<QuestionDTO> questionDTOList = questionBankService.findQuestionListByCQBId(cqbId);
        List<QuestionVO> result = new ArrayList<>();
        for (int i = 0 ; i < questionDTOList.size() ; i++)
        {
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(questionDTOList.get(i),questionVO);

            List<QuestionOptionVO> questionOptionVOList = new ArrayList<>();
            // 为空的话肯定是问答题啦，不为空才填选项
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

            // 填入上次作答
            // 前端需求：如果上次是已完成了，那么这次请求questionList的时候我不传userAnswer
            if(practiceRecordDTO!=null)
                if(practiceRecord.getCorrectRatio() == -1) // 按照规定，正确率等于-1时说明这条做题记录 未完成或者没开始
                {
                    if(!CollectionUtils.isEmpty(practiceRecordDTO.getUserAnswerList()))
                        questionVO.setUserAnswer(practiceRecordDTO.getUserAnswerList().get(questionVO.getQuestionSeq()-1));
                }


            // 填入收藏状态
            questionVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid, FavoriteTypeEnum.QUESTION.getCode(),questionVO.getQuestionId()));
            result.add(questionVO);
        }

        return ResultVOUtil.success(result);
    }

    /**
     * 用于"错题解析"界面，只返回该题库中上次做错的题目，格式和questionlist一致
     * @param openid
     * @param cqbId
     * @return
     */
    @GetMapping("analysislist")
    public ResultVO analysisList(@RequestParam("openid") String openid,
                                 @RequestParam("cqbId") String cqbId,
                                 @RequestParam("mistaken")Integer mistaken)
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

        // 先拿做题记录
        PracticeRecord practiceRecord = questionBankService.findPracticeRecord(openid,cqbId);
        PracticeRecordDTO practiceRecordDTO = null;
        if(practiceRecord == null)
        {
            throw new WritestException(ResultEnum.RECORD_NOT_EXIST);
        }

        practiceRecordDTO = PracticeRecord2PracticeRecordDTOConverter.convert(practiceRecord);

        // 用Service查询questionDTOs转化为VO
        List<QuestionDTO> questionDTOList = questionBankService.findQuestionListByCQBId(cqbId);
        List<QuestionVO> result = new ArrayList<>();

        for (int i = 0 ; i < questionDTOList.size() ; i++)
        {
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(questionDTOList.get(i),questionVO);
            List<QuestionOptionVO> questionOptionVOList = new ArrayList<>();
            // 为空的话肯定是问答题啦，不为空才填选项
            if(questionDTOList.get(i).getQuestionOptions() != null)
            {
                for (QuestionOption questionOption : questionDTOList.get(i).getQuestionOptions())
                {
                    QuestionOptionVO questionOptionVO = new QuestionOptionVO();
                    BeanUtils.copyProperties(questionOption,questionOptionVO);
                    // seq要重设
                    questionVO.setQuestionSeq(i+1);
                    questionOptionVOList.add(questionOptionVO);
                }

                questionVO.setQuestionOptionVOList(questionOptionVOList);
            }

            // 填入上次做题历史作答，如果是错题解析的话，应该需要这个userAnswer的吧
            questionVO.setUserAnswer(practiceRecordDTO.getUserAnswerList().get(questionVO.getQuestionSeq()-1));


            // 填入收藏状态
            questionVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid, FavoriteTypeEnum.QUESTION.getCode(),questionVO.getQuestionId()));

            if(mistaken == 1)
            {
                // 只返错题，未作答的题和回答错误的题才叫错题
//                if(StringUtils.isEmpty(userAnswerList.get(i)) || ((questionDTOList.get(i).getQuestionType()!=QuestionTypeEnum.ESSAY_QUESTION.getCode())  && !userAnswerList.get(i).equals(questionDTOList.get(i).getAnswer())))

                    if( ( questionVO.getQuestionType()!= QuestionTypeEnum.ESSAY_QUESTION.getCode() && !questionVO.getUserAnswer().equals(questionVO.getAnswer()))
                        || StringUtils.isEmpty(questionVO.getUserAnswer()))
                    result.add(questionVO);
            }
            else result.add(questionVO);
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
        Integer questionNumber = questionBankService.getQuestionNumber(recordForm.getCqbId());

        if(recordForm.getUserAnswerList().size() != questionNumber)
        {
            log.error("【创建记录】参数不正确，UserAnswerList长度与题库题数不符");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        log.info("【创建记录】用户作答列表为{}",recordForm.getUserAnswerList());

        PracticeRecordDTO practiceRecordDTO = RecordForm2PracticeRecordDTOConverter.convert(recordForm);

        log.info("【创建记录】recordForm{}",recordForm);

        Map<String, Integer> result = new HashMap<>();

        // 前端需求 完成时lastMode要为0 , 估计后期还得改回来
        if(practiceRecordDTO.getComplete() == RecordStateEnum.COMPLETED.getCode())
        {
            practiceRecordDTO.setLastMode(RecordLastModeEnum.UNKOWN_MODE.getCode());

            // todo  现在是根据用户的正确率来映射的假超越率
            practiceRecordDTO.setSurpassRatio(this.surpassRatioFromCorrect(practiceRecordDTO.getCorrectRatio()));
            log.info("正确率{}",practiceRecordDTO.getCorrectRatio());
            log.info("超越率{}",practiceRecordDTO.getSurpassRatio());


            result.put("surpassRatio",practiceRecordDTO.getSurpassRatio());
        }

        // 因为form里没有title，而前端想在做题报告中获取title，所以单独给practiceRecordDTO填入这一字段
        practiceRecordDTO.setChildQbTitle(questionBankService.getCQbTitle(recordForm.getCqbId()));

        log.info("【创建记录】子题库标题为{}",practiceRecordDTO.getChildQbTitle());


        practiceRecordService.addOrUpdateRecord(practiceRecordDTO);

        //完成作答才记录错题
        if(recordForm.getComplete() == 1)
        {
            // 在记录之前，如果是完成的记录，则将【未作答和错题】计入错题本！
            // 1. 先拿到错题IDs
            List<String> userAnswerList = recordForm.getUserAnswerList();
            List<String> wrongQuestionIds = new ArrayList<>();
            List<Integer> wrongQuestionSeqs = new ArrayList<>();

            List<QuestionDTO> questionDTOList = questionBankService.findQuestionListByCQBId(recordForm.getCqbId());
            for (int i = 0; i < questionDTOList.size(); i++)
            {
                // 筛选未作答和错题 (问答题只要非空就算正确)
                if(StringUtils.isEmpty(userAnswerList.get(i)) || ((questionDTOList.get(i).getQuestionType()!=QuestionTypeEnum.ESSAY_QUESTION.getCode())  && !userAnswerList.get(i).equals(questionDTOList.get(i).getAnswer())))
                {
                    wrongQuestionIds.add(questionDTOList.get(i).getQuestionId());
                    wrongQuestionSeqs.add(questionDTOList.get(i).getQuestionSeq());
                }
            }
            log.info("【创建错题本】用户这次做错了{}题",wrongQuestionSeqs);

            // 2. 添加到错题本
            MistakeBook mistakeBook = mistakeBookService.findMistakeBook(recordForm.getOpenid(),recordForm.getCqbId());
            if (mistakeBook == null) mistakeBook = mistakeBookService.initAMistakeBook(recordForm.getOpenid(),recordForm.getCqbId());
            mistakeBookService.addMistake(wrongQuestionIds,mistakeBook);
        }


        return ResultVOUtil.success(result);
    }


    public List<ChildQuestionBankVO> convertToChildQuestionBankVOList(List<ChildQuestionBank> childQuestionBanks, String openid)
    {
        List<ChildQuestionBankVO> childQuestionBankVOS = new ArrayList<>();

        for (ChildQuestionBank childQuestionBank : childQuestionBanks)
        {
            ChildQuestionBankVO childQuestionBankVO = new ChildQuestionBankVO();
            BeanUtils.copyProperties(childQuestionBank,childQuestionBankVO);

            // 为每个子题库VO写入"练习记录"
            PracticeRecord practiceRecord = questionBankService.findPracticeRecord(openid,childQuestionBankVO.getChildQbId());

            if(practiceRecord == null)
            {
                childQuestionBankVO.setComplete(0);
                childQuestionBankVO.setSpentTime(0);
                childQuestionBankVO.setCompleteNumber(0);
                childQuestionBankVO.setCorrectRatio(-1);// 没有记录的时候就传-1
                childQuestionBankVO.setLastMode(RecordLastModeEnum.UNKOWN_MODE.getCode()); // 暂时传0
            }else
            {
                childQuestionBankVO.setComplete(practiceRecord.getComplete());
                childQuestionBankVO.setSpentTime(practiceRecord.getSpentTime());
                childQuestionBankVO.setCompleteNumber(practiceRecord.getCompleteNumber());
                childQuestionBankVO.setCorrectRatio(practiceRecord.getCorrectRatio());
                childQuestionBankVO.setLastMode(practiceRecord.getLastMode());
            }

            // 为每个子题库VO写入"收藏夹"关系值
            childQuestionBankVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid,FavoriteTypeEnum.CHILD_QUESTIONBANK.getCode(),childQuestionBank.getChildQbId()));

            childQuestionBankVOS.add(childQuestionBankVO);
        }
        return childQuestionBankVOS;
    }


    /**先暂时用的假超越率
     * 打败用户率：
     * 如果用户做题正确率为70—100%，就在80%-95%中随机选一个值作为打败用户率；
     * 如果用户做题正确率为50—70%，就在60%-80%中随机选一个值作为打败用户率；
     * 如果用户做题正确率为20—50%，就在30%-60%中随机选一个值作为打败用户率；
     * 如果用户做题正确率为1—20%，就在1%-30%中随机选一个值作为打败用户率；
     * 用户做题正确率为0%，打败用户率为0%；
     * @param correct
     * @return
     */
    public Integer surpassRatioFromCorrect(Integer correct)
    {
        Integer surpassRatio;
        if (correct > 70 && correct<=100 )
        {
            surpassRatio = this.randomGenerate(80,95);// 80-95随机
        }
        else if(correct>50 && correct <= 70)
        {
            surpassRatio = this.randomGenerate(60,80);// 60-80
        }
        else if (correct>20 && correct<= 50)
        {
            surpassRatio = this.randomGenerate(30,60);//30-60
        }
        else if (correct>1 && correct <= 20)
        {
            surpassRatio = this.randomGenerate(1,30); //1-30
        }
        else surpassRatio = 0;
        return surpassRatio;
    }

    public Integer randomGenerate(Integer min, Integer max)
    {
        Integer randomInt = (int) (Math.random()*(max-min)+min);
        return randomInt;
    }


}
