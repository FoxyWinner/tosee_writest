package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@DynamicUpdate
@Data
public class WorkPosition
{
    /** 职位ID. */
    @Id
    @GeneratedValue
    private Integer positionId;

    /** 所属行业编号. */
    private Integer fieldType;

    /** 职位编号. */
    private Integer positionType;

    /** 职位名称. */
    private String positionName;
}
