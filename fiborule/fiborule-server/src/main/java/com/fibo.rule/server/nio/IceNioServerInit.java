package com.fibo.rule.server.nio;

import com.fibo.rule.server.config.ServerProperties;
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
public class IceNioServerInit implements CommandLineRunner, DisposableBean {

    @Resource
    private ServerProperties properties;

    private IceNioServer iceNioServer;

    public static volatile boolean ready = false;

    @Override
    public void destroy() throws Exception {
        if (iceNioServer != null) {
            iceNioServer.destroy();
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        serverService.refresh();
        iceNioServer = new IceNioServer(properties);
        try {
            iceNioServer.start();
        } catch (Throwable t) {
            iceNioServer.destroy();
            throw new RuntimeException("ice nio server start error", t);
        }
        ready = true;
    }
}
 