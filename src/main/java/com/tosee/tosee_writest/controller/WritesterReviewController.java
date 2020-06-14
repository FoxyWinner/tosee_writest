package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.CollectBookDTO;
import com.tosee.tosee_writest.dto.MistakeBookDTO;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.*;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.UserRepository;
import com.tosee.tosee_writest.service.*;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 11:00 下午
 */
@RestController
@RequestMapping("/writester/review")
@Slf4j
public class WritesterReviewController
{
    @Autowired
    private MistakeBookService mistakeBookService;

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ExperienceArticleService experienceArticleService;

    @Autowired
    private PracticeRecordService practiceRecordService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/mistakebooklist")
    public ResultVO mistakeBookList(@RequestParam("openid") String openid,
                                    @RequestParam(value = "page",defaultValue = "0") Integer page,
                                    @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询错题本列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<MistakeBookDTO> mistakeBookDTOS = mistakeBookService.findAllMistakeBooks(openid);
        List<MistakeBookVO> result = new ArrayList<>();

        if(!CollectionUtils.isEmpty(mistakeBookDTOS))
        {
            for (MistakeBookDTO mistakeBookDTO : mistakeBookDTOS)
            {
                MistakeBookVO mistakeBookVO = new MistakeBookVO();
                BeanUtils.copyProperties(mistakeBookDTO,mistakeBookVO);
                // 还需要tile和updateTime
                ChildQuestionBank childQuestionBank = questionBankService.findById(mistakeBookDTO.getCqbId());
                mistakeBookVO.setTitle(childQuestionBank.getCqbTitle());
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
                mistakeBookVO.setUpdateTime(df.format(mistakeBookDTO.getUpdateTime()));

                result.add(mistakeBookVO);
            }
        }
        return ResultVOUtil.success(result);
    }

    @GetMapping("/domistake")
    public ResultVO doMistake(@RequestParam("openid") String openid,
                              @RequestParam("mistakeBookId") String mistakeBookId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【错题练习】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(mistakeBookId))
        {
            log.error("【错题练习】mistakeBookId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<MistakeVO> result = new ArrayList<>();
        MistakeBookDTO mistakeBookDTO = mistakeBookService.findMistakeBookById(mistakeBookId);

        if(mistakeBookDTO == null)
        {
            log.error("【错题练习】错题本不存在");
            throw new WritestException(ResultEnum.MISTAKE_BOOK_NOT_EXIST);
        }

        // 在DTO中的mistakeList已经是按照UpdateTime排列好的
        int mistakeSeq = 1;
        for (Mistake mistake : mistakeBookDTO.getMistakeList())
        {
            //将mistake转换为一个个的mistakeVO，添加进result
            MistakeVO mistakeVO = new MistakeVO();
            BeanUtils.copyProperties(mistake,mistakeVO);
            // mistakeSeq,questionType,questionStem,questionOptionVOList,answer,explanation,collectState

            QuestionDTO questionDTO = questionBankService.findQuestionById(mistakeVO.getQuestionId());
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(questionDTO,questionVO);
            List<QuestionOptionVO> questionOptionVOList = new ArrayList<>();

            if(questionDTO.getQuestionOptions() != null)
            {
                for (QuestionOption questionOption : questionDTO.getQuestionOptions())
                {
                    QuestionOptionVO questionOptionVO = new QuestionOptionVO();
                    BeanUtils.copyProperties(questionOption,questionOptionVO);
                    questionOptionVOList.add(questionOptionVO);
                }
                questionVO.setQuestionOptionVOList(questionOptionVOList);

            }

            // 填入收藏状态
            questionVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid, FavoriteTypeEnum.QUESTION.getCode(),questionVO.getQuestionId()));

            BeanUtils.copyProperties(questionVO,mistakeVO);
            mistakeVO.setMistakeSeq(mistakeSeq);// 按错题UpdateTime排列题号
            mistakeSeq++;

            result.add(mistakeVO);
        }

