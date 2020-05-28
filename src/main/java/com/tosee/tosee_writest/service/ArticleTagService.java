package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.ArticleTag;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:06 下午
 */
public interface ArticleTagService
{
    List<ArticleTag> findTagList();

    String getTagName(Integer tagId);
}
