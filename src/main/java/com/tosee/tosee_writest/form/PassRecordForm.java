package com.tosee.tosee_writest.form;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: FoxyWinner
 * @Date: 2020/5/20 10:07 下午
 */
@Data
public class PassRecordForm
{
    private String name;

    private String institute;

    private String state;

    private String studentNumber;

    private String time;
}
