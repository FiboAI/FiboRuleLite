package com.fibo.rule.core.exception;

/**
 *<p>节点属性名称重复异常</p>
 *
 *@author JPX
 *@since 2022/11/29 14:24
 */
public class FiboFieldNameRepeatException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 异常信息 */
	private String message;

	public FiboFieldNameRepeatException(String message) {
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
