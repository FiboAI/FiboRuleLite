package com.fibo.rule.server.nio;

import com.fibo.rule.server.config.ServerProperties;
import com.fibo.rule.server.service.EngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author waitmoon
 */
@Slf4j
@Component
public class NioServerInit implements CommandLineRunner, DisposableBean {

    @Resource
    private ServerProperties properties;

    @Resource
    private EngineService engineService;

    private NioServer nioServer;

    public static volatile boolean ready = false;

    @Override
    public void destroy() throws Exception {
        if (nioServer != null) {
            nioServer.destroy();
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        serverService.refresh();
        nioServer = new NioServer(properties, engineService);
        try {
            nioServer.start();
        } catch (Throwable t) {
            nioServer.destroy();
            throw new RuntimeException("ice nio server start error", t);
        }
        ready = true;
    }
}
 