package org.ionian.server.facade;

import org.ionian.api.HelloService;

/**
 * @author paakciu
 * @ClassName: HelloServiceImpl
 * @since: 2022/5/19 11:19
 */
public class HelloServiceImpl implements HelloService {

    /**
     * example
     * @param param
     * @return
     */
    @Override
    public String doSome(String param){
        System.out.println("HelloServiceImpl.doSome param="+param);

        return "搜到消息，并且从服务端输出该文本";
    }
}
