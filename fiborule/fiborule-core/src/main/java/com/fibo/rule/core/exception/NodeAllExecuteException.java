package com.fibo.rule.core.exception;

/**
 *<p>并行节点执行异常</p>
 *
 *@author JPX
 *@since 2022/11/29 14:24
 */
public class NodeAllExecuteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 异常信息 */
	private String message;

	public NodeAllExecuteException(String message) {
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
