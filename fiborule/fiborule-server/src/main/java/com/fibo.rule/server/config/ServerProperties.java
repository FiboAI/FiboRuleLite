package com.fibo.rule.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "fibo")
public class ServerProperties {

    private String host = "0.0.0.0";
    //nio port
    private int port = 18121;
    //if there is no read request for readerIdleTime, close the client
    private int readerIdleTime;
    //default 16M, size bigger than this may dirty data
    private int maxFrameLength = 16 * 1024 * 1024;
    //timeout for client response
    private int clientRspTimeOut = 3000;
    //thread pool
    private ServerThreadPoolProperties pool = new ServerThreadPoolProperties();

    private ServerHaProperties ha = new ServerHaProperties();

    @Data
    public static class ServerThreadPoolProperties {
        private int coreSize = 4;
        private int maxSize = 4;
        private int keepAliveSeconds = 60;
        private int queueCapacity = 60000;
    }

    @Data
    public static class ServerHaProperties {
        private String address;
        private int baseSleepTimeMs = 1000;
        private int maxRetries = 3;
        private int maxSleepMs = 10000;
        private int connectionTimeoutMs = 5000;
        private String host;
    }
}
