package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.ArticleTag;
import com.tosee.tosee_writest.repository.ArticleTagRepository;
import com.tosee.tosee_writest.service.ArticleTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:07 下午
 */
@Slf4j
@Service
public class ArticleTagServiceImpl implements ArticleTagService
{
    @Autowired
    private ArticleTagRepository articleTagRepository;

    @Override
    public List<ArticleTag> findTagList()
    {
        return articleTagRepository.findAll();
    }
}