package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:47 下午
 */
public interface ChildQuestionBankRepository extends JpaRepository<ChildQuestionBank, String>
{
    // 首页搜索用
    List<ChildQuestionBank> findByCqbTitleLike(String search);

    // 首页热点题库用
    List<ChildQuestionBank> findTop6ByOrderByCqbHeatDesc();

    List<ChildQuestionBank> findByParentQbIdOrderByCqbHeatDesc(String parentQbId);

    List<ChildQuestionBank> findByParentQbIdOrderByRelaseTimeDesc(String parentQbId);

    List<ChildQuestionBank> findByIsRecommendedOrderByCqbHeatDesc(Integer isRecommended);

    Page<ChildQuestionBank> findByParentQbIdOrderByRelaseTime(Pageable pageable , String parentQbId);

    Integer countByParentQbId(String parentQbId);

    List<ChildQuestionBank> findByParentQbIdIn(List<String> pqbIds);
}
