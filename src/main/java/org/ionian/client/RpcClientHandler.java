package org.ionian.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.ionian.common.exception.BaseException;
import org.ionian.core.cache.CommonClientCache;
import org.ionian.core.protocal.packet.BasePacket;
import org.ionian.core.protocal.packet.RpcPacket;

/**
 * @author paakciu
 * @ClassName: RpcClientHandler
 * @since: 2022/5/9 12:59
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcPacket msg) throws Exception {
        if(!CommonClientCache.RESPONSE_MAP.containsKey(msg.getUuid())){
            throw new IllegalArgumentException("server response is error!");
        }
        CommonClientCache.RESPONSE_MAP.put(msg.getUuid(),msg);
//        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
