package com.tosee.tosee_writest.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tosee.tosee_writest.enums.PositionPQBTypeEnum;
import com.tosee.tosee_writest.utils.EnumUtil;
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

    // 20200526重构该岗位属于特定的类型！
    private Integer pqbType;

    /** 所属行业编号. */
    private Integer fieldType;

    /** 职位编号. */
    private Integer positionType;

    /** 职位名称. */
    private String positionName;

    @JsonIgnore
    public String getPqbTypeName()
    {
        return EnumUtil.getByCode(pqbType, PositionPQBTypeEnum.class).getMessage();
    }
}
