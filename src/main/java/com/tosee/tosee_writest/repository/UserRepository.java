package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.MistakeBook;
import com.tosee.tosee_writest.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:48 下午
 */
public interface UserRepository extends JpaRepository<User, String>
{
    User findByOpenid(String openid);
}
