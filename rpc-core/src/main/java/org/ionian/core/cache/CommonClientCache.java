package org.ionian.core.cache;

import org.ionian.core.protocal.packet.RpcPacket;
import org.ionian.register.BusLine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公用缓存 存储请求队列等公共信息
 * @Date created in 8:44 下午 2021/12/1
 */
public class CommonClientCache {

    public static BlockingQueue<RpcPacket> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String,Object> RESPONSE_MAP = new ConcurrentHashMap<>();

    /**
     * 相当于consumer的配置
     * @author paakciu
     * @since 2022/5/19:11:07
     */
    public static Map<String,Class> SERVICE_MAP = new HashMap<>();

    /**
     * 注册中心同步的所有服务
     */
    public static Map<String, BusLine> ALL_BUS_LINE = new HashMap();
}
