package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.Mistake;
import com.tosee.tosee_writest.dataobject.MistakeBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface MistakeBookRepository extends JpaRepository<MistakeBook, String>
{
    MistakeBook findByOpenidAndCqbId(String openid, String cqbid);

    List<MistakeBook> findByOpenidOrderByUpdateTimeDesc(String openid);

}
