# The security policy of Linux


# Conducting integrity checks of the installation medium using checksum
`md5sum ubuntu-filename.iso`

GUI checksum calculator：`sudo apt-get install gtkhash`
http://gtkhash.sourceforge.net/

# Using the LUKS disk encryption
Linux Unified Key Setup-on-disk-format (LUKS)硬盘加密

```
telinit 1                   # 改变运行级别为1
umount /home                # 解挂文件系统（/home目录）
fuser -mvk /home            # 杀掉相关进程
grep home /proc/mounts      # 确保/home分区已经被解挂
shred -v --iterations=1 /dev/MYDisk/home    # 粉碎文件（覆盖一次）
cryptsetup --verbose --verify-passphrase luksFormat /dev/MYDisk/home    # 初始化加密分区
cryptsetup luksOpen /dev/MYDisk/home                                    # 使用加密分区
ls -l /dev/mapper | grep home                                           # 确保分区展现
mkfs.ext3 /dev/mapper/home      # 创建ext3文件系统
mount /dev/mapper/home /home    # 挂载该新系统
df -h | grep home               # 确保该文件系统仍然可见
home /dev/MYDisk/home none                  # 在/etc/crypttab文件中输入该行
/dev/mapper/home /home ext3 defaults 1 2    # 修改/etc/fsta文件，删除/home入口
/sbin/restorecon -v -R /home    # 恢复默认的SELinux安全设置
shutdown -r now                 # 重启电脑
```

# Making use of sudoers – configuring sudo access
```
useradd USERNAME    # 添加新用户
passwd USERNAME     # 为新用户设置密码
visudo              # 运行该命令以修改/etc/sudoers文件
%test ALL=(ALL) ALL         # 在文件中新增此行，为该组的成员新增root权限
usermod -aG test USERNAME   # 将用户添加到test组中
su USERNAME -       # 切换用户
groups              # 列出所有组名
sudo whoami         # whoami
```

# Scanning hosts with Nmap
下载：https://nmap.org/download.html

```
nmap -vv -sP 103.46.192.2-100       # 扫描100个host
```
`-n`：tells Nmap not to perform the DNS resolution of the IP addresses，加快扫描速度
`-T4`：设置速度为T4（T1最慢，T5最快）
`--max-rtt-timeout 500ms`：设置需要等待的最大时间为500ms
`-Ss`：执行stealth扫描
`-A`：发现OS和服务的版本信息

`nmap -sS -vv -n -PN -p21 --max-rtt-timeout 500ms 192.168.1.1/24 -T4 -oG - | grep 'open'`：列出扫描到的开放21端口的IP地址

# Gaining a root on a vulnerable Linux system
Metasploitable下载：http://sourceforge.net/projects/metasploitable/files/Metasploitable2/

1. 打开：`Main Menu | Backtrack | Exploitation Tools | Network Exploitation Tools | Metasploit Framework | Msfconsole`
2. 扫描


# Configuring a Secure and Optimized Kernel
引导加载程序syslinux下载
```
syslinux /dev/sdb1                          # 为USB设备安装syslinux
mkdir /mnt/isoboot /mnt/diskboot            # 创建挂载点
mount –o loop boot.iso /mnt/isoboot         # 挂载boot.iso
mount /dev/sdb1 /mnt/diskboot               # 挂载USB设备
cp /mnt/isoboot/isolinux/* /mnt/diskboot    # 复制isolinux文件从boot.iso到USB设备
# 使用boot.iso中的isolinux.cfg作为USB中的syslinux.cfg
grep –v local /mnt/isoboot/isolinux/isolinux.cfg > /mnt/diskboot/syslinux.cfg
unmount /mnt/isoboot /mnt/diskboot          # 卸载两个设备
```

## Retrieving a kernel source
http://kernel.ubuntu.com/git-repos/ubuntu/

下载：`git clone git://kernel.ubuntu.com/ubuntu/ubuntu-<release>`
解压：`tar xvjf linux-x.y.z.tar.bz2`

## Configuring and building a kernel
更新系统：`sudo apt-get update && sudo apt-get upgrade`
安装build-essential：`sudo apt-get install build-essential gcc libncurses5-dev binutils-multiarch alien ncurses-dev`

## Installing and booting from a kernel


# Viewing file and directory details using the ls command
`-FC`：在文件名末尾添加标识符
`-l`：显示文件的详细信息（创建时间、拥有者、权限）
`-a`：显示所有文件（包括隐藏文件）
`-lh`：显示文件大小（以MB, GB, TB格式显示）
`-d */`：排除所有文件，只显示子目录
`-R`：显示子目录的内容
`ls –lah`：推荐常用

# Changing the file permissions using the chmod command
```
u is used for user/owner
g is used for group
o is used for others

r: read
w: write
x: execute
```

`chmod u+x testfile.txt`：添加权限用+号
`chmod g+x, o+x testfile.txt`：添加多个权限用,隔开
`chmod o-x testfile.txt`：删除权限用-号
`chmod a+r testfile.txt`：a代表所有用户(owner, group, others)
`chmod o+x –R /example`：-R代表递归地为目录下所有文件添加权限
`chmod --reference=file1 file2`：复制相同的权限给另一个文件


