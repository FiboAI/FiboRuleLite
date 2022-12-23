package com.fibo.rule.common.utils;

import com.alibaba.fastjson.JSON;
import com.fibo.rule.common.dto.FiboNioDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 *<p>nio操作类</p>
 *
 *@author JPX
 *@since 2022/12/5 15:42
 */
@Slf4j
public final class FiboNioUtils {

    public static FiboNioDto readNioModel(ByteBuf buf) {
        try {
            return JSON.parseObject(getNioModelJsonBytes(buf), FiboNioDto.class);
        } catch (Exception e) {
            log.warn("nio解析数据失败", e);
        }
        return null;
    }

    public static void writeNioModel(ChannelHandlerContext ctx, FiboNioDto fiboNio) {
        //向服务端/客户端传输FiboNioDto
        writeNioModel(ctx.channel(), fiboNio);
    }

    public static void writeNioModel(Channel channel, FiboNioDto fiboNio) {
        //向服务端/客户端传输FiboNioDto
        byte[] bytes = JSON.toJSONBytes(fiboNio);
        if (bytes != null) {
            ByteBuf buf = Unpooled.buffer(bytes.length);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
            channel.writeAndFlush(buf);
        }
    }

    public static byte[] getNioModelJsonBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }
}
