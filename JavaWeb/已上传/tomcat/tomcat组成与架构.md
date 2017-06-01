# 一、Connector参数配置
* 指定依赖的线程池配置
* 直接指定线程池配置参数(minSpareThreads maxThreads)

### 1. port
端口
### 2. address
默认在所有的地址上监听请求，使用它来指定只监听哪几个地址
### 3. protocol
默认http1.1，一般只有在涉及性能调优的时候才用
### 4. connectionTimeout
单位ms，如果连接不发请求一定时间后就关闭
### 5. acceptCount
当系统非常繁忙（所有线程都在请求）的时候，如果再来请求怎么办呢？用它来配置队列长度，默认100，如果超过长度，则会拒绝处理后面的请求
### 6. maxConnections
能支持的最大连接数，默认BIO方式的值等于最大线程数。若超过这个数，后面的请求虽然被接受，但不会被处理  
若为-1，则不限制最大连接数

# 二、线程池配置项<Executor>
事先创建一定数目的线程。当有任务时，从池中取出，用完后再放回池内。  
最小空闲线程数`minSpareThreads`  
最大线程数`maxThreads`  

# 三、Tomcat日志
* 系统运行日志：记录运行信息与状态
* 访问日志：记录请求访问
* 应用日志

```
<Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
       prefix="localhost_access_log" suffix=".txt"
       fileDateFormat="yyyy-MM-dd.HH."    默认:yyyy-MM-dd.
       rotatable="true"                   默认:true
       pattern="%h %l %u %t &quot;%r&quot; %s %b" />
       pattern="method: %m, client ip: %a, time:%t &quot;%r&
       quot; statusCode: %s, byteSent: %b, User-Agent: %{User-Agent}i" />
```

# 四、附图
tomcat架构：  
![](http://p1.bpimg.com/567571/474e71a9de8714c1.jpg)
![](http://i1.piimg.com/567571/a1d24076c4314b6b.jpg)

tomcat目录结构：  
![](http://i1.piimg.com/567571/cb3b76c18fcc7760.jpg)

server.xml配置：  
![](http://i1.piimg.com/567571/7d22c5b8dcd3e2d8.jpg)