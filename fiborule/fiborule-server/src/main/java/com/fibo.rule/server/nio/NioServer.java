package com.fibo.rule.server.nio;

import com.fibo.rule.server.config.ServerProperties;
import com.fibo.rule.server.service.EngineService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NioServer {

    private final ServerProperties properties;

    private EventLoopGroup bossEventLoop;

    private EventLoopGroup workEventLoop;

    private final EngineService engineService;

    public NioServer(ServerProperties properties, EngineService engineService) {
        this.engineService = engineService;
        this.properties = properties;
    }

    public void start() throws Exception {

        bossEventLoop = new NioEventLoopGroup();
        workEventLoop = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventLoop, workEventLoop)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new IdleStateHandler(properties.getReaderIdleTime(), 0, 0, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(properties.getMaxFrameLength(), 0, 4, 0, 4));
                        socketChannel.pipeline().addLast(new NioServerHandler(engineService));
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(properties.getHost(), properties.getPort()).sync();
        new Thread(() -> {
            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("interrupted", e);
            } finally {
                if (bossEventLoop != null) {
                    bossEventLoop.shutdownGracefully();
                }
                if (workEventLoop != null) {
                    workEventLoop.shutdownGracefully();
                }
            }
        }).start();

        log.info("nio server start success");
    }

    public void destroy() throws IOException {
        if (bossEventLoop != null) {
            bossEventLoop.shutdownGracefully();
        }
        if (workEventLoop != null) {
            workEventLoop.shutdownGracefully();
        }

    }
}