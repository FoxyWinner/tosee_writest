package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.Company;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.CompanyRepository;
import com.tosee.tosee_writest.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:07 下午
 */
@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService
{
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public String findCompanyIconUrlByID(Integer companyId)
    {
        Company company = companyRepository.findById(companyId).orElse(null);

        if(company == null)
        {
            throw new WritestException(ResultEnum.COMPANY_NOT_EXIST);
        }

        return company .getCompanyIcon();
    }

    @Override
    public Company findCompanyById(Integer companyId)
    {
        log.info("【查询公司】id{}",companyId);
        Company company = companyRepository.findById(companyId).orElse(null);

        return company;
    }

    @Override
    public List<Company> findAllCompanies()
    {
        return companyRepository.findAll();
    }
}