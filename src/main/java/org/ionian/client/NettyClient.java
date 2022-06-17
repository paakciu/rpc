package org.ionian.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.ionian.common.IMConfig;
import org.ionian.server.NettyServer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class NettyClient {

    public static void main(String[] args) {
        System.out.println("请输入你要连接服务器的端口：(4000-4500),真实生产环境应当写死该端口，如使用4396端口");
        Scanner sc=new Scanner(System.in);
        int port=sc.nextInt();
        NettyClient nettyClient=new NettyClient();
        nettyClient.startConnection("localhost",port);
    }

    private static final int MAX_RETRY = IMConfig.ClientConnectionRetry;

    /**
     * 逻辑处理器处理器 跟server的对应
     * server的:{@link NettyServer 的childHandler}
     * 简单的样例如:{@link SimpleClientHandler}
     */
    ChannelHandler handler=new ChannelInitializer<SocketChannel>() {
        //连接初始化
        @Override
        protected void initChannel(SocketChannel socketChannel) {
            //接口处理初始化
            System.out.println("正在初始化...");
            //这里是责任链模式，然后加入逻辑处理器
            socketChannel.pipeline()
                    //插入测试处理器
                    .addLast(new SimpleClientHandler())
                    ;
        }
    };

    public void startConnection(String host,int port)
    {
        //线程组
        NioEventLoopGroup workerGroup =new NioEventLoopGroup();
        //引导类
        Bootstrap bootstrap =new Bootstrap();
        //核心配置,指定线程模型,指定IO类型为NIO
        bootstrap.group(workerGroup).channel(NioSocketChannel.class);
        //指定IO的处理逻辑
        bootstrap.handler(handler);
        setBootstrapExtraConfig(bootstrap);
        //启动连接
        connect(bootstrap, host, port,MAX_RETRY);
    }


    //建立连接 随机退避算法
    private void connect (Bootstrap bootstrap,String host,int port,int retry)
    {
        //操作表,为了让代码看起来更加扁平,action存起来false 跟 true 对应的操作
        Map<Boolean, Consumer<Future<?>>> action=new HashMap<>();

        //连接成功
        action.put(true,future->{
            Channel channel = ((ChannelFuture) future).channel();
            //todo,这里应当使用线程，使操作变成异步，以免阻塞欢迎连接
            System.out.println("连接成功！");
        });
        //连接失败
        action.put(false,future->{
            //随机退避算法
            //todo,这里应当使用线程，使操作变成异步，以免阻塞欢迎连接
            System.out.println("连接失败！正在重试");

            if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
                return;
            }
            // 第几次重连
            int order = (MAX_RETRY - retry) + 1;
            // 此次重连的间隔时间
            int delay = 1 << order;
            System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
            //使用计划任务来实现退避重连算法
            bootstrap.config().group().schedule(
                    () -> connect(bootstrap, host, port, retry - 1)
                    ,delay
                    ,TimeUnit.SECONDS
            );
        });

        //bootstrap开始连接,使用异步线程,结果返回为future.isSuccess()
        bootstrap.connect(host, port)
                .addListener(future -> {
                    action.get(future.isSuccess()).accept(future);
                });
    }

    /**
     * 额外配置
     * todo 后面可以改成udp的方式
     * @param bootstrap
     * @return
     */
    private Bootstrap setBootstrapExtraConfig(Bootstrap bootstrap){
        //连接的超时时间
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        //是否开启TCP底层心跳机制
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        //是否开启Nagle，即高实时性（true）减少发送次数（false）
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }
}
