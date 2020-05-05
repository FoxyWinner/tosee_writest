package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Favorite;
import com.tosee.tosee_writest.dataobject.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface FavoriteRepository extends JpaRepository<Favorite, String>
{
    boolean existsByOpenidAndFavoriteTypeAndTargetId(String openid, Integer favoriteType, String targetId);

    void deleteByOpenidAndFavoriteTypeAndTargetId(String openid, Integer favoriteType, String targetId);
}
