package com.tosee.tosee_writest.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
@Slf4j
public class WeChatController
{

    @Autowired
    private WxMaService wxMaService;

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
}
