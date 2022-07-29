package org.ionian.register;

import com.alibaba.fastjson.JSON;
import org.ionian.common.IMConfig;
import org.ionian.common.util.EmptyUtil;
import org.ionian.register.utils.ZookeeperHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname ZookeeperRegister
 * @Date 2022/7/1 22:12
 * @Created by paakciu
 */
public class ZookeeperRegister implements Register{

    String address= IMConfig.ZK_ADDRESS;

    /**
     * @date: 2022/7/1 22:25
     * @author paakciu
     */
    ZookeeperHelper zookeeperHelper ;
    /**
     * @date: 2022/7/1 22:25
     * @author paakciu
     */
    public ZookeeperRegister(){
        zookeeperHelper = new ZookeeperHelper(address,null,null);
    }


    @Override
    public Boolean register(BusLine busLine) {
        //创造空节点
        if (!zookeeperHelper.existNode(busLine.getRootPath())) {
            zookeeperHelper.createPersistentData(busLine.getRootPath(), "");
        }
        //provider的路径
        String providerPath = busLine.getProviderPath();

        if (!zookeeperHelper.existNode(providerPath)) {
            zookeeperHelper.createTemporaryData(providerPath, JSON.toJSONString(busLine.getProviderNode()));
        } else {
            zookeeperHelper.deleteNode(providerPath);
            zookeeperHelper.createTemporaryData(providerPath, JSON.toJSONString(busLine.getProviderNode()));
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean unRegister(BusLine busLine) {
        //provider的路径
        String providerPath = busLine.getProviderPath();
        zookeeperHelper.deleteNode(providerPath);
        return Boolean.TRUE;
    }

    @Override
    public Boolean subscribe(BusLine busLine) {
        //创造空节点
        if (!zookeeperHelper.existNode(busLine.getRootPath())) {
            zookeeperHelper.createPersistentData(busLine.getRootPath(), "");
        }
        //consumerPath
        String consumerPath = busLine.getConsumerPath();
        if (!zookeeperHelper.existNode(consumerPath)) {
            zookeeperHelper.createTemporaryData(consumerPath, JSON.toJSONString(busLine.getConsumerNode()));
        } else {
            zookeeperHelper.deleteNode(consumerPath);
            zookeeperHelper.createTemporaryData(consumerPath, JSON.toJSONString(busLine.getConsumerNode()));
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean Unsubscribe(BusLine busLine) {
        //consumerPath
        String consumerPath = busLine.getConsumerPath();
        zookeeperHelper.deleteNode(consumerPath);
        return Boolean.TRUE;
    }

    @Override
    public Map<String,BusLine> getAllBusLine(){
        String root = BusLine.ROOT;
        List<String> childrenData = zookeeperHelper.getChildrenData(root);
        System.out.println("childrenData="+ JSON.toJSONString(childrenData));
        Map<String,BusLine>  map = new HashMap<>();
        if (EmptyUtil.isEmpty(childrenData)) {
            return map;
        }
        for (String childPath : childrenData) {
            BusLine busLine = new BusLine();
            busLine.setServiceName(childPath);

            String providerListPath = BusLine.getProviderListPath(childPath);
            List<String> providerChildren = zookeeperHelper.getChildrenData(providerListPath);
            List<ServiceData> providerServiceDataList = getServiceData(providerListPath, providerChildren);
            busLine.setProvider(providerServiceDataList);

            String consumerListPath = BusLine.getConsumerListPath(childPath);
            List<String> consumerChildren = zookeeperHelper.getChildrenData(consumerListPath);
            List<ServiceData> consumerServiceDataList = getServiceData(consumerListPath, consumerChildren);
            busLine.setConsumer(consumerServiceDataList);

            map.put(childPath,busLine);
        }
        return map;
    }
    private List<ServiceData> getServiceData(String listPath,List<String> children){
        if (EmptyUtil.isEmpty(children)) {
            return null;
        }
        List<ServiceData> list = new ArrayList<>();
        for (String child : children) {
            String nodeData = zookeeperHelper.getNodeData(BusLine.add(listPath, child));
            ServiceData serviceData = JSON.parseObject(nodeData, ServiceData.class);
            list.add(serviceData);
        }
        return list;
    }
}
