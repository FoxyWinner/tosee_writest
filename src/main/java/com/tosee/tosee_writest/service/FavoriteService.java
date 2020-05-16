package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.CollectBookDTO;
import com.tosee.tosee_writest.dto.MistakeBookDTO;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:21 下午
 */
public interface FavoriteService
{
    Integer findByOpenidAndFavoriteTypeAndTargetId(String openid, Integer favoriteType, String targetId);

    List<Favorite> findByOpenidAndFavoriteType(String openid, Integer favoriteType);

    void cancelFavorite(String openid, Integer favoriteType, String targetId);

    void addFavorite(String openid, Integer favoriteType, String targetId);

    CollectBook initACollectBook(String openid, String cqbId);

    CollectBook addCollectQuestion(String questionId, CollectBook collectBook);

    void cancelCollect(String openid, String questionId);

    CollectBook findCollectBookById(String collectBookId);

    CollectBookDTO findCollectBookDTOById(String collectBookId);
}
