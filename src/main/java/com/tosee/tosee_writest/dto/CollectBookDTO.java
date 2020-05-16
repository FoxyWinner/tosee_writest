package com.tosee.tosee_writest.dto;

import com.tosee.tosee_writest.dataobject.CollectQuestion;
import com.tosee.tosee_writest.dataobject.Mistake;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/12 11:07 下午
 */
@Data
public class CollectBookDTO
{
    private String collectBookId;

    private String openid;

    private String cqbId;

    private Integer collectNumber;

    private List<CollectQuestion> collectQuestionList;

    private Date createTime;

    private Date updateTime;
}
