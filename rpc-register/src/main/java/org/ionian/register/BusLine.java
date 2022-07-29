package org.ionian.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ionian.common.exception.BaseException;
import org.ionian.common.util.EmptyUtil;

import java.util.List;

import org.ionian.common.enums.ExceptionEnum;

/**
 * 总线
 * @Classname BusLine
 * @Date 2022/7/1 22:47
 * @Created by paakciu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusLine {
    /**
     * 预置节点
     */
    public static String ROOT = "/rpc";
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

    /**
     *
     */
    ServiceData consumerNode;

    /**
     * 需要新加入的服务者节点
     */
    ServiceData providerNode;


    public String getRootPath() {
        return ROOT;
    }

    public static String getProviderListPath(String serviceName){
        String providers = ROOT+'/'+serviceName+'/'+"Provider";
        return providers;
    }
    public static String getConsumerListPath(String serviceName){
        String consumers = ROOT+'/'+serviceName+'/'+"Consumer";
        return consumers;
    }
    public static String add(String path,String nodeName){
        return path+'/'+nodeName;
    }
    public String getProviderPath(){
        if(EmptyUtil.isEmpty(ServiceName)){
            throw BaseException.with(ExceptionEnum.SERVICE_NAME_EMPTY_ERROR);
        }
        StringBuilder sb=new StringBuilder();
        sb.append(ROOT).append('/').append(ServiceName).append('/').append("Provider");
        if(providerNode != null && EmptyUtil.isNotEmpty(providerNode.getIp()) && EmptyUtil.isNotEmpty(providerNode.getPort())){
            sb.append('/').append(providerNode.getIp()).append(":").append(providerNode.getPort());
        }
        return sb.toString();
    }

    public String getConsumerPath(){
        if(EmptyUtil.isEmpty(ServiceName)){
            throw BaseException.with(ExceptionEnum.SERVICE_NAME_EMPTY_ERROR);
        }
        StringBuilder sb=new StringBuilder();
        sb.append(ROOT).append('/').append(ServiceName).append('/').append("Consumer");
        if(consumerNode != null && EmptyUtil.isNotEmpty(consumerNode.getIp()) && EmptyUtil.isNotEmpty(consumerNode.getPort())){
            sb.append('/').append(consumerNode.getIp()).append(":").append(consumerNode.getPort());
        }
        return sb.toString();
    }


}
