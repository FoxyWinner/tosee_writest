package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.CollectBookDTO;
import com.tosee.tosee_writest.dto.MistakeBookDTO;
import com.tosee.tosee_writest.dto.PracticeRecordDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.RecordLastModeEnum;
import com.tosee.tosee_writest.enums.RecordStateEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.service.*;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 11:00 下午
 */
@RestController
@RequestMapping("/writester/experience")
@Slf4j
public class WritesterExperienceController
{
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired ExperienceArticleService experienceArticleService;
    @GetMapping("/taglist")
    public ResultVO tagList(@RequestParam("openid") String openid)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询标签列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<ArticleTag> tags = articleTagService.findTagList();
        List<ArticleTagVO> result = new ArrayList<>();

        // 填入虚拟推荐标签，tagId为0
        ArticleTagVO articleTagVO = new ArticleTagVO();
        articleTagVO.setTagId(0);
        articleTagVO.setTagName("推荐");
        result.add(articleTagVO);

        if(!CollectionUtils.isEmpty(tags))
        {
            for (ArticleTag tag : tags)
            {
                ArticleTagVO tagVO = new ArticleTagVO();
                BeanUtils.copyProperties(tag,tagVO);

                result.add(tagVO);
            }
        }
        return ResultVOUtil.success(result);
    }

    @GetMapping("/articlelist")
    public ResultVO articleList(@RequestParam("openid") String openid,
                                @RequestParam("tagId") Integer tagId,
                                @RequestParam(value = "search",required = false) String search)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询文章列表】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        List<ExperienceArticle> articleList;

        articleList = experienceArticleService.findArticlesByTagAndSearch(tagId,search);

        List<ArticleVO4List> result = new ArrayList<>();

        for (ExperienceArticle experienceArticle : articleList)
        {
            ArticleVO4List articleVO4List = new ArticleVO4List();
            BeanUtils.copyProperties(experienceArticle,articleVO4List);

            result.add(articleVO4List);
        }
        return ResultVOUtil.success(result);
    }

    @GetMapping("/articlecontent")
    public ResultVO articleContent(@RequestParam("openid") String openid,
                                   @RequestParam("articleId") String articleId)
    {
        if(StringUtils.isEmpty(openid))
        {
            log.error("【查询文章内容】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(articleId))
        {
            log.error("【查询文章内容】articleId为空");
            throw new WritestException(ResultEnum.PARAM_ERROR);
        }

        ArticleVO4Content articleVO4Content = new ArticleVO4Content();
        ExperienceArticle article = experienceArticleService.findArticleById(articleId);

        if (article == null)
        {
            log.error("【查询文章内容】文章不存在:{}",articleId);
            throw new WritestException(ResultEnum.ARTICLE_NOT_EXIST);
        }

        BeanUtils.copyProperties(article,articleVO4Content);
        articleVO4Content.setArticleTagName(articleTagService.getTagName(article.getArticleTagId()));
        articleVO4Content.setCollectState(favoriteService.findByOpenidAndFavoriteTypeAndTargetId(openid,FavoriteTypeEnum.EXPERIENCE_ARTICLE.getCode(),articleId));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        articleVO4Content.setRelaseTime(df.format(article.getRelaseTime()));

        // 最后给阅读量+1
        experienceArticleService.increaseReadNum(article.getArticleId());

        return ResultVOUtil.success(articleVO4Content);
    }
}
