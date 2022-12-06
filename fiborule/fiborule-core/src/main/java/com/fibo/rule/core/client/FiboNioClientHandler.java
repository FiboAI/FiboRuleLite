package com.fibo.rule.core.client;

import com.fibo.rule.common.dto.FiboNioDto;
import com.fibo.rule.common.enums.NioOperationTypeEnum;
import com.fibo.rule.common.enums.NioTypeEnum;
import com.fibo.rule.common.utils.FiboNioUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 *<p>通信类</p>
 *
 *@author JPX
 *@since 2022/12/5 15:39
 */
@Slf4j
public class FiboNioClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final FiboNioClient fiboNioClient;

    private final Long app;

    public FiboNioClientHandler(Long app, FiboNioClient fiboNioClient) {
        this.app = app;
        this.fiboNioClient = fiboNioClient;
    }

    /**
     * channel激活时传递客户端初始化信息
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //初始化客户端
        FiboNioDto initRequest = new FiboNioDto();
        initRequest.setOperationType(NioOperationTypeEnum.INIT);
        initRequest.setType(NioTypeEnum.REQ);
        initRequest.setAppId(app);
        initRequest.setAddress(fiboNioClient.getFiboAddress());
        //场景对应的节点信息
        initRequest.setSceneBeansMap(fiboNioClient.getFiboBeanMap());
        FiboNioUtils.writeNioModel(ctx, initRequest);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        FiboNioDto fiboNio = FiboNioUtils.readNioModel(buf);
        if (fiboNio != null && fiboNio.getType() != null && fiboNio.getOperationType() != null) {
            switch (fiboNio.getType()) {
                case REQ:
                    fiboNioClient.waitStarted(); //provide service after client started
                    FiboNioDto response = new FiboNioDto();
                    response.setType(NioTypeEnum.RSP);
                    response.setId(fiboNio.getId());
                    response.setOperationType(fiboNio.getOperationType());
                    switch (fiboNio.getOperationType()) {
                        case RELEASE_ENGINE:
                            String error = FiboNioClientService.releaseEngine(fiboNio.getEngineDtoList());
                            response.setReleaseError(error);
                            break;
                        case UNRELEASE_ENGINE:
                            FiboNioClientService.unReleaseEngine(fiboNio.getUnReleaseEngineId());
                            break;
                    }
                    FiboNioUtils.writeNioModel(ctx, response);
                    break;
                case RSP:
                    if (fiboNio.getOperationType() == NioOperationTypeEnum.INIT) {
                        fiboNioClient.initDataReady(fiboNio.getEngineDtoList());
                    }
                    break;
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws InterruptedException {
        if (!fiboNioClient.isDestroy()) {
            log.info("客户端已断开，重新连接中...");
            //wait 1.5s to reconnect
            Thread.sleep(1500);
            fiboNioClient.reconnect();
        }
    }

    /**
     * 如果没有请求，则5秒发送一次心跳
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals((event.state()))) {
                FiboNioDto nioModel = new FiboNioDto();
                nioModel.setOperationType(NioOperationTypeEnum.SLAP);
                nioModel.setType(NioTypeEnum.REQ);
                nioModel.setAddress(fiboNioClient.getFiboAddress());
                nioModel.setAppId(app);
                FiboNioUtils.writeNioModel(ctx, nioModel);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("客户端channel错误:", cause);
        ctx.close();
    }
}