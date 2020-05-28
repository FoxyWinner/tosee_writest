package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ArticleTag;
import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:47 下午
 */
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Integer>
{


}
