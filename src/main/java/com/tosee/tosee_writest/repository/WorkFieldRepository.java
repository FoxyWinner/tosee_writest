package com.tosee.tosee_writest.repository;

import com.tosee.tosee_writest.dataobject.WorkField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 4:21 下午
 */
public interface WorkFieldRepository extends JpaRepository<WorkField, Integer>
{
    WorkField findByFieldType(Integer fieldType);

    List<WorkField> findByFieldTypeIn(List<Integer> fieldTypes);
}
