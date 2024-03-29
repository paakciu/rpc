package org.ionian.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author paakciu
 * @ClassName: SimpleServerHandler
 * @date: 2021/7/17 23:12
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 简单的处理字符串变成字节流
     * @param str
     * @param ctx
     * @return
     */
    private ByteBuf getByteBuf(String str, ChannelHandlerContext ctx) {
        if(Objects.isNull(str) || "".equals(str)){
            return null;
        }
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("进入handler被激活的状态");
        Scanner scanner=new Scanner(System.in);
        new Thread(()->{
            while (true){
                System.out.println("可以输入一句话到客户端中：");
                String str=scanner.next();
                System.out.println(new Date() + ": 服务端写出数据:"+str);
                // 1.转换成字节流数据
                ByteBuf buffer = getByteBuf(str,ctx);
                // 2.写数据
                ctx.channel().writeAndFlush(buffer);
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }
}
