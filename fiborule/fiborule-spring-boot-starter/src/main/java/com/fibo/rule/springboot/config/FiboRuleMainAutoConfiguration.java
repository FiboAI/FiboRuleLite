package com.fibo.rule.springboot.config;

import com.fibo.rule.core.monitor.MonitorManager;
import com.fibo.rule.core.property.FiboRuleConfig;
import com.fibo.rule.core.runner.FiboApplication;
import com.fibo.rule.spring.FiboNioClientInit;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter({FiboRulePropertyAutoConfiguration.class})
@ConditionalOnBean(FiboRuleConfig.class)
@Import(FiboNioClientInit.class)
public class FiboRuleMainAutoConfiguration {

    @Bean
    public FiboApplication fiboApplication(FiboRuleConfig fiboRuleConfig) {
        FiboApplication fiboApplication = new FiboApplication(fiboRuleConfig);
        return fiboApplication;
    }

    @Bean
    public MonitorManager monitorManager(FiboRuleConfig fiboRuleConfig) {
        MonitorManager monitorManager = MonitorManager.loadInstance(fiboRuleConfig);
        return monitorManager;
    }

}
