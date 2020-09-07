package com.tosee.tosee_writest.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/** 该表作为出入学校功能中的出校记录使用
 * @Author: FoxyWinner
 * @Date: 2020/4/29 4:34 下午
 */
@Entity
@DynamicUpdate
@Data
public class PassRecord
{
    @Id
    private String recordId;

    private String name;

    private String institute;

    private String state;

    private String studentNumber;

    private String time;


    public PassRecord()
    {
    }
}
