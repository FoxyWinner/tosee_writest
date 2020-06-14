package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.Mistake;
import com.tosee.tosee_writest.dataobject.MistakeBook;
import com.tosee.tosee_writest.dto.MistakeBookDTO;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.MistakeBookRepository;
import com.tosee.tosee_writest.repository.MistakeRepository;
import com.tosee.tosee_writest.service.MistakeBookService;
import com.tosee.tosee_writest.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 9:52 下午
 */
@Slf4j
@Service
public class MistakeBookServiceImpl implements MistakeBookService
{
    @Autowired
    private MistakeBookRepository mistakeBookRepository;

    @Autowired
    private MistakeRepository mistakeRepository;

    // 如果用户没有该子题库的错题本，调用本函数生成一个空的错题本
    @Override
    public MistakeBook initAMistakeBook(String openid, String cqbId)
    {
        MistakeBook mistakeBook = new MistakeBook();
        mistakeBook.setMistakeBookId(KeyUtil.genUniqueKey());
        mistakeBook.setCqbId(cqbId);
        mistakeBook.setOpenid(openid);
        mistakeBook.setMistakeNumber(0);
        return mistakeBook;
    }

    /**
     * 该函数传入错题Id列表以及mistakeBook
     * @param questionIds
     * @param mistakeBook
     * @return
     */
    @Override
    @Transactional
    public MistakeBook addMistake(List<String> questionIds , MistakeBook mistakeBook)
    {
        for (String questionId :  questionIds)
        {
            // 先看看错题库里有没有这道题？用openid和questionId查询即可；
            Mistake mistake = mistakeRepository.findByOpenidAndQuestionId(mistakeBook.getOpenid(),questionId);
            if(mistake != null)
            {
                // 如果有这道题，则更新一下updateTime即可
                log.info("【错题本】更新修改时间");
                mistake.setUpdateTime(new Date());
                mistakeBook.setUpdateTime(new Date());
                mistakeRepository.save(mistake);
                mistakeBookRepository.save(mistakeBook);
            }
            else
            {
                // 如果没有则新建，且让mistakeBook的num++
                log.info("【错题本】新错题");
                mistake = new Mistake();
                mistake.setMistakeBookId(mistakeBook.getMistakeBookId());
                mistake.setOpenid(mistakeBook.getOpenid());
                mistake.setMistakeId(KeyUtil.genUniqueKey());
                mistake.setQuestionId(questionId);
                // updateTime自动更新
                mistakeRepository.save(mistake);
                mistakeBook.setMistakeNumber(mistakeBook.getMistakeNumber()+1);
            }
        }
        mistakeBookRepository.save(mistakeBook);
        return mistakeBook;
    }

    @Override
    public MistakeBook findMistakeBook(String openid, String cqbid)
    {
        return mistakeBookRepository.findByOpenidAndCqbId(openid,cqbid);
    }

    // 这个DTO里实际上没有那个List<Mistake>的 以后用到再加吧
    @Override
    public List<MistakeBookDTO> findAllMistakeBooks(String openid)
    {
        List<MistakeBook> mistakeBookList = mistakeBookRepository.findByOpenidOrderByUpdateTimeDesc(openid);
        List<MistakeBookDTO> result = new ArrayList<>();
        for (MistakeBook mistakeBook : mistakeBookList)
        {
            MistakeBookDTO mistakeBookDTO = new MistakeBookDTO();
            BeanUtils.copyProperties(mistakeBook,mistakeBookDTO);
            result.add(mistakeBookDTO);
        }
        return result;
    }


    @Override
    public MistakeBookDTO findMistakeBookById(String mistakeBookId)
    {
        MistakeBook mistakeBook = mistakeBookRepository.findById(mistakeBookId).orElse(null);
        MistakeBookDTO result = new MistakeBookDTO();
        if(mistakeBook != null)
        {
            BeanUtils.copyProperties(mistakeBook,result);
            // 填入列表
            List<Mistake> mistakes = mistakeRepository.findByMistakeBookIdOrderByUpdateTimeAsc(mistakeBookId);
            result.setMistakeList(mistakes);
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteMistake(String openid, String questionId)
    {
        Mistake mistake = mistakeRepository.findByOpenidAndQuestionId(openid,questionId);
        if (mistake ==  null)
        {
            log.error("【删除错题】没有该错题信息");
            throw new WritestException(ResultEnum.MISTAKE_NOT_EXIST);
        }

        mistakeRepository.delete(mistake);

        // 还要对错题列表重新排序吗？不需要，每次请求列表的时候序号都是动态填入的

        // 查看对应的错题本，错题数为0则直接删除，否则就更新
        MistakeBook mistakeBook = mistakeBookRepository.findById(mistake.getMistakeBookId()).orElse(null);
        if (mistakeBook == null)
        {
            log.error("【删除错题】没有该错题本信息");
            throw new WritestException(ResultEnum.MISTAKE_NOT_EXIST);
        }

        mistakeBook.setMistakeNumber(mistakeBook.getMistakeNumber()-1);


        if(mistakeBook.getMistakeNumber() == 0)
        {
            mistakeBookRepository.delete(mistakeBook);
        }else
        {
            mistakeBookRepository.save(mistakeBook);
        }
    }

    @Override
    public Integer isInMistakeBook(String openid, String questionId)
    {
        Mistake mistake = mistakeRepository.findByOpenidAndQuestionId(openid,questionId);
        if (mistake ==  null)
        {
            return 0;
        }
        return 1;
    }
}
