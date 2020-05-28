package com.tosee.tosee_writest.dto;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**主要是为了给运营管理系统-子题库列表显示parentQbTitle
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:34 下午
 */
@Data
public class ChildQuestionBankDTO
{
    private String childQbId;

    private String parentQbId;

    private String parentQbTitle;

    private String cqbTitle;

    private Integer cqbHeat;

    private Integer questionNumber;

    private Integer simulationTime;

    private Integer isRecommended;

    private String relaseTime;

    public ChildQuestionBankDTO()
    {
    }
}
