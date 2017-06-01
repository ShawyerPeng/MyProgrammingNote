# 简介
Mencached是一个自由开源的、高性能、分布式内存对象缓存系统。

Memcached是一种基于key—value键值对存储，用来存储小块的任意数据（字符串、对象）。这些数据可以是数据库调用、API调用或者页面渲染的结果。

本质上，它是一个简洁的key-value存储系统。

# 原理
当应用服务器首次访问数据时，首先从数据库中查询数据，从数据库中查询出来的数据，返回给应用服务器，同时也会存储到memcached中，下次查询数据的时候先从memcached进行查询，如果缓存中有想要的数据，则直接从中取数据，如果没有想要的数据再从数据库中查询。这样极大地节省了数据查询的时间，特别是应用系统数据量比较大的时候，提高了系统的查询效率。

![](http://upload-images.jianshu.io/upload_images/524615-382306f758f38e48.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 操作
```
-p <num>      监听的TCP端口号 (默认: 11211)
-U <num>      监听的UDP端口号 (默认: 11211, 0 is off)
-s <file>     UNIX socket path to listen on (disables network support)
-A            enable ascii "shutdown" command
-a <mask>     access mask for UNIX socket, in octal (default: 0700)
-l <addr>     连接的IP地址 (default: INADDR_ANY, all addresses)
-d            以守护进程方式运行
-r            maximize core file limit
-u <username> 以username的身份运行 (仅在以root运行的时候有效)
-m <num>      最大内存使用 (默认: 64 MB)
-M            内存耗尽时返回错误，而不是删除项
-c <num>      最大同时连接数 (默认: 1024)
-k            lock down all paged memory.  Note that there is a
              limit on how much memory you may lock.  Trying to
              allocate more than that would fail, so be sure you
              set the limit correctly for the user you started
              the daemon with (not for -u <username> user;
              under sh this is done with 'ulimit -S -l NUM_KB').
-v            输出警告和错误信息
-vv           打印客户端的请求和返回信息
-vvv          extremely verbose (also print internal state transitions)
-h            显示帮助
-i            显示memcached和libevent的license
-V            显示版本
-P <file>     设置保存Memcache的pid文件。save PID in <file>, only used with -d option
-f <factor>   块大小增长因子 (默认: 1.25)
-n <bytes>    minimum space allocated for key+value+flags (default: 48)
-L            Try to use large memory pages (if available). Increasing
              the memory page size could reduce the number of TLB misses
              and improve the performance. In order to get large pages
              from the OS, memcached will allocate the total item-cache
              in one large chunk.
-D <char>     Use <char> as the delimiter between key prefixes and IDs.
              This is used for per-prefix stats reporting. The default is
              ":" (colon). If this option is specified, stats collection
              is turned on automatically; if not, then it may be turned on
              by sending the "stats detail on" command to the server.
-t <num>      线程数 (默认: 4)
-R            Maximum number of requests per event, limits the number of
              requests process for a given connection to prevent
              starvation (default: 20)
-C            关闭CAS的使用
-b <num>      Set the backlog queue limit (default: 1024)
-B            Binding protocol - one of ascii, binary, or auto (default)
-I            Override the size of each slab page. Adjusts max item size
              (default: 1mb, min: 1k, max: 128m)
-S            Turn on Sasl authentication
-F            Disable flush_all command
-o            Comma separated list of extended or experimental options
```

启动：`systemctl start memcached`
连接：`telnet localhost 11211`

命令：
统计类：stats, stats items, stats slabs, stats sizes
存储类：set, add, replace, append, prepend
命令格式：<command name> <key> <flags> <exptime> <bytes>  <cas unique>
检索类：get, delete, incr/decr
清空：flush_all

示例：
telnet> add KEY <flags> <expiretime> <bytes> \r
telnet> VALUE

Memecached状态监控
```
pid: memcached服务进程的进程ID
uptime: memcached服务从启动到当前所经过的时间，单位是秒。
time: memcached服务器所在主机当前系统的时间，单位是秒。
version: memcached组件的版本。
libevent:当前使用的libevent的版本
pointer_size：服务器所在主机操作系统的指针大小，一般为32或64.
curr_items：表示当前缓存中存放的所有缓存对象的数量。
total_items：表示从memcached服务启动到当前时间，系统存储过的所有对象的数量，包括目前已经从缓存中删除的对象。
bytes：表示系统存储缓存对象所使用的存储空间，单位为字节。
curr_connections：表示当前系统打开的连接数。
total_connections：表示从memcached服务启动到当前时间，系统打开过的连接的总数。
connection_structures：表示从memcached服务启动到当前时间，被服务器分配的连接结构的数量，这个解释是协议文档给的，具体什么意思，我目前还没搞明白。
cmd_get：累积获取数据的数量，这里是3，因为我测试过3次，第一次因为没有序列化对象，所以获取数据失败，是null，后边有2次是我用不同对象测试了2次。
cmd_set：累积保存数据的树立数量，这里是2.虽然我存储了3次，但是第一次因为没有序列化，所以没有保存到缓存，也就没有记录。
get_hits：表示获取数据成功的次数。
get_misses：表示获取数据失败的次数。
evictions：为了给新的数据项目释放空间，从缓存移除的缓存对象的数目。比如超过缓存大小时根据LRU算法移除的对象，以及过期的对象。
bytes_read：memcached服务器从网络读取的总的字节数。
bytes_written：memcached服务器发送到网络的总的字节数。
limit_maxbytes：memcached服务缓存允许使用的最大字节数。这里为67108864字节，也就是是64M.与我们启动memcached服务设置的大小一致。
threads：被请求的工作线程的总数量
```

# 命令
## set命令和get命令
`set KEY <flags> <expiretime> <bytes>`：可以用来存储除了键值对外的额外信息、表示可以存放900秒(0表示永远)、字节数
`memcached`：表示value值，在第二行   STORED：表示存储成功。当存储失败的时候，显示ERROR

## delete命令
删除一个存在的key
`delete foo`
`DELETE`

## state命令
`state`:用于返回统计信息例如 PID(进程号)、版本号、连接数等

memcached的add、set和replace方法的区别：
方法 | 当key值存在时 | 当key值不存在时
add	| false	| true
replace | true | false
set | true | true

# 结合Spring
[Memcached结合Spring](http://www.jianshu.com/p/56d9d79d75b3)  


# 资料
[Memcached服务器安装、配置、使用详解](https://yq.aliyun.com/articles/27768)  
[Memcached · 最佳实践 · 热点 Key 问题解决方案](https://yq.aliyun.com/articles/51181)  
