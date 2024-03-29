package com.tosee.tosee_writest.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.UserDTO;
import com.tosee.tosee_writest.enums.*;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.form.RecordForm;
import com.tosee.tosee_writest.form.UserInfoForm;
import com.tosee.tosee_writest.repository.FeedbackRepository;
import com.tosee.tosee_writest.service.PositionService;
import com.tosee.tosee_writest.service.UserService;
import com.tosee.tosee_writest.utils.EnumUtil;
import com.tosee.tosee_writest.utils.KeyUtil;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import io.netty.util.internal.ResourcesUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.Position;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
@Slf4j
/**
 * 这个路径名其实不算很好，对应"我的"页面
 */
public class WeChatController
{

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    //这是给微信小程序用的，不是web管理
    @GetMapping("/auth")
    public ResultVO<Map<String,String>> auth(@RequestParam("code") String code)
    {
        if (StringUtils.isBlank(code))
        {
            return ResultVOUtil.error(ResultEnum.WECHAT_MA_EMPTY_JSCODE.getCode(),ResultEnum.WECHAT_MA_EMPTY_JSCODE.getMessage());
        }


        //1.配置 已完成 通过WxMaConfiguration WxMaProperty @Autowired wxMaService;
        //2.调用方法
        WxMaJscode2SessionResult sessionInfo = new WxMaJscode2SessionResult();
        try
        {
            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e)
        {
            log.error("【小程序登录授权】{}",e);
            throw new WritestException(ResultEnum.WECHAT_MA_ERROR.getCode(),e.getError().getErrorMsg());
        }

        //如果没问题的话 拿到key 和 openid
        String openid = sessionInfo.getOpenid();
        log.info("【根据JSCODE获取sessionKey={},openid={}】",sessionInfo.getSessionKey(),sessionInfo.getOpenid());

        //将openid返回供前端使用
        Map<String, String> map = new HashMap<>();
        map.put("openid",openid);

        return ResultVOUtil.success(map);
    }

    @PostMapping("/saveinfo")
    public ResultVO saveUserInfo(@Valid UserInfoForm userInfoForm,
                                 BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            log.error("【保存用户信息】参数不正确，userInfoForm={}",userInfoForm);
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        userInfoForm.setAuthorised(1);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfoForm,userDTO);

        log.info("【保存用户信息】准备保存userDTO：{}",userDTO);

