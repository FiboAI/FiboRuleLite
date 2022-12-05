package com.fibo.rule.common.enums;

public enum StatusEnum {

    INVALID(0, "无效"),
    VALID(1, "有效");
    public final Integer status;
    public final String content;

    StatusEnum(Integer status, String content) {
        this.status = status;
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }


}
