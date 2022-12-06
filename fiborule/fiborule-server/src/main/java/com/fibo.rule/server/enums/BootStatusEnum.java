package com.fibo.rule.server.enums;

public enum BootStatusEnum {

    CREATE(0, "创建"),
    BOOT(1, "发布"),
    NOT_BOOT(2, "未发布");
    public final Integer status;
    public final String content;

    BootStatusEnum(Integer status, String content) {
        this.status = status;
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }


}
