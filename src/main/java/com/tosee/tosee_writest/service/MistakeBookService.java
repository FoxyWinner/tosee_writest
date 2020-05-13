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
    public MistakeBook initAMistakeBook(String openid, String cqbId);

    public MistakeBook addMistake(List<String> questionIds, MistakeBook mistakeBook);

    public MistakeBook findMistakeBook(String openid, String cqbid);

    public List<MistakeBookDTO> findAllMistakeBooks(String openid);

    public MistakeBookDTO findMistakeBookById(String mistakeBookId);

    public void deleteMistake(String openid, String questionId);
}
