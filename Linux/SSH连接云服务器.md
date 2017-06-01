1. 若本机没有id_rsa秘钥，需生成：`ssh-keygen -t rsa`
2. 将本机生成的公钥发送到服务器上（建立信任关系）：`ssh-copy-id -i C:/Users/UserName/.ssh/id_rsa.pub root@server_ip`
```
/usr/bin/ssh-copy-id: INFO: Source of key(s) to be installed: "C:/Users/PatrickYates/.ssh/id_rsa.pub"
The authenticity of host '120.24.213.130 (120.24.213.130)' can't be established.
ECDSA key fingerprint is SHA256:ywXkxup3IBFFyIjIM+prMxWQrzjKAuvkbkGTSTjKwTc.
Are you sure you want to continue connecting (yes/no)? yes
/usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed
/usr/bin/ssh-copy-id: INFO: 1 key(s) remain to be installed -- if you are prompted now it is to install the new keys
root@120.24.213.130's password:

Number of key(s) added: 1

Now try logging into the machine, with:   "ssh 'root@120.24.213.130'"
and check to make sure that only the key(s) you wanted were added.
```

3. ssh远程登录：`ssh root@server_ip`

参考：[《ssh-copy-id帮你建立信任》–linux命令五分钟系列之四十一](http://roclinux.cn/?p=2551)
[SSH如何通过公钥连接云服务器](http://blog.csdn.net/u014743697/article/details/52678916)


# 新增用户并添加权限
1. `adduser git`
2. 编辑配置文件，加入git到sudo用户组：`nano /etc/sudoers`
```
# User privilege specification
root    ALL=(ALL:ALL) ALL
git     ALL=(ALL:ALL) ALL   # 新增这一行
```
3. 切换用户：`su git`
4. 本机新建rsa key：`ssh-keygen -t rsa`
5. 本机：`cat .ssh/id_rsa.pub`
6. 服务器新建authorized_keys文件(/home/git/.ssh/authorized_keys)：`cat /home/git/.ssh/id_rsa.pub >> authorized_keys`
7. 编辑authorized_keys，加入本机rsa-pub公钥，建立信任关系：`nano /home/git/.ssh/authorized_keys`

8. 进入用户home目录：`cd /home/git`
9. 新建blog目录：`mkdir blog`
10. 初始化仓库：`git init --bare project.git`
11. 本机clone服务器git项目：`git clone git@git.jjhh.com:/data/git/project.git`
