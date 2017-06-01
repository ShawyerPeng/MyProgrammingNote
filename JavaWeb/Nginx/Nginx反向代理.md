# 概念
正向代理是为客户端做代理，代替客户端去访问服务器，而反向代理是为服务器做代理，代替服务器接受客户端请求

# 意义
* 保护网站安全，防止外网对内网服务器的恶性攻击，因为任何来自Internet的请求都必须先经过代理服务器。
* 访问安全控制。
* 通过缓存静态资源，加速Web请求，减少服务器的压力。
* 实现负载均衡，将用户请求分配给多个服务器。目前市面上，主流的负载均衡方案，硬件设备有F5，软件方案有四层负载均衡的LVS，七层负载均衡的Nginx、Haproxy等。

# 原理

# 配置
1. `/uri` will be transformed to `/newuri` when the request is passed on to the upstream:
```
location /uri {
  proxy_pass http://localhost:8080/newuri;
}
```

2. if the location is defined with a regular expression, no transformation of the URI occurs
`/local`, will be passed directly to the upstream, and not be transformed to `/foreign` as intended
```
location ~ ^/local {
    proxy_pass http://localhost:8080/foreign;
}
```

3. if within the location a rewrite rule changes the URI, and then NGINX uses this URI to process the request, no transformation occurs.
the URI passed to the upstream will be `/index.php?page=<match>`, with `<match>` being whatever was captured in the parentheses, and not `/index`, as indicated by the URI part of the `proxy_pass` directive
```
location / {
    rewrite /(.*)$ /index.php?page=$1 break;
    proxy_pass http://localhost:8080/index;
}
```

### try_files
不好：
```
location ~* \.jsp$ {
    proxy_pass http://127.0.0.1:8080;
}
location / {
    expires 30d;
}
```
应该用try_files
```
location / {
    # Try serving requested file, or forward to Tomcat
    try_files $uri $uri/ @proxy;
    # - If the file does not exist, append /
    # - If the directory does not exist, redirect to /index.php forwarding the request URI
    # and other request arguments
    # try_files $uri $uri/ /index.php?q=$uri&$args;

    # For 404 errors, submit the query to the @proxy named location block
    error_page 404 @proxy;
}
location @proxy {
    # Forwards requests to Tomcat
    proxy_pass http://127.0.0.1:8080;
}
```

### if
```
location / {
    # If the requested file extension ends with .jsp, forward the query to Apache
    if ($request_filename ~* \.jsp$) {
        break; # prevents further rewrites
        proxy_pass http://127.0.0.1:8080;
    }
    # If the requested file does not exist, forward the query to Apache
    if (!-f $request_filename) {
        break;      # prevents further rewrites
        proxy_pass http://127.0.0.1:8080;
    }
    # Your static files are served here
    expires 30d;
}
```

可以创建`proxy.conf`文件，然后`proxy_pass`指令后把它包括进去：`include proxy.conf;`





1. 基本
```
location /example {
    proxy_pass http://192.168.0.1;      # 写hostname或IP都行
    proxy_pass http://192.168.0.1:8080; # 如果upstream server监听非标准端口，可以在后面添加端口
}
```

2. URL重写
如果location为正则表达式，则proxy_pass的url不能加具体路径
```
location /download {
    proxy_pass http://localhost:8080/media;
}
```
如果请求`http://localhost/download/BigFile.zip`，将会引导至`http://localhost:8080/media/BigFile.zip`

再如：
```
location /api {
    proxy_pass http://localhost:8080/index.jsp;
}
```
如果请求`http://localhost/api`，将会引导至`http://localhost:8080/index.jsp`

```
location ~ \.jsp$ {
    proxy_pass http://localhost:8080;
}
```
如果请求`http://localhost/index.jsp`，将会引导至`http://localhost:8080/index.jsp`

3. try_files
```
location / {
    try_files $uri $uri/ @proxy;
}

location @proxy {
    proxy_pass http://backend;
}
```

4. Handling redirects
```
location @proxy {
    proxy_pass http://localhost:8080;
    proxy_redirect http://localhost:8080/app http://www.example.com;
}
```
当upstream server遇到temporary or permanent redirect（301/302），绝对路径或刷新头需要被重写一遍包含合适的hostname

假设应用部署在`http://localhost:8080/app`，原始服务器有地址`http://www.example.com`，`http://localhost:8080/app/login`遇到了302错误，配置后，重写头为`http://www.example.com/login`，如果没有被重写，客户端将被重定向到`http://localhost:8080/app/login`，这个无法访问

5. Handling cookies
[nginx 配置之 proxy_pass 的 Cookie 转换](https://www.web-tinker.com/article/21212.html)  
[解决nginx使用proxy_pass反向代理时，cookie丢失的问题](http://blog.csdn.net/we_shell/article/details/45153885)  
```
location @proxy {
    proxy_pass http://localhost:8080;
    proxy_cookie_domain localhost:8080 www.example.com;
}
```

5. Using SSL
If an upstream uses a trusted certificate that cannot be verified by well-known certification authorities or a self-signed certificate：
```
location @proxy {
    proxy_pass https://192.168.0.1;
    proxy_ssl_verify on;
    proxy_ssl_trusted_certificate /etc/nginx/upstream.pem;
}
```
If Nginx needs to authenticate itself to the upstream server：
```
location @proxy {
    proxy_pass https://192.168.0.1;
    proxy_ssl_certificate /etc/nginx/client.pem;
    proxy_ssl_certificate_key /etc/nginx/client.key;
}
```

## Handling errors
```
location ~* (script1|script2|script3)\.php$ {
    proxy_pass http://192.168.0.1;
    error_page 500 502 503 504 /50x.html;
}
```

## Choosing an outbound IP address
`proxy_bind`
```
location @proxy {
    proxy_pass https://192.168.0.1;
    proxy_bind 192.168.0.2;
}
```

## Accelerating downloads
```
location ~* (script1|script2|script3)\.php$ {
    proxy_pass https://192.168.0.1;
}

location /internal-media/ {
    internal;
    alias /var/www/media/;
}
```

## Forwarding the correct IP address
`proxy_set_header X-Real-IP $remote_addr`