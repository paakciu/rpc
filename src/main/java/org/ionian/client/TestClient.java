package org.ionian.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.*;
import io.netty.util.concurrent.Future;
import org.ionian.api.HelloService;
import org.ionian.core.cache.CommonClientCache;
import org.ionian.core.protocal.packet.RpcPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.ionian.common.IMConfig;
import org.ionian.core.protocal.codec.B2MPacketCodecHandler;
import org.ionian.core.proxy.impl.ProxyFactoryEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


/**
 * @author paakciu
 * @ClassName: TestClient
 * 修改
 * @since: 2022/5/9 13:55
 */
public class TestClient {

    public static void main(String[] args) throws Throwable {
        TestClient client = new TestClient();
        RpcServiceFactory rpcServiceFactory = client.startClientApplication();
        //获取远程服务
        HelloService service = (HelloService)rpcServiceFactory.getService("HelloService");
        //调用并返回远程服务结果
        String result = service.doSome("传输入参");
        //正确执行并输出结果
        System.out.println("HelloService.result="+result);
    }

    public RpcServiceFactory startClientApplication() throws InterruptedException {
        //线程组
        NioEventLoopGroup workerGroup =new NioEventLoopGroup();
        //引导类
        Bootstrap bootstrap =new Bootstrap();
        //核心配置
        bootstrap.group(workerGroup).channel(NioSocketChannel.class);
        setBootstrapExtraConfig(bootstrap);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new B2MPacketCodecHandler());
                ch.pipeline().addLast(new RpcClientHandler());
            }
        });
        ChannelFuture channelFuture = bootstrap.connect(IMConfig.HOST, IMConfig.PORT).sync();
        System.out.println(("============ 服务启动 ============"));
        this.startClientSendJob(channelFuture);
        RpcServiceFactory rpcServiceFactory = new RpcServiceFactory(ProxyFactoryEnum.JDK);
        return rpcServiceFactory;
    }

    /**
     * 开启发送线程
     * todo paakciu 失败重连机制
     * @param channelFuture
     */
    private void startClientSendJob(ChannelFuture channelFuture) {
        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
        channelFuture.addListener(future -> {
            if(future.isSuccess()){
                asyncSendJob.start();
            }
        });

    }

    class AsyncSendJob implements Runnable {

        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcPacket data = CommonClientCache.SEND_QUEUE.take();
                    System.out.println("发送data="+JSON.toJSONString(data));
                    channelFuture.channel().writeAndFlush(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 额外配置
     * todo 后面可以改成udp的方式
     * @param bootstrap
     * @return
     */
    private Bootstrap setBootstrapExtraConfig(Bootstrap bootstrap){
        //额外的配置
        bootstrap
                // 设置TCP底层属性
                //连接的超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启TCP底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //是否开启Nagle，即高实时性（true）减少发送次数（false）
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }


}
