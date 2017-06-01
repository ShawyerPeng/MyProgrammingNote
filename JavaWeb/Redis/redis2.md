Redis 默认端口:6379
Memcached 默认端口:11211
默认监听地址:127.0.0.1
1. 如果增加 Redis 最大内存大小?
```
vi /usr/local/redis/etc/redis.conf
maxmemory 1024000000
service redis-server restart
#单位字节,默认 1G,可调整
#重启生效
```
2. 如果增加 Memcached 最大内存大小?
```
vi /etc/init.d/memcached
CACHESIZE=256
#单位 M,默认 256M,可调整
service memcached restart #重启生效
```

3. 更改监听端口
Redis:
```
vi /usr/local/redis/etc/redis.conf
bind 127.0.0.1
#改成 bind 0.0.0.0,保存
service redis-server restart
#重启生效
```
Memcached:
```
vi /etc/init.d/memcached
OPTIONS="-l 127.0.0.1" 改成 OPTIONS="",保存
service memcached restart #重启生效
```
