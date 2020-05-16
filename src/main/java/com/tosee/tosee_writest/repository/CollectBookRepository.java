package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.CollectBook;
import com.tosee.tosee_writest.dataobject.MistakeBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface CollectBookRepository extends JpaRepository<CollectBook, String>
{
    CollectBook findByOpenidAndCqbId(String openid, String cqbid);

    List<CollectBook> findByOpenidOrderByUpdateTimeDesc(String openid);

}
