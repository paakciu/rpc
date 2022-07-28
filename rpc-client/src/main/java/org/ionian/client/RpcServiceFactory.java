package org.ionian.client;

import org.ionian.core.cache.CommonClientCache;
import org.ionian.core.proxy.ProxyFactory;

/**
 *
 */
public class RpcServiceFactory {

    public ProxyFactory proxyFactory;

    public RpcServiceFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }

    public Object getService(String serviceName) throws Throwable {
        Class clazz = CommonClientCache.SERVICE_MAP.getOrDefault(serviceName, null);
        if(clazz==null){
            return null;
        }
        return get(clazz);
    }
}
