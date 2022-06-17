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

# TodoList
[ TodoList详情 ](TodoList.md)


# 设计

代理层：负责对底层调用细节的封装；

路由层：负责在集群目标服务中的调用筛选策略；

协议层：负责请求数据的转码封装等作用；

链路层：负责执行一些自定义的过滤链路，可以供后期二次扩展；

注册中心层：关注服务的上下线，以及一些权重，配置动态调整等功能；

序列化层：负责将不同的序列化技术嵌套在框架中；

容错层：当服务调用出现失败之后需要有容错层的兜底辅助；

接入层：考虑如何与常用框架Spring的接入。

公共层：主要存放一些通用配置，工具类，缓存等信息。


# 阶段性分支

> 想要一步步学习的同学可以根据分支一起跟着项目成长

- dev_ionian_20220427
- dev_ionian_220521

# 开源依赖以及特别鸣谢：

```
Netty 高性能网络通信框架。

lombok 自动生成Getter、Setter、toString等（编辑器需要安装插件并引入依赖）

mapstrut 对象映射工具，编译构建期自动生成映射实现类，避免运行时映射对性能的影响（编辑器需要安装插件并引入依赖）

fastjson 阿里巴巴序列化工具
```