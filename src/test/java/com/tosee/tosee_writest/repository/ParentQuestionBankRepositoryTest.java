package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ParentQuestionBank;
import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.enums.ParentQuestionBankTypeEnum;
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
 * @Date: 2020/4/29 11:45 上午
 */

@RunWith(SpringRunner.class)
@SpringBootTest
class ParentQuestionBankRepositoryTest
{
    @Autowired
    private ParentQuestionBankRepository repository;

    @Test
    @Transactional
    public void findByTargetPositionTypeAndPqbTypeOrderByPqbHeatDesc(){

    }

}