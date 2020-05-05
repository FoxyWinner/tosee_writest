package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.WorkPosition;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:16 下午
 */

@RunWith(SpringRunner.class)
@SpringBootTest
class WorkPositionRepositoryTest
{
    @Autowired
    private WorkPositionRepository repository;

    @Test
    @Transactional
    public void findByFieldTypeTest(){
        List<WorkPosition> workPositions = repository.findByFieldType(1);
        System.out.println(workPositions.toString());
    }
}