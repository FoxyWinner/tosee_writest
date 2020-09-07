package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.dataobject.*;
import com.tosee.tosee_writest.form.PassRecordForm;
import com.tosee.tosee_writest.service.*;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;


/**出校申请控制器
 * @Author: FoxyWinner
 * @Date: 2020/5/12 11:00 下午
 */
@RestController
@RequestMapping("/writester/pass")
@Slf4j
public class WritesterPassController
{
    @Autowired
    private PassRecordService passRecordService;


    @PostMapping("/commitinfo")
    public ResultVO mistakeBookList(@Valid PassRecordForm form,
                                    BindingResult bindingResult
                                    )
    {
        if (bindingResult.hasErrors()) {
            return ResultVOUtil.error(1,"表单验证失败");
        }
        PassRecord passRecord = new PassRecord();
        BeanUtils.copyProperties(form,passRecord);

        passRecordService.commitPassRecord(passRecord);

        return ResultVOUtil.success();
    }


}
