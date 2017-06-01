# PuTTY
1. 输入远程主机IP或域名,设定端口为22号端口，登录协议选择SSH，点open登录 
https://www.digitalocean.com/community/tutorials/how-to-connect-to-your-droplet-with-ssh?utm_source=Customerio&utm_medium=Email_Internal&utm_campaign=Email_UbuntuDistroNginxWelcome&mkt_tok=eyJpIjoiWmpoaE5Ea3dOVGN4WkRVNSIsInQiOiJhallSOTRkQSs4OGNPMnBpRWwyVWxLZU1na1FuVmxMaDByb2dWWlJGTlR3OTZrMzIwcStPeWdWVDdJeXBNN2RhbTZoVTBiajFvY3V6V3ZPU3NJMFlDMVEra3V1NHJ2VXdxczJpOEVmQjZrUzFrb1ZzZmhnR3FCN0h4emdcL3p6VTgifQ%3D%3D

搜索软件： apt-cache search 软件名关键字，支持模糊查询

# 服务器环境安装
### MySQL
查看当前安装的linux版本：`lsb_release -a`
1. 检查服务器是否已经安装了MySQL：`sudo netstat -tap | grep mysql`
2. 更新源：`apt-get update`
3. 下载安装：`apt-get install mysql-server`
4. 修改/etc/mysql/my.cnf（非conf）：`vim /etc/mysql/my.cnf`
在[client]下追加`default-character-set = utf8`
5. 重启mysql：`sudo service mysql stop` `sudo service mysql start`
修改 MySQL 的管理员密码： sudo mysqladmin -u root password newpassword

### Tomcat
tomcat安装很简单，直接解压tomcat至指定目录，在conf/server.xml中配置相应的接口，启动bin下的startup就可以运行了
 修改sudo vim /etc/init.d/tomcat7
在JDK_DIRS变量最后加入 /usr/lib/jvm/java-8-oracle
重启服务
1. 下载源文件：`wget http://apache.fayea.com/tomcat/tomcat-8/v8.5.12/bin/apache-tomcat-8.5.12.tar.gz`
2. 解压：`$tar -zxvf apache-tomcat-8.5.9.tar.gz`
`mv apache-tomcat-8.5.9.tar.gz tomcat8`
2. `sudo tar -zxf apache-tomcat-8.5.9.tar.gz -C /opt`
3. 配置环境变量：
`vim /etc/profile`：添加以下三行
```
export CATALINA_HOME=/opt/tomcat  
export CLASSPATH=.:$JAVA_HOME/lib:$CATALINA_HOME/lib  
export PATH=$PATH:$CATALINA_HOME/bin  
```

### Nginx
`apt-get install nginx`

启动：`nginx -c /etc/nginx/nginx.conf`或`sudo /etc/init.d/nginx start`
关闭：`nginx -s stop`
重启：`nginx -s reload` `nginx -s reopen`
`service nginx {start|stop|status|restart|reload|configtest|}`

### Java JDK
1. 下载安装
默认JDK 安装：
apt-get update
apt-get install default-jre
apt-get install default-jdk

Oracle JDK 安装：
add-apt-repository ppa:webupd8team/java
apt-get update 
apt-get install oracle-java8-installer

OpenJdk 7安装：
sudo add-apt-repository ppa:openjdk-r/ppa  
sudo apt-get update
sudo apt-get install openjdk-7-jdk  

切换版本：`update-alternatives --config java` 有 2 个候选项可用于替换 java (提供 /usr/bin/java)
`update-alternatives --config javac`

2. 解压缩安装(Oracle JDK)
`vi ~/.bashrc`在最后面加上如下四句
```
export JAVA_HOME=/usr/java/java8
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
```
保存后退出vi 刷新变量使配置立即生效：`source ~/.bashrc`

配置环境变量：`vim /etc/environment`
```
JAVA_HOME="/usr/lib/jvm/java-8-oracle"
```

加载环境变量：`source /etc/environment`
使用命令查看环境变量是否配置成功：`echo $JAVA_HOME`

`wget http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz`
`tar -zxvf jdk-8u101-linux-x64.tar.gz`
`cp -r jdk1.8.0_101 /usr/lib/` 将 “jdk1.8.0_101” 文件夹复制到 “/usr/lib” 目录下
`ls /usr/lib`

# Redis
1. 安装：`apt-get install redis-server`
2. 查看是否运行：`service redis-server status`
3. 进入到/etc/redis/redis.conf将bind-address = 127.0.0.1前面加“#”注释掉，这样就可以允许其他机器远程访问本机redis-server了


