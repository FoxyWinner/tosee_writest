package com.tosee.tosee_writest.service.impl;

import com.google.common.collect.Lists;
import com.tosee.tosee_writest.config.ImgFTPConfig;
import com.tosee.tosee_writest.dataobject.CollectBook;
import com.tosee.tosee_writest.dataobject.CollectQuestion;
import com.tosee.tosee_writest.dataobject.Favorite;
import com.tosee.tosee_writest.dto.CollectBookDTO;
import com.tosee.tosee_writest.enums.CollectStateEnum;
import com.tosee.tosee_writest.enums.FavoriteTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.repository.CollectBookRepository;
import com.tosee.tosee_writest.repository.CollectQuestionRepository;
import com.tosee.tosee_writest.repository.FavoriteRepository;
import com.tosee.tosee_writest.service.FavoriteService;
import com.tosee.tosee_writest.service.FileService;
import com.tosee.tosee_writest.service.QuestionBankService;
import com.tosee.tosee_writest.utils.FTPUtil;
import com.tosee.tosee_writest.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 5:24 下午
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService
{
    @Override
    public String upload(ImgFTPConfig imgFTPConfig, MultipartFile file, String path)
    {
        String fileName = file.getOriginalFilename();

        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString() + "."+fileExtensionName;

        log.info("【上传文件】开始上传文件，上传文件的文件名：{}，上传路径：{}，新文件名：{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        // 若不存在，创建目录
        if(!fileDir.exists())
        {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path,uploadFileName);

        try
        {
            file.transferTo(targetFile);
            // 文件已经上传成功

            FTPUtil.uploadFile(imgFTPConfig,Lists.newArrayList(targetFile));
            // 已经上传到FTP服务器

            targetFile.delete();
        } catch (IOException e)
        {
            log.error("【上传文件异常】{}",e);
            return null;
        }

        return targetFile.getName();
    }
}
