package com.fibo.rule.common.model;

import lombok.Data;

@Data
public final class ChannelInfo {
    private Long app;
    private String address;
    private long lastUpdateTime;

    public ChannelInfo(Long app, String address, long lastUpdateTime) {
        this.app = app;
        this.address = address;
        this.lastUpdateTime = lastUpdateTime;
    }
}
