package org.ionian.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.ionian.client.RpcClientHandler;
import org.ionian.common.IMConfig;
import org.ionian.core.protocal.codec.B2MPacketCodecHandler;

/**
 * @author paakciu
 * @ClassName: TestServer
 * @since: 2022/5/19 11:31
 */
public class TestServer {
    private static EventLoopGroup bossGroup = null;

    private static EventLoopGroup workerGroup = null;

    public void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("初始化provider过程");
                ch.pipeline().addLast(new B2MPacketCodecHandler());
                ch.pipeline().addLast(new RpcServerHandler());
            }
        });
        bootstrap.bind(IMConfig.PORT).sync();
    }

//    public void registyService(Object serviceBean){
//        if(serviceBean.getClass().getInterfaces().length==0){
//            throw new RuntimeException("service must had interfaces!");
//        }
//        Class[] classes = serviceBean.getClass().getInterfaces();
//        if(classes.length>1){
//            throw new RuntimeException("service must only had one interfaces!");
//        }
//        Class interfaceClass = classes[0];
//        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
//    }

    public static void main(String[] args) throws InterruptedException {
        TestServer server = new TestServer();
//        server.registyService(new DataServiceImpl());
        server.startApplication();
    }
}
