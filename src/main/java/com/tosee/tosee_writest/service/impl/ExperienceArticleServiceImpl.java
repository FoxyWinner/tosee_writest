package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.ExperienceArticle;
import com.tosee.tosee_writest.dto.ExperienceArticleDTO;
import com.tosee.tosee_writest.repository.ExperienceArticleRepository;
import com.tosee.tosee_writest.service.ArticleTagService;
import com.tosee.tosee_writest.service.ExperienceArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:07 下午
 */
@Slf4j
@Service
public class ExperienceArticleServiceImpl implements ExperienceArticleService
{
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ExperienceArticleRepository experienceArticleRepository;

    @Override
    public List<ExperienceArticle> findArticlesByTagAndSearch(Integer tagId, String search)
    {
        // tagId为0的标签实际上是虚拟标签"推荐"
        if (tagId == null)
        {
            if(StringUtils.isEmpty(search))
            {
                List<ExperienceArticle> articles = new ArrayList<>();
                return articles;
            }
            else
                return experienceArticleRepository.findByArticleTitleLike("%" + search + "%");
        }
        else if(tagId == 0)
        {
            // 返回推荐列表
            if (StringUtils.isEmpty(search))
                return experienceArticleRepository.findByIsRecommended(1);
            else
                return experienceArticleRepository.findByIsRecommendedAndArticleTitleLike(1,"%" + search + "%");
        }
        else
        {
            if (StringUtils.isEmpty(search))
                return experienceArticleRepository.findByArticleTagIdOrderByIsRecommendedDesc(tagId);
            else
                return experienceArticleRepository.findByArticleTagIdAndArticleTitleLikeOrderByIsRecommendedDesc(tagId,"%" + search + "%");
        }
    }

    @Override
    public List<ExperienceArticle> findArticlesByIds(List<String> ids)
    {
        return experienceArticleRepository.findAllById(ids);
    }

    @Override
    public ExperienceArticle findArticleById(String articleId)
    {
        return experienceArticleRepository.findById(articleId).orElse(null);
    }

    @Override
    public Page<ExperienceArticleDTO> findAll(Pageable pageable)
    {
        Page<ExperienceArticle> experienceArticlePage = experienceArticleRepository.findAll(pageable);

        List<ExperienceArticleDTO> experienceArticleDTOList = new ArrayList<>();
        for (ExperienceArticle article : experienceArticlePage.getContent())
        {
            ExperienceArticleDTO articleDTO = new ExperienceArticleDTO();
            BeanUtils.copyProperties(article,articleDTO);
            articleDTO.setArticleTagName(articleTagService.getTagName(article.getArticleTagId()));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            articleDTO.setRelaseTime(df.format(article.getRelaseTime()));
            experienceArticleDTOList.add(articleDTO);
        }

        return new PageImpl<>(experienceArticleDTOList, pageable,experienceArticlePage.getTotalElements());

    }

    @Override
    public void increaseReadNum(String articleId)
    {
        ExperienceArticle experienceArticle = experienceArticleRepository.findById(articleId).orElse(null);
        if(experienceArticle != null)
        {
            experienceArticle.setReaderNumber(experienceArticle.getReaderNumber() + 1);
            experienceArticleRepository.save(experienceArticle);
        }
    }

    @Override
    public ExperienceArticle saveArticle(ExperienceArticle article)
    {
        return experienceArticleRepository.save(article);
    }
}