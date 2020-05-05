package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 9:08 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ChildQuestionBankRepositoryTest
{
    @Autowired
    private ChildQuestionBankRepository repository;

    @Test
    @Transactional
    public void findChildQuesitonBanksByParentQbIdOrOrderByCqbHeatDescTest(){
        List<ChildQuestionBank> result = repository.findByParentQbIdOrderByCqbHeatDesc("15881323384921812445");

        System.out.println(result);
    }
}