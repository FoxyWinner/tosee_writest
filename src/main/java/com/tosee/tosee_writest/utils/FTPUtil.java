package com.tosee.tosee_writest.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import com.tosee.tosee_writest.config.ImgFTPConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/6/2 1:39 下午
 */

@Slf4j
@Data
public class FTPUtil
{
    private ImgFTPConfig imgFTPConfig;

    private FTPClient ftpClient;


    public static boolean uploadFile(ImgFTPConfig imgFTPConfig, List<File> fileList) throws IOException
    {
        FTPUtil ftpUtil = new FTPUtil();
        ftpUtil.imgFTPConfig = imgFTPConfig;
        log.info("【开始连接FTP服务器】");
        boolean result = ftpUtil.uploadFile("img",fileList);
        log.info("【结束上传】上传结果：{}",result);

        return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException
    {
        boolean uploaded = true;
        FileInputStream fis = null;
        // 链接FTP服务器
        boolean isConnect = connectServer(imgFTPConfig.getServer(),imgFTPConfig.getPort(),imgFTPConfig.getUsername(),imgFTPConfig.getPassword());
        if (isConnect)
        {
            try
            {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList)
                {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);

                }

            } catch (IOException e)
            {
                log.error("【上传文件异常】{}",e);
                uploaded = false;
            } finally
            {
                fis.close();
                ftpClient.disconnect();
            }
        }
        else uploaded = false;
        return uploaded;
    }

    private boolean connectServer(String server,int port, String username, String password)
    {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try
        {
            ftpClient.connect(server,port);
            isSuccess = ftpClient.login(username,password);
        } catch (IOException e)
        {
            log.error("【连接FTP服务器异常】{}",e);
        }
        return isSuccess;
    }
}
