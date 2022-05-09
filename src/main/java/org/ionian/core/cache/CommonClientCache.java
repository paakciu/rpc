package org.ionian.core.cache;

import org.ionian.core.protocal.packet.RpcPacket;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公用缓存 存储请求队列等公共信息
 *
 * @Author linhao
 * @Date created in 8:44 下午 2021/12/1
 */
public class CommonClientCache {

    public static BlockingQueue<RpcPacket> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String,Object> RESPONSE_MAP = new ConcurrentHashMap<>();
}
