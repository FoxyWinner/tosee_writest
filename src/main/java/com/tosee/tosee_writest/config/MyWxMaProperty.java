package com.tosee.tosee_writest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@EnableConfigurationProperties(com.tosee.tosee_writest.config.MyWxMaProperty.class)
@ConfigurationProperties(prefix = "wechat.miniapp")
//将自动通过yml配置文件中的配置导入属性
public class MyWxMaProperty
{
    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 开放平台ID
     */
    private String openAppId;

    /**
     * 开放平台Secret
     */
    private String openAppSecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 微信支付商户密钥
     */
    private String mchKey;
    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;

    //微信支付异步通知地址
    private String notifyUrl;

    private String msgDataFormat;

}
