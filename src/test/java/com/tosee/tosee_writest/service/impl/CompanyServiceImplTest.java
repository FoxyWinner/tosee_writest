package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.repository.ChildQuestionBankRepository;
import com.tosee.tosee_writest.repository.CompanyRepository;
import com.tosee.tosee_writest.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/7 6:04 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class CompanyServiceImplTest
{
    @Autowired
    private CompanyService companyService;

    @Test
    public void testFindCompany()
    {
        System.out.println(companyService.findCompanyById(3));
    }
}