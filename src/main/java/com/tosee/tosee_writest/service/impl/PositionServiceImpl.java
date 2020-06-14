package com.tosee.tosee_writest.service.impl;

import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;
import com.tosee.tosee_writest.enums.PositionPQBTypeEnum;
import com.tosee.tosee_writest.repository.WorkFieldRepository;
import com.tosee.tosee_writest.repository.WorkPositionRepository;
import com.tosee.tosee_writest.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:25 下午
 */
@Service
@Slf4j
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
    public List<WorkPosition> findAllPositionsByPQBType(Integer pqbType)
    {
        // 过滤岗位综合
        List<WorkPosition> allPositions = workPositionRepository.findByPqbType(pqbType);
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
    public List<WorkPosition> findByFieldTypeAndPQBType(Integer fieldType, Integer pqbType)
    {
        return workPositionRepository.findByFieldTypeAndPqbType(fieldType,pqbType);
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

    /**
     * 该方法传入的postion的pqbType均为2，不能作为检索题库用，必须用模糊查找名字的方式转为对应的可检索positionTypes
     * @param targetFieldTypes
     * @param targetPostionTypes
     * @return
     */
    @Override
    public Set<Integer> convertRetrievablePositionTypes(List<Integer> targetFieldTypes, List<Integer> targetPostionTypes)
    {

        Set<Integer> retrievablePositionTypes = new HashSet<>();
        List<WorkField> workFields = workFieldRepository.findByFieldTypeIn(targetFieldTypes);
        List<WorkPosition> workPositions = workPositionRepository.findByPositionTypeIn(targetPostionTypes);

        if (workFields == null) return retrievablePositionTypes;
        if (workPositions == null) return retrievablePositionTypes;

//        log.info("【推荐子题库查询】目标行业{}，岗位{}",workFields,workPositions);

        for (WorkField workField : workFields)
        {
            for (WorkPosition workPosition : workPositions)
            {
                List<Integer> positionPQBTypes = Arrays.asList(PositionPQBTypeEnum.ENTERPRISE_BANK.getCode(),PositionPQBTypeEnum.PROFESSIONAL_BANK.getCode());

                // 模糊查询，若查找到就加入set
                // 由于用户选的意向可能是复合岗，先根据/分割
                List<String> positionNameSplit =  CollectionUtils.arrayToList(workPosition.getPositionName().split("/"));

                for (String positionName : positionNameSplit)
                {
//                    log.info("【推荐子题库查询】分割positionName{}",positionName);
                    List<WorkPosition> queryResult = workPositionRepository.findByFieldTypeAndPqbTypeInAndPositionNameLike(workField.getFieldType(),positionPQBTypes,"%"+positionName+"%");
//                    log.info("【推荐子题库查询】可检索岗位{}",queryResult);

                    if (!CollectionUtils.isEmpty(queryResult))
                    {
                        for (WorkPosition item : queryResult)
                        {
                            retrievablePositionTypes.add(item.getPositionType());

                        }
                    }
                }

            }
        }
        return retrievablePositionTypes;
    }


}