        return ResultVOUtil.success(result);
    }


    @PostMapping("/deletemistake")
    public ResultVO deleteMistake(@RequestParam("openid") String openid,
                                  @RequestParam("questionId") String questionId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【删除错题】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(questionId))
        {
            log.error("【删除错题】questionId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        mistakeBookService.deleteMistake(openid,questionId);

        return ResultVOUtil.success();
    }


    @GetMapping("/recordlist")
    public ResultVO recordList(@RequestParam("openid") String openid,
                               @RequestParam(value = "page",defaultValue = "0") Integer page,
                               @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询记录列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        List<PracticeRecordVO> result = new ArrayList<>();
        List<PracticeRecordDTO> practiceRecordDTOList = practiceRecordService.findAllRecordsByOpenid(openid);
        for (PracticeRecordDTO practiceRecordDTO : practiceRecordDTOList)
        {
            PracticeRecordVO practiceRecordVO = new PracticeRecordVO();
            BeanUtils.copyProperties(practiceRecordDTO,practiceRecordVO);

            Integer questionNumber = questionBankService.getQuestionNumber(practiceRecordVO.getChildQbId());
            if (questionNumber != null) practiceRecordVO.setQuestionNumber(questionNumber);

            Integer simulationTime = questionBankService.getSimulationTime(practiceRecordVO.getChildQbId());
            if(simulationTime != null) practiceRecordVO.setSimulationTime(simulationTime);

            // 前端需求 当complete为1时，lastMode必为0（迷惑）
            if (practiceRecordVO.getComplete() == RecordStateEnum.COMPLETED.getCode())
                practiceRecordVO.setLastMode(RecordLastModeEnum.UNKOWN_MODE.getCode());

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
            practiceRecordVO.setUpdateTime(df.format(practiceRecordDTO.getUpdateTime()));
            result.add(practiceRecordVO);
        }

        return ResultVOUtil.success(result);
    }

    @PostMapping("/recorddelete")
    public ResultVO recordDelete(@RequestParam("openid") String openid,
                               @RequestParam("recordId") String recordId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【删除记录】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(recordId))
        {
            log.error("【删除记录】recordId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        practiceRecordService.deleteRecord(openid,recordId);

        return ResultVOUtil.success();
    }

    /**
     * 根据用户信息推荐。如果用户未填写岗位信息，就推荐默认推荐列表（子题库被置推荐标记的）
     * @param openid
     * @return
     */
    @GetMapping("/recommendlist")
    public ResultVO recommendList(@RequestParam("openid") String openid)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【复习推荐】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        List<ChildQuestionBankVO> result = new ArrayList<>();


        User user = userRepository.findByOpenid(openid);
        // 几种情况：
        // 1. 查不到该用户信息
        if (user == null)
        {
            // 直接返回默认推荐
            log.info("【复习推荐】查不到用户信息，返回默认列表");
            List<ChildQuestionBank> childQuestionBankList = questionBankService.recommendCQBListDefault();
            result = this.convertToChildQuestionBankVOList(childQuestionBankList,openid);
            return ResultVOUtil.success(result);
        }
        // 这是个容错处理，user不为null但targetPostions为空的情况其实不应该发生
        else if (StringUtils.isEmpty(user.getTargetPositions()))
        {
            log.error("【复习推荐】该页面走入了一个不该出现的分支，数据库里有user信息，但目标岗位为空");
            List<ChildQuestionBank> childQuestionBankList = questionBankService.recommendCQBListDefault();
            result = this.convertToChildQuestionBankVOList(childQuestionBankList,openid);
            return ResultVOUtil.success(result);
        }
        else
        {
            List<ChildQuestionBank> childQuestionBankList = questionBankService.recommendCQBListByUserTargets(user);

            // 由于题目数量限制 现在的根据岗位的推荐结果可能为空 这时候要用默认推荐列表代替
            if (CollectionUtils.isEmpty(childQuestionBankList))
                childQuestionBankList = questionBankService.recommendCQBListDefault();
            result = this.convertToChildQuestionBankVOList(childQuestionBankList,openid);
            return ResultVOUtil.success(result);
        }

    }

    @GetMapping("/collectlist")
    public ResultVO collectList(@RequestParam("openid") String openid,
                                @RequestParam("type") Integer type,
                                @RequestParam(value = "page",defaultValue = "0") Integer page,
                                @RequestParam(value = "size",defaultValue = "10") Integer size)
    {
        // 子题库
        if(type == FavoriteTypeEnum.CHILD_QUESTIONBANK.getCode())
        {
            List<Favorite> favorites = favoriteService.findByOpenidAndFavoriteType(openid,type);

            List<ChildQuestionBankVO> result = new ArrayList<>();

            // 根据查询到的favorite来查询子题库列表
            List<ChildQuestionBank> childQuestionBanks = new ArrayList<>();
            for (Favorite favorite : favorites)
            {
                ChildQuestionBank childQuestionBank = questionBankService.findById(favorite.getTargetId());
                if (childQuestionBank != null) childQuestionBanks.add(childQuestionBank);
                else  log.error("【获取收藏子题库列表】targetId={}对应的子题库不存在",favorite.getTargetId());
            }

            // VO转换
            result = this.convertToChildQuestionBankVOList(childQuestionBanks,openid);

            return ResultVOUtil.success(result);
        }
        // 题目
        else if (type == FavoriteTypeEnum.QUESTION.getCode())
        {
            List<Favorite> favorites = favoriteService.findByOpenidAndFavoriteType(openid,type);

            List<CollectBookVO> result = new ArrayList<>();

            for (Favorite favorite : favorites)
            {
                CollectBook collectBook = favoriteService.findCollectBookById(favorite.getTargetId());
                CollectBookVO collectBookVO = new CollectBookVO();

                // VO转换
                if (collectBook != null)
                {
                    collectBookVO.setCollectBookId(collectBook.getCollectBookId());
                    collectBookVO.setCollectNumber(collectBook.getCollectNumber());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
                    collectBookVO.setUpdateTime(df.format(collectBook.getUpdateTime()));
                    collectBookVO.setTitle(questionBankService.getCQbTitle(collectBook.getCqbId()));

                    result.add(collectBookVO);
                } else log.error("【获取收藏子题库列表】targetId={}对应的收藏集不存在", favorite.getTargetId());
            }
            return ResultVOUtil.success(result);
        }
        // 笔经干货
        else
        {
            List<Favorite> favorites = favoriteService.findByOpenidAndFavoriteType(openid,type);

            List<String> favArticleIds = new ArrayList<>();
            for (Favorite favorite : favorites)
            {
                favArticleIds.add(favorite.getTargetId());
            }


            List<ExperienceArticle> articleList = experienceArticleService.findArticlesByIds(favArticleIds);

            List<ArticleVO4List> result = new ArrayList<>();

            for (ExperienceArticle experienceArticle : articleList)
            {
                ArticleVO4List articleVO4List = new ArticleVO4List();
                BeanUtils.copyProperties(experienceArticle,articleVO4List);

                result.add(articleVO4List);
            }

            log.info("【收藏文章列表】{}",result);
            return ResultVOUtil.success(result);
        }

    }

    @GetMapping("/docollectquestion")
    public ResultVO doCollecQuestiont(@RequestParam("openid") String openid,
                                        @RequestParam("collectBookId") String collectBookId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【收藏题目练习】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        if(StringUtils.isEmpty(collectBookId))
        {
            log.error("【收藏题目练习】collectBookId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<CollectQuestionVO> result = new ArrayList<>();

        CollectBookDTO collectBookDTO = favoriteService.findCollectBookDTOById(collectBookId);

        if(collectBookDTO == null)
        {
            log.error("【收藏题目练习】收藏册不存在");
            throw new WritestException(ResultEnum.COLLECT_BOOK_NOT_EXIST);
        }

        // 在DTO中的collectQuestionList已经是按照UpdateTime排列好的
        int collectQuestionSeq = 1;
        for (CollectQuestion collectQuestion : collectBookDTO.getCollectQuestionList())
        {
            //将mistake转换为一个个的mistakeVO，添加进result
            CollectQuestionVO collectQuestionVO = new CollectQuestionVO();
            BeanUtils.copyProperties(collectQuestion,collectQuestionVO);

            QuestionDTO questionDTO = questionBankService.findQuestionById(collectQuestionVO.getQuestionId());
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(questionDTO,questionVO);

            List<QuestionOptionVO> questionOptionVOList = new ArrayList<>();

            if(questionDTO.getQuestionOptions() != null)
            {
                for (QuestionOption questionOption : questionDTO.getQuestionOptions())
                {
                    QuestionOptionVO questionOptionVO = new QuestionOptionVO();
                    BeanUtils.copyProperties(questionOption,questionOptionVO);
                    questionOptionVOList.add(questionOptionVO);
                }
                questionVO.setQuestionOptionVOList(questionOptionVOList);

            }

            // 填入收藏状态
            questionVO.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid, FavoriteTypeEnum.QUESTION.getCode(),questionVO.getQuestionId()));

            BeanUtils.copyProperties(questionVO,collectQuestionVO);
            collectQuestionVO.setCollectQuestionSeq(collectQuestionSeq);// 按错题UpdateTime排列题号
            collectQuestionSeq++;

            result.add(collectQuestionVO);
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




}
