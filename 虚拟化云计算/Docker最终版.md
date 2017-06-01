# 安装
```
sudo apt-get install apt-transport-https ca-certificates curl software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu xenial stable"

sudo apt-get update

sudo apt-get install docker-ce
# sudo apt-get install docker-ce=<VERSION>

sudo docker run hello-world
```

# 下载
```
sudo apt-get purge docker-ce

sudo rm -rf /var/lib/docker
```

# non-root user
1. 新建docker组：`sudo groupadd docker`
2. 把用户添加到docker组：`sudo usermod -aG docker $USER`

```
Unable to find image 'hello-world:latest' locally
latest: Pulling from library/hello-world
78445dd45222: Pull complete 
Digest: sha256:c5515758d4c5e1e838e9cd307f6c6a0d620b5e07e6f927b07d05f6d12a1ac8d7
Status: Downloaded newer image for hello-world:latest
```
当我们执行docker run的时候我们告诉Docker启动一个容器，一个基于名为nginx镜像的容器。因为Docker正在启动一个基于特定镜像的容器，它需要首先找到这个镜像。在检查远端的仓库之前，Docker首先检查是否本地已经存在有一个该特定名称的镜像。

因为我们的系统是全新的，没有一个名为nginx的Docker镜像，这意味着Docker需要在Docker仓库里面下载：

# 开机启动
`sudo systemctl enable docker`

# 命令
`docker version`：查看版本信息
`docker info`：查看Docker系统信息，包括镜像和容器数
`docker search xxx`：从Docker Hub中搜索镜像（--automated 只列出 automated build，--no-trunc 可显示完整的镜像描述，-s 40 列出收藏数不小于40的镜像）
`docker push`：将镜像推送至远程仓库
`docker pull xxx`：拉取镜像
`docker login`：登录到Docker Hub
`docker logout`：注销登录
`docker events`：从服务器拉取个人动态，可选择时间区间

`docker ps`：查看运行中的容器（-a 列出所有容器，-l 仅列出最新创建的一个容器，--no-trunc 显示完整的容器ID，-n=4 列出最近创建的4个容器，-q 仅列出容器ID，-s 显示容器大小）
`docker images`：列出所有镜像（-a 列出所有镜像，-f 过滤镜像，--no-trunc 可显示完整的镜像ID，-q 仅列出镜像ID，--tree 以树状结构列出镜像的所有提交历史）
`docker history xxx`：查看指定镜像的创建历史

`docker start|stop|restart`：启动、停止和重启一个或多个指定容器
`docker kill xxx`：停止镜像
`docker rm xxx`：移除容器

`docker commit mycentos my/os:latest`：提交镜像
`docker tag`：标记本地镜像，将其归入某一仓库
`docker tag image_name new_image_name`：重命名image
`docker rename nginx nginx_1`：container重命名


`docker run registry`：部署仓库
`docker run -it centos /bin/echo haha`：-it表示前端运行
`docker run -dt -p 80:80 --name mycentos my/os`：后端运行并映射80端口
`docker run -it --name testecho centos /bin/echo haha`：创建container
`docker run --rm -it centos /bin/echo haha`：不创建container运行docker


`docker save -i "xxx.tar"`：将指定镜像保存成tar归档文件，docker load的逆操作
`docker load -i "xxx.tar"`：从tar镜像归档中载入镜像，docker save的逆操作
`docker export`：将指定的容器保存成 tar 归档文件， docker import 的逆操作
`docker import`：从归档文件（支持远程文件）创建一个镜像， export 的逆操作


`docker top`：查看一个正在运行容器进程，支持 ps 命令参数
`docker inspect`：查看image或container的底层信息，检查镜像或者容器的参数，默认返回 JSON 格式
`docker pause`：暂停某一容器的所有进程
`docker unpause`：恢复某一容器的所有进程

`docker logs`：获取容器运行时的输出日志
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：

# Dockerfile
```
# Use an official Python runtime as a base image
FROM python:2.7-slim

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app

# Install any needed packages specified in requirements.txt
RUN pip install -r requirements.txt

# Make port 80 available to the world outside this container
EXPOSE 80

# Define environment variable
ENV NAME World

# Run app.py when the container launches
CMD ["python", "app.py"]
```


# 参考资料
[Dockerfile reference](https://docs.docker.com/engine/reference/builder/)  
[Best practices for writing Dockerfiles](https://docs.docker.com/engine/userguide/eng-image/dockerfile_best-practices/)  
[Docker Resources All In One](https://github.com/hangyan/docker-resources/blob/master/README_zh.md)  

# troubleshooting
[国内 docker 仓库镜像对比](http://www.datastart.cn/tech/2016/09/28/docker-mirror.html)  
https://www.daocloud.io/mirror
