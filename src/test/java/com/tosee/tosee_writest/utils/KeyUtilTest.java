package com.tosee.tosee_writest.utils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:50 上午
 */

@RunWith(SpringRunner.class)
@SpringBootTest
class KeyUtilTest
{
    @Test
    public void genUniqueKeyTest(){
        String genId = KeyUtil.genUniqueKey();
        System.out.println(genId);
    }
}