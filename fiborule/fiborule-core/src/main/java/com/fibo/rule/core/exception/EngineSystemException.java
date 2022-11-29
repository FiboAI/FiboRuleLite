package com.fibo.rule.core.exception;

/**
 *<p>引擎执行异常</p>
 *
 *@author JPX
 *@since 2022/11/29 14:24
 */
public class EngineSystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 异常信息 */
	private String message;

	public EngineSystemException(String message) {
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
