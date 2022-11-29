package com.fibo.rule.core.exception;

/**
 *<p>没有引擎异常</p>
 *
 *@author JPX
 *@since 2022/11/29 13:52
 */
public class EngineNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 异常信息 */
	private String message;

	public EngineNotFoundException(String message) {
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
