package org.ionian.core.proxy;

/**
 * @author paakciu
 * @since 2022/5/9:12:42
 */
public interface ProxyFactory {

    <T> T getProxy(final Class clazz) throws Throwable;
}