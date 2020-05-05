package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:30 上午
 */
public interface CompanyRepository extends JpaRepository<Company, Integer>
{
}
