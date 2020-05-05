package com.tosee.tosee_writest.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WxMaConfiguration
{
    @Autowired
    private MyWxMaProperty myWxMaProperty;

    /**
     * 将对应的wxService返回
     * @param
     * @return wxService
     */

    @Bean
    public WxMaService wxMaService()
    {
        WxMaConfig wxMaConfig = wxMaConfig();
        WxMaService wxService = new WxMaServiceImpl();

//        if (!appid.equalsIgnoreCase(wxMaConfig.getAppid()))
//        {
//            throw new IllegalArgumentException(String.format("未找到该AppId[%d]配置，请核实！",appid));
//        }

        wxService.setWxMaConfig(wxMaConfig);
        return wxService;
    }

    public WxMaConfig wxMaConfig()
    {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(myWxMaProperty.getAppid());
        config.setSecret(myWxMaProperty.getSecret());
        config.setMsgDataFormat(myWxMaProperty.getMsgDataFormat());
        return config;
    }
}
