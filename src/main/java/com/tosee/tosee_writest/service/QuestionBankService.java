package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.ChildQuestionBank;
import com.tosee.tosee_writest.dataobject.ParentQuestionBank;
import com.tosee.tosee_writest.dataobject.PracticeRecord;
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
    /** 查询企业真题主题库列表
     * @param positionType
     * @param pqbType
     * @param sortRule*/
    List<ParentQuestionBank> findPQBListByPositionTypeAndPqbTypeOrderBy(Integer positionType, Integer pqbType, QuestionBankSortEnum sortRule);


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

     /** 为某套子题库热度++，顺便重新计算父题库热度*/
    public void increaseChildQuestionBankHeat(String childQbId);


    // 以下为运营用

    /** 获取父题库列表 */
    Page<ParentQuestionBankDTO> findPQBList(Pageable pageable);

    /** 获取子题库列表 */
    Page<ChildQuestionBank> findCQBList(Pageable pageable);

    Page<ChildQuestionBank> findCQBList(Pageable pageable,String pqbId);

    List<ChildQuestionBank> findAllCQBs();

    Page<QuestionDTO> findQuestionsDTOList(Pageable pageable);

    Page<QuestionDTO> findQuestionsDTOList(Pageable pageable,String cqbId);

    ParentQuestionBankDTO findOneParentQuestionBankDTO(String pqbId);

    ParentQuestionBank findOneParentQuestionBank(String pqbId);

    ParentQuestionBank saveParentQuestionBankDTO(ParentQuestionBankDTO parentQuestionBankDTO);

    ParentQuestionBank saveParentQuestionBank(ParentQuestionBank parentQuestionBank);

    ChildQuestionBank findById(String childQbId);

    List<ParentQuestionBankDTO> findAllPQB();

}
