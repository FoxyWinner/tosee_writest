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

    // 按多个岗位查询主题库列表(热度排序)
    List<ParentQuestionBank> findByPositionTypeInAndPqbTypeOrderByPqbHeatDesc(List<Integer> positionTypes,Integer pqbType);

    // 按多个岗位查询主题库列表(时间排序)
    List<ParentQuestionBank> findByPositionTypeInAndPqbTypeOrderByRelaseTimeDesc(List<Integer> positionTypes,Integer pqbType);

    List<ParentQuestionBank> findByIsRecommendedAndPqbTypeOrderByPqbHeatDesc(Integer isRecommended,Integer pqbType);

    List<ParentQuestionBank> findByIsRecommendedAndPqbTypeOrderByRelaseTimeDesc(Integer isRecommended,Integer pqbType);

    // 查询行测父题库列表
    List<ParentQuestionBank> findByPqbTypeOrderByPqbHeat(Integer pqbType);


}
