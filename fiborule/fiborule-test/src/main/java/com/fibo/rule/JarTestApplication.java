package com.fibo.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JarTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarTestApplication.class, args);
    }

}
