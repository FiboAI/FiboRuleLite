package com.fibo.rule.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author waitmoon
 */
@Data
@Component
@ConfigurationProperties(prefix = "ice")
public class ServerProperties {

    private String host = "0.0.0.0";
    //ice nio port
    private int port = 18121;
    //if there is no read request for readerIdleTime, close the client
    private int readerIdleTime;
    //default 16M, size bigger than this may dirty data
    private int maxFrameLength = 16 * 1024 * 1024;
    //timeout for client response
    private int clientRspTimeOut = 3000;
    //ice thread pool
    private IceServerThreadPoolProperties pool = new IceServerThreadPoolProperties();

    private IceServerHaProperties ha = new IceServerHaProperties();

    @Data
    public static class IceServerThreadPoolProperties {
        private int coreSize = 4;
        private int maxSize = 4;
        private int keepAliveSeconds = 60;
        private int queueCapacity = 60000;
    }

    @Data
    public static class IceServerHaProperties {
        private String address;
        private int baseSleepTimeMs = 1000;
        private int maxRetries = 3;
        private int maxSleepMs = 10000;
        private int connectionTimeoutMs = 5000;
        private String host;
    }
}
