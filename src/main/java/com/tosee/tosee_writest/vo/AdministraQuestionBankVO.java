package com.tosee.tosee_writest.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 2:01 下午
 */
@Data
public class AdministraQuestionBankVO
{
    private String pqbId;

    @JsonProperty("title")
    private String pqbTitle;

    private Integer heat;

    private Integer cqbNumber;

    private List<ChildQuestionBankVO> cqbList;

}
