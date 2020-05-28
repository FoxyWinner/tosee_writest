package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.converter.User2UserDTOConverter;
import com.tosee.tosee_writest.dataobject.Company;
import com.tosee.tosee_writest.dataobject.User;
import com.tosee.tosee_writest.dto.UserDTO;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.CompanyRepository;
import com.tosee.tosee_writest.repository.UserRepository;
import com.tosee.tosee_writest.service.CompanyService;
import com.tosee.tosee_writest.service.UserService;
import com.tosee.tosee_writest.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:07 下午
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUserInfo(UserDTO userDTO)
    {
        User user = userRepository.findByOpenid(userDTO.getOpenid());
        // 如果该用户在我们的用户表中无信息，则新建条目
        if(user == null)
        {
            user = new User();
            BeanUtils.copyProperties(userDTO,user);
            user.setUserId(KeyUtil.genUniqueKey());
            user.setTargetFields(userDTO.getTargetFields().toString());
            user.setTargetPositions(userDTO.getTargetPositions().toString());
        }
        else
        {
            // 得保留userId
            String userId = user.getUserId();
            BeanUtils.copyProperties(userDTO,user);
            // 复写回去
            user.setUserId(userId);
            user.setTargetFields(userDTO.getTargetFields().toString());
            user.setTargetPositions(userDTO.getTargetPositions().toString());
        }

        userRepository.save(user);
    }

    @Override
    public UserDTO getUserInfo(String openid)
    {
        User user = userRepository.findByOpenid(openid);
        UserDTO result = null;
        if (user == null)
        {
            log.info("【获取用户信息】该用户不存在");
        }
        else
        {
            result = User2UserDTOConverter.convert(user);
        }

        return result;
    }


}