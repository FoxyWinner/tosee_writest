package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface QuestionRepository extends JpaRepository<Question, String>
{
    List<Question> findQuestionsByChildQbIdOrderByQuestionSeqAsc(String cqbId);
}
