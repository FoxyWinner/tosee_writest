package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ParentQuestionBank;
import com.tosee.tosee_writest.dataobject.WorkField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:31 上午
 */
public interface ParentQuestionBankRepository extends JpaRepository<ParentQuestionBank, String>
{
    // 热度倒序排列
    List<ParentQuestionBank> findByPositionTypeAndPqbTypeOrderByPqbHeatDesc(Integer positionType, Integer pqbType);

    // 时间最新排列
    List<ParentQuestionBank> findByPositionTypeAndPqbTypeOrderByRelaseTimeDesc(Integer positionType, Integer pqbType);
}
