package com.tosee.tosee_writest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.ChildQuestionBankDTO;
import com.tosee.tosee_writest.dto.ExperienceArticleDTO;
import com.tosee.tosee_writest.dto.ParentQuestionBankDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.ParentQuestionBankTypeEnum;
import com.tosee.tosee_writest.enums.QuestionTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.form.ChildQuestionBankForm;
import com.tosee.tosee_writest.form.ExperienceArticleForm;
import com.tosee.tosee_writest.form.ParentQuestionBankForm;
import com.tosee.tosee_writest.form.QuestionForm;
import com.tosee.tosee_writest.service.*;
import com.tosee.tosee_writest.utils.KeyUtil;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/6 7:00 下午
 */
@RestController
@RequestMapping("/operator/experience")
@Slf4j
public class OperatorExperienceController
{
    @Autowired
    private ExperienceArticleService experienceArticleService;

    @Autowired
    private ArticleTagService articleTagService;


    @GetMapping("/articlelist")
    public ModelAndView cqbList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                Map<String, Object> map)
    {
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ExperienceArticleDTO> articlePage;

        articlePage = experienceArticleService.findAll(request);
        map.put("articlePage", articlePage);

        map.put("currentPage", page);
        map.put("size", size);
        //viewName对应ftl的位置
        return new ModelAndView("/article/list", map);
    }


    @GetMapping("/articleindex")
    public ModelAndView cqbindex(@RequestParam(value = "articleId", required = false) String articleId,
                                Map<String, Object> map) {
        if (!StringUtils.isEmpty(articleId)) {
            ExperienceArticle experienceArticle = experienceArticleService.findArticleById(articleId);
            map.put("experienceArticle", experienceArticle);
        }

        //查询父题库列表
        List<ArticleTag> articleTagList = articleTagService.findTagList();

        /** 供下拉列表读取的各项参数. */
        map.put("articleTagList", articleTagList);
        return new ModelAndView("article/index", map);
    }

    @PostMapping("/articlesave")
    public ModelAndView cqbsave(@Valid ExperienceArticleForm form,
                                BindingResult bindingResult,
                                Map<String, Object> map)
    {
        if (bindingResult.hasErrors()) {
            log.info("【文章保存FORM】{}",form);
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/toseewritest/operator/experience/articleindex");
            return new ModelAndView("common/error", map);
        }

        ExperienceArticle experienceArticle  = new ExperienceArticle();
        try
        {
            //如果id为空, 说明是新增
            if (!StringUtils.isEmpty(form.getArticleId()))
            {
                experienceArticle = experienceArticleService.findArticleById(form.getArticleId());
            } else
            {
                form.setArticleId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(form, experienceArticle);
            experienceArticle.setArticleType(0); // 这是个保留字段，暂时用不着
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            experienceArticle.setRelaseTime(df.parse(form.getRelaseTime()));
            if (form.getIsRecommended()) experienceArticle.setIsRecommended(1);
            else experienceArticle.setIsRecommended(0);


            log.info("【文章保存】{}",experienceArticle);
            experienceArticleService.saveArticle(experienceArticle);

        } catch (WritestException | ParseException e)
        {
            map.put("msg", e.getMessage());
            map.put("url", "/toseewritest/operator/experience/articleindex");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/toseewritest/operator/experience/articleindex");
        return new ModelAndView("common/success", map);
    }

}
