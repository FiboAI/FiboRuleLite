package com.fibo.rule.springboot;

import com.fibo.rule.core.property.FiboSceneConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 *<p>参数配置</p>
 *
 *@author JPX
 *@since 2022/11/28 16:45
 */
@Data
@ConfigurationProperties(prefix = "fiborule", ignoreUnknownFields = true)
public class FiboRuleProperty {

    /**appId*/
    private Long app;
    /**服务端地址host:port*/
    private String server;
    /**是否打印fiborule logo，默认true*/
    private Boolean printBanner;
    /**context数量，默认1024*/
    private Integer contextSize;
    /**场景列表，默认场景default，扫描全包*/
    private List<FiboSceneConfig> sceneList;
    /**并行节点线程池最大线程数，默认16*/
    private Integer allMaxWorkers;
    /**并行节点线程池最大队列数量，默认512*/
    private Integer allQueueLimit;
    /**是否打印执行过程中日志，默认true*/
    private Boolean printExecuteLog;
    /**默认16M，大小大于此值可能会损坏数据*/
    private Integer maxFrameLength;
    /**客户端初始化重试次数，默认3次*/
    private Integer initRetryTimes;
    /**客户端初始化重试延迟时间，默认2s*/
    private Integer initRetrySleepMs;

}
