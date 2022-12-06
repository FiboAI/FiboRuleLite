package com.fibo.rule.springboot;

import com.fibo.rule.core.property.FiboSceneConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

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
    /**是否打印fiborule banner*/
    private Boolean printBanner;
    /**context数量*/
    private Integer contextSize;
    /**场景列表*/
    private List<FiboSceneConfig> sceneList;
    /**并行节点线程池最大线程数*/
    private Integer allMaxWorkers;
    /**并行节点线程池最大队列数量*/
    private Integer allQueueLimit;
    /**是否打印执行过程中日志*/
    private Boolean printExecuteLog;
    /**是否打印监控log*/
    private Boolean enableLog;
    /**监控存储信息最大队列数量*/
    private Integer queueLimit;
    /**延迟多少秒打印*/
    private Long delay;
    /**每隔多少秒打印*/
    private Long period;
    /**default 16M, size bigger than this may dirty data*/
    private Integer maxFrameLength;
    /**初始化重试次数，默认3次*/
    private Integer initRetryTimes;
    /**初始化重试延迟时间，默认2s*/
    private Integer initRetrySleepMs;

}
