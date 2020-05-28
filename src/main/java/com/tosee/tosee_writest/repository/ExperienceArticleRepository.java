package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ExperienceArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface ExperienceArticleRepository extends JpaRepository<ExperienceArticle, String>
{
    List<ExperienceArticle> findByArticleTitleLike(String search);

    List<ExperienceArticle> findByIsRecommendedAndArticleTitleLike(Integer isRecommended, String search);

    List<ExperienceArticle> findByIsRecommended(Integer isRecommended);

    List<ExperienceArticle> findByArticleTagIdOrderByIsRecommendedDesc(Integer tagId);

    List<ExperienceArticle> findByArticleTagIdAndArticleTitleLikeOrderByIsRecommendedDesc(Integer tagId,String search);




}
