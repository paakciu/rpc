package org.ionian.server;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.ionian.common.IMConfig;
import org.ionian.common.util.IpUtil;
import org.ionian.core.cache.CommonServerCache;
import org.ionian.core.protocal.codec.B2MPacketCodecHandler;
import org.ionian.register.BusLine;
import org.ionian.register.Register;
import org.ionian.register.ServiceData;
import org.ionian.register.ZookeeperRegister;
import org.ionian.server.facade.HelloServiceImpl;

import java.net.SocketException;
import java.util.Map;

/**
 * @author paakciu
 * @ClassName: RpcServer
 * @since: 2022/5/19 11:31
 */
@Slf4j
public class RpcServer {
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
                System.out.println("客户机连接,初始化provider过程");
                ch.pipeline().addLast(new B2MPacketCodecHandler());
                ch.pipeline().addLast(new RpcServerHandler());
            }
        });
        bootstrap.bind(IMConfig.PORT).sync();
    }

    private void registryService(Register register, String serviceName,Object serviceImplObject) {
        CommonServerCache.PROVIDER_MAP.put(serviceName, serviceImplObject);
        ServiceData serviceData = new ServiceData();
        serviceData.setServiceName(serviceName);
        serviceData.setIp(IpUtil.getDefaultIpv4());
        serviceData.setPort(String.valueOf(IMConfig.PORT));
        BusLine busLine = new BusLine();
        busLine.setProviderNode(serviceData);
        busLine.setServiceName(serviceName);
        register.register(busLine);
        System.out.println(String.format("成功注册服务%s,busLine=%s",serviceName, JSON.toJSONString(busLine)));
    }


    public static void main(String[] args) throws InterruptedException {
        RpcServer server = new RpcServer();
        Register register = new ZookeeperRegister();

        server.registryService(register,"HelloService",new HelloServiceImpl());
        server.startApplication();

        //定时拉取或者监听zk就可以实时更新这个列表
        Map<String, BusLine> allBusLine = register.getAllBusLine();
        CommonServerCache.ALL_BUS_LINE = allBusLine;
    }
}
