package com.tosee.tosee_writest.exception;

import com.tosee.tosee_writest.enums.ResultEnum;
import lombok.Getter;

@Getter
public class WritestException extends RuntimeException
{
    private Integer code;

    public WritestException(ResultEnum resultEnum)
    {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public WritestException(Integer code, String message)
    {
        super(message);
        this.code = code;
    }
}
