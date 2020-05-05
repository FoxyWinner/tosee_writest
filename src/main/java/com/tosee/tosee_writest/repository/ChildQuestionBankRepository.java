package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:47 下午
 */
public interface ChildQuestionBankRepository extends JpaRepository<ChildQuestionBank, String>
{
    List<ChildQuestionBank> findByParentQbIdOrderByCqbHeatDesc(String parentQbId);

    List<ChildQuestionBank> findByParentQbIdOrderByRelaseTimeDesc(String parentQbId);
}
