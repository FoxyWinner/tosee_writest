package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tosee.tosee_writest.enums.PositionPQBTypeEnum;
import com.tosee.tosee_writest.utils.EnumUtil;
import lombok.Data;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 6:04 下午
 */
@Data
public class WorkPostionVO
{
    private Integer positionType;

    private String positionName;

    @JsonIgnore
    private Integer pqbType;

    @JsonIgnore
    public String getPqbTypeName()
    {
        return EnumUtil.getByCode(pqbType, PositionPQBTypeEnum.class).getMessage();
    }
}
