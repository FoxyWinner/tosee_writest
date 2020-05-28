package com.tosee.tosee_writest.service;

import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:21 下午
 */
public interface PositionService
{
    List<WorkField> findAllFields();

    List<WorkPosition> findAllPositionsByPQBType(Integer pqbType);

    List<WorkPosition> findAllPositions();

    List<WorkPosition> findByFieldTypeAndPQBType(Integer fieldType,Integer pqbType);

    WorkField findFieldByType(Integer fieldType);

    WorkPosition findPositionByType(Integer positionType);
}
