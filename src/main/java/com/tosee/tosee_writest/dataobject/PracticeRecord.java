package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/4 4:59 下午
 */
@Entity
@DynamicUpdate
@Data
public class PracticeRecord
{
    @Id
    private String recordId;

    private String openId;

    private String childQbId;

    private Integer completeNumber;

    private String userAnswerList;

    private Integer spentTime;

    private Integer complete;

    private Integer lastMode;

    private Integer correctRatio;

    private Integer surpassRatio;
}