        userService.saveUserInfo(userDTO);
        return ResultVOUtil.success();
    }

    @GetMapping("/getminiinfo")
    public ResultVO getMiniUserInfo(@RequestParam("openid") String openid)
    {
        if (StringUtils.isEmpty(openid))
        {
            log.error("【查询用户信息】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        UserDTO userDTO;
        userDTO = userService.getUserInfo(openid);
        if (userDTO  == null)
            return ResultVOUtil.error(ResultEnum.USERINFO_NOT_EXIST.getCode(),ResultEnum.USERINFO_NOT_EXIST.getMessage());

        MiniUserInfoVO userInfoVO = this.convertUserDTO2MiniUserVO(userDTO);

        return ResultVOUtil.success(userInfoVO);
    }


    @GetMapping("/getauthstate")
    public ResultVO getAuthorState(@RequestParam("openid") String openid)
    {
        if (StringUtils.isEmpty(openid))
        {
            log.error("【查询用户信息】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        Map<String, Object> result = new HashMap();

        UserDTO userDTO;
        userDTO = userService.getUserInfo(openid);
        if (userDTO  == null)
            result.put("authorised",0);
        else
        {
            Integer authorised = userDTO.getAuthorised();
            result.put("authorised",authorised);
        }


        return ResultVOUtil.success(result);
    }

    @GetMapping("/getinfo")
    public ResultVO getUserInfo(@RequestParam("openid") String openid)
    {
        if (StringUtils.isEmpty(openid))
        {
            log.error("【查询用户信息】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        UserDTO userDTO;
        userDTO = userService.getUserInfo(openid);

        if (userDTO  == null)
            return ResultVOUtil.error(ResultEnum.USERINFO_NOT_EXIST.getCode(),ResultEnum.USERINFO_NOT_EXIST.getMessage());

        UserVO userVO = this.convertUserDTO2UserVO(userDTO);

        return ResultVOUtil.success(userVO);
    }

    @PostMapping("/feedback")
    public ResultVO feedback(@RequestParam("openid") String openid,
                             @RequestParam("content") String content)
    {
        if (StringUtils.isEmpty(openid))
        {
            log.error("【用户反馈】openid为空");
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        if (StringUtils.isEmpty(content))
        {
            log.error("【用户反馈】content为空");
            throw new WritestException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackId(KeyUtil.genUniqueKey());
        feedback.setContent(content);
        feedback.setOpenid(openid);
        feedbackRepository.save(feedback);

        return ResultVOUtil.success();
    }

    public UserVO convertUserDTO2UserVO(UserDTO userDTO)
    {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO,userVO);
        if (userDTO.getEduDegree() != null)
            userVO.setEduDegree(EnumUtil.getByCode(userDTO.getEduDegree(),UserEduDegreeEnum.class).getMessage());
        if (userDTO.getGender() != null)
            userVO.setGender(EnumUtil.getByCode(userDTO.getGender(), UserGenderEnum.class).getMessage());

        List<WorkFieldVO> workFieldVOS = new ArrayList<>();
        List<WorkPostionVO> workPostionVOS = new ArrayList<>();

        if (userDTO.getTargetFields() != null)
            for (Integer fieldType : userDTO.getTargetFields())
            {
                WorkFieldVO workFieldVO = new WorkFieldVO();
                WorkField workField = positionService.findFieldByType(fieldType);
                BeanUtils.copyProperties(workField, workFieldVO);
                workFieldVOS.add(workFieldVO);
            }

        if (userDTO.getTargetPositions() != null)
            for (Integer positionType : userDTO.getTargetPositions())
            {
                WorkPostionVO workPostionVO = new WorkPostionVO();
                WorkPosition workPosition = positionService.findPositionByType(positionType);
                BeanUtils.copyProperties(workPosition, workPostionVO);
                workPostionVOS.add(workPostionVO);
            }

        userVO.setTargetFields(workFieldVOS);
        userVO.setTargetPositions(workPostionVOS);
        return userVO;
    }

    public MiniUserInfoVO convertUserDTO2MiniUserVO(UserDTO userDTO)
    {
        MiniUserInfoVO miniUserInfoVO = new MiniUserInfoVO();
        BeanUtils.copyProperties(userDTO,miniUserInfoVO);

        List<String> workFieldVOS = new ArrayList<>();
        List<String> workPostionVOS = new ArrayList<>();

        if (userDTO.getTargetFields() != null)
            for (Integer fieldType : userDTO.getTargetFields())
            {
                WorkFieldVO workFieldVO = new WorkFieldVO();
                WorkField workField = positionService.findFieldByType(fieldType);
                if (workField != null)
                {
                    BeanUtils.copyProperties(workField, workFieldVO);
                    workFieldVOS.add(workFieldVO.getFieldName());
                }
            }

        if (userDTO.getTargetPositions() != null)
            for (Integer positionType : userDTO.getTargetPositions())
            {
                WorkPostionVO workPostionVO = new WorkPostionVO();
                WorkPosition workPosition = positionService.findPositionByType(positionType);
                if (workPosition != null)
                {
                    BeanUtils.copyProperties(workPosition, workPostionVO);
                    workPostionVOS.add(workPostionVO.getPositionName());
                }
            }

        miniUserInfoVO.setTargetFields(workFieldVOS);
        miniUserInfoVO.setTargetPositions(workPostionVOS);
        return miniUserInfoVO;
    }




}
