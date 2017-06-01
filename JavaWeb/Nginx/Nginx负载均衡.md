# 介绍
![](https://www.packtpub.com/graphics/9781785280337/graphics/0337OS_08_04.jpg)  

### The upstream module
* `round-robin`  
轮询：每个请求会依次分配给后端不同的应用程序服务器，不理会后端服务器的实际压力。如果后端服务器down掉，能自动剔除
加权轮询：权重越大的服务器，被分配到的次数就会越多，通常用于后端服务器性能不一致的情况
* `ip_hash`
根据请求所属的客户端IP计算得到一个数值，然后把请求发往该数值对应的后端。
同一客户端连续的Web请求都会被分发到同一服务器进行处理，可以解决动态网站SESSION共享问题
* `least_conn`
When the distribution of requests leads to widely varying response times per request把请求转发给连接数较少的后端，首选遍历后端集群，比较每个后端的conns/weight，选取该值最小的后端。
如果有多个后端的conns/weight值同为最小的，那么对它们采用加权轮询算法。
* `fair`：按后端服务器的响应时间来分配请求，响应时间短的优先分配。
* `url_hash`：按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，后端服务器为缓存时比较有效。

### Multiple upstream server
```
upstream app {
  server 127.0.0.1:9000;
  server 127.0.0.1:9001;
  server 127.0.0.1:9002;
}

server {
  location / {
    try_files $uri @apache;
  }

  location @apache {
    proxy_pass proxy_pass http://app;
  }
}
```

### Memcached upstream servers
```
upstream memcaches {
  server 10.0.100.10:11211;
  server 10.0.100.20:11211;
}

server {
  location / {
    # set $memcached_key "$uri?$args";
    charset utf-8; 
    set $memcached_key "$request_method$request_uri"; 
    memcached_pass memcaches;
    error_page 404 = @appserver;
  }
  location @appserver {
      rewrite ^/(.*) /$1 break;
      proxy_pass http://127.0.0.1:8080;
      proxy_set_header X-Cache-Key "$request_method$request_uri"; 
  }
}

http{ 
    memcached_send_timeout 30s; 
    memcached_connect_timeout 30s; 
    memcached_read_timeout 30s; 
}
```

### FastCGI upstream servers
```
upstream fastcgis {
  server 10.0.200.10:9000;
  server 10.0.200.20:9000;
  server 10.0.200.30:9000;
}

location / {
  fastcgi_pass fastcgis;
}
```

### SCGI upstream servers


### uWSGI upstream servers







## 负载均衡
[nginx负载均衡详解](https://www.zfl9.com/nginx-rproxy+.html)  
[nginx 负载均衡配置](http://blog.huangang.net/2016/10/17/nginx-%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1%E9%85%8D%E7%BD%AE/)  

### 轮询
```
upstream backserver {
    server 192.168.1.14;
    server 192.168.1.15;
}
```

### 加权轮询
```
upstream backserver {
    server 192.168.1.14 weight=1;
    server 192.168.1.15 weight=2;
}
```
* `weight`：设定服务器的权重，默认是1。
* `max_fails`：设定Nginx与服务器通信的尝试失败的次数。  
在`fail_timeout`参数定义的时间段内，如果失败的次数达到此值，Nginx就认为服务器不可用。
在下一个`fail_timeout`时间段，服务器不会再被尝试。失败的尝试次数默认是1。  
设为0就会停止统计尝试次数，认为服务器是一直可用的。  
默认配置时，http 404状态不被认为是失败的尝试。
* `fail_timeout`：服务器被认为不可用的时间段。默认情况下，该超时时间是10秒。
* `backup`：标记为备用服务器。当主服务器不可用以后，请求会被传给这些服务器。
* `down`：标记服务器永久不可用，可以跟ip_hash指令一起使用。

### ip哈希
```
upstream back_end{   
    ip_hash;

    # 一台普通上游服务器
    server 127.0.0.1:80;
    # 默认值1和30s，30s内连接3次失败，休息一会30s后再来
    server 127.0.0.1:8080 max_fails=3 fail_timeout=30s;
    # 备机，不可用时，不能使用ip_hash，扰乱哈希结果违背ip_hash策略初衷。
    server UNIX:/tmp/backend3 backup;
    # 主动宕机，不参与被选。
    server 192.168.0.1:9000 down;
    # 权重3，默认1，与加权策略配合使用。
    server backend1.example.com weight=3;

    hash $cookie_username;
}
```



# TCP load balancer
## The Stream module
### An example of MySQL load balancing
```
stream {
    upstream MyGroup {
        # use IP address-based distribution
        hash $remote_addr;
        server 10.0.0.201 weight=2;
        server 10.0.0.202;
        server 10.0.0.203 backup; # use as backup only
    }
    server {
        # listen on the default MySQL port
        listen 3306;
        proxy_pass MyGroup; # forward requests to upstream
    }
}
```


# Thread pools and I/O mechanisms
## Relieving worker processes


## AIO, Sendfile, and DirectIO
为了开启thread polls支持，应该在安装时添加`--with-threads`参数，默认没有。

```
thread_pool MyPool threads=32 max_queue=65536;

location /downloads/ {
    aio threads=MyPool;
    directio 8k;    # 如果文件请求超过8k，将会使用aio，否则通过sendfile发送
    sendfile on;
}
```