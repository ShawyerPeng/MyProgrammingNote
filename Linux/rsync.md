# rsync
`rsync -av [source] [dest]`
`rsync -av --delete [source] [dest]`
`rsync -av --delete -e ssh [source] root@120.24.213.130:/ROOT/BACKUP/`

# tmux
[Tmux 速成教程：技巧和调整](http://blog.jobbole.com/87584/)
[tmux - Linux终端管理软件 [LinuxCast IT播客]](https://www.youtube.com/watch?v=aUuz6SurqN4)
`tmux list-session`：列出所有tmux终端
`tmux a -t [编号]`：切换终端
`exit`：退出终端
`ctrl+b d`：离开终端
`tmux kill-server`：关闭所有终端
`tmux new-session -s [name]`：为终端命名，便于识别
`ctrl+b "`：横向分屏
`ctrl+b %`：竖向分屏
`ctrl+b`：离开终端，按下方向键可切换终端
`ctrl+b [`：进入粘贴模式，这样就支持上下键

# 网站根目录权限
网站根目录权限遵循:
文件 644, 文件夹 755 ,权限用户和用户组 www
如出现文件权限问题时,请执行下面 3 条命令:
chown
-R www.www /data/wwwroot/
find /data/wwwroot/ -type d -exec chmod 755 {} \;
find /data/wwwroot/ -type f -exec chmod 644 {} \;



# Apache
参考：[Compiling and Installing](http://httpd.apache.org/docs/2.4/install.html)  
1. 下载：http://httpd.apache.org/download.cgi
2. 解压：`gzip -d httpd-2.4.25.tar.gz`
`tar xvf httpd-2.4.25.tar`
3. 进入主目录：`cd httpd-2.4.25`
4. Configure：`./configure [--prefix=PREFIX]`
./configure --with-included-apr --with-apr-util=/usr/local/bin/pcre-config --with-pcre=/usr/local/bin/pcre-config
如果PREFIX没有被指定，会默认安装到/usr/local/apache2  
确保安装了APR和APR-Util，如果没有，去官网 http://apr.apache.org/ 下载，然后解压到/httpd_source_tree_root/srclib/apr和/httpd_source_tree_root/srclib/apr-util（确保没有版本号）
然后还要安装pcre（不是pcre2）官网：http://pcre.org
5. 编译：`make`
6. 安装：`make install`
7. 配置：`nano /usr/local/apache2/conf/httpd.conf`
修改ServerName
8. 测试：`PREFIX/bin/apachectl -k start`  
如果提示
```
/usr/local/apache2/bin/httpd: error while loading shared libraries: libpcre.so.1: cannot open shared object file: No such file or directory
```
则需`cd /usr/local/lib` `ldconfig`
参考：[error while loading shared libraries: xxx.so.x" 错误的原因和解决办法](http://www.cnblogs.com/Anker/p/3209876.html)


# 配置
```
# 服务器主目录
ServerRoot "/usr/local/apache2"

Mutex default:logs

# Listen 12.34.56.78:80，如果前面不加ip地址则在服务器所有的ip地址上监听80端口
Listen 80

# Dynamic Shared Object (DSO) Support，扩展功能加载
LoadModule authn_file_module modules/mod_authn_file.so

# 父进程、子进程
<IfModule unixd_module>
    User daemon
    Group daemon
</IfModule>

# 错误信息发送的管理员的邮箱地址
ServerAdmin you@example.com

# 服务器的域名
ServerName localhost:80

# 访问控制
<Directory />
    AllowOverride none
    Require all denied
</Directory>

DocumentRoot "/usr/local/apache2/htdocs"
<Directory "/usr/local/apache2/htdocs">
    Options Indexes FollowSymLinks
    AllowOverride None
    Require all granted
</Directory>

# 默认首页
<IfModule dir_module>
    DirectoryIndex index.html
</IfModule>

# 对文件访问进行控制
<Files ".ht*">
    Require all denied
</Files>

# 日志信息设置
ErrorLog "logs/error_log"
LogLevel warn
<IfModule log_config_module>
    #
    # The following directives define some format nicknames for use with
    # a CustomLog directive (see below).
    #
    LogFormat "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\"" co$
    LogFormat "%h %l %u %t \"%r\" %>s %b" common

    <IfModule logio_module>
      # You need to enable mod_logio.c to use %I and %O
      LogFormat "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\" %$
    </IfModule>

    CustomLog "logs/access_log" common
    #CustomLog "logs/access_log" combined
</IfModule>

<IfModule alias_module>
</IfModule>

<IfModule cgid_module>
    #
    # ScriptSock: On threaded servers, designate the path to the UNIX
    # socket used to communicate with the CGI daemon of mod_cgid.
    #
    #Scriptsock cgisock
</IfModule>

<Directory "/usr/local/apache2/cgi-bin">
    AllowOverride None
    Options None
    Require all granted
</Directory>

<IfModule headers_module>
    #
    # Avoid passing HTTP_PROXY environment to CGI's on this or any proxied
    # backend servers which have lingering "httpoxy" defects.
    # 'Proxy' request header is undefined by the IETF, not listed by IANA
    #
    RequestHeader unset Proxy early
</IfModule>

<IfModule mime_module>
    TypesConfig conf/mime.types
    #AddType application/x-gzip .tgz
</IfModule>

#MIMEMagicFile conf/magic

#ErrorDocument 500 "The server made a boo boo."
#ErrorDocument 404 /missing.html
#ErrorDocument 404 "/cgi-bin/missing_handler.pl"
#ErrorDocument 402 http://www.example.com/subscription_info.html

#MaxRanges unlimited

#EnableMMAP off
#EnableSendfile on

# Supplemental configuration
#
# The configuration files in the conf/extra/ directory can be
# included to add extra features or to modify the default configuration of
# the server, or you may simply copy their contents here and change as
# necessary.

# Server-pool management (MPM specific)
#Include conf/extra/httpd-mpm.conf

# Fancy directory listings
#Include conf/extra/httpd-autoindex.conf

# Language settings
#Include conf/extra/httpd-languages.conf

# User home directories
#Include conf/extra/httpd-userdir.conf

# Real-time info on requests and configuration
#Include conf/extra/httpd-info.conf

# Virtual hosts
#Include conf/extra/httpd-vhosts.conf

# Local access to the Apache HTTP Server Manual
#Include conf/extra/httpd-manual.conf

# Distributed authoring and versioning (WebDAV)
#Include conf/extra/httpd-dav.conf

# Various default settings
#Include conf/extra/httpd-default.conf

# Configure mod_proxy_html to understand HTML4/XHTML1
<IfModule proxy_html_module>
Include conf/extra/proxy-html.conf
</IfModule>

# Secure (SSL/TLS) connections
#Include conf/extra/httpd-ssl.conf
#
# Note: The following must must be present to support
#       starting without SSL on platforms with no /dev/random equivalent
#       but a statically compiled-in mod_ssl.
#
<IfModule ssl_module>
SSLRandomSeed startup builtin
SSLRandomSeed connect builtin
</IfModule>
```
