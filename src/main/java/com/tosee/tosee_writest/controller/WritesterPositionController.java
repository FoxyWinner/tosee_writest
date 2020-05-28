package com.tosee.tosee_writest.controller;

import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;
import com.tosee.tosee_writest.enums.PositionPQBTypeEnum;
import com.tosee.tosee_writest.service.PositionService;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import com.tosee.tosee_writest.vo.WorkFieldVO;
import com.tosee.tosee_writest.vo.WorkPostionVO;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResultVO fieldAndPositionList(@RequestParam(value = "type", defaultValue = "3")Integer type)
    {
        List<WorkFieldVO> workFieldVOList = new ArrayList<>();

        // 先查出所有的field
        List<WorkField> workFields = positionService.findAllFields();

        for (WorkField workField : workFields)
        {
            WorkFieldVO workFieldVO = new WorkFieldVO();
            List<WorkPosition> workPositions;
            // 这个type默认为3，对应着岗位类型"全部"
            if (type == PositionPQBTypeEnum.ALL_PQBTYPE.getCode())
            {
                workPositions = positionService.findByFieldTypeAndPQBType(workField.getFieldType(),type);
            }
            else
            {
                // 拼接
                workPositions = positionService.findByFieldTypeAndPQBType(workField.getFieldType(),PositionPQBTypeEnum.ALL_PQBTYPE.getCode());
                List<WorkPosition> specialWorkPositions = positionService.findByFieldTypeAndPQBType(workField.getFieldType(),type);
                workPositions.addAll(specialWorkPositions);
            }

            List<WorkPostionVO> workPostionVOList = new ArrayList<>();

            // VO转换
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

    @GetMapping("/fieldlist")
    public ResultVO fieldList()
    {
        List<WorkField> workFields = positionService.findAllFields();

        List<WorkFieldVO> result = new ArrayList<>();
        for (WorkField workField : workFields)
        {
            WorkFieldVO workFieldVO = new WorkFieldVO();
            BeanUtils.copyProperties(workField,workFieldVO);
            result.add(workFieldVO);
        }

        return ResultVOUtil.success(result);
    }

    @GetMapping("/positionlist")
    public ResultVO positionList(@RequestParam(value = "type", defaultValue = "3")Integer type)
    {
        List<WorkPosition> workPositions;
        // 这个type默认为3，对应着岗位类型"全部"
        if (type == PositionPQBTypeEnum.ALL_PQBTYPE.getCode())
        {
            workPositions = positionService.findAllPositionsByPQBType(type);
        }
        else
        {
            // 拼接
            workPositions = positionService.findAllPositionsByPQBType(PositionPQBTypeEnum.ALL_PQBTYPE.getCode());
            List<WorkPosition> specialWorkPositions = positionService.findAllPositionsByPQBType(type);
            workPositions.addAll(specialWorkPositions);
        }

        // 切换VO
        List<WorkPostionVO> result = new ArrayList<>();
        for (WorkPosition workPosition : workPositions)
        {
            WorkPostionVO workPostionVO = new WorkPostionVO();
            BeanUtils.copyProperties(workPosition,workPostionVO);
            result.add(workPostionVO);
        }

        return ResultVOUtil.success(result);
    }
}
