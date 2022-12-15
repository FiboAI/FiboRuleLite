package com.fibo.rule.test.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FiboRuleMallTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiboRuleMallTestApplication.class, args);
    }

}
