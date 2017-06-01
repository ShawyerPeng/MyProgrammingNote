# 简介
RabbitMQ是实现了高级消息队列协议（AMQP）的开源消息代理软件（亦称面向消息的中间件）。RabbitMQ服务器是用Erlang语言编写的，而群集和故障转移是构建在开放电信平台框架上的。所有主要的编程语言均有与代理接口通讯的客户端库。
MQ全称为Message Queue, 消息队列（MQ）是一种应用程序对应用程序的通信方法。应用程序通过读写出入队列的消息（针对应用程序的数据）来通信，而无需专用连接来链接它们。消 息传递指的是程序之间通过在消息中发送数据进行通信，而不是通过直接调用彼此来通信，直接调用通常是用于诸如远程过程调用的技术（摘自网络）

# 基本概念
![One-way interaction with message queuing](https://www.packtpub.com/graphics/9781783983209/graphics/3209OS_01_02.jpg)  
![Message enabling a loosely coupled architecture](https://www.packtpub.com/graphics/9781783983209/graphics/3209OS_01_10.jpg)  
![](http://oqcr0rg2c.bkt.clouddn.com/17-5-29/12360926.jpg)
左侧 P 代表 生产者，也就是往 RabbitMQ 发消息的程序。
中间即是 RabbitMQ，其中包括了 交换机 和 队列。
右侧 C 代表 消费者，也就是往 RabbitMQ 拿消息的程序。
![]()
![]()
![]()
![]()
![]()


## AMQP核心概念
AMQP：Advanced Message Queuing Protocol
* `Server`(`Broker`)：接受客户端连接，实现AMQP消息队列和路由功能的进程。
* `Virtual Host`：一个虚拟概念，类似于权限控制组，一个Virtual Host里面可以有若干个Exchange和Queue，但是权限控制的最小粒度是Virtual Host
* `Connection`：连接，对于RabbitMQ而言，其实就是一个位于客户端和Broker之间的TCP连接。
* `Channel`：信道，仅仅创建了客户端到Broker之间的连接后，客户端还是不能发送消息的。需要为每一个Connection创建Channel，AMQP协议规定只有通过Channel才能执行AMQP的命令。一个Connection可以包含多个Channel。之所以需要Channel，是因为TCP连接的建立和释放都是十分昂贵的，如果一个客户端每一个线程都需要与Broker交互，如果每一个线程都建立一个TCP连接，暂且不考虑TCP连接是否浪费，就算操作系统也无法承受每秒建立如此多的TCP连接。RabbitMQ建议客户端线程之间不要共用Channel，至少要保证共用Channel的线程发送消息必须是串行的，但是建议尽量共用Connection。
* `Exchange`：接受生产者发送的消息，并根据Binding规则将消息路由给服务器中的队列，用于转发消息，它不会做存储。  
如果没有Queue bind到Exchange的话，它会直接丢弃掉Producer发送过来的消息。
* `Queue`：消息队列，用于存储还未被消费者消费的消息。
* `Binding`：Binding联系了Exchange与Queue，是多对多的关系。Exchange在与多个Queue发生Binding后会生成一张路由表，路由表中存储着Queue所需消息的限制条件即Binding Key。当Exchange收到Message时会解析其Header得到Routing Key，Exchange根据Routing Key与Exchange Type将Message路由到Queue。Binding Key由Consumer在Binding Exchange与Queue时指定，而Routing Key由Producer发送Message时指定，两者的匹配方式由Exchange Type决定。
* `Message`：由Header和Body组成，Header是由生产者添加的各种属性的集合，包括Message是否被持久化、由哪个Queue接受、优先级是多少等。而Body是真正需要传输的APP数据。
* `Command`：AMQP的命令，客户端通过Command完成与AMQP服务器的交互来实现自身的逻辑。例如在RabbitMQ中，客户端可以通过publish命令发送消息，txSelect开启一个事务，txCommit提交一个事务。
![Overview of the concepts defined by the AMQP specification](https://www.packtpub.com/graphics/9781783983209/graphics/3209OS_01_04.jpg)

## 交换机和交换机类型
[RabbitMQ学习心得——RabbitMQ简介（上）](https://zhuanlan.zhihu.com/p/25457169)

## 应用场景
[ActiveMQ、rabbitMQ 等这些东西在做什么的地方会用到？想多了解一下，请大家不吝赐教](https://www.zhihu.com/question/49910532)  
消息系统允许软件、应用相互连接和扩展。这些应用可以相互链接起来组成一个更大的应用，或者将用户设备和数据进行连接。消息系统通过将消息的发送和接受分离开来实现应用程序的异步和解耦。
* 数据传输（消息缓冲）
* 非阻塞的操作，异步执行过程（应用程序的异步和解耦）
* 推送通知（消息分发）
* 使用发布/订阅机制
* 使用一个工作队列
1.日志同步
2.分布式事务
3.数据复制
4.广播通知
5.业务系统解耦

# 安装
## dpkg
1. 下载：`wget http://www.rabbitmq.com/releases/rabbitmq-server/v3.6.9/rabbitmq-server_3.6.9-1_all.deb`  
2. 安装：  
    ```
    sudo dpkg -i rabbitmq-server_3.6.9-1_all.deb
    sudo apt-get -f --force-yes --yes install
    ```
3. 卸载：  
    ```
    apt-get remove --purge xxx # 移除应用及配置
    apt-get autoremove # 移除没用的包
    ```

## apt-get
1. 添加RabbitMQ repository到APT repository（/etc/apt/sources.list）：`deb http://www.rabbitmq.com/debian/ testing main`
2. 安装：`sudo apt-get install rabbitmq-server`

Tips：默认安装位置：  
* 配置文件：`/etc/rabbitmq`
* 应用文件：`/usr/lib/rabbitmq`
* 数据文件：`/var/lib/rabbitmq`

# 操作
* Run RabbitMQ Server：`sudo service rabbitmq-server restart`或`invoke-rc.d rabbitmq-server stop/start/etc`
* 查看状态（在sbin目录下运行）：`sudo rabbitmqctl status`或`sudo service rabbitmq-server status`
* 安装RabbitMQ管理插件：`sudo rabbitmq-plugins enable rabbitmq_management`  
(默认端口:5672,web端登录管理端口:15672)
* 
* 


# 默认用户权限
broker创建了一个账户，账号和密码都是guest，并且只能通过localhost连接virtual host(/)。
如需远程链接，需要如下配置：  
把loopback_users配置项设为[]  
`abbitmq.config`
```
[{rabbit, [{loopback_users, []}]}].
```

RabbitMQ通过在一个资源上configure, write 和 read区分。configure操作创建或销毁资源,或者改变他们的行为。write操作把消息插入资源里。read操作从资源里retrieve消息。

# rabbitmqctl
[RabbitMQ-官方指南－rabbitmqctl(1)指南](http://www.blogjava.net/qbna350816/archive/2016/07/30/431394.html)  
1. Application and Cluster Management
* `stop [pid_file]`：Stops the Erlang node on which RabbitMQ is running
* `stop_app`：关闭应用，但Erlang节点仍然运行
* `start_app`：启动应用，和上述关闭命令配合使用，达到清空队列的目的
* `wait {pid_file}`：Wait for the RabbitMQ application to start.
* `reset`：将RabbitMQ node还原到最初状态.包括从所在群集中删除此node,从管理数据库中删除所有配置数据，如已配置的用户和虚拟主机，以及删除所有持久化消息
* `force_reset`：强制RabbitMQ node还原到最初状态，不管当前管理数据库的状态和集群的配置，它只能在数据库或集群配置已损坏的情况下才可使用
* `rotate_logs {suffix}`：指示RabbitMQ node循环日志文件
* `hipe_compile {directory}`：

2. Cluster management
* `cluster_status`：查看分布式节点状态
* `join_cluster {clusternode} [--ram]`：将节点加入集群
* `change_cluster_node_type {disc | ram}`：修改集群节点的类型
* `forget_cluster_node [--offline]`：允许节点从脱机节点中删除
* `rename_cluster_node {oldnode1} {newnode1} [oldnode2] [newnode2 ...]`：支持在本地数据库中重命名集群节点
* `update_cluster_nodes {clusternode}`：指示已集群的节点醒来时联系clusternode
* `force_boot`：强制节点下次启动时不用等待其它节点
* `sync_queue [-p vhost] {queue}`：指示未同步slaves上的镜像队列自行同步
* `cancel_sync_queue [-p vhost] {queue}`：指示同步镜像队列停止同步
* `purge_queue [-p vhost] {queue}`：清除队列(删除其中的所有消息)
* `set_cluster_name {name}`：设置集群名称

3. User management
* `add_user {username} {password}`：添加用户
* `delete_user {username}`：删除用户
* `change_password {username} {newpassword}`：修改用户密码
* `clear_password {username}`：清除用户密码,禁止用户登录
* `authenticate_user {username} {password}`：指示broker授权给用户
* `set_user_tags {username} {tag ...}`：设置用户角色(Tag可以为administrator,monitoring,policymaker,management)
* `list_users`：列出所有用户

4. Access control
* `add_vhost {vhost}`：创建虚拟主机
* `delete_vhost {vhost}`：删除虚拟主机
* `list_vhosts [vhostinfoitem ...]`：列出所有虚拟主机
* `set_permissions [-p vhost] {user} {conf} {write} {read}`：设置用户对某个虚拟主机的权限，如果不指定vhost，则默认为“/”为vhost.
`sudo rabbitmqctl set_permissions -p ccm-dev-vhost ccm-admin ".*" ".*" ".*"`：赋予全部权限
* `clear_permissions [-p vhost] {username}`：清除用户对某个vhost的权限信息
* `list_permissions [-p vhost]`：查看某个虚拟主机下(指定hostpath)所有用户的权限信息
* `list_user_permissions {username}`：查看指定用户的权限信息
```
添加一个管理员代替 guest
rabbitmqctl add_user admin 123456
指定用户的角色
rabbitmqctl set_user_tags admin administrator         
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"    
分配给用户指定虚拟主机的权限,虽然是administrator角色,但不对所有虚拟主机都有权限,一样需要对每个虚拟主机都授权
```

5. Parameter Management
* `set_parameter [-p vhost] {component_name} {name} {value}`：设置一个参数
* `clear_parameter [-p vhost] {component_name} {key}`：清除参数
* `list_parameters [-p vhost]`：列出虚拟主机上的所有参数
* `set_global_parameter {name} {value}`：
* `clear_global_parameter {name}`：
* `list_global_parameters`：

6. Policy Management
* `set_policy [-p vhost] [--priority priority] [--apply-to apply-to] {name} {pattern} {definition}`：设置策略
* `clear_policy [-p vhost] {name}`：清除策略
* `list_policies [-p vhost]`：显示虚拟主机上的所有策略

7. Server Status
* `list_queues [-p vhost] [[--offline] | [--online] | [--local]] [queueinfoitem ...]`：查看队列中的数据
* `list_exchanges [-p vhost] [exchangeinfoitem ...]`：Exchange信息
* `list_bindings [-p vhost] [bindinginfoitem ...]`：Binding信息
* `list_connections [connectioninfoitem ...]`：Connection信息(Connectioninfoitem有：recv_oct，recv_cnt，send_oct，send_cnt，send_pend等)
* `list_channels [channelinfoitem ...]`：Channel信息(Channelinfoitem有consumer_count，messages_unacknowledged，messages_uncommitted，acks_uncommitted，messages_unconfirmed，prefetch_count，client_flow_blocked)
* `list_consumers [-p vhost]`：Consumer信息
* `status`：查看节点状态
* `node_health_check`：
* `environment`：显示每个运行程序环境中每个变量的名称和值
* `report`：为所有服务器状态生成一个服务器状态报告，输出应该重定向到一个文件
* `eval {expr}`：执行任意Erlang表达式

8. Miscellaneous
* `close_connection {connectionpid} {explanation}`：指示broker关闭与Erlang进程id相关的链接
* `trace_on [-p vhost]`：
* `trace_off [-p vhost]`：
* `set_vm_memory_high_watermark {fraction}`：当一个浮点数大于或等于0时，会触发流量控制新内存阈值部分
* `set_vm_memory_high_watermark absolute {memory_limit}`：流程控制触发的新内存限制, 以字节来表示大于或等于０的整数或以字符串和内存单位来表示
* `set_disk_free_limit {disk_limit}`：以整数或字符串单位的可用磁盘下限限制，一旦可用磁盘空间达到这个限制，就会设置磁盘报警
* `set_disk_free_limit mem_relative {fraction}`：相对于整个可用内存的限制，其值为非负浮点数. 当值小于１.０时是很危险的，应该谨慎使用
* `encode [--decode] [value] [passphrase] [--list-ciphers] [--list-hashes] [--cipher cipher] [--hash hash] [--iterations iterations]`：

# 配置
[解决RabbitMQ队列超长QueueingCons](http://blog.csdn.net/runyon1982/article/details/49018869)
[RabbitMQ Configuration](http://www.rabbitmq.com/configure.html#configuration-file)
[define-environment-variables](http://www.rabbitmq.com/configure.html#define-environment-variables)  
[RabbitMQ解决大量unacked问题](http://blog.csdn.net/weinianjie1/article/details/50611379)

默认的配置文件：`/etc/rabbitmq/rabbitmq.config`   https://github.com/rabbitmq/rabbitmq-server/blob/stable/docs/rabbitmq.config.example
Windows：`C:\Program Files (x86)\RabbitMQ_Server\etc\rabbitmq.config`

## 环境变量
定义端口、文件位置和名称（从shell或者环境配置文件rabbitmq-env.conf/rabbitmq-env-conf.bat配置）

Name | Default | Description
----|------|----
RABBITMQ_BASE | | This is the directory in which RabbitMQ server's database and log files are located.
RABBITMQ_CONFIG_FILE | | This is the name of configuration file. The name doesn't consist of the extension ".config".
RABBITMQ_CONSOLE_LOG | | new或reuse
RABBITMQ_LOGS | log文件的目录
RABBITMQ_LOG_BASE | 如果设置了RABBITMQ_LOGS或RABBITMQ_SASL_LOGS，这个就没有效果了
RABBITMQ_NODE_IP_ADDRESS | 空字符串(绑定所有网络接口) | 只绑定一个网络接口
RABBITMQ_NODE_PORT | 5672 | RabbitMQ server绑定的端口号 |
RABBITMQ_DIST_PORT | RABBITMQ_NODE_PORT + 20000 |
RABBITMQ_NODENAME | On Unix: rabbit@hostname On Windows:rabbit@%COMPUTERNAME% | RabbitMQ server的结点名。每个Erlang node和machine combination都不同
RABBITMQ_CONF_ENV_FILE |
RABBITMQ_USE_LONGNAME |
RABBITMQ_SERVICENAME | On Windows Service:RabbitMQ On Unix:rabbitmq-server | 指定安装在系统的服务名
RABBITMQ_CTL_ERL_ARGS |
RABBITMQ_SERVER_ERL_ARGS |
RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS |
RABBITMQ_SERVER_START_ARGS |

## Unix-specific default location
Name | Location
--- | ---
RABBITMQ_BASE | This variable is not used for Unix
RABBITMQ_CONFIG_FILE | ${install_prefix}/etc/rabbitmq/rabbitmq
RABBITMQ_LOGS | $RABBITMQ_LOG_BASE/$RABBITMQ_NODENAME.log
RABBITMQ_LOG_BASE | ${install_prefix}/var/log/rabbitmq
RABBITMQ_MNESIA_BASE | ${install_prefix}/var/lib/rabbitmq/mnesia
RABBITMQ_MNESIA_DIR | $RABBITMQ_MNESIA_BASE/$RABBITMQ_NODENAME
RABBITMQ_PLUGINS_DIR | $RABBITMQ_HOME/plugins
RABBITMQ_SASL_LOGS | $RABBITMQ_LOG_BASE/$RABBITMQ_NODENAME-sasl.log

## Windows-specific default location
Name | Location
--- | ---
RABBITMQ_BASE | %APPDATA%\RabbitMQ
RABBITMQ_CONFIG_FILE | %RABBITMQ_BASE%\rabbitmq
RABBITMQ_LOGS | %RABBITMQ_LOG_BASE%\%RABBITMQ_NODENAME%.log
RABBITMQ_LOG_BASE | %RABBITMQ_LOG_BASE%\log
RABBITMQ_MNESIA_BASE | %RABBITMQ_BASE%\db
RABBITMQ_MNESIA_DIR | %RABBITMQ_MNESIA_BASE%\%RABBITMQ_NODENAME%
RABBITMQ_PLUGINS_DIR | %RABBITMQ_BASE%\plugins
RABBITMQ_SASL_LOGS | %RABBITMQ_LOG_BASE%\%RABBITMQ_NODENAME%-sasl.log

# 整合Spirng
[Spring整合Rabbitmq](https://my.oschina.net/u/136848/blog/288736)


# 集群
## 查看RabbitMQ当前的运行状态
使用命令`sudo rabbitmqctl status`
上图中，红框内为运行的应用。Rabbit即为RabbitMQ 服务器。其余则为Erlang／OTP提供的application
```
• os_mon: 操作系统监控
• xmerl: 导出XML数据到外部格式的函数
• mnesia: 一个分布式数据库管理系统，适合于电信应用和其他需要持续操作并展示实时信息的应用
• SASL (System Architecture Support Libraries)
• Kernel & stdlib: 是第一个启动的服务，其作为Erlang框架的核心。
```
注：以上Erlang／OTP的应用都可以通过 “erl –man ”查询它的帮助文档。

































# Java
```xml
<!-- https://mvnrepository.com/artifact/com.rabbitmq/amqp-client -->
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>4.1.0</version>
</dependency>
```
`com.rabbitmq.client`provides classes and interfaces for `AMQP connections`, `channels`, and `wire-protocol framing descriptions`
`com.rabbitmq.tools`provides classes and methods for `non-core utilities` and `administration tools`
`com.rabbitmq.utility`provides helper classes which are mostly used in the `implementation of a library`


```java
ConnectionFactory factory = new ConnectionFactory();
// 1. 分别设置
// factory.setHost("localhost"); factory.setUsername("guest"); factory.setPassword("guest");
// 2. 通过Uri设置
factory.setUri("amqp://guest:guest@localhost");

// Connection
Connection conn = factory.newConnection();
// Channel
Channel channel = conn.createChannel();
// Exchanges
channel.exchangeDeclare("mastering.rabbitmq","fanout");
// Queues (Bind the queue to the exchange without routing key)
channel.queueBind(channel.queueDeclare().getQueue(), "mastering.rabbitmq","");
// queueDeclare第一个参数表示队列名称、第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、第四个参数为当所有消费者客户端连接断开时是否自动删除队列、第五个参数为队列的其他参数
// channel.queueDeclare(QUEUE_NAME,false,false,false,null);

// Publishing messages
String message ="Hello Mastering RabbitMQ!";
// basicPublish第一个参数为交换机名称、第二个参数为队列映射的路由key、第三个参数为消息的其他属性、第四个参数为发送信息的主体
channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
System.out.println("Following Message Sent: "+ message);

channel.close();
conn.close();
```

## Exchange的四种模式
[RabbitMQ 三种Exchange](http://melin.iteye.com/blog/691265)  
所有生产者提交的消息都由Exchange来接受，然后Exchange按照特定的策略转发到Queue进行存储

1. fanout：扇形交换机。不处理路由键，只需要简单的将队列绑定到交换机上。一个发送到交换机的消息都会被转发到与该交换机绑定的`所有队列`上。很像子网广播，每台子网内的主机都获得了一份复制的消息。
* 转发消息是最快的
* 不需要RouteKey
* 需要提前将Exchange与Queue进行绑定，一个Exchange可以绑定多个Queue，一个Queue可以同多个Exchange进行绑定
* 如果接受到消息的Exchange没有与任何Queue绑定，则消息会被抛弃
* 如果不同的consumer需要对同样的消息进行不同的处理，那么这种方式是很有用的。
![](http://oqcr0rg2c.bkt.clouddn.com/17-5-29/49365459.jpg)
```java
Channel channel = connection.createChannel();
channel.exchangeDeclare("exchangeName", "fanout");
channel.queueDeclare("queueName", false, false, false, null);
// 虽然指定了一个routing key，但实际上是没有任何效果的，我们还可以用空字符串，最后消息都是到达所有queue的。
channel.queueBind("queueName", "exchangeName", "routingKey");
channel.queueDeclare("queueName1", false, false, false, null);
channel.queueBind("queueName1", "exchangeName", "routingKey1");

byte[] messageBodyBytes = "hello world".getBytes();
//路由键需要设置为空  
channel.basicPublish("exchangeName", "", MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
```  

2. direct：直连交换机。主要根据路由键来分发消息到不同的队列中。路由键是消息的一个属性，由生产者加在消息头中。需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog。 
* 路由键必须是一串字符，用句号（.） 隔开，比如说 agreements.us，或者 agreements.eu.stockholm 等。
* 
* 
* 
![](http://oqcr0rg2c.bkt.clouddn.com/17-5-29/78393978.jpg)  
```java
Channel channel = connection.createChannel();  
channel.exchangeDeclare("exchangeName", "direct");
channel.queueDeclare("queueName");  
channel.queueBind("queueName", "exchangeName", "routingKey");  
  
byte[] messageBodyBytes = "hello world".getBytes();  
//需要绑定路由键  
channel.basicPublish("exchangeName", "routingKey", MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
```  

3. topic：主题交换机。将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”
* 
* 
* 
* 
![](http://oqcr0rg2c.bkt.clouddn.com/17-5-29/67925837.jpg)  
```java
Channel channel = connection.createChannel();  
channel.exchangeDeclare("exchangeName", "topic");
channel.queueDeclare("queueName");  
channel.queueBind("queueName", "exchangeName", "routingKey.*");  
  
byte[] messageBodyBytes = "hello world".getBytes();  
channel.basicPublish("exchangeName", "routingKey.one", MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);  
```  

4. header：头交换机。 
This can be used to deliver messages to queues based on other message header attributes (and not the routing key). There are two pre-created headers exchanges with names .amq.headers and .amq.match.
* 
* 
* 
* 

性能排序：fanout > direct >> topic，比例大约为11：10：6


## Spring对RabbitMQ的注解支持
`@EnableRabbit`
`@RabbitListener`：
`@RabbitHandler`