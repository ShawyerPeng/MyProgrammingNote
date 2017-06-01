```
deb http://mirrors.aliyuncs.com/ubuntu/ xenial main restricted universe multiverse
deb http://mirrors.aliyuncs.com/ubuntu/ xenial-security main restricted universe multiverse
deb http://mirrors.aliyuncs.com/ubuntu/ xenial-updates main restricted universe multiverse
deb http://mirrors.aliyuncs.com/ubuntu/ xenial-proposed main restricted universe multiverse
deb http://mirrors.aliyuncs.com/ubuntu/ xenial-backports main restricted universe multiverse
deb-src http://mirrors.aliyuncs.com/ubuntu/ xenial main restricted universe multiverse
deb-src http://mirrors.aliyuncs.com/ubuntu/ xenial-security main restricted universe multiverse
deb-src http://mirrors.aliyuncs.com/ubuntu/ xenial-updates main restricted universe multiverse
deb-src http://mirrors.aliyuncs.com/ubuntu/ xenial-proposed main restricted universe multiverse
deb-src http://mirrors.aliyuncs.com/ubuntu/ xenial-backports main restricted universe multiverse
```


1. 添加源的加密公钥：`wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc | sudo apt-key add -`
2. 更新源：`sudo apt-get update`
3. 安装：`sudo apt-get install rabbitmq-server`

[Ubuntu安装RabbitMQ](http://www.jianshu.com/p/2d4b81c8b403)