package org.ionian.core.proxy.impl;


import org.ionian.core.cache.CommonClientCache;
import org.ionian.core.protocal.packet.RpcPacket;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


/**
 * 各种代理工厂统一使用这个InvocationHandler
 *
 * @Author linhao
 * @Date created in 6:59 下午 2021/12/5
 */
public class JDKClientInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private Class<?> clazz;

    public JDKClientInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcPacket rpcPacket = new RpcPacket();
        rpcPacket.setArgs(args);
        rpcPacket.setTargetMethod(method.getName());
        rpcPacket.setTargetServiceName(clazz.getName());
        rpcPacket.setUuid(UUID.randomUUID().toString());
        CommonClientCache.RESPONSE_MAP.put(rpcPacket.getUuid(), OBJECT);
        CommonClientCache.SEND_QUEUE.add(rpcPacket);
        long beginTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - beginTime < 3*1000) {
            //fixme 这种方式没有remove，会导致积累溢出
            Object object = CommonClientCache.RESPONSE_MAP.get(rpcPacket.getUuid());
            if (object instanceof RpcPacket) {
                return ((RpcPacket)object).getResponse();
            }
        }
        throw new TimeoutException("client wait server's response timeout!");
    }
}
