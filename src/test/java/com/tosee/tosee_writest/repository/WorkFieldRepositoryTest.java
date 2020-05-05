package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.WorkField;
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
 * @Date: 2020/4/28 4:22 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class WorkFieldRepositoryTest
{
    @Autowired
    private WorkFieldRepository repository;

    @Test
    @Transactional
    public void findOneTest(){
        List<WorkField> workFields = repository.findAll();
        System.out.println(workFields.toString());
    }

}