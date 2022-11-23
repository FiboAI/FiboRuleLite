package com.fibo.rule.springboot.config;

import com.fibo.rule.core.runner.FiboApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.fibo.rule.springboot")
public class FiboRuleMainAutoConfiguration {

    @Bean
    public FiboApplication flowExecutor() {
        FiboApplication flowExecutor = new FiboApplication();
        return flowExecutor;
    }

}
