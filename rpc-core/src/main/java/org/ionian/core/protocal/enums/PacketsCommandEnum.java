package org.ionian.core.protocal.enums;

import org.ionian.core.protocal.packet.RpcPacket;

import java.util.HashMap;

/**
 * @author paakciu
 * @EnumName: PacketsCommandEnum
 * @since: 2022/5/9 12:23
 */
public enum PacketsCommandEnum {
    PRC_PACKET(1, RpcPacket.class),
    ;


    private byte command;

    private Class clazz;

    static HashMap<Byte,Class> hashMap=new HashMap();
    static {
        for (PacketsCommandEnum value : PacketsCommandEnum.values()) {
            hashMap.put(value.getCommand(),value.getClazz());
        }
    }

    PacketsCommandEnum(int command, Class clazz) {
        this.command = (byte)command;
        this.clazz = clazz;
    }

    public static Class getRequestType(byte command){
        return hashMap.getOrDefault(command,null);
    }

    public byte getCommand() {
        return command;
    }

    public Class getClazz() {
        return clazz;
    }
}
