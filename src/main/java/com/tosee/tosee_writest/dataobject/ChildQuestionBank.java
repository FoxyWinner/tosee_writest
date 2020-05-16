package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:34 下午
 */
@Entity
@DynamicUpdate
@Data
public class ChildQuestionBank
{
    @Id
    private String childQbId;

    private String parentQbId;

    private String cqbTitle;

    private Integer cqbHeat;

    private Integer questionNumber;

    private Integer simulationTime;

    private Integer isRecommended;

    private String relaseTime;

    public ChildQuestionBank()
    {
    }
}
