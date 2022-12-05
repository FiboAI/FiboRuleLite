package com.fibo.rule.common.enums;

public enum DelFlagEnum {

    DEL_YES(1, "删除"),
    DEL_NO(0, "未删除");
    public final Integer status;
    public final String content;

    DelFlagEnum(Integer status, String content) {
        this.status = status;
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }


}
