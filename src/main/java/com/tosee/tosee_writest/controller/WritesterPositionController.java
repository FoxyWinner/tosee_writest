package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;
import com.tosee.tosee_writest.service.PositionService;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import com.tosee.tosee_writest.vo.WorkFieldVO;
import com.tosee.tosee_writest.vo.WorkPostionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:44 下午
 */
@RestController
@RequestMapping("/writester/position")
public class WritesterPositionController
{
    @Autowired
    private PositionService positionService;


    @GetMapping("/list")
    public ResultVO fieldList()
    {
        List<WorkFieldVO> workFieldVOList = new ArrayList<>();

        // 先查出所有的field
        List<WorkField> workFields = positionService.findAllFields();

        for (WorkField workField : workFields)
        {
            WorkFieldVO workFieldVO = new WorkFieldVO();
            List<WorkPosition> workPositions = positionService.findByFieldType(workField.getFieldType());
            List<WorkPostionVO> workPostionVOList = new ArrayList<>();
            for (WorkPosition workPosition : workPositions)
            {
                WorkPostionVO workPostionVO = new WorkPostionVO();
                workPostionVO.setPositionName(workPosition.getPositionName());
                workPostionVO.setPositionType(workPosition.getPositionType());

                workPostionVOList.add(workPostionVO);
            }

            workFieldVO.setFieldName(workField.getFieldName());
            workFieldVO.setFieldType(workField.getFieldType());
            workFieldVO.setWorkPostionVOList(workPostionVOList);

            workFieldVOList.add(workFieldVO);
        }

        return ResultVOUtil.success(workFieldVOList);
    }
}
