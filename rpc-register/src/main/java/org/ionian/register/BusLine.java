package org.ionian.register;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 总线
 * @Classname BusLine
 * @Date 2022/7/1 22:47
 * @Created by paakciu
 */
@Data
@AllArgsConstructor
public class BusLine {
    /**
     * 预置节点
     */
    private static String ROOT = "/rpc";
    /**
     * 服务名称
     */
    String ServiceName;
    /**
     *
     */
    List<ServiceData> consumer;

    /**
     *
     */
    List<ServiceData> provider;


    public String getRootPath() {
        return ROOT;
    }
    public String getProviderPath(){
        if()
        StringBuilder sb=new StringBuilder();
        sb.append(ROOT).append('/').append(ServiceName).append('/').append("Provider");
        return sb.toString();
    }

}
