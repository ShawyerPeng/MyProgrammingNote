# 介绍
高并发、高可用、负载均衡
动静分离：前端服务器（静态web服务器、负载均衡）、后端服务器（动态web服务器）  
![](https://www.packtpub.com/graphics/9781785281839/graphics/B04592_01_01.jpg)  
![](https://www.packtpub.com/graphics/9781785281839/graphics/B04592_01_02.jpg)  

# 安装
## apt-get
1. 安装NGINX signing key（http://nginx.org/keys/nginx_signing.key）：  
`sudo apt-key add nginx_signing.key`
2. 添加一下两行到repository：`vi /etc/apt/sources.list`  
```
deb http://nginx.org/packages/debian/ jessie nginx
deb-src http://nginx.org/packages/debian/ jessie nginx
```
3. 安装：  
`sudo apt-get update`  
`sudo apt-get install nginx`

## binaries
1. 下载：
`wget http://nginx.org/download/nginx-1.12.0.tar.gz`
2. 解压：
`tar -xzvf nginx-1.12.0.tar.gz`
3. 安装：
`cd nginx-1.12.0 && ./configure`  
`make && make install`
4. 检测是否安装成功：
`sudo /usr/local/nginx/sbin/nginx -t`  
或`sudo nginx -t -c /usr/local/nginx.conf`

5. 设置开机自启动：
复制到init.d目录：`sudo cp /usr/local/nginx/sbin/nginx /etc/init.d/`  
赋予可执行权限：`chmod +x /etc/init.d/nginx`  
为脚本开启自启动：`update-rc.d -f nginx defaults`

~~添加环境变量：`sudo nano /etc/profile`对所有用户有效或`.bash_profile"`只对这个用户有效~~
```bash
PATH=$PATH:路径1:路径2:...:路径n
PATH=$PATH:$HOME/bin:/sbin:/usr/bin:/usr/sbin
```

```bash
nginx path prefix: "/usr/local/nginx"
nginx binary file: "/usr/local/nginx/sbin/nginx"
nginx modules path: "/usr/local/nginx/modules"
nginx configuration prefix: "/usr/local/nginx/conf"
nginx configuration file: "/usr/local/nginx/conf/nginx.conf"
nginx pid file: "/usr/local/nginx/logs/nginx.pid"
nginx error log file: "/usr/local/nginx/logs/error.log"
nginx http access log file: "/usr/local/nginx/logs/access.log"
```

# 文件目录
## apt-get
Description | Path/Folder
--- | ---
配置文件目录 | `/etc/nginx`
主配置文件 | `/etc/nginx/nginx.conf`
Virtual hosts配置文件 | `/etc/nginx/sites-enabled`
Custom configuration files | `/etc/nginx/conf.d`
日志文件 (both access and error log) | `/var/log/nginx`
Temporary files | `/var/lib/nginx`
默认 virtual host 文件 | `/usr/share/nginx/html`

## binaries
Description | Path/Folder
--- | ---
配置文件目录 | `/usr/local/nginx`
主配置文件 | `/usr/local/nginx/conf`
Main configuration file | `/usr/local/nginx/conf/nginx.conf`
Log files (both access and error log) | `/usr/local/nginx/logs`
Temporary files | `/usr/local/nginx` |
默认 virtual host files | `/usr/local/nginx/html`


# 启用，停止和重载配置
1. 运行可执行文件就可以开启nginx(-c 为 nginx 的配置文件)：
~~nginx -c /usr/local/nginx/conf/nginx.conf~~  
`/usr/local/nginx/sbin/nginx`
2. 若nginx已经开启就可以通过使用-s参数的可执行命令控制：`nginx -s signal`
    * stop — 直接关闭nginx
    * quit — 会在处理完当前正在的请求后退出，也叫优雅关闭
    * reload — 重新加载配置文件，相当于重启
    * reopen — 重新打开日志文件。比如，等待当前子进程处理完正在执行的请求后，结束nginx进程，可以使用下列命令
    `/usr/local/nginx/sbin/nginx -s stop/reload/reopen`
3. 查看80端口是否运行：`netstat -ano/-ntulp | grep 80`
4. 查看nginx进程pid：`ps -ef | grep nginx`

```
USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
用户名     进程号  使用率  总虚拟内存大小 使用的总物理内存数 终端的次要装置号码 进程状态 进程开始时间 进程执行起到现在总的CPU暂用时间 执行命令的简单格式

stat 中的参数意义如下：
    D 不可中断 Uninterruptible（usually IO）
    R 正在运行，或在队列中的进程
    S 处于休眠状态(中断)
    T 停止或被追踪
    Z 僵尸进程（僵死）
    W 进入内存交换（从内核2.6开始无效）
    X 死掉的进程

    < 高优先级
    n   低优先级
    s   包含子进程
    +   位于后台的进程组
```

# 配置
使用tree命令以目录树的形式查看nginx目录下的文件：
```
.
├── conf
│   ├── fastcgi.conf
│   ├── fastcgi_params
│   ├── koi-utf
│   ├── koi-win
│   ├── mime.types
│   ├── nginx.conf
│   ├── scgi_params
│   ├── uwsgi_params
│   └── win-utf
├── html
│   ├── 50x.html
│   └── index.html
├── logs
│   ├── access.log
│   ├── error.log
│   └── nginx.pid
├── sbin
│   └── nginx：用于nginx的启动、关闭等命令
├── client_body_temp：POST一个大文件，长度超过了nginx缓冲区的大小时，暂存到该目录下的临时文件
├── fastcgi_temp
├── proxy_temp
├── scgi_temp
└── uwsgi_temp
```

主配置目录：/etc/nginx/nginx.conf
1. 修改root目录：`nano /etc/nginx/sites-enabled/default`
```

```
2. 重启nginx：`service nginx restart`


```
# 1. 全局块
#user  nobody;                                  # 配置用户或者组，默认为nobody
worker_processes  1;                            # 允许生成的进程数，默认为1，设置成和cpu数量相等

# 制定全局错误日志路径和级别。级别依次为：debug|info|notice|warn|error|crit|alert|emerg
#error_log  logs/error.log;                         
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;                     # 指定nginx进程运行pid文件存放地址

# 2. events块
events {
    worker_connections  1024;                   # 单个进程的最大连接数，默认为1024
    accept_mutex on;                            # 设置网路连接序列化，防止惊群现象发生，默认为on
    multi_accept on;                            # 设置一个进程是否同时接受多个网络连接，默认为off
    #use epoll;       # 事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
}

# 3. http块
http {
    # 3.1 http全局块
    include       mime.types;                   # 文件扩展名与文件类型映射表
    default_type  application/octet-stream;     # 默认文件类型，默认为text/plain

    #access_log off;                            # 取消服务日志

    # 自定义日志格式
    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';
    #access_log  logs/access.log  main;

    sendfile        on;                         # 允许sendfile方式传输文件
    sendfile_max_chunk 1M;
    #tcp_nopush     on;

    keepalive_timeout  65;                      # 连接超时时间，默认为65s，设为0自动断连

    #gzip  on;                                  # 开启gzip压缩

    # 3.2 虚拟主机server块
    # 定义某个负载均衡服务器
    server {
        # 3.2.1 server全局块
        listen       80;                        # 监听端口
        server_name  localhost;                 # 访问地址，域名可以有多个，用空格隔开

        charset utf-8;
        #access_log  logs/host.access.log  main;# 设定本虚拟主机的访问日志

        # 3.2.2 location块
        # 请求的url过滤，正则匹配，~为区分大小写，~*为不区分大小写。
        location / {
            root   html;                        # 网站根目录
            index  index.html index.htm;        # 设置默认页
            proxy_pass http://127.0.0.1:8080;        
            add_header 'Access-Control-Allow-Origin' '*';
            add_header 'Access-Control-Allow-Credentials' 'true';
            add_header "Access-Control-Allow-Headers" "X-Requested-With, Content-Type";
            add_header 'Access-Control-Allow-Methods' 'GET,POST,PUT,DELETE,OPTIONS';
        }

        #error_page  404              /404.html;        # 错误页

        # 错误提示页面
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # 选择以.gif，.jpg，或者.png 结束的请求并且映射到/data/images 目录
        location ~ \.(gif|jpg|jpeg|png|bmp|swf)$ {
            root /data/images;
            expires 30d;                        # 配置缓存的过期时间
        }

        #下列文件缓存在本地浏览器1小时
        location ~ .*\.(js|css)?${
            expires 1h;
        }

        # 动静分离反向代理配置，所有jsp的页面均交由tomcat或resin处理
        location ~ .(jsp|jspx|do)?$ {
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_pass http://127.0.0.1:8080;
        }
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}
}
```



## 主机与端口
```
listen 127.0.0.1:8000;
listen *:8000;
listen localhost:8000;
# IPV6
listen [2001:3CA1:10F:1A:121B:0:0:10]:8000;
# 听本机的所有IPv4与IPv6地址
listen [::]:80;  
# other params
listen 443 default_serer ssl;
listen 127.0.0.1 default_server accept_filter=dataready backlog=1024
```

## 服务域名
```
# 支持多域名配置
server_name www.barretlee.com barretlee.com;
# 支持泛域名解析
server_name *.barretlee.com;
# 支持对于域名的正则匹配
server_name ~^\.barret\.com$;
```





# 参考资料
[Nginx 配置从零开始](http://www.open-open.com/lib/view/open1419826381531.html)
[Nginx配置详解](http://www.cnblogs.com/knowledgesea/p/5175711.html)
[构建基于Nginx的web服务器](http://www.cnblogs.com/mchina/archive/2012/04/25/2469680.html)
[使用Nginx搭建WEB服务器](http://www.linuxidc.com/Linux/2013-09/89768.htm)

[你真的了解如何将 Nginx 配置为Web服务器吗](https://lufficc.com/blog/configure-nginx-as-a-web-server)  
[Nginx 配置汇总](http://vinc.top/2017/02/07/nginx-%E9%85%8D%E7%BD%AE%E6%B1%87%E6%80%BB/)  
[使用 Nginx + Apache 实现服务器负载均衡](https://ifengge.me/2016/09/18/205.html)  
[nginx负载均衡详解](https://www.zfl9.com/nginx-rproxy+.html)  
[Nginx学习之负载均衡](http://blog.52itstyle.com/archives/623/)  
[nginx 负载均衡配置](http://blog.huangang.net/2016/10/17/nginx-%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1%E9%85%8D%E7%BD%AE/)  
[Nginx基本的负载均衡配置+静态资源分离](http://wxyz.ren/2016/05/09/004-nginx-upstream-deploy/)  
https://github.com/gaoxt/blog/issues/1  
