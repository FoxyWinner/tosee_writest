package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import com.tosee.tosee_writest.dataobject.Mistake;
import com.tosee.tosee_writest.dataobject.MistakeBook;
import com.tosee.tosee_writest.dataobject.QuestionOption;
import com.tosee.tosee_writest.dto.MistakeBookDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.service.FavoriteService;
import com.tosee.tosee_writest.service.MistakeBookService;
import com.tosee.tosee_writest.service.QuestionBankService;
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
    @GetMapping("/mistakebooklist")
    public ResultVO mistakeBookList(@RequestParam("openid") String openid)
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

}
