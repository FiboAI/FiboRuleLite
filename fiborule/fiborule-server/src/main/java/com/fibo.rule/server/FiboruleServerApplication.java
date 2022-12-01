package com.fibo.rule.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@MapperScan({"com.fibo.rule.server.dao"})
@ComponentScan(basePackages = "com.fibo.rule.server.**")
@EnableAsync
public class FiboruleServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FiboruleServerApplication.class, args);
    }
}
