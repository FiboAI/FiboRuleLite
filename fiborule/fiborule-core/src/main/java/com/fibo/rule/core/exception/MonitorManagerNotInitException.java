package com.fibo.rule.core.exception;

/**
 *<p>监控类未初始化异常</p>
 *
 *@author JPX
 *@since 2022/11/29 18:04
 */
public class MonitorManagerNotInitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 异常信息 */
	private String message;

	public MonitorManagerNotInitException(String message) {
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
