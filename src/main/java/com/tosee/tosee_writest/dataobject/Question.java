package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:41 下午
 */
@Entity
@DynamicUpdate
@Data
public class Question
{
    @Id
    private String questionId;

    private String childQbId;

    private Integer questionType;

    private Integer questionSeq;

    private String questionStem;

    private String answer;

    private String explanation;
}
