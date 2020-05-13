package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.Company;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:06 下午
 */
public interface CompanyService
{
    /** 根据公司ID 查询公司iconUrl */
    String findCompanyIconUrlByID(Integer companyId);

    Company findCompanyById(Integer companyId);

    List<Company> findAllCompanies();
}
