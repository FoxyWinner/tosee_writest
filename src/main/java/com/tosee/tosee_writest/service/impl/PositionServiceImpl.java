package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;
import com.tosee.tosee_writest.repository.WorkFieldRepository;
import com.tosee.tosee_writest.repository.WorkPositionRepository;
import com.tosee.tosee_writest.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:25 下午
 */
@Service
public class PositionServiceImpl implements PositionService
{
    @Autowired
    private WorkFieldRepository workFieldRepository;
    @Autowired
    private WorkPositionRepository workPositionRepository;

    @Override
    public List<WorkField> findAllFields()
    {
        // 这段代码意思是过滤掉行业："综合"
        List<WorkField>  allWorkFields= workFieldRepository.findAll();
        List<WorkField>  resultWorkFields = new ArrayList<>();

        for (WorkField workField : allWorkFields)
        {
            if (workField.getFieldType() != 0 )
            {
                resultWorkFields.add(workField);
            }
        }
        return resultWorkFields;
    }

    @Override
    public List<WorkPosition> findAllPositions()
    {
        // 过滤岗位综合
        List<WorkPosition> allPositions = workPositionRepository.findAll();
        List<WorkPosition> resultPositions = new ArrayList<>();

        for (WorkPosition workPosition : allPositions)
        {
            if(workPosition.getPositionType() != 0)
            {
                resultPositions.add(workPosition);
            }
        }

        return resultPositions;
    }


    @Override
    public List<WorkPosition> findByFieldType(Integer fieldType)
    {
        return workPositionRepository.findByFieldType(fieldType);
    }

    @Override
    public WorkField findFieldByType(Integer fieldType)
    {
        return workFieldRepository.findByFieldType(fieldType);
    }

    @Override
    public WorkPosition findPositionByType(Integer positionType)
    {
        return workPositionRepository.findByPositionType(positionType);
    }


}
