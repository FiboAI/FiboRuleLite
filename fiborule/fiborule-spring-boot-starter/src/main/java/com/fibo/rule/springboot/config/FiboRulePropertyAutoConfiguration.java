package com.fibo.rule.springboot.config;

import com.fibo.rule.core.property.FiboRuleConfig;
import com.fibo.rule.springboot.FiboRuleMonitorProperty;
import com.fibo.rule.springboot.FiboRuleProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *<p>FiboRuleConfig装配类</p>
 *
 * 这里设置了默认的参数路径，如果在springboot的application.properties/yml里没取到的话，就取默认值
 *
 *@author JPX
 *@since 2022/11/28 16:53
 */
@Configuration
@EnableConfigurationProperties({FiboRuleProperty.class, FiboRuleMonitorProperty.class})
@PropertySource(
        name = "Fiborule Default Properties",
        value = "classpath:/META-INF/fiborule-default.properties")
public class FiboRulePropertyAutoConfiguration {

    @Bean
    public FiboRuleConfig fiboRuleConfig(FiboRuleProperty property, FiboRuleMonitorProperty monitorProperty){
        FiboRuleConfig config = new FiboRuleConfig();
        config.setApp(property.getApp());
        config.setServer(property.getServer());
        config.setPrintBanner(property.getPrintBanner());
        config.setContextSize(property.getContextSize());
        config.setSceneList(property.getSceneList());
        config.setAllMaxWorkers(property.getAllMaxWorkers());
        config.setAllQueueLimit(property.getAllQueueLimit());
        config.setPrintExecuteLog(property.getPrintExecuteLog());
        config.setEnableLog(monitorProperty.getEnableLog());
        config.setQueueLimit(monitorProperty.getQueueLimit());
        config.setDelay(monitorProperty.getDelay());
        config.setPeriod(monitorProperty.getPeriod());
        config.setMaxFrameLength(property.getMaxFrameLength());
        config.setInitRetryTimes(property.getInitRetryTimes());
        config.setInitRetrySleepMs(property.getInitRetrySleepMs());
        return config;
    }
}
