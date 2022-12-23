package com.fibo.rule.server.exception;


import com.fibo.rule.server.enums.ErrorCodeEnum;

/**
 * 自定义异常消息处理
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1136843834946392402L;

	/**
	 * 异常编码
	 */
	public final String errCode;

	/**
	 * 异常消息
	 */
	public final String message;


	/**
	 * 异常英文消息
	 */
	public final String enUsMessage;

	/**
	 * data
	 */
	public transient final Object data;

	public ApiException(Throwable e) {
		super(e);
		errCode = "";
		message = "";
		enUsMessage = "";
		data = null;
	}

	public ApiException(String errCode, String message, String enUsMessage) {
		super(message);
		this.errCode = errCode;
		this.message = message;
		this.enUsMessage = enUsMessage;
		this.data = null;
	}

	public ApiException(String errCode, String message, String enUsMessage, Object data) {
		super(message);
		this.errCode = errCode;
		this.message = message;
		this.enUsMessage = enUsMessage;
		this.data = data;
	}

	public ApiException(String errCode, String message, String enUsMessage, Throwable e) {
		super(message, e);
		this.errCode = errCode;
		this.message = message;
		this.enUsMessage = enUsMessage;
		this.data = null;
	}

	public ApiException(ErrorCodeEnum errorCodeEnum) {
		this.errCode = errorCodeEnum.getCode();
		this.message = errorCodeEnum.getMessage();
		this.enUsMessage = errorCodeEnum.getEnUsMessage();
		this.data = null;
	}

	public ApiException(String errCode, String message) {
		super(message);
		this.errCode = errCode;
		this.message = message;
		this.enUsMessage = message;
		this.data = null;
	}

}
