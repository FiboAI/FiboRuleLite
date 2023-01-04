package com.fibo.rule.test.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class FiboRulePayTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiboRulePayTestApplication.class, args);
    }

}
