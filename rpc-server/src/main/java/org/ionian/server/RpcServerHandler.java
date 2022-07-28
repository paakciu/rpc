package org.ionian.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.ionian.core.cache.CommonServerCache;
import org.ionian.core.protocal.packet.RpcPacket;

import java.lang.reflect.Method;

/**
 * @author paakciu
 * @ClassName: RpcServerHandler
 * @since: 2022/5/19 11:24
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcPacket msg) throws Exception {
        System.out.println("接收到msg="+msg);
        Object serviceImpl = CommonServerCache.PROVIDER_MAP.get(msg.getTargetServiceName());
        Method[] methods = serviceImpl.getClass().getDeclaredMethods();
        Object result = null;
        for (Method method : methods) {
            if (method.getName().equals(msg.getTargetMethod())) {
                if (method.getReturnType().equals(Void.TYPE)) {
                    method.invoke(serviceImpl, msg.getArgs());
                } else {
                    result = method.invoke(serviceImpl, msg.getArgs());
                }
                break;
            }
        }
        msg.setResponse(result);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}