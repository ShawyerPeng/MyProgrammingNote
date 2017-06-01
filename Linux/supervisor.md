# 介绍
可以将一个普通命令进程变为后台daemon（守护进程）并监控进程状态，异常退出时候能自动重启。

# 安装
`apt-get install supervisor`或`pip install supervisor`或`easy_install supervisor`
使用配置文件模板：`echo_supervisord_conf > /etc/supervisord.conf`

# 组成
`supervisord`：是后台管理服务器，用来依据配置文件的策略管理后台守护进程，会跟随系统启动而启动
`supervisorctl`：用于管理员向后台管理程序发送 启动/重启/停止等指令

# 命令
启动：`supervisord -c /etc/supervisord.conf`或`service supervisor start`
启动进程：`supervisorctl start app`
停止进程：`supervisorctl stop app`
重载进程：`supervisorctl reload app`
查看各进程状态：`supervisorctl status`
如果修改了配置文件，需要重新载入：`supervisorctl reload`

# 为应用程序编写配置文件
每个进程的配置文件都可以单独分拆，放在`/etc/supervisor/conf.d/`目录下，以.conf作为扩展名，
例如，app.conf定义了一个gunicorn的进程：
`/etc/supervisor/conf.d/app.conf`
```
[program:app]
# 运行目录下执行命令
command = python /data/shadowsocks/server.py -c /data/shadowsocks/config.json
process_name=%(program_name)s   # 进程名
directory=/srv/www              # 指定运行目录
user=root                       # 以root用户执行
umask=022                       # 掩码:--- -w- -w-, 转换后rwx r-x w-x
priority=999                    # 优先级,值越高,最后启动,最先被关闭,默认值999优先级
numprocs=1                      # 进程数
autostart=true                  # 当supervisord启动时自动启动
autorestart=true                # 自动重启

startsecs=10                    # 启动延时执行,默认1秒
startretries=3                  # 启动尝试次数,默认3次

exitcodes=0,2                   # 当退出码是0,2时,执行重启,默认值0,2
stopsignal=TERM                 # 停止信号,默认TERM。中断:INT 终止:TERM 挂起:HUP 从容停止:QUIT
stopwaitsecs=10
stopasgroup=false
killasgroup=false

redirect_stderr=false           # 重定向，为true表示禁止监听错误
# 输出日志
stdout_logfile=/var/log/shadowsocks_error.log
stdout_logfile_maxbytes=1MB
stdout_logfile_backups=10
stdout_capture_maxbytes=1MB
stdout_events_enabled=false
# 错误日志
stderr_logfile=/var/log/shadowsocks.log
stderr_logfile_maxbytes=1MB
stderr_logfile_backups=10
stderr_capture_maxbytes=1MB
stderr_events_enabled=false

environment=A="1",B="2"         # 环境变量设置
serverurl=AUTO
```

`/etc/supervisor/supervisord.conf`
```
[unix_http_server]
file=/var/run/supervisor.sock   ; UNIX socket 文件，监听HTTP/XML-RPC请求，supervisorctl 会使用
chmod=0700                      ; socket 文件的 mode，默认是 0700
;chown=nobody:nogroup       ; socket 文件的 owner，格式： uid:gid

;[inet_http_server]         ; HTTP 服务器，提供 web 管理界面
;port=127.0.0.1:9001        ; Web 管理后台运行的 IP 和端口，如果开放到公网，需要注意安全性
;username=user              ; 登录管理后台的用户名
;password=123               ; 登录管理后台的密码

[supervisord]
logfile=/var/log/supervisor/supervisord.log ; 主日志文件;默认为$CWD/supervisord.log
logfile_maxbytes=50MB   ; 日志文件大小，超出会rotate
logfile_backups=10 ;    ; 日志保留备份数量
loglevel=info           ; 日志级别，可以是critical, error, warn, info, debug, trace, blather
pidfile=/var/run/supervisord.pid ; pid 文件
umask=022 ;             ; 掩码:--- -w- -w-, 转换后rwx r-x w-x
nodaemon=false          ; 是否在前台启动，默认是 false，即以 daemon 的方式启动
minfds=1024 ;           ; 可以打开的文件描述符的最小值，默认 1024
minprocs=200 ;          ; 可以打开的进程数的最小值，默认 200
nocleanup=false ;
childlogdir=/var/log/supervisor            ; ('AUTO' child log dir, default $TEMP)

[rpcinterface:supervisor]
supervisor.rpcinterface_factory = supervisor.rpcinterface:make_main_rpcinterface

[supervisorctl]
serverurl=unix:///var/run/supervisor.sock   ; 通过UNIX socket连接supervisord
;serverurl=http://127.0.0.1:9001            ; 通过 HTTP 的方式连接 supervisord


[include]               ; 包含其他的配置文件
files = /etc/supervisor/conf.d/*.conf       ; 可以是*.conf或*.ini
```



## 开机自动启动Supervisord
```
vi /etc/rc.local

# 在exit前添加以下内容
supervisord -c /etc/supervisord.conf
```


# 参考资料
[Supervisor](http://supervisord.org/index.html)  
[supervisor(一)基础篇](http://lixcto.blog.51cto.com/4834175/1539136)  
[Supervisor学习](http://beginman.cn/linux/2015/04/06/Supervisor/)  
