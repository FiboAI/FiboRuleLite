package com.fibo.rule.common.model;

import lombok.Data;

@Data
public final class ChannelInfo {
    private int app;
    private String address;
    private long lastUpdateTime;

    public ChannelInfo(int app, String address, long lastUpdateTime) {
        this.app = app;
        this.address = address;
        this.lastUpdateTime = lastUpdateTime;
    }
}
