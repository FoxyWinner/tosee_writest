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
    public List<WorkField> findAll()
    {
        // 这段代码意思是过滤掉行业："综合"
        List<WorkField>  allWorkFields= workFieldRepository.findAll();
        List<WorkField>  resultWorkFields = new ArrayList<>();
        int deleteIndex;
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
    public List<WorkPosition> findByFieldType(Integer fieldType)
    {
        return workPositionRepository.findByFieldType(fieldType);
    }
}
