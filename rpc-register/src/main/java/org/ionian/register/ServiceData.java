package org.ionian.register;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @date: 2022/6/29 21:18
 * @author paakciu
 * {"appName": "服务别名", "ip": "地址", "port": "端口","token": "(隐式)传输的token"}
 */
@Data
public class ServiceData {
    /**
     * 服务应用名称
     */
    private String appName;

    /**
     * 注册到节点到服务名称
     */
    private String serviceName;

    /**
     * 地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * token
     */
    private String token;
    /**
     * 这里面可以自定义不限进行扩展
     * 分组
     * 权重
     */
    private Map<String, String> parameters = new HashMap<>();
}
