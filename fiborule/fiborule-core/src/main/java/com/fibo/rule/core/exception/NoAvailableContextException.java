package com.fibo.rule.core.exception;

/**
 *<p>没有可用context异常</p>
 *
 *@author JPX
 *@since 2022/11/29 10:19
 */
public class NoAvailableContextException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常信息
     */
    private String message;

    public NoAvailableContextException(String message) {
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
