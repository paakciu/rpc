# RPC框架

从0到1实现一个简单的RPC框架

目标是：

```
public static void main(String[] args)
{
    //获取远程服务
    Service service = RpcServiceFactory.getService("ServiceName");
    //调用并返回远程服务结果
    Object ref = service.doSome(xxx);
    //正确执行并输出结果
    System.out.println(ref);
}
```


# 开源依赖以及特别鸣谢：

Netty 高性能网络通信框架。

lombok 自动生成Getter、Setter、toString等（编辑器需要安装插件并引入依赖）

mapstrut 对象映射工具，编译构建期自动生成映射实现类，避免运行时映射对性能的影响（编辑器需要安装插件并引入依赖）

fastjson 阿里巴巴序列化工具