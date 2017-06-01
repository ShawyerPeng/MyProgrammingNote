1. 对静态文件进行缓存
```
location ~ ^/(images|javascript|js|css|flash|media|static)/ {
    #过期30天
    expires 30d;
}

location ~ .*.(gif|jpg|jpeg|png|bmp)$ {
  expires 30d;
}
 
location ~ .*.(js|css)$ {
  expires 1h;
}
```

2. 定义错误提示页面
```
error_page   500 502 503 504  /50x.html;
    location = /50x.html {
    root   html;
}
```



## 缓存
[HTTP 缓存的四种风味与缓存策略](https://segmentfault.com/a/1190000006689795)
### FastCGI
[Module ngx_http_fastcgi_module](http://nginx.org/en/docs/http/ngx_http_fastcgi_module.html)  
[FastCGI Example](https://www.nginx.com/resources/wiki/start/topics/examples/fastcgiexample/)  
[FastCGI模块（FastCGI）](http://www.cnblogs.com/shengshuai/archive/2013/01/11/fastcgi.html)  
```
fastcgi_connect_timeout 300;            # 连接到后端FastCGI的超时时间
fastcgi_send_timeout 300;               # 向FastCGI传送请求的超时时间
fastcgi_read_timeout 300;               # 接收FastCGI应答的超时时间

fastcgi_buffer_size 4k;                 # 读取FastCGI应答第一部分需要用多大的缓冲区
fastcgi_buffers 8 4k;                   # 本地需要用多少和多大的缓冲区来缓冲FastCGI的应答
fastcgi_busy_buffers_size 8k;           # 默认值是fastcgi_buffers的两倍
fastcgi_temp_file_write_size 8k;        # 在写入fastcgi_temp_path时将用多大的数据块
fastcgi_cache_path /usr/local/nginx/fastcgi_temp levels=1:2 keys_zone=cache_fastcgi:128m inactive=1d max_size=10g;
# 为FastCGI缓存指定一个路径，目录结构等级，关键字区域存储时间和非活动删除时间。以及最大占用空间。

fastcgi_cache TEST                      # 开启FastCGI缓存并且为其制定一个名称
fastcgi_cache_valid 200 302 1h;         # 为指定的应答代码指定缓存时间
fastcgi_cache_valid 301 1d;  
fastcgi_cache_valid any 1m;
fastcgi_cache_min_uses 1;               # 缓存在fastcgi_cache_path指令inactive时间内最少使用次数

```