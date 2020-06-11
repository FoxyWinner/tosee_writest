package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.config.ImgFTPConfig;
import com.tosee.tosee_writest.dataobject.CollectBook;
import com.tosee.tosee_writest.dataobject.Favorite;
import com.tosee.tosee_writest.dto.CollectBookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:21 下午
 */
public interface FileService
{
    String upload(ImgFTPConfig imgFTPConfig, MultipartFile file, String path);

}
