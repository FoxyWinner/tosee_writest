package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.Favorite;
import com.tosee.tosee_writest.enums.CollectStateEnum;
import com.tosee.tosee_writest.repository.FavoriteRepository;
import com.tosee.tosee_writest.service.FavoriteService;
import com.tosee.tosee_writest.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:24 下午
 */
@Service
public class FavoriteServiceImpl implements FavoriteService
{
    @Autowired
    private FavoriteRepository favoriteRepository;


    @Override
    public Integer findByOpenidAndFavoriteTypeAndTargetId(String openid, Integer favoriteType, String targetId)
    {
        if(favoriteRepository.existsByOpenidAndFavoriteTypeAndTargetId(openid,favoriteType,targetId))
        {
            return CollectStateEnum.IS_COLLECTED.getCode();
        }
        return CollectStateEnum.IS_NOT_COLLECTED.getCode();
    }

    @Override
    @Transactional
    public void cancelFavorite(String openid, Integer favoriteType, String targetId)
    {
        favoriteRepository.deleteByOpenidAndFavoriteTypeAndTargetId(openid,favoriteType,targetId);
    }

    @Override
    public void addFavorite(String openid, Integer favoriteType, String targetId)
    {
        Favorite favorite = new Favorite();
        favorite.setFavoriteId(KeyUtil.genUniqueKey());
        favorite.setOpenid(openid);
        favorite.setFavoriteType(favoriteType);
        favorite.setTargetId(targetId);

        favoriteRepository.save(favorite);
    }
}
