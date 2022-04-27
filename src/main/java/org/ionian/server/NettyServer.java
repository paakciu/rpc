package org.ionian.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;


public class NettyServer {
    //单例模式
//    public static final NettyServer INSTANCE =new NettyServer();

    public static void main(String[] args)
    {
        System.out.println("请输入你要监听的端口：(4000-4500),真实生产环境应当写死该端口，如使用4396端口");
        Scanner sc=new Scanner(System.in);
        int port=sc.nextInt();
        NettyServer nettyServer = new NettyServer();
        //开始监听端口并且处理连接
        nettyServer.startListening(port);
    }

    /**
     * 这个比较重要,这个使用了责任链模式,pipeline 可以理解成一个List,存放逻辑处理器[Handler],然后服务端的收发处理都在[Handler]里了
     * 简单的样例:{@link SimpleServerHandler}
     */
    ChannelHandler childHandler=new ChannelInitializer<NioSocketChannel>() {
        @Override
        //服务端客户端接入处理器 每个客户端接入都会执行这一句来初始化信道
        protected void initChannel(NioSocketChannel nioSocketChannel) {
            System.out.println("客户机接入");
            //添加逻辑处理器
            nioSocketChannel.pipeline()
                    .addLast(new SimpleServerHandler())
            ;
        }
    };

    ChannelHandler handler = new ChannelInitializer<NioServerSocketChannel>() {
        @Override
        //服务端--初始化处理器
        protected void initChannel(NioServerSocketChannel nioServerSocketChannel)  {
            System.out.println("服务器启动中");
        }
    };


    private void startListening(int port){
        //监听欢迎端口-线程组
        NioEventLoopGroup bossGroup =new NioEventLoopGroup();
        //处理每条链接数据读写的线程组
        NioEventLoopGroup workerGroup =new NioEventLoopGroup();
        //服务器启动-引导类
        ServerBootstrap serverBootstrap =new ServerBootstrap();
        //核心配置部分
        serverBootstrap
                //配置两大线程组
                .group(bossGroup,workerGroup)
                //指定IO模型 如果要设定成BIO的模型，则改成OioServerSocketChannel.class，NIO则是NioServerSocketChannel
                .channel(NioServerSocketChannel.class);

        //设置客户机连接处理器链
        //定义后续每条连接的数据读写，业务处理逻辑，这里的NioSocketChannel是netty对NIO类型连接的抽象，如Socket
        serverBootstrap.childHandler(childHandler);
        //设置服务器监听处理器
        serverBootstrap.handler(handler);
        //设置额外设置
        setServerBootstrapExtraConfig(serverBootstrap);
        //绑定端口，监听开始 递归使用
        bind(serverBootstrap,port,0);
    }

    /**
     * 其他配置部分
     * 这里可以不关心
     * todo 后续支持UDP的连接方式,目前是使用tcp连接
     * @param serverBootstrap
     */
    private static void setServerBootstrapExtraConfig(ServerBootstrap serverBootstrap){
        //其他配置部分
        serverBootstrap
                //给Server连接维护一个map
                //.attr(AttributeKey.newInstance("serverName"), "nettyServer")
                //给每条连接连接维护map
                //.childAttr(clientKey, "随便输入点东西看看行不行")
                //存放已经三次握手的请求的队列的最大长度
                .option(ChannelOption.SO_BACKLOG, 1024)
                //开启TCP底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //实时性设置，开则有数据发送马上发送，关泽减少发送次数
                .childOption(ChannelOption.TCP_NODELAY, true)
        ;
    }
    private static void bind(ServerBootstrap serverBootstrap,int port,int time)
    {
        final int next=time+1;
        if(time>5){
            System.out.println("端口监听失败，停止服务，请检查端口是否被占用并更换端口！");
            return;
        }
        //Lambda写法
        serverBootstrap.bind(port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("端口["+port+"]第"+time+"次监听成功！");
            }
            else{
                System.out.println("端口["+port+"]第"+time+"次监听失败！");
                //重新连接，端口自动增一重试
                //但服务器一般不建议更换端口，所以监听失败便停止
                bind(serverBootstrap, port,next);
            }
        });
    }
}
