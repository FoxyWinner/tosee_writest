package com.tosee.tosee_writest.converter;

import com.tosee.tosee_writest.dataobject.Question;
import com.tosee.tosee_writest.dataobject.User;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.dto.UserDTO;
import com.tosee.tosee_writest.utils.String2ListConvertUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 5:28 下午
 */
public class User2UserDTOConverter
{
    public static UserDTO convert(User user)
    {
        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(user,userDTO);

        // 两个List

        userDTO.setTargetFields(String2ListConvertUtil.String2ListInteger(user.getTargetFields()));
        userDTO.setTargetPositions(String2ListConvertUtil.String2ListInteger(user.getTargetPositions()));

        return userDTO;
    }

    public static List<UserDTO> convert(List<User> userList)
    {
        return userList
                .stream()
                .map(e -> convert(e))
                .collect(Collectors.toList());
    }
}
