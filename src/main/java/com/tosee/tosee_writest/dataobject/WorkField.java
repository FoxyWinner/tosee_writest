package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@DynamicUpdate
@Data
public class WorkField
{

    @Id
    @GeneratedValue
    private Integer fieldId;

    /** 行业编号. */
    private Integer fieldType;

    /** 行业名. */
    private String fieldName;

    public WorkField()
    {
    }


    public WorkField(Integer fieldType, String fieldName) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
    }
}
