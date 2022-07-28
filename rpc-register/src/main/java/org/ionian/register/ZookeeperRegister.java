package org.ionian.register;

import org.ionian.common.IMConfig;
import org.ionian.register.utils.ZookeeperHelper;

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
        if (!zookeeperHelper.existNode(busLine.getROOT())) {
            zookeeperHelper.createPersistentData(busLine.getROOT(), "");
        }
        //provider的路径
        String providerPath =

    }

    @Override
    public Boolean unRegister(BusLine busLine) {
        return null;
    }

    @Override
    public Boolean subscribe(BusLine busLine) {
        return null;
    }

    @Override
    public Boolean Unsubscribe(BusLine busLine) {
        return null;
    }
}
