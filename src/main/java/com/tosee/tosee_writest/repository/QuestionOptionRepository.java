package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Question;
import com.tosee.tosee_writest.dataobject.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:49 下午
 */
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, String>
{
    List<QuestionOption> findByQuestionIdOrderByOptionNameAsc(String questionId);
}
