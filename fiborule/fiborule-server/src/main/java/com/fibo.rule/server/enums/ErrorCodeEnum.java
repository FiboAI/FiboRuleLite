package com.fibo.rule.server.enums;

public enum ErrorCodeEnum {

    SERVER_ERROR(ErrorCodeEnum.ERROR_CODE + 101, "服务繁忙,请稍后再试!","The service is busy, please try again later"),
    LOGIN_ERROR(ErrorCodeEnum.ERROR_CODE + 102, "登录失败","login fail"),
    ERROR_TOKEN_EXPIRE(ErrorCodeEnum.ERROR_CODE + 103, "登录授权码已过期","The login authorization code has expired"),
    PARAMS_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 104, "参数异常","Parameter exception"),

    APP_CODE_REDO_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 201, "appCode已存在","Parameter exception"),
    APP_NAME_REDO_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 202, "appName已存在","Parameter exception"),
    APP_NOT_ALLOWED_DELETE_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 203, "app下存在引擎","Parameter exception"),

    ENGINE_CODE_REDO_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 301, "engineCode已存在","Parameter exception"),
    ENGINE_BOOT_EDIT_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 302, "engine已发布","Parameter exception"),
    ENGINE_BOOT_DELETE_EXCEPTION(ErrorCodeEnum.ERROR_CODE + 302, "engine已发布","Parameter exception"),
    
    
    
    /**消息队列指标配置错误*/
    FIELD_KAFKA_CONFIG_ERROR(ErrorCodeEnum.ERROR_CODE + 402, "消息队列指标配置错误");

    /**
     * 默认ERROR_CODE.<br>
     * 按公司要求8位长度，前两位产品。
     */
    public static final String ERROR_CODE = "01000";

    private String code;
    private String message;
    private String enUsMessage;

    private ErrorCodeEnum(String code, String message, String enUsMessage) {
        this.code = code;
        this.message = message;
        this.enUsMessage = enUsMessage;
    }

    private ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getEnUsMessage() {
        return enUsMessage;
    }

}
