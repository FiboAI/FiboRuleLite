package com.fibo.rule.springboot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *<p>参数配置</p>
 *
 *@author JPX
 *@since 2022/11/28 16:45
 */
@Data
@ConfigurationProperties(prefix = "fiborule.monitor", ignoreUnknownFields = true)
public class FiboRuleMonitorProperty {

    /**是否打印监控log*/
    private Boolean enableLog;
    /**监控存储信息最大队列数量*/
    private Integer queueLimit;
    /**延迟多少秒打印*/
    private Long delay;
    /**每隔多少秒打印*/
    private Long period;

}
