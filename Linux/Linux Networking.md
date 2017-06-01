# Configuring a Router
## Setting up the physical network
以太网卡命名：
* enp5s0f1 -> eth0
* wlp4s0 -> 
* lo -> localhost=127.0.0.1

`ip link set dev eth0 up`
`ip link show eth0`

ifconfig：可设置网络设备的状态，或是显示目前的设置。
down 关闭指定的网络设备。
up 启动指定的网络设备。
-arp 打开或关闭指定接口上使用的ARP协议。前面加上一个负号用于关闭该选项。
-allmuti 关闭或启动指定接口的无区别模式。前面加上一个负号用于关闭该选项。
-promisc 关闭或启动指定网络设备的promiscuous模式。前面加上一个负号用于关闭该选项。
add<地址> 设置网络设备IPv6的IP地址。
del<地址> 删除网络设备IPv6的IP地址。
media<网络媒介类型> 设置网络设备的媒介类型。
mem_start<内存地址> 设置网络设备在主内存所占用的起始地址。
metric<数目> 指定在计算数据包的转送次数时，所要加上的数目。
mtu<字节> 设置网络设备的MTU。
netmask<子网掩码> 设置网络设备的子网掩码。
tunnel<地址> 建立IPv4与IPv6之间的隧道通信地址。
-broadcast<地址> 将要送往指定地址的数据包当成广播数据包来处理。
-pointopoint<地址> 与指定地址的网络设备建立直接连线，此模式具有保密功能。

## Configuring IPv4
* 配置IP地址：`ip addr add dev eth0 10.0.0.1/24`
* `ip addr list eth0`

* 两台主机相互连接：`ping –c 2 –n 10.0.0.2`

RFC1918：私有网络地址分配
```
A类：10.0.0.0    -  10.255.255.255  (10/8 比特前缀)
B类：172.16.0.0  -  172.31.255.255  (172.16/12 比特前缀)
C类：192.168.0.0 -  192.168.255.255 (192.168/16 比特前缀)
```

## Configuring IPv4 permanently
* 添加`eth0`配置到`/etc/network/interfaces`：
```
auto eth0
iface eth0 inet static
    address 10.0.0.1
    netmask 255.255.255.0
```
* Bring up the network interface：`ifup eth0`

