package com.tosee.tosee_writest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.dto.ChildQuestionBankDTO;
import com.tosee.tosee_writest.dto.ParentQuestionBankDTO;
import com.tosee.tosee_writest.dto.QuestionDTO;
import com.tosee.tosee_writest.enums.ParentQuestionBankTypeEnum;
import com.tosee.tosee_writest.enums.QuestionTypeEnum;
import com.tosee.tosee_writest.enums.ResultEnum;
import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.form.ChildQuestionBankForm;
import com.tosee.tosee_writest.form.ParentQuestionBankForm;
import com.tosee.tosee_writest.form.QuestionForm;
import com.tosee.tosee_writest.service.CompanyService;
import com.tosee.tosee_writest.service.PositionService;
import com.tosee.tosee_writest.service.QuestionBankService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/6 7:00 下午
 */
@RestController
@RequestMapping("/operator/questionbank")
@Slf4j
public class OperatorQuestionBankController
{
    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PositionService positionService;


    /**
     * 父题库列表
     * @param page 第几页，从第一页开始
     * @param size 一页有多少条数据
     * @return
     */
    @GetMapping("/pqblist")
    public ModelAndView pqbList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                Map<String, Object> map)
    {
        log.info("【运营父题库列表】进入该请求");
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ParentQuestionBankDTO> parentQuestionBankDTOPage = questionBankService.findPQBList4Admin(request);
        map.put("parentQuestionBankDTOPage", parentQuestionBankDTOPage);

        map.put("currentPage", page);
        map.put("size", size);
        //viewName对应ftl的位置
        return new ModelAndView("/parentquestionbank/list", map);
    }

    @GetMapping("/pqbindex")
    public ModelAndView pqbndex(@RequestParam(value = "parentQbId", required = false) String parentQbId,
                              Map<String, Object> map) {
        if (!StringUtils.isEmpty(parentQbId)) {
//            ParentQuestionBankDTO parentQuestionBankDTO = questionBankService.findOneParentQuestionBankDTO(parentQbId);
            ParentQuestionBank parentQuestionBank = questionBankService.findOneParentQuestionBank(parentQbId);
            map.put("parentQuestionBank", parentQuestionBank);
        }

        // 查询题库类型
        List<ParentQuestionBankTypeEnum> typeEnumList = new ArrayList<>();
        typeEnumList.add(ParentQuestionBankTypeEnum.ENTERPRISE_BANK);
        typeEnumList.add(ParentQuestionBankTypeEnum.PROFESSIONAL_BANK);
        typeEnumList.add(ParentQuestionBankTypeEnum.ADMINISTRATIVE_APTITUDE_BANK);

        //查询所有的公司供列表选择
        List<Company> companyList = companyService.findAllCompanies();

        //查询行业列表
        List<WorkField> workFieldList = positionService.findAllFields();

        //查询职位列表
        List<WorkPosition> workPositionList = positionService.findAllPositions();

        /** 供下拉列表读取的各项参数. */
        map.put("typeEnumList", typeEnumList);
        map.put("companyList", companyList);
        map.put("workFieldList", workFieldList);
        map.put("workPositionList", workPositionList);


        return new ModelAndView("parentquestionbank/index", map);
    }

    @PostMapping("/pqbsave")
    public ModelAndView pqbSave(@Valid ParentQuestionBankForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map)
    {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/toseewritest/operator/questionbank/pqbindex");
            return new ModelAndView("common/error", map);
        }

        ParentQuestionBank parentQuestionBank  = new ParentQuestionBank();
        try
        {
            //如果parentQbId为空, 说明是新增
            if (!StringUtils.isEmpty(form.getParentQbId()))
            {
                parentQuestionBank = questionBankService.findOneParentQuestionBank(form.getParentQbId());
            } else
            {
                parentQuestionBank.setIsRelase(0);
                parentQuestionBank.setCqbNumber(0);
                form.setParentQbId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(form, parentQuestionBank);

            if(form.getIsRecommended()) parentQuestionBank.setIsRecommended(1);
            else parentQuestionBank.setIsRecommended(0);



            log.info("【父题库保存】{}",parentQuestionBank);

            questionBankService.saveParentQuestionBank(parentQuestionBank);
        } catch (WritestException e)
        {
            map.put("msg", e.getMessage());
            map.put("url", "/toseewritest/operator/questionbank/pqbindex");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/toseewritest/operator/questionbank/pqblist");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("/pqbrelase")
    public ModelAndView pqbRelase(@RequestParam (value = "parentQbId" , required = true) String parentQbId,
                                Map<String, Object> map)
    {

        ParentQuestionBank parentQuestionBank  = questionBankService.findOneParentQuestionBank(parentQbId);
        try
        {
            questionBankService.relaseParentQuestionBank(parentQuestionBank);
        } catch (WritestException e)
        {
            map.put("msg", e.getMessage());
            map.put("url", "/toseewritest/operator/questionbank/pqblist");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/toseewritest/operator/questionbank/pqblist");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("/pqbcancelrelase")
    public ModelAndView pqbCancelRelase(@RequestParam (value = "parentQbId" , required = true) String parentQbId,
                                  Map<String, Object> map)
    {

        ParentQuestionBank parentQuestionBank  = questionBankService.findOneParentQuestionBank(parentQbId);
        try
        {
            questionBankService.cancelParentQuestionBank(parentQuestionBank);
        } catch (WritestException e)
        {
            map.put("msg", e.getMessage());
            map.put("url", "/toseewritest/operator/questionbank/pqblist");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/toseewritest/operator/questionbank/pqblist");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("/cqblist")
    public ModelAndView cqbList(@RequestParam (value = "parentQbId" , required = false) String parentQbId,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                Map<String, Object> map)
    {
        log.info("【运营子题库列表】{}",parentQbId);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ChildQuestionBankDTO> childQuestionBankPage;

        if(StringUtils.isEmpty(parentQbId))
            childQuestionBankPage = questionBankService.findCQBList(request);
        else
            childQuestionBankPage = questionBankService.findCQBList(request,parentQbId);
        map.put("childQuestionBankPage", childQuestionBankPage);

        map.put("currentPage", page);
        map.put("size", size);
        //viewName对应ftl的位置
        return new ModelAndView("/childquestionbank/list", map);
    }


    @GetMapping("/cqbindex")
    public ModelAndView cqbindex(@RequestParam(value = "childQbId", required = false) String childQbId,
                                Map<String, Object> map) {
        if (!StringUtils.isEmpty(childQbId)) {
            ChildQuestionBank childQuestionBank = questionBankService.findCQBById(childQbId);
            map.put("childQuestionBank", childQuestionBank);
        }

        //查询父题库列表
        List<ParentQuestionBank> parentQuestionBankList = questionBankService.findAllPQBs();

        /** 供下拉列表读取的各项参数. */
        map.put("parentQuestionBankList", parentQuestionBankList);
        return new ModelAndView("childquestionbank/index", map);
    }

    @PostMapping("/cqbsave")
    public ModelAndView cqbsave(@Valid ChildQuestionBankForm form,
                                BindingResult bindingResult,
                                Map<String, Object> map)
    {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/toseewritest/operator/questionbank/cqbindex");
            return new ModelAndView("common/error", map);
        }

        ChildQuestionBank childQuestionBank  = new ChildQuestionBank();
        try
        {
            //如果parentQbId为空, 说明是新增
            if (!StringUtils.isEmpty(form.getChildQbId()))
            {
                childQuestionBank = questionBankService.findCQBById(form.getChildQbId());
            } else
            {
                form.setChildQbId(KeyUtil.genUniqueKey());
                childQuestionBank.setQuestionNumber(0);
            }
            BeanUtils.copyProperties(form, childQuestionBank);
            if (form.getIsRecommended()) childQuestionBank.setIsRecommended(1);
            else childQuestionBank.setIsRecommended(0);


            log.info("【子题库保存】{}",childQuestionBank);
            questionBankService.saveChildQuestionBank(childQuestionBank);

        } catch (WritestException e)
        {
            map.put("msg", e.getMessage());
            map.put("url", "/toseewritest/operator/questionbank/cqbindex");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/toseewritest/operator/questionbank/cqblist");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("questionlist")
    public ModelAndView questionlist(@RequestParam (value = "childQbId" , required = false) String childQbId,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                Map<String, Object> map)
    {
        log.info("【运营题目列表】{}",childQbId);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<QuestionDTO> questionDTOPage;

        if(StringUtils.isEmpty(childQbId))
            questionDTOPage = questionBankService.findQuestionsDTOList(request);
        else
            questionDTOPage = questionBankService.findQuestionsDTOList(request,childQbId);

        map.put("currentchildQbId",childQbId);

        map.put("questionDTOPage", questionDTOPage);
        map.put("currentPage", page);
        map.put("size", size);

        return new ModelAndView("/question/list", map);
    }


    @GetMapping("questionindex")
    public ModelAndView questionIndex(@RequestParam(value = "questionId", required = false) String questionId,
                                      @RequestParam(value = "lastChildQbId", required = false) String lastChildQbId,
                                      Map<String, Object> map)
    {
        if (!StringUtils.isEmpty(questionId)) {
            QuestionDTO questionDTO = questionBankService.findQuestionById(questionId);
            map.put("questionDTO", questionDTO);
        }

        if (!StringUtils.isEmpty(lastChildQbId)) {
            map.put("lastChildQbId", lastChildQbId);
        }

        // 题型选择列表
        List<QuestionTypeEnum> typeEnumList = new ArrayList<>();
        typeEnumList.add(QuestionTypeEnum.SINGLE_CHOICE);
        typeEnumList.add(QuestionTypeEnum.MULTIPLE_CHOICE);
        typeEnumList.add(QuestionTypeEnum.ESSAY_QUESTION);

        // 子题库选择列表
        List<ChildQuestionBank> childQuestionBankList = questionBankService.findAllCQBs();

        /** 供下拉列表读取的各项参数. */
        map.put("typeEnumList", typeEnumList);
        map.put("childQuestionBankList", childQuestionBankList);

        return new ModelAndView("question/index", map);
    }

    /**
     * todo 这个方法被弃用了
     * @param questionId
     * @param map
     * @return
     */
    @GetMapping("questionindextest")
    public ModelAndView questionIndexTest(@RequestParam(value = "questionId", required = false) String questionId,
                                      Map<String, Object> map)
    {
        if (!StringUtils.isEmpty(questionId)) {
            QuestionDTO questionDTO = questionBankService.findQuestionById(questionId);
            map.put("questionDTO", questionDTO);
        }

        // 题型选择列表
        List<QuestionTypeEnum> typeEnumList = new ArrayList<>();
        typeEnumList.add(QuestionTypeEnum.SINGLE_CHOICE);
        typeEnumList.add(QuestionTypeEnum.MULTIPLE_CHOICE);
        typeEnumList.add(QuestionTypeEnum.ESSAY_QUESTION);

        // 子题库选择列表
        List<ChildQuestionBank> childQuestionBankList = questionBankService.findAllCQBs();

        /** 供下拉列表读取的各项参数. */
        map.put("typeEnumList", typeEnumList);
        map.put("childQuestionBankList", childQuestionBankList);

        return new ModelAndView("question/indextest", map);
    }

    @PostMapping("questionsave")
    public ModelAndView questionsave(@Valid QuestionForm form,
                                BindingResult bindingResult,
                                Map<String, Object> map)
    {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/toseewritest/operator/questionbank/questionindex");
            return new ModelAndView("common/error", map);
        }

        log.info("form",form);
        QuestionDTO questionDTO = new QuestionDTO();
        try
        {
            if (!StringUtils.isEmpty(form.getQuestionId()))
            {
                // 是更改题目
                questionDTO = questionBankService.findQuestionById(form.getQuestionId());

                map.put("msg", "返回子题库列表");
                map.put("url", "/toseewritest/operator/questionbank/questionlist?childQbId="+questionDTO.getChildQbId());
            } else
            {
                // 是新增题目
                form.setQuestionId(KeyUtil.genUniqueKey());
                // 题号
                Integer questionSeq = questionBankService.getQuestionNumber(form.getChildQbId()) + 1;
                questionDTO.setQuestionSeq(questionSeq);

                // 跳转继续新增
                map.put("msg", "将继续为该子题库添加题目");
                map.put("url", "/toseewritest/operator/questionbank/questionindex?lastChildQbId="+questionDTO.getChildQbId());
            }

            BeanUtils.copyProperties(form, questionDTO);


            // 将form中的questionOptions字符串转为对象List，并存入questionDTO中。
            if (form.getQuestionType() != QuestionTypeEnum.ESSAY_QUESTION.getCode())
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setPrettyPrinting();
                Gson gson = gsonBuilder.create();
                List<QuestionOption> questionOptionList = gson.fromJson(form.getQuestionOptions(),  new TypeToken<List<QuestionOption>>(){}.getType());

//            log.info("【选项列表】{}",questionOptionList);
                for (QuestionOption questionOption : questionOptionList)
                {
                    if (questionOption.getOptionId() == null || questionOption.getOptionId().isEmpty())
                        questionOption.setOptionId(KeyUtil.genUniqueKey());
                    if (questionOption.getQuestionId() == null || questionOption.getQuestionId().isEmpty())
                        questionOption.setQuestionId(questionDTO.getQuestionId());
                }

                questionDTO.setQuestionOptions(questionOptionList);
            }

            log.info("【题目保存】{}",questionDTO);


            // 这个save方法中，包含了对子题库题目数的维护
            questionBankService.saveQuestionDTO(questionDTO);
        } catch (WritestException e)
        {
            map.put("msg", e.getMessage());
            map.put("url", "/toseewritest/operator/questionbank/questionindex");
            return new ModelAndView("common/error", map);
        }


        return new ModelAndView("common/success", map);
    }


    /**
     * todo 这个方法被弃用了
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("questionsaveajax")
    public ResultVO questionSaveAjax(@Valid QuestionForm form,
                                     BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        QuestionDTO questionDTO = new QuestionDTO();
        try
        {
            if (!StringUtils.isEmpty(form.getQuestionId()))
            {
                // 是更改题目
                questionDTO = questionBankService.findQuestionById(form.getQuestionId());
            } else
            {
                // 是新增题目
                form.setQuestionId(KeyUtil.genUniqueKey());
                // 题号
                Integer questionSeq = questionBankService.getQuestionNumber(form.getChildQbId()) + 1;
                questionDTO.setQuestionSeq(questionSeq);
            }

            BeanUtils.copyProperties(form, questionDTO);


            // 将form中的questionOptions字符串转为对象List，并存入questionDTO中。
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            List<QuestionOption> questionOptionList = gson.fromJson(form.getQuestionOptions(),  new TypeToken<List<QuestionOption>>(){}.getType());

            for (QuestionOption questionOption : questionOptionList)
            {
                if (questionOption.getOptionId() == null || questionOption.getOptionId().isEmpty())
                    questionOption.setOptionId(KeyUtil.genUniqueKey());
                if (questionOption.getQuestionId() == null || questionOption.getQuestionId().isEmpty())
                    questionOption.setQuestionId(questionDTO.getQuestionId());
            }

            questionDTO.setQuestionOptions(questionOptionList);
            log.info("【题目保存】{}",questionDTO);


            // 这个save方法中，包含了对子题库题目数的维护
            questionBankService.saveQuestionDTO(questionDTO);
        } catch (WritestException e)
        {
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
        }

        return ResultVOUtil.success("保存成功");
    }


    @GetMapping("questiondelete")
    public ModelAndView questionDelete(@RequestParam("questionId") String questionId,
                                          Map<String, Object> map)
    {
        if (StringUtils.isEmpty(questionId)) {
            map.put("msg", ResultEnum.PARAM_ERROR.getMessage());
            map.put("url", "/toseewritest/operator/questionbank/questionlist");
            return new ModelAndView("common/error", map);
        }


        // 删除该题目，内部包含了对子题库题目数的处理
        String childQbId = questionBankService.deleteQuestionById(questionId);



        map.put("msg", "将返回该子题库列表");
        map.put("url", "/toseewritest/operator/questionbank/questionlist?childQbId="+childQbId);
        return new ModelAndView("common/success", map);
    }
}
