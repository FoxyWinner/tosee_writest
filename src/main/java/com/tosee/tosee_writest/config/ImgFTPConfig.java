package com.tosee.tosee_writest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: FoxyWinner
 * @Date: 2020/6/2 1:38 下午
 */
@Component
@EnableConfigurationProperties(com.tosee.tosee_writest.config.ImgFTPConfig.class)
@ConfigurationProperties("ftp")
@Data
public class ImgFTPConfig
{
    private String server;

    private int port;

    private String username;

    private String password;

    private String host;
}
