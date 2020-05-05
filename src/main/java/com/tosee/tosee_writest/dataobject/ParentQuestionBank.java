package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:08 上午
 */
@Entity
@DynamicUpdate
@Data
public class ParentQuestionBank
{
    @Id
    private String parentQbId;

    /** 父题库类别. */
    private Integer pqbType;

    /** 父题库名称. */
    private String pqbTitle;

    /** 所属公司. */
    private Integer companyId;

    /** 目标行业. */
    private Integer fieldType;

    /** 目标岗位. */
    private Integer positionType;

    private Integer cqbNumber;

    private Integer isRecommended;

    private Integer pqbHeat;

    private String relaseTime;

    public ParentQuestionBank()
    {
    }
}
