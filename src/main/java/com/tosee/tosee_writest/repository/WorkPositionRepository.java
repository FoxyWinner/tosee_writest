package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.WorkPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:13 下午
 */
public interface WorkPositionRepository extends JpaRepository<WorkPosition, Integer>
{
    List<WorkPosition> findByPositionNameLike(String search);

    List<WorkPosition> findByFieldTypeAndPqbType(Integer fieldType,Integer pqbType);

    List<WorkPosition> findByPqbType(Integer pqbType);

    WorkPosition findByPositionType(Integer positionType);
}
