package org.ionian.core.cache;

import org.ionian.register.BusLine;

import java.util.HashMap;
import java.util.Map;

public class CommonServerCache {

    public static final Map<String,Object> PROVIDER_MAP = new HashMap<>();

    /**
     * 注册中心同步的所有服务
     */
    public static Map<String, BusLine> ALL_BUS_LINE = new HashMap();
}
