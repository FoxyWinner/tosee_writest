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
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            BeanUtils.copyProperties(userDTO,user,this.getNullPropertyNames(userDTO));
            // 复写回去,不然可能被userDTO中空的ID所覆盖
            user.setUserId(userId);

            if (userDTO.getTargetFields() != null)
                user.setTargetFields(userDTO.getTargetFields().toString());
            if (userDTO.getTargetPositions() != null)
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

    public String[] getNullPropertyNames (Object source)
    {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds)
        {
            Object srcValue = src.getPropertyValue(pd.getName());
            // 此处判断可根据需求修改
            if (srcValue == null)
            {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }



}