package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.ParentQuestionBank;
import com.tosee.tosee_writest.dataobject.WorkField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:31 上午
 */
public interface ParentQuestionBankRepository extends JpaRepository<ParentQuestionBank, String>
{
    // 按题库名检索
    List<ParentQuestionBank> findByPqbTitleLike(String search);

    // 热度倒序排列
    List<ParentQuestionBank> findByPositionTypeAndPqbTypeAndIsRelaseOrderByPqbHeatDesc(Integer positionType, Integer pqbType,Integer isRelase);

    // 时间最新排列
    List<ParentQuestionBank> findByPositionTypeAndPqbTypeAndIsRelaseOrderByRelaseTimeDesc(Integer positionType, Integer pqbType,Integer isRelase);

    // 按多个岗位查询主题库列表(热度排序)
    List<ParentQuestionBank> findByPositionTypeInAndPqbTypeAndIsRelaseOrderByPqbHeatDesc(List<Integer> positionTypes,Integer pqbType,Integer isRelase);

    // 按多个岗位查询主题库列表(时间排序)
    List<ParentQuestionBank> findByPositionTypeInAndPqbTypeAndIsRelaseOrderByRelaseTimeDesc(List<Integer> positionTypes,Integer pqbType,Integer isRelase);

    List<ParentQuestionBank> findByIsRecommendedAndPqbTypeAndIsRelaseOrderByPqbHeatDesc(Integer isRecommended,Integer pqbType,Integer isRelase);

    List<ParentQuestionBank> findByIsRecommendedAndPqbTypeAndIsRelaseOrderByRelaseTimeDesc(Integer isRecommended,Integer pqbType,Integer isRelase);

    // 查询行测父题库列表
    List<ParentQuestionBank> findByPqbTypeAndIsRelaseOrderByPqbHeat(Integer pqbType,Integer isRelase);

    // 查询定制推荐的父题库列表然后转子题库，这里必须是发布的
    List<ParentQuestionBank> findByPositionTypeInAndIsRelase(Set<Integer> positionTypes,Integer isRelase);


}
