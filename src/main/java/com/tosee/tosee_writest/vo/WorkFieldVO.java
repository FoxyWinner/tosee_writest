package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tosee.tosee_writest.dataobject.WorkPosition;
import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/28 5:57 下午
 */
@Data
public class WorkFieldVO
{
    private Integer fieldType;

    private String fieldName;

    @JsonProperty("positions")
    private List<WorkPostionVO> workPostionVOList;
}
