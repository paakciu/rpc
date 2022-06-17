package org.ionian.core.cache;

import org.ionian.server.facade.HelloServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author linhao
 * @Date created in 8:45 下午 2021/12/1
 */
public class CommonServerCache {

    public static final Map<String,Object> PROVIDER_MAP = new HashMap<>();
    static {
        PROVIDER_MAP.put("HelloService", new HelloServiceImpl());
    }
}
