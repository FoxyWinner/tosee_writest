package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Favorite;
import com.tosee.tosee_writest.dataobject.Mistake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface MistakeRepository extends JpaRepository<Mistake, String>
{
    Mistake findByOpenidAndQuestionId(String openid, String questionId);

    List<Mistake> findByMistakeBookIdOrderByUpdateTimeAsc(String mistakeBookId);
}