# 参考资料
[Ubuntu 下 JDK+Tomcat+MySql 环境的搭建](http://www.cnblogs.com/leisurely/p/4231078.html)
[CentOS 6.4 安装 JAVA + MYSQL + APACHE + TOMCAT 环境](http://blog.csdn.net/huaishuming/article/details/39179665)
[Linux(Ubuntu)下MySQL的安装与配置](http://www.2cto.com/database/201401/273423.html)
[阿里云 Ubuntu 14.04 安装mysql 5.6](http://blog.csdn.net/chenpy/article/details/50344085)
[Ubuntu安装MySQL及遇到的问题解决方案](http://blog.csdn.net/flyfish111222/article/details/52808464)

[Tomcat 安装及其单机多实例部署](http://blog.csdn.net/kefengwang/article/details/54233542)
[在阿里云ubuntu上搭建 tomcat8网页服务器](http://blog.csdn.net/jcq521045349/article/details/53291719?locationNum=3&fps=1)
[Ubuntu安装Tomcat8](http://blog.csdn.net/imzhujun/article/details/53868630)
[Linux下安装Tomcat服务器和部署Web应用](http://www.cnblogs.com/xdp-gacl/p/4097608.html)
[阿里云服务器Ubuntu安装tomcat](http://www.codingyun.com/article/41.html)
[Ubunt安装和配置Tomcat8服务](http://www.linuxidc.com/Linux/2016-11/136959.htm)

[How To Install Java on Ubuntu with Apt-Get](https://www.digitalocean.com/community/tutorials/how-to-install-java-on-ubuntu-with-apt-get)
[ubuntu server 服务器部署（一） jdk 安装配置手记](http://www.cnblogs.com/zfeixiang/p/5910272.html)
[在有openJDK的情况下，安装官方JDK，并改为默认](http://www.cnblogs.com/3dant/archive/2011/09/01/2161754.html)
[Ubuntu Linux下安装Oracle JDK](http://blog.csdn.net/gobitan/article/details/24322561)
[ubuntu服务器上安装与配置java](http://blog.csdn.net/u012115522/article/details/46662435)


# hexo
1. 安装nvm：`curl https://raw.githubusercontent.com/creationix/nvm/master/install.sh | sh`
`curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash`
nvm绑定某个版本为默认：`nvm alias default XXX`
2. 设置nvm环境变量：`export NVM_DIR="$HOME/.nvm"`
`[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh" # This loads nvm`
2. 安装Node.js：`nvm install stable` `nvm install latest`
遇到错误：
```
=> Close and reopen your terminal to start using nvm or run the following to use it now:

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # This loads nvm
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # This loads nvm bash_completion
```
解决办法：
`nvm install stable`--> ow using node v7.7.3 (npm v4.1.2)

3. 安装hexo：`npm install -g hexo-cli`
遇到错误：
```
/root/.nvm/versions/node/v7.7.3/bin/hexo -> /root/.nvm/versions/node/v7.7.3/lib/node_modules/hexo-cli/bin/hexo

> dtrace-provider@0.8.1 install /root/.nvm/versions/node/v7.7.3/lib/node_modules/hexo-cli/node_modules/dtrace-provider
> node scripts/install.js

sh: 1: node: Permission denied

> hexo-util@0.6.0 postinstall /root/.nvm/versions/node/v7.7.3/lib/node_modules/hexo-cli/node_modules/hexo-util
> npm run build:highlight

sh: 1: npm: Permission denied
/root/.nvm/versions/node/v7.7.3/lib
└── (empty)

npm WARN optional SKIPPING OPTIONAL DEPENDENCY: fsevents@^1.0.0 (node_modules/hexo-cli/node_modules/chokidar/node_modules/fsevents):
npm WARN notsup SKIPPING OPTIONAL DEPENDENCY: Unsupported platform for fsevents@1.1.1: wanted {"os":"darwin","arch":"any"} (current: {"os":"linux","arch":"x64"})
npm WARN optional SKIPPING OPTIONAL DEPENDENCY: dtrace-provider@0.8.1 (node_modules/hexo-cli/node_modules/dtrace-provider):
npm WARN optional SKIPPING OPTIONAL DEPENDENCY: dtrace-provider@0.8.1 install: `node scripts/install.js`
npm WARN optional SKIPPING OPTIONAL DEPENDENCY: spawn ENOENT
npm ERR! Linux 4.4.0-66-generic
npm ERR! argv "/root/.nvm/versions/node/v7.7.3/bin/node" "/root/.nvm/versions/node/v7.7.3/bin/npm" "install" "-g" "hexo-cli"
npm ERR! node v7.7.3
npm ERR! npm  v4.1.2
npm ERR! file sh
npm ERR! code ELIFECYCLE
npm ERR! errno ENOENT
npm ERR! syscall spawn

npm ERR! hexo-util@0.6.0 postinstall: `npm run build:highlight`
npm ERR! spawn ENOENT
npm ERR! 
npm ERR! Failed at the hexo-util@0.6.0 postinstall script 'npm run build:highlight'.
npm ERR! Make sure you have the latest version of node.js and npm installed.
npm ERR! If you do, this is most likely a problem with the hexo-util package,
npm ERR! not with npm itself.
npm ERR! Tell the author that this fails on your system:
npm ERR!     npm run build:highlight
npm ERR! You can get information on how to open an issue for this project with:
npm ERR!     npm bugs hexo-util
npm ERR! Or if that isn't available, you can get their info via:
npm ERR!     npm owner ls hexo-util
npm ERR! There is likely additional logging output above.

npm ERR! Please include the following file with any support request:
npm ERR!     /npm-debug.log
```
解决办法：
`wget --no-check-certificate https://raw.github.com/creationix/nvm/master/install.sh`
`chmod +x install.sh`
`./install.sh` --> => nvm is already installed in /root/.nvm, trying to update using git
`nvm install 0.10` --> 
`npm config set user 0`
`npm config set unsafe-perm true`
`rm -rf install.sh`
参考：`http://www.cnblogs.com/lidonghao/p/3543747.html` `https://segmentfault.com/q/1010000006038485` `http://www.07net01.com/2015/04/825142.html` `https://www.liquidweb.com/kb/how-to-install-nvm-node-version-manager-for-node-js-on-ubuntu-12-04-lts/`



# FileZilla
Client:客户端(Windows本地电脑)
Server:服务端(Linux服务器)

开启ssh：`service sshd start`