# 一、简单介绍
Rackspace和NASA共同发起
一系列开源软件的组合
基础设施资源的系统管理平台

# 二、模块组成
### 1. Horizon--UI面板模块
主要分为两种：云管理员(DashBoard)和云用户(Customer Portal)

为云管理员提供整体视图，以实现资源整合和配额管理；
为终端用户提供自助服务门户，在配额范围内，自由操作，使用资源

### 2. Keystone--身份服务模块
是控制核心。有身份认证、权限控制等功能，其他模块都与之关联

User用户、Tenant租户、Role角色
![](http://p1.bqimg.com/567571/e6ab953b966253cf.png)

Service服务、Endpoint访问点、Token访问资源的令牌
![](http://p1.bpimg.com/567571/9bddccc64c6b924c.png)

提供的服务：
![](http://i1.piimg.com/567571/28995e348866a7c1.png)

易购环境集成：
![](http://p1.bqimg.com/567571/78dd9ea94e7f2f81.png)

### 3. Nova--计算服务模块
主要功能：
![](http://p1.bqimg.com/567571/2ee36f32854021f3.png)

组件介绍：
![](http://p1.bqimg.com/567571/e4df13cdd028c058.png)
api：位于表示层。接受外部的REST请求  
scheduler：位于逻辑控制层。调度中枢，根据不同的算法选择相应的Host  
compute：创建分配虚拟机，支持不同的虚拟机形式  
rabbit MQ：消息中间件

### 4. Glance--镜像服务模块
![](http://p1.bqimg.com/567571/567b4b5ba14f7ac6.png)
glance-api：不管是来自WEB的请求还是命令行的请求，首先都要发送到表示层的api上，通过该空间对请求进行解析  
glance-registry：根据请求的信息到这里查询相应的信息

### 5. Swift--对象存储服务模块
![](http://p1.bqimg.com/567571/e95ed606d26596ff.png)
所有的请求通过Proxy进行处理，通过Proxy到合适的Account下寻找相应的container下的某一个object进行存取服务

### 6. Cinde--块存储服务模块
![](http://p1.bpimg.com/567571/1537ba029eb1c564.png)
api负责对发过来的请求进行处理，处理后的结果通过消息中间件进行传递，发送给scheduler调度器，通过调度器来决定去哪里申请块存储服务  
并且要创建一个VM，挂靠在volume上，对具体的存储模块进行管理

### 7. Neutron--网络服务模块
![](http://p1.bpimg.com/567571/f0d8d0b8dde2a2c0.png)
三种模式：
1. Flat模式：网桥模式，所有的都需要手工配置
2. Flat DHCP模式：在网关处单独启动了一个DHCP的进程，可以辅助用户进行网络配置
3. VLAN模式：为每个租户分配了不同的虚拟子网，用户在此虚拟子网中可以有自己私有的IP，用户需要时可以在这些虚拟IP中选择分配给不同的虚拟机

# 组件间的关系
![](http://i1.piimg.com/567571/093dfe0ebe9301de.png)

# 访问控制流程
![](http://p1.bqimg.com/567571/aa27b343bdea0149.png)
