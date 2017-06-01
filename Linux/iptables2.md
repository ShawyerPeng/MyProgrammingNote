默认情况下开通 22(ssh)80、443(Nginx) 21、20000:30000(FTP)
其它端口都拒绝,如下需要允许其它端口,请如下操作(如开启 8080 端口)
```
# 允许 8080 端口
iptables -I INPUT 4 -p tcp -m state --state NEW -m tcp --dport 8080 -j ACCEPT
# 保存 iptables 规则
service iptables save
```

![](http://i2.muimg.com/567571/929066fa705765cf.png)
