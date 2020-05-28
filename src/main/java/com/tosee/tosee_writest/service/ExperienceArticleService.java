package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.ExperienceArticle;
import com.tosee.tosee_writest.dto.ExperienceArticleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:06 下午
 */
public interface ExperienceArticleService
{
    List<ExperienceArticle> findArticlesByTagAndSearch(Integer tagId, String search);

    List<ExperienceArticle> findArticlesByIds(List<String> ids);

    ExperienceArticle findArticleById(String articleId);

    Page<ExperienceArticleDTO> findAll(Pageable pageable);

    void increaseReadNum(String articleId);

    ExperienceArticle saveArticle(ExperienceArticle article);
}
