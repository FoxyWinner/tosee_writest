package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.Company;
import com.tosee.tosee_writest.dto.UserDTO;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:06 下午
 */
public interface UserService
{
    void saveUserInfo(UserDTO userDTO);

    UserDTO getUserInfo(String openid);
}
