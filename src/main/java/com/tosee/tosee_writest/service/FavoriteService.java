package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.Favorite;
import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:21 下午
 */
public interface FavoriteService
{
    Integer findByOpenidAndFavoriteTypeAndTargetId(String openid, Integer favoriteType, String targetId);

    void cancelFavorite(String openid, Integer favoriteType, String targetId);

    void addFavorite(String openid, Integer favoriteType, String targetId);
}
