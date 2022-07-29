package org.ionian.core.cache;

import org.ionian.register.BusLine;
import org.ionian.server.facade.HelloServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author linhao
 * @Date created in 8:45 下午 2021/12/1
 */
public class CommonServerCache {

    public static final Map<String,Object> PROVIDER_MAP = new HashMap<>();

    /**
     * 注册中心同步的所有服务
     */
    public static Map<String, BusLine> ALL_BUS_LINE = new HashMap();
}
