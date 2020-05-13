package com.tosee.tosee_writest.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/5 9:00 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class PracticeRecordRepositoryTest
{
    @Autowired
    private PracticeRecordRepository repository;

    @Test
    @Transactional
    public void findRecordTest(){
        String str = "[A,B,,BD]";
        if(str.startsWith("[")) str = str.replace("[","");
        if(str.endsWith("]")) str = str.replace("]","");

        System.out.println(str);
        System.out.println(CollectionUtils.arrayToList(str.split(",")));
    }
}