`chmod xxx file/directory`：xxx是三个1-7的数字组合，分别代表owner,group和others的权限

# Implementing access control list (ACL)
`getfacl <filename>`
`setfacl -m xxx`

# File handling using the mv command (moving and renaming)
`mv testfile1.txt /home/practical/example`：移动单个文件
`mv testfile2.txt testfile3.txt testfile4.txt /home/practical/example`：移动多个文件
`mv directory1/ /home/practical/example`：移动整个目录
`mv example_1.txt example_2.txt`：重命名文件
`mv test_directory_1/ test_directory_2/`：重命名目录

`mv -v *.txt /home/practical/example`：-v显示详细过程
`mv -i testfile1.txt /home/practical/example`：-i不要覆盖已存在的文件
`mv –uv *.txt /home/practical/example/`：-u更新
`mv –nv *.txt /home/practical/example/`：-n不覆盖同名文件
`mv -bv *.txt /home/practical/example`：-b创建备份

# Install and configure a basic LDAP server on Ubuntu
LDAP：Lightweight Directory Access Protocol
下载：http://www.ubuntu.com/download/server
`sudo apt-get install slapd`
`sudo apt-get install ldap-utils`

`sudodpkg-reconfigure slapd`：开启包配置工具
Omit OpenLDAP server configuration? ---NO
Database backend to use?            ---HDB
Allow LDAPv2 protocol?              ---NO

`sudo apt-get install phpldapadmin`：web端管理




# Local Authentication in Linux
## User authentication and logging
`lastb root`：查看不正确的登录尝试
`dmesg | grep USB`：通过命令行查看日志信息
`tail -n 10 /var/log/auth.log`：只查看最近10条log
`last`：显示/etc/log/wtmp文件（最近登录尝试）
`lastlog`：查看最近产生的日志


## Limiting the login capabilities of users
用户账号细节存储在`/etc/passwd`和`/etc/shadow`文件中
`cat /etc/passwd`
`cat /etc/shadow`

`passwd -l user1`或`usermod -L user1`：锁住账号
`passwd -u user1`或`usermod -U user1`：解锁账号
`passwd -S user1`：查看账号是否被锁

## Monitoring user activity using acct
`apt-get install acct`
http://packages.ubuntu.com/precise/admin/acct

## Login authentication using a USB device and PAM
`sudo apt-get install pamusb-tools libpam-usb`
`sudo pamusb-conf --add-device usb-device`

## Defining user authorization controls
`user2 ALL = (user1) /bin/ps`：编辑/etc/sudoers，加入该行，使得user2能够作为user1使用命令`sudo -u user1 ps`

`Defaults:user1 timestamp_timeout = 0`：编辑/etc/sudoers，加入该行，使得每次执行命令都必须输入密码

# Remote Authentication
安装SSH：`sudo apt-get install openssh-server`
安装客户端：`sudo apt-get install openssh-client`

运行SSH服务：`sudo service ssh start`

进行SSH连接：  
`ssh remote_ip_address`（默认）
`ssh username@remote_ip_address`（指定用户名）
`ssh -p port_number remote_ip_address`（指定端口）

备份配置文件：`sudo cp /etc/ssh/sshd_config{,.bak}`

## Disabling or enabling SSH root login
修改配置文件：`sudo nano /etc/ssh/sshd_config`
改成：`PermitRootLogin no`
重启服务：`sudo service ssh restart`

只允许登录到指定的用户：`AllowUsers tajinder user1`

## Restricting remote access with key-based login into SSH
生成SSH key-pair到~./ssh/目录：`ssh-keygen-t rsa`
复制public key到远程SSH服务器：`ssh-copy-id 192.168.1.101`


## Copying files remotely
1. 将本地的myfile.txt文件复制到远程服务器的Desktop目录：`scp myfile.txt tajinder@sshserver.com:~Desktop/`
2. 将本地的mydata目录复制到远程服务器的Desktop目录：`scp -r mydata/ tajinder@sshserver.com:~Desktop/`
3. 从远程服务器复制文件到本机：`scp –r tajinder@sshserver.com:/home/tajinder/Desktop/newfile.txt`

也可以使用FTP的sftp命令：`sftp tajinder@sshserver.com`
从远程服务器获取文件到本机：`get sample.txt /home/tajinder/Desktop`

## Setting up a Kerberos server with Ubuntu
`sudo apt-get install krb5-admin-server krb5-kdc`

# Network Security
## Managing the TCP/IP network

## Using Iptables to configure a firewall
`iptables -L`：查看所有的规则
`iptables -S`：格式化查看规则
`lsmod | grep up_tables`：查看iptables模块是否被默认加载

`iptables -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT`
`iptables -I INPUT 1 -i lo -j ACCEPT`
`iptables –A INPUT –j DROP`


`apt-get install iptables-persistent`：能保存规则

## Blocking spoofed addresses


## Blocking incoming traffic

## Configuring and using the TCP Wrapper


# 