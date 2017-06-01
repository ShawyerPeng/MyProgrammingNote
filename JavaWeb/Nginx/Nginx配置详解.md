![Structure blocks](https://www.packtpub.com/graphics/9781785280337/graphics/0337OS_03_01.jpg)

# 组成
`ngx_core_module`
`ngx_http_core_module`
`ngx_mail_core_module`
`ngx_http_proxy_module`

# Global configuration
Global configuration directives | Explanation
--- | ---
`user` | 定义工作进程使用的user和group身份。如果省略group，nginx会使用与user相同的组名。
`worker_processes` | 定义工作进程的数量。可将它设置为等于可用的CPU核数。
`error_log` | 设置存放日志的文件名及日志级别。(debug, info, notice, warn, error, crit, alert, emerg)
`pid` | 定义存储nginx主进程ID的file。
`use` | 指定使用的连接处理method(方式)。通常不需要明确设置，因为nginx默认会使用最高效的方法。
`worker_connections` | 设置每个工作进程可以打开的最大并发连接数。记住，这个数量包含所有连接。
`include` | 将另一个file，或者匹配指定mask的文件，包含到配置中。被包含的文件应由语法正确的指令和块组成。

# HTTP server section
## Client directives
The HTTP client directives | Explanation
--- | ---
`chunked_transfer_encoding` | 允许返回客户端时禁止standard HTTP/1.1 chunked transfer encoding
`client_body_buffer_size` | 指定连接请求使用的缓冲区大小。如果连接请求超过缓存区指定的值，那么这些请求或部分请求将尝试写入一个临时文件
`client_body_in_file_only` | 指定是否将用户请求体存储到一个文件里，但是请求结束后该文件不会被删除
`client_body_in_single_buffer` | 指定将一个完整的连接请求放入缓冲区，当使用$request_body时推荐使用这个指令以减少复制操作
`client_body_temp_path` | 设置存储用户请求体的文件的目录路径。如果客户端POST一个比较大的文件，长度超过了nginx缓冲区的大小，需要把这个文件的部分或者全部内容暂存到此处
`client_body_timeout` | 设置用户请求体的超时时间。如果这个时间后用户什么都没发，将返回requests time out 408
`client_header_buffer_size` | 指定客户端请求的http头部缓冲区大小
`large_client_header_buffers` | 指令指定客户端请求的一些比较大的头文件到缓冲区的最大值，如果一个请求的URI大小超过这个值，服务器将返回一个"Request URI too large" (414)，同样，如果一个请求的头部字段大于这个值，服务器将返回"Bad request" (400)。
`client_header_timeout` | 设置用户请求头的超时时间
`client_max_body_size` | 设置所能接收的最大请求体的大小即客户端请求Header头信息中设置的Content-Length的最大值，如果超过该指令设置的最大值.Nginx将返回"Request Entity Too Large 413"。如果Nginx服务器提供上传1MB以上的大文件等操作,则要加大该值
`keepalive_disable` | 禁用某些用户带来的keepalive功能
`keepalive_requests` | 设置一个keep-alive连接使用的次数.一次请求结束后,如果该连接使用的次数没有超过keepalive_requests指令设置的请求次数,则服务器并不立即主动断开连接,而是直到达到keepalive_timeout指令设置的时间,才关闭连接
`keepalive_timeout` | 一个http产生的tcp连接在传送完最后一个响应后，还需要hold住keepalive_timeout秒后，才开始关闭这个连接
`msie_padding` | 允许禁止添加评论
`msie_refresh` | 允许给MSIE clients发送refresh而不是redirect

## File I/O directives
HTTP file I/O directives | Explanation
--- | ---
`aio` | 开启aio异步文件上传机制
`sendfile` | sendfile系统调用，开启高效文件传输模式，sendfile指令指定nginx是否调用sendfile函数来输出文件，对于普通应用设为 on，如果用来进行下载等应用磁盘IO重负载应用，可设置为off，以平衡磁盘与网络I/O处理速度，降低系统的负载。注意：如果图片显示不正常把这个改成off。
`directio` | 直接I/O，写请求不做缓存，直接刷到磁盘上。慢，可靠性高
`directio_alignment` |
`open_file_cache_valid 60s` | 
`open_file_cache max=5000 inactive=20s` | 打开文件缓存
`open_file_cache_errors on` | 缓存打开文件的错误信息，是否缓存查找时发生错误时的文件相关信息
`open_file_cache_min_uses 5` | 不被淘汰的最小访问次数，缓存项在非活动期限内最少应该被访问的次数，才可以称为活动项
`open_file_cache_valid` | 检验缓存中元素有效性的频率，每隔多久检查一次缓存中缓存项的有效性
`postpone_output` |
`read_ahead` |
`sendfile_max_chunk` | 每次发送大小为xxxB的块数据

## Hash directives
`server_names_hash_bucket_size` | 服务器名字的hash表大小
`server_names_hash_max_size` |
`types_hash_bucket_size` |
`types_hash_max_size` |
`variables_hash_bucket_size` |
`variables_hash_max_size` |

## Socket directives
`lingering_close` | 用于在请求完成后，接收客户端发送的额外数据并全部忽略掉，防止因关闭连接时接收缓冲区中仍有数据而停止发送响应并返回RST
`lingering_time` | 处理总时间
`lingering_timeout` | 等待额外数据的时间
`reset_timedout_connection` | 在客户端停止响应之后,允许服务器关闭连接,释放socket关联的内存
`send_lowat` | 当设置为非零值时，nginx就会试图减少对客户端socket的发送操作，默认设置为零
`send_timeout` | 设置服务端传送回应包时的超时时间，针对两个连续的写操作，而不是整个回应过程的超时设置，默认设置60s
`tcp_nodelay` | 禁止nagle算法,有需要发送的就立即发送。只有在长连接的时候才启用
`tcp_nopush` | 开启或者关闭nginx在FreeBSD上使用TCP_NOPUSH套接字选项， 在Linux上使用TCP_CORK套接字选项。 选项仅在使用sendfile的时候才开启。一次性发送整个文件

# virtual server section
The listen parameters | Explanation | Comments
--- | --- | ---
`default_server` | 定义地址/端口绑定作为请求默认值 | 
`setfib` | 为监听的socket设置对应的FIB | 仅支持on FreeBSD and not for UNIX-domain sockets.
`backlog` | sets the backlog parameter in the listen() call | 默认在FreeBSD-1，其他所有平台为511
`rcvbuf` | 为监听端口指定SO_RCVBUF | 
`sndbuf` | 为监听端口指定SO_SNDBUF | 
`accept_filter` | 指定accept-filter | 仅支持FreeBSD
`deferred` | 设置TCP_DEFER_ACCEPT以便使用延时accept()请求 | 仅支持Linux
`bind` | makes a separate bind() call for this address/port pair | A separate bind() call will be made implicitly if any of the other socket-specific parameters are used.
`ipv6only` | 设置IPV6_ONLY值 | only be set on a fresh start and not for UNIX-domain sockets
`ssl` | 设置在该端口仅有HTTPS连接 | 允许紧凑配置
`so_keepalive` | 为监听的socket配置TCP keepalive连接 | 

# Location





# Mail server section



# Proxy module
The Proxy module directives | Explanation
--- | ---
`proxy_pass` | 设置后端服务器的协议和地址，还可以设置可选的URI以定义本地路径和后端服务器的映射关系
`proxy_redirect` | 允许重写URL appearing in the Location HTTP header on redirections triggered by the backend server.（参数：off/default/URL）
`proxy_method` | 覆盖请求到后端服务器的HTTP方法，比如如果指定为POST，则所有请求到后端的都为POST请求
`proxy_http_version` | 设置代理使用的HTTP协议版本
`proxy_connect_timeout` | nginx跟后端服务器连接超时时间(代理连接超时)
`proxy_read_timeout` | 从后端服务器读取响应的超时
`proxy_send_timeout` | 向后端服务器传输请求的超时
`proxy_next_upstream_timeout` | 与proxy_next_upstream一起用
`proxy_next_upstream_tries` | 与proxy_next_upstream一起用
`proxy_ignore_client_abort` | 决定当客户端在响应传输完成前就关闭连接时，nginx是否应关闭后端连接
`proxy_intercept_errors` | 当后端服务器的响应状态码大于等于400时，决定是否直接将响应发送给客户端，亦或将响应转发给nginx由error_page指令来处理。
`proxy_send_lowat` | 
`proxy_limit_rate` | 
`proxy_set_header` | 允许重新定义或者添加发往后端服务器的请求头
`proxy_set_body` | 允许重新定义或者添加发往后端服务器的请求体
`proxy_hide_header` | 设置额外的响应头，这些响应头不会发送给客户端
`proxy_pass_header` | 设置额外的响应头，这些响应头会发送给客户端（允许传送被屏蔽的后端服务器响应头到客户端）
`proxy_pass_request_headers` | 定义额外的请求头是否应该被传到后端服务器，默认on
`proxy_pass_request_body` | 定义请求体是否应该被传到后端服务器，默认on
`proxy_ignore_headers` | 不处理后端服务器返回的指定响应头
`proxy_headers_hash_bucket_size` |
`proxy_headers_hash_max_size` |
`proxy_store` | 
`proxy_store_access` | 
`proxy_cookie_domain` | 设置“Set-Cookie”响应头中的domain属性的替换文本
`proxy_cookie_path` | 设置“Set-Cookie”响应头中的path属性的替换文本
`proxy_temp_path` | 从后端服务器接收的临时文件的存放路径，可以为临时文件路径定义至多三层子目录的目录树
`proxy_max_temp_file_size` | 打开响应缓冲以后，如果整个响应不能存放在proxy_buffer_size和proxy_buffers指令设置的缓冲区内，部分响应可以存放在临时文件中。 这条指令可以设置临时文件的最大容量
`proxy_temp_file_write_size` | 在开启缓冲后端服务器响应到临时文件的功能后，设置nginx每次写数据到临时文件的size(大小)限制

# Caching, buffering, and temporary files
Directive | Description
--- | ---
`proxy_buffer_size` | 
`proxy_buffering` | 
`proxy_request_buffering` | 
`proxy_buffers` | 
`proxy_busy_buffers_size` | 
`proxy_cache` | 
`proxy_cache_key` | 
`proxy_cache_path` | 
`proxy_cache_methods` | 
`proxy_cache_min_uses` | 
`proxy_cache_valid` | 
`proxy_cache_use_stale` | 

# SSL-related directives
Directive | Description
--- | ---
`proxy_ssl_certificate` | 
`proxy_ssl_certificate_key` | 
`proxy_ssl_ciphers` | 
`proxy_ssl_crl` | 
`proxy_ssl_name` | 
`proxy_ssl_password_file` | 
`proxy_ssl_server_name` | 
`proxy_ssl_session_reuse` | 
`proxy_ssl_protocols` | 
`proxy_ssl_trusted_certificate` | 
`proxy_ssl_verify` | 
`proxy_ssl_verify_depth` | 

# Upstream module
The upstream module directives | Explanation
--- | ---
`ip_hash` | ensures the distribution of connecting clients evenly over all servers by hashing the IP address, keying on its class-C network.
`keepalive` | specifies the number of connections to the upstream servers that are cached per worker process. When used with the HTTP connections, proxy_http_version should be set to 1.1 and proxy_set_header to Connection ""
`least_conn` | activates the load-balancing algorithm where the server with the least number of active connections is chosen for the next new connection.
`server` | defines an address，可选参数：`weight` `max_fails` `fail_timeout` `backup` `down`






