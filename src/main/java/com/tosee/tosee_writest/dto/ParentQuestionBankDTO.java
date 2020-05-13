package com.tosee.tosee_writest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tosee.tosee_writest.dataobject.Company;
import com.tosee.tosee_writest.dataobject.WorkField;
import com.tosee.tosee_writest.dataobject.WorkPosition;
import com.tosee.tosee_writest.enums.ParentQuestionBankTypeEnum;
import com.tosee.tosee_writest.utils.EnumUtil;
import com.tosee.tosee_writest.vo.WorkPostionVO;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 11:08 上午
 */

@Data
public class ParentQuestionBankDTO
{
    @Id
    private String parentQbId;

    /** 父题库类别. */
    private Integer pqbType;

    /** 父题库名称. */
    private String pqbTitle;

    /** 所属公司. */
    private Company company;

    /** 目标行业. */
    private WorkField workField;

    /** 目标岗位. */
    private WorkPosition workPosition;

    private Integer cqbNumber;

    private Integer isRecommended;

    private Integer pqbHeat;

    private String relaseTime;

    /**
     * 返回对应状态的枚举类
     * @return
     */
    @JsonIgnore
    public ParentQuestionBankTypeEnum getParentQuestionBankTypeEnum()
    {
        return EnumUtil.getByCode(pqbType, ParentQuestionBankTypeEnum.class);
    }

}