## Connecting two networks
![](https://www.packtpub.com/graphics/9781785287916/graphics/B04177_01_01.jpg)
1. Configure the network interface on eth1 on server 1:
```
ip link set dev eth1 up
ip addr add dev eth1 192.168.0.1/24
ip addr list eth1
```
2. Connect your third system to eth1 on server 1
3. Configure eth0 on server 3 with an IP address of 192.168.0.2
```
ip link set dev eth0 up
ip addr add dev eth0 192.168.0.2/24
ip addr list eth1
```
4. Add a default route on server 3:`ip route add default via 192.168.0.1`
5. Enable routing on server 1:
```
echo net.ipv4.ip_forward=1 > /etc/sysctl.conf
sysctl -p /etc/sysctl.conf
```
6. Add a default route on server 2:`ip route add default via 10.0.0.1`

## Enabling NAT to the outside
NAT rewrites your packet headers in order to make them appear as if they come from your router, thus effectively hiding your system's address from the destination.

## Setting up DHCP

## Setting up a firewall with IPtables

## Setting up port forwarding

## Adding VLAN Tagging




# Configuring DNS
## Setting up your system to talk to a nameserver
* 配置DNS服务器：在`/etc/resolv.conf`加入一行`nameserver 8.8.8.8`

## Setting up a local recursive resolver
* 安装bind9：`sudo apt-get install bind9`
* 在bind9配置中添加`allow-recursion`入口：
```
allow-recursion {
192.168.1.0/24;
"localhost;"
};
```
* 监听自己的内网IP地址：
```
listen-on {
  192.168.1.1;
};
```
* 如果想要使用forwarders，添加如下选项：
```
forwarders {
8.8.8.8;
8.8.4.4;
};
```

## Configuring dynamic DNS on your local network
* 
```
zone "example.org" {         
  type master;
  notify no; 
  file "/var/lib/bind/example.org.db";
}
zone "0.168.192.in-addr.arpa" {      
  type master;
  notify no; 
  file "/var/lib/bind/rev.1.168.192.in-addr.arpa"; 
};
```
* populate the zone in `example.org.db`
```
example.org.  IN  SOA  router.example.org. admin.example.org. (
  2015081401
  28800
  3600
  604800 
  38400
)  
example.org.     IN      NS      ns1.example.org.
router    IN      A       192.168.1.1
```
* populate the reverse zone in `rev.1.168.192.in-addr.arpa`
```
@ IN SOA ns1.example.org. admin.example.org. (
  2006081401
  28800
  604800
  604800
  86400  
)  
IN    NS     ns1.example.org. 
1                    IN    PTR    router.example.org.
```
* generate a HMAC key for securing the communication：
`dnssec-keygen -a HMAC-SHA512 -b 512 -r /dev/urandom -n USER DDNS`
* Create a file called ddns.key and insert the following content with <key> replaced by the string marked Key: in the .private file:
```
key DDNS {
  algorithm HMAC-SHA512;
  secret "<key>"; 
};
```
* Copy ddns.key to both /etc/dhcp and /etc/bind with the proper permissions using the following:
```
# install -o root -g bind -m 0640 ddns.key \
/etc/bind/ddns.key
# install -o root -g root -m 0640 ddns.key \
/etc/dhcp/ddns.key
```
* Tell bind about the DDNS updating key by adding it to /etc/bind/named.conf.local:
```
include "/etc/bind/ddns.key";
```
* Then tell bind to allow updating of the zones you previously created by adding an allow-update entry to your zones so that they look similar to the following:
```
zone "example.org" {         
  type master;
  notify no;
  file "/var/lib/bind/example.org.db";
  allow-update { key DDNS; };
}
```
* Now we need to update the DHCP server to have it hand out your nameserver instead of Google's and send hostname updates to your DNS server using the correct key:
```
option domain-name "example.org";
option domain-name-servers 192.168.1.1;
default-lease-time 600;
max-lease-time 7200;
authoritative;
ddns-updates           on;
ddns-update-style      interim; 
ignore                 client-updates; 
update-static-leases   on;
include "/etc/dhcp/ddns.key"; 

subnet 10.0.0.0 netmask 255.255.255.0 {
  range 10.0.0.10 10.0.0.100;
  option routers 10.0.0.1;
}
zone EXAMPLE.ORG. {   
  primary 127.0.0.1;   
  key DDNS; 
}  

zone 2.168.192.in-addr.arpa. {   
  primary 127.0.0.1;   
  key DDNS; 
} 
```

## Setting up a nameserver for your public domain

## Setting up a slave nameserver





# Configuring IPv6
## Setting up an IPv6 tunnel via Hurricane Electric

## Using ip6tables to firewall your IPv6 traffic

## Route an IPv6 netblock to your local network




# Remote Access
## Installing OpenSSH
* 安装SSH server：`sudo apt-get install ssh`

## Using OpenSSH as a basic shell client

## Using OpenSSH to forward defined ports
* Forward a remote port locally: –L 8000:192.168.1.123:80
* Forward a local port remotely: –R 5000:localhost:22
* Make either port available from remote systems with –g

## Using OpenSSH as a SOCKS proxy

## Using OpenVPN
1. 安装OpenVPN：`sudo apt-get install openvpn`
2. Generate a static key：`openvpn --genkey --secret /etc/openvpn/static.key`
3. 服务器配置样例：`usr/share/doc/openvpn/examples/sample-config-files`
```
proto udp
user nobody
secret /etc/openvpn/static.key
ifconfig 10.8.0.1 10.8.0.2
comp-lzo
verb 3
```
4. 客户端配置：
```
remote wanaddress
proto udp
dev tun
secret /path/to/static.key
ifconfig 10.8.0.2 10.8.0.1
route 192.168.1.0 255.255.255.0
comp-lzo
verb 3
```
5. Copy the static key to the client via scp
6. 连接：`sudo openvpn --config client.conf`


# Web Servers
## Configuring Apache with TLS
1. 安装：`sudo apt-get install apache2`
2. Enable the SSL modules and stock SSL configuration：
```
sudo a2enmod ssl
sudo a2ensite default-ssl
```
3. Add the appropriate SSL certs to the machine. The private key file should be delivered to `/etc/ssl/private` while the public certificate and relevant intermediate certs should be delivered to `/etc/ssl/certs`.
4. Update the Apache configuration to point to the correct certs. Edit `/etc/apache2/sites-enabled/default-ssl.conf` in the editor of your choice and update the `SSLCertificateFile` and `SSLCertificateKeyFile` variables to point to your new cert and key. If you're hosting your own internal CA, you'll want to uncomment `SSLCertificateChainFile` and point it to your CA chain.
5. 重启：`service apache2 restart`

## Improving scaling with the Worker MPM

## Setting up PHP using an Apache module

## Securing your web applications using mod_security

## Configuring NGINX with TLS

## Setting up PHP in NGINX with FastCGI



# Directory Services
## Configuring Samba as an Active Directory compatible directory service

## Joining a Linux box to the domain



# Setting up File Storage
## Serving files with SMB/CIFS through Samba


## Granting authenticated access

## Setting up an NFS server

## Configuring WebDAV through Apache



# Setting up E-mail
## Configuring Postfix to send and receive e-mail

## Setting up DNS records for e-mail delivery

## Configuring IMAP

## Configuring authentication for outbound e-mail

## Configuring Postfix to support TLS

## Blocking spam with Greylisting

## Filtering spam with SpamAssassin
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 
* 