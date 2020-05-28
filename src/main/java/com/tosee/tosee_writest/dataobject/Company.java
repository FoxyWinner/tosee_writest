package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:28 上午
 */

@Entity
@DynamicUpdate
@Data
public class Company
{
    @Id
    @GeneratedValue
    private Integer companyId;

    private Integer fieldType;

    private String companyName;

    private String companyIcon;

    public Company()
    {
    }
}
