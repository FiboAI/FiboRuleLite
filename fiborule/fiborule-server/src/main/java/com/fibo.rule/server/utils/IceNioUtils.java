package com.fibo.rule.server.utils;

import com.fibo.rule.common.dto.FiboNioDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author waitmoon
 * ice nio operate
 */
@Slf4j
public final class IceNioUtils {

    public static FiboNioDto readNioModel(ByteBuf buf) {
        try {
            return JacksonUtils.readJsonBytes(getNioModelJsonBytes(buf), FiboNioDto.class);
        } catch (Exception e) {
            log.warn("ice nio error please check data", e);
        }
        return null;
    }

    public static void writeNioModel(ChannelHandlerContext ctx, FiboNioDto nioModel) {
        //write nio model to server/client
        writeNioModel(ctx.channel(), nioModel);
    }

    public static void writeNioModel(Channel channel, FiboNioDto nioModel) {
        //write nio model to server/client
        byte[] bytes = JacksonUtils.toJsonBytes(nioModel);
        writeModel(channel, bytes);
    }

    public static void writeModel(Channel channel, byte[] modelBytes) {
        //write nio model to server/client
        if (modelBytes != null) {
            ByteBuf buf = Unpooled.buffer(modelBytes.length);
            buf.writeInt(modelBytes.length);
            buf.writeBytes(modelBytes);
            channel.writeAndFlush(buf);
        }
    }

    public static byte[] getNioModelJsonBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }
}
