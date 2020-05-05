package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Question;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/30 10:19 下午
 */

@RunWith(SpringRunner.class)
@SpringBootTest
class QuestionRepositoryTest
{
    @Autowired
    private QuestionRepository repository;

    @Test
    @Transactional
    public void findQuestionsByChildQbIdTest()
    {
//        List<Question> questionList = repository.findQuestionsByChildQbId("15881323384921812123");
//        System.out.println(questionList);
    }
}