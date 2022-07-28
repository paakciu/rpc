package org.ionian.register;

/**
 * @date: 2022/6/29 21:24
 * @author paakciu
 */
public interface Register {

    /**
     * 注册接口
     * @date: 2022/6/29 21:25
     * @author paakciu
     * @param serviceData
     * @return
     */
    Boolean register(BusLine busLine);

    /**
     * 下线
     * @date: 2022/6/29 21:25
     * @author paakciu
     * @param serviceData
     * @return
     */
    Boolean unRegister(BusLine busLine);

    /**
     * 订阅
     * @date: 2022/6/29 21:25
     * @author paakciu
     * @param serviceData
     * @return
     */
    Boolean subscribe(BusLine busLine);

    /**
     * 取消订阅
     * @date: 2022/6/29 21:25
     * @author paakciu
     * @param busLine
     * @return
     */
    Boolean Unsubscribe(BusLine busLine);
}
