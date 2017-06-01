# Docker安装
### 1. 安装前的检查
通过`uname -i`，如果是x86_64则为64位系统。确认Linux内核版本>=3.10，通过`uname -r`可以查看到内核版本信息

### 2. 从官方仓库安装Docker稳定版
1. 更新仓库：`sudo apt-get update`
2. 安装Docker：`sudo apt-get install docker.io`
3. 检查是否安装成功：`ps axf | grep docker`
4. 查看版本：`sudo docker version`

### 3. 从私有仓库安装Docker最新版（推荐）
1. 将Docker私有仓库添加到apt-get源中：  
`sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9`
2. 将Docker私有仓库添加到apt-get源中：  
`sudo sh -c "echo deb https://get.docker.io/ubuntu docker main  > /etc/apt/sources.list.d/docker.list"`
3. 更新仓库：`sudo apt-get update`
4. 安装Docker：`sudo apt-get install lxc-docker`
5. 检查是否安装成功：`ps axf | grep docker`
6. 查看版本：`sudo docker version`
7. 找到安装路径：`which docker`

### 4. 启动和停止Docker Server
启动：`sudo service docker start`  
关闭：`sudo service docker stop`  
重启：`sudo service docker restart`



# Docker镜像
###
通过docker images指令查看本地有哪些镜像

镜像ID：唯一。长度为64个字符，通常只使用前12个就可以了。
镜像TAG：每个镜像上可以打上多个TAG。
镜像Repository：每个镜像存储在一个仓库中。

Repository:TAG唯一标识了一个镜像

镜像和镜像仓库存储在Registry中
本地Registry：通过docker images查看的就是本地Registry。
官方Registry：Docker官方维护了一个Registry，里面存储了各种各样的镜像。


### 从官方Registry拉取镜像
从官方仓库中获取Redis镜像：`docker pull redis:2.8.19`
如果速度很慢，可尝试daocloud提供的加速器服务：https://dashboard.daocloud.io/mirror


### 构建镜像
1. 通过容器构建：`docker commit`  
`docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]`
-a：作者信息
-m：commit message
-p：在commit时暂停容器
2. 通过Dockerfile文件构建：`docker build`
创建存放Dockerfile文件的目录：`mkdir -p dockerfile/df_test`
`cd dockerfile/df_test`
`docker build [OPTIONS] PATH | URL | -`

### 1. 创建容器A
`sudo docker run -it Repository:TAG /bin/bash`
`cd /tmp`
`echo hello > hello.txt`
`cat hello.txt`

### 2. 创建容器B
`sudo docker run -it Repository:TAG /bin/bash`
`cd /tmp`
`echo world > world.txt`
`cat world.txt`







# Docker容器
### 1. 运行容器
`docker run`
选项：
-i -t：经常一起使用，缩写为-it，用于创建交互式容器
-d：让容器运行在后台，用于创建守护式容器
--name：为容器指定一个名称
容器运行成功后，会返回一个64字符的容器ID，作为容器的唯一标识。类似于镜像ID，容器ID也可以采用简写形式。

### 查看容器基本信息
`docker ps`
查看容器的基本信息，包括容器ID、命令、状态等。

### 查看容器详细信息
`docker inspect [ID]`
查看容器的详细信息，返回丰富的JSON格式信息。
`docker inspect -f`
指定查看某种信息，例如查看容器的IP地址：`sudo docker inspect -f '{{.NetworkingSettings.IPAddress}}' [ID]`

### 停止守护式容器
`docker stop [ID]`

### 删除容器
`docker rm [ID]`


### 配置国内镜像仓库
1. 修改配置文件
/etc/default/docker

2. 修改Daemon

3. 使用公共仓库

4. 使用私有仓库