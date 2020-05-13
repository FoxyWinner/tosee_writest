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

     String getCQbTitle(String childQbId);

     /** 为某套子题库热度++，顺便重新计算父题库热度*/
    public void increaseChildQuestionBankHeat(String childQbId);


    // 以下为运营用

    /** 获取父题库列表 */
    public Page<ParentQuestionBankDTO> findPQBList(Pageable pageable);

    public ParentQuestionBankDTO findOneParentQuestionBankDTO(String pqbId);

    public ParentQuestionBank findOneParentQuestionBank(String pqbId);

    public ParentQuestionBank saveParentQuestionBankDTO(ParentQuestionBankDTO parentQuestionBankDTO);

    public ParentQuestionBank saveParentQuestionBank(ParentQuestionBank parentQuestionBank);

    public ChildQuestionBank findById(String childQbId);

}
