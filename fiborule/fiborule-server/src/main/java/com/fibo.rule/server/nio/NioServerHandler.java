package com.fibo.rule.server.nio;

import com.fibo.rule.common.dto.FiboNioDto;
import com.fibo.rule.common.enums.NioOperationTypeEnum;
import com.fibo.rule.common.enums.NioTypeEnum;
import com.fibo.rule.server.service.EngineService;
import com.fibo.rule.server.utils.NioUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NioServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static Map<String, Object> lockMap = new ConcurrentHashMap<>();

    public static Map<String, FiboNioDto> resultMap = new ConcurrentHashMap<>();

    private final EngineService engineService;
    
    public NioServerHandler(EngineService engineService) {
        this.engineService = engineService;
    }

    /*
     * unregister while channel inactive
     * 记录一下ChannelInactive的触发场景，以下使用netty官方示例测试
     * 1.客户端发送关闭帧
     * 2.客户端结束进程
     * 3.服务端主动调用channel.close()
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        NioClientManager.unregister(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        Channel channel = ctx.channel();
        FiboNioDto nioModel = NioUtils.readNioModel(buf);
        if (nioModel != null && nioModel.getType() != null && nioModel.getOperationType() != null) {
            switch (nioModel.getType()) {
                case REQ:
                    if (nioModel.getOperationType() == NioOperationTypeEnum.INIT) {
                        FiboNioDto response = new FiboNioDto();
                        response.setType(NioTypeEnum.RSP);
                        response.setOperationType(NioOperationTypeEnum.INIT);
                        //synchronized for update after init
                        synchronized (channel) {
                            response.setEngineDtoList(engineService.getEngineDtoList(nioModel.getAppId(), null));
                            NioUtils.writeNioModel(ctx, response);
                            NioClientManager.register(nioModel.getAppId(), channel, nioModel.getAddress(), nioModel.getSceneBeansMap());
                        }
                    } else if (nioModel.getOperationType() == NioOperationTypeEnum.SLAP) {
                        NioClientManager.register(nioModel.getAppId(), channel, nioModel.getAddress());
                    }
                    break;
                case RSP:
                    //handle the response of client
                    String id = nioModel.getId();
                    if (id != null) {
                        Object lock = lockMap.get(id);
                        if (lock != null) {
                            synchronized (lock) {
                                if (lockMap.containsKey(id)) {
                                    resultMap.put(id, nioModel);
                                    lock.notifyAll();
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    /*
     * if there is no read request for readerIdleTime, close the client
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals((event.state()))) {
                NioClientManager.unregister(ctx.channel());
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("server channel error:", cause);
        ctx.close();
    }
}