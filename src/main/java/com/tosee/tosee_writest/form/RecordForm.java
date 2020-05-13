package com.tosee.tosee_writest.form;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RecordForm
{
    @NotEmpty(message = "OPENID必填")
    private String openid;

    @NotEmpty(message = "子题库ID必填")
    private String cqbId;

    @NotNull(message = "是否完成必填")
    private Integer complete;

    @NotNull(message = "完成数必填")
    private Integer completeNumber;

    @NotNull(message = "花费时间完成必填")
    private Integer spentTime;

    // 正确率 当未完成时，这个值是无用的，可以默认为-1
    private Integer correct = -1;

    @NotNull(message = "模式必填")
    private Integer mode;

    private List<String> userAnswerList;
}
