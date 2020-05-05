package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:41 下午
 */

@Entity
@DynamicUpdate
@Data
public class QuestionOption
{
    @Id
    private String optionId;

    private String questionId;

    private String optionName;

    private String optionValue;
}
