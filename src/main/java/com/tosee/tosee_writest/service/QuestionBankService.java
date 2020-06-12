package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.ChildQuestionBankDTO;
import com.tosee.tosee_writest.dto.ParentQuestionBankDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.QuestionBankSortEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:38 上午
 */

public interface QuestionBankService
{
    /**
     * 搜索（模糊查找）子题库列表，关键字模糊查找，覆盖主题库岗位 主题库名 子题库名 题目题干岗位
     * @param search
     * @return
     */
    List<ChildQuestionBank> searchChildQuestionBank(String search);


    /**
     * 查找子题库内热度前6
     * @return
     */
    List<ChildQuestionBank> findHotPoint6CQBs();



    /** 查询主题库列表
     * @param positionType
     * @param pqbType 题库类型
     * @param sortRule*/
    List<ParentQuestionBank> findPQBListByPositionTypeAndPqbTypeOrderBy(Integer positionType, Integer pqbType, QuestionBankSortEnum sortRule);

    /**
     * 查询企业真题主题库列表（positionTypes为多个）
     * @param positionTypes
     * @param sortRule
     * @return
     */
    List<ParentQuestionBank> findEPQBListByPositionTypesAndPqbTypeOrderBy(List<Integer> positionTypes, QuestionBankSortEnum sortRule);


    List<ParentQuestionBank> findEPQBListRecommendedOrderBy(QuestionBankSortEnum sortRule);

    /** 查询行测题库列表
     *
     */
    List<ParentQuestionBank> findAdministraPqb();

    /** 根据父题库ID 查询子题库列表 */
    List<ChildQuestionBank> findCQBListByPQBIdOrderBy(String parentQbId, QuestionBankSortEnum sortRule);

    ChildQuestionBank findCQBById(String childQbId);

    List<ChildQuestionBank> findRecommendedCQB(String openid);

    /** 根据子题库ID 查询题目列表 */
    List<QuestionDTO> findQuestionListByCQBId(String childQbId);

    /** 根据题目ID查询对应的QuestionDTO*/
    QuestionDTO findQuestionById(String questionId);

    /** 根据子题库ID和openid 查询做题记录信息
     * @return*/
    PracticeRecord findPracticeRecord(String openid, String childQbId);

    /** 根据子题库ID查询答案列表
     * @return*/
     List<String> findAnswerList(String childQbId);

     Integer getQuestionNumber(String childQbId);

     Integer getSimulationTime(String childQbId);

     String getCQbTitle(String childQbId);

     String getPQBTitle(String parentQbId);

     /** 为某套子题库热度++，顺便重新计算父题库热度*/
     void increaseChildQuestionBankHeat(String childQbId);

    void updateParentQuestionBankHeat(String parentQbId);


    // 以下为运营用

    /** 获取父题库列表 */
    /**
     * 对于管理员来说，哪怕是没有发布我也要呈现
     * @param pageable
     * @return
     */
    Page<ParentQuestionBankDTO> findPQBList4Admin(Pageable pageable);


    void relaseParentQuestionBank(ParentQuestionBank parentQuestionBank);

    void cancelParentQuestionBank(ParentQuestionBank parentQuestionBank);


    /** 获取子题库列表 */
    Page<ChildQuestionBankDTO> findCQBList(Pageable pageable);

    Page<ChildQuestionBankDTO> findCQBList(Pageable pageable,String pqbId);

    List<ChildQuestionBank> findAllCQBs();

    Page<QuestionDTO> findQuestionsDTOList(Pageable pageable);

    Page<QuestionDTO> findQuestionsDTOList(Pageable pageable,String cqbId);

    ParentQuestionBankDTO findOneParentQuestionBankDTO(String pqbId);

    ParentQuestionBank findOneParentQuestionBank(String pqbId);

    ParentQuestionBank saveParentQuestionBankDTO(ParentQuestionBankDTO parentQuestionBankDTO);

    ParentQuestionBank saveParentQuestionBank(ParentQuestionBank parentQuestionBank);

    ChildQuestionBank findById(String childQbId);

    List<ParentQuestionBankDTO> findAllPQBDTOs();

    List<ParentQuestionBank> findAllPQBs();

    /**
     * QuestionOption相关
     */

    // 运营管理-新建/更改题目
    Question saveQuestionDTO(QuestionDTO questionDTO);

    ChildQuestionBank saveChildQuestionBank(ChildQuestionBank childQuestionBank);

    String deleteQuestionById(String questionId);



}
