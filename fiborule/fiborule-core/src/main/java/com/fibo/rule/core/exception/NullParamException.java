package com.fibo.rule.core.exception;

import java.io.Serializable;

/**
 *<p>参数为空异常</p>
 *
 *@author JPX
 *@since 2022/11/29 10:34
 */
public class NullParamException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -864259139568071245L;

    private String message;

    public NullParamException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
