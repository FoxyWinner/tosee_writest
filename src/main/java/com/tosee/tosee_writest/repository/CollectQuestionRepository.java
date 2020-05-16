package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.CollectQuestion;
import com.tosee.tosee_writest.dataobject.Mistake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface CollectQuestionRepository extends JpaRepository<CollectQuestion, String>
{
    CollectQuestion findByOpenidAndQuestionId(String openid, String questionId);

    List<CollectQuestion> findByCollectBookIdOrderByUpdateTimeAsc(String mistakeBookId);

    boolean existsByOpenidAndQuestionId(String openid, String questionId);
}
