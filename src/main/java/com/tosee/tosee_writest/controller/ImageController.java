package com.tosee.tosee_writest.controller;

import com.google.common.collect.Maps;
import com.tosee.tosee_writest.config.ImgFTPConfig;
import com.tosee.tosee_writest.dto.ExperienceArticleDTO;
import com.tosee.tosee_writest.dto.UserDTO;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.form.UserInfoForm;
import com.tosee.tosee_writest.service.FileService;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: FoxyWinner
 * @Date: 2020/6/2 12:37 下午
 */
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController
{
    @Autowired
    private FileService fileService;

    @Autowired
    private ImgFTPConfig imgFTPConfig;

    /**
     * 富文本编辑器对应的图片上传接口
     * {
     *   "code": 0 //0表示成功，其它失败
     *   ,"msg": "" //提示信息 //一般上传失败后返回
     *   ,"data":
     *   {
     *     "src": "图片路径"
     *     ,"title": "图片名称" //可选
     *   }
     * }
     * @param file
     * @param request
     * @return
     */
    @PostMapping("richtextupload")
    public ResultVO richtextupload(@RequestParam("file") MultipartFile file,
                           HttpServletRequest request)
    {

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(imgFTPConfig,file,path);
        String url = imgFTPConfig.getHost() + targetFileName;


        Map map = Maps.newHashMap();
        map.put("title", targetFileName);
        map.put("src",url);
        return ResultVOUtil.success(map);
    }


    @PostMapping("/upload")
    public ResultVO upload(@RequestParam("file_data") MultipartFile multipartFile,
                           HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(imgFTPConfig,multipartFile,path);
        String url = imgFTPConfig.getHost() + targetFileName;

        Map map = new HashMap<>();
        map.put("fileName", url);
        return ResultVOUtil.success(map);
    }

    @GetMapping("imgbed")
    public ModelAndView imgBed(Map<String, Object> map)
    {
//        map.put("articlePage", articlePage);
//
//        map.put("currentPage", page);
//        map.put("size", size);
        //viewName对应ftl的位置
        return new ModelAndView("/img/index", map);
    }



}
