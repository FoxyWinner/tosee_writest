package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.CollectBookDTO;
import com.tosee.tosee_writest.enums.CollectStateEnum;
import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.*;
import com.tosee.tosee_writest.service.FavoriteService;
import com.tosee.tosee_writest.service.QuestionBankService;
import com.tosee.tosee_writest.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:24 下午
 */
@Service
@Slf4j
public class FavoriteServiceImpl implements FavoriteService
{
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private CollectBookRepository collectBookRepository;

    @Autowired
    private CollectQuestionRepository collectQuestionRepository;

    @Autowired
    QuestionBankService questionBankService;

    @Override
    // 当favoriteType为题目时，这里的targetId为questionId，而非数据库中的targetId（targetId对应了collectBookId）
    public Integer findByOpenidAndFavoriteTypeAndTargetId(String openid, Integer favoriteType, String targetId)
    {
        if(favoriteType!= FavoriteTypeEnum.QUESTION.getCode())
        {
            if(favoriteRepository.existsByOpenidAndFavoriteTypeAndTargetId(openid,favoriteType,targetId))
            {
                return CollectStateEnum.IS_COLLECTED.getCode();
            }
            return CollectStateEnum.IS_NOT_COLLECTED.getCode();
        }
        else
        {
            if(collectQuestionRepository.existsByOpenidAndQuestionId(openid,targetId))
            {
                return  CollectStateEnum.IS_COLLECTED.getCode();
            }
            return CollectStateEnum.IS_NOT_COLLECTED.getCode();
        }

    }

    @Override
    public List<Favorite> findByOpenidAndFavoriteType(String openid, Integer favoriteType)
    {
        return favoriteRepository.findByOpenidAndFavoriteTypeOrderByUpdateTimeDesc(openid, favoriteType);
    }

    @Override
    @Transactional
    public void cancelFavorite(String openid, Integer favoriteType, String targetId)
    {
        if(favoriteType!= FavoriteTypeEnum.QUESTION.getCode())
        {
            favoriteRepository.deleteByOpenidAndFavoriteTypeAndTargetId(openid,favoriteType,targetId);
        }
        else// 若为题目的话，这里的targetId其实是questionId
        {
            this.cancelCollect(openid,targetId);
        }
    }

    @Override
    @Transactional
    public void addFavorite(String openid, Integer favoriteType, String targetId)
    {
        if(favoriteType!= FavoriteTypeEnum.QUESTION.getCode())
        {
            Favorite favorite = new Favorite();
            favorite.setFavoriteId(KeyUtil.genUniqueKey());
            favorite.setOpenid(openid);
            favorite.setFavoriteType(favoriteType);
            favorite.setTargetId(targetId);
            favoriteRepository.save(favorite);
        }
        else // 收藏题目时
        {
            String childQbId = questionBankService.findQuestionById(targetId).getChildQbId().trim();
            CollectBook collectBook = collectBookRepository.findByOpenidAndCqbId(openid,childQbId);

            if (collectBook == null) collectBook = this.initACollectBook(openid,childQbId);
            else collectBook = this.addCollectQuestion(targetId,collectBook);

            // 存储favorite，先看存不存在这条favorite 如果存在的话不做新增
            if(!favoriteRepository.existsByOpenidAndFavoriteTypeAndTargetId(openid,favoriteType,collectBook.getCollectBookId()))
            {
                Favorite favorite = new Favorite();
                favorite.setFavoriteId(KeyUtil.genUniqueKey());
                favorite.setOpenid(openid);
                favorite.setFavoriteType(favoriteType);
                favorite.setTargetId(collectBook.getCollectBookId());
                favoriteRepository.save(favorite);
            }
        }
    }

    @Override
    public CollectBook initACollectBook(String openid, String cqbId)
    {
        CollectBook collectBook = new CollectBook();
        collectBook.setCollectBookId(KeyUtil.genUniqueKey());
        collectBook.setCqbId(cqbId);
        collectBook.setOpenid(openid);
        collectBook.setCollectNumber(0);
        return collectBook;
    }

    @Override
    public CollectBook addCollectQuestion(String questionId, CollectBook collectBook)
    {
        // 先看看收藏库里有没有这道题？用openid和questionId查询即可；
        CollectQuestion collectQuestion = collectQuestionRepository.findByOpenidAndQuestionId(collectBook.getOpenid(), questionId);
        if (collectQuestion != null)
        {
            // 如果有这道题，则更新一下updateTime即可
            log.info("【收藏题目】更新修改时间");
            collectQuestion.setUpdateTime(new Date());
            collectBook.setUpdateTime(new Date());
            collectQuestionRepository.save(collectQuestion);
            collectBookRepository.save(collectBook);
        } else
        {
            // 如果没有则新建，且让collectBook的num++
            log.info("【收藏题目】新题目");
            collectQuestion = new CollectQuestion();
            collectQuestion.setCollectBookId(collectBook.getCollectBookId());
            collectQuestion.setOpenid(collectBook.getOpenid());
            collectQuestion.setCollectQuestionId(KeyUtil.genUniqueKey());
            collectQuestion.setQuestionId(questionId);
            // updateTime自动更新
            collectQuestionRepository.save(collectQuestion);
            collectBook.setCollectNumber(collectBook.getCollectNumber() + 1);
        }

        collectBookRepository.save(collectBook);
        return collectBook;
    }

    // 该函数被cancelFavorite调用，当且仅当favoriteType为题目
    @Override
    @Transactional
    public void cancelCollect(String openid, String questionId)
    {
        CollectQuestion collectQuestion = collectQuestionRepository.findByOpenidAndQuestionId(openid,questionId);
        if (collectQuestion ==  null)
        {
            log.error("【删除收藏题】没有该收藏题信息");
            throw new WritestException(ResultEnum.COLLECT_QUESTION_NOT_EXIST);
        }

        collectQuestionRepository.delete(collectQuestion);

        // 查看对应的收藏集，错题数为0则直接删除，否则就更新
        CollectBook collectBook = collectBookRepository.findById(collectQuestion.getCollectBookId()).orElse(null);
        if (collectBook == null)
        {
            log.error("【删除收藏题】没有该收藏集信息");
            throw new WritestException(ResultEnum.COLLECT_BOOK_NOT_EXIST);
        }

        collectBook.setCollectNumber(collectBook.getCollectNumber()-1);


        if(collectBook.getCollectNumber() == 0)
        {
            // 删除收藏集，并删除favorite表中对该收藏集的引用
            collectBookRepository.delete(collectBook);
            favoriteRepository.deleteByOpenidAndFavoriteTypeAndTargetId(openid,FavoriteTypeEnum.QUESTION.getCode(),collectBook.getCollectBookId());
        }else
        {
            collectBookRepository.save(collectBook);
        }
    }

    @Override
    public CollectBook findCollectBookById(String collectBookId)
    {
        return collectBookRepository.findById(collectBookId).orElse(null);
    }

    @Override
    public CollectBookDTO findCollectBookDTOById(String collectBookId)
    {
        CollectBook collectBook = collectBookRepository.findById(collectBookId).orElse(null);
        CollectBookDTO result = new CollectBookDTO();
        if(collectBook != null)
        {
            BeanUtils.copyProperties(collectBook,result);
            // 填入列表
            List<CollectQuestion> collectQuestions = collectQuestionRepository.findByCollectBookIdOrderByUpdateTimeAsc(collectBookId);
            result.setCollectQuestionList(collectQuestions);
        }
        return result;
    }

}
