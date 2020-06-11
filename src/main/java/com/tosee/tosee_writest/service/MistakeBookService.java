package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.Mistake;
import com.tosee.tosee_writest.dataobject.MistakeBook;
import com.tosee.tosee_writest.dto.MistakeBookDTO;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 9:50 下午
 */
public interface MistakeBookService
{
    MistakeBook initAMistakeBook(String openid, String cqbId);

    MistakeBook addMistake(List<String> questionIds, MistakeBook mistakeBook);

    MistakeBook findMistakeBook(String openid, String cqbid);

    List<MistakeBookDTO> findAllMistakeBooks(String openid);

    MistakeBookDTO findMistakeBookById(String mistakeBookId);

    void deleteMistake(String openid, String questionId);

    Integer isInMistakeBook(String openid, String questionId);
}
