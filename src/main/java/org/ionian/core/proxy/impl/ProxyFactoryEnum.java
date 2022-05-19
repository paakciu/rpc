package org.ionian.core.proxy.impl;

import org.ionian.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @author paakciu
 * @EnumName: test
 * @since: 2022/5/9 12:48
 */
public enum ProxyFactoryEnum implements ProxyFactory {
    JDK{
        @Override
        public <T> T getProxy(Class clazz) throws Throwable {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new JDKClientInvocationHandler(clazz));
        }
    },

    ;


}
