# 一、grub修复
如果进入不了Ubuntu引导界面,显示"Minimal BASH-like line editing is supported. Fot the first word, TAB lists possible command completions......"
请按以下步骤来解决~

1. 查找`grub.cfg`所在磁盘分区名
```
grub> ls
grub> ls (hd0)/boot/grub
grub> ls (hd1)/boot/grub
grub> ls (hd2,gpt1)/boot/grub
grub> ls (hd2,gpt2)/boot/grub
......
grub> ls (hd2,gpt7)/boot/grub
```
一直试到发现了`grub.cfg`文件为止(假设我在(hd2,gpt7)分区下找到)

2. 修改启动分区
    ```
    grub> root=(hd2)
    grub> prefix=/boot/grub
    grub> set root=(hd2,gpt7)
    grub> set prefix=(hd2,gpt7)/boot/grub
    grub> insmod normal
    grub> normal
    ```
    输入完以上命令应该就会成功进入到ubuntu引导界面了,然后进入Ubuntu系统,就看到熟悉的界面了~

3. 成功进入Ubuntu
你以为这样就OK了,哼,naive!你下次重新启动电脑然后发现TM又出现第一步的grub命令行,又进不去Ubuntu了!
我按照其他教程讲的输入以下两个命令来修复grub:`sudo update-grub`   
`sudo grub-install /dev/sda`
然后,重启...  
"去你Y的,怎么还是不行!"  
"MD,室友,把你的移动硬盘借给我,我要把我电脑里的无数G"学习资料"移过去(好羞羞哦...为小姐姐们搬个家....),又重装个系统算了!"
"你再试试这个方法,安装个boot-repair来修复,再不行的话就重装系统吧~"

4. 祭出大杀器`boot-repair`
我抱着没啥希望的心态安装了boot-repair(需要重复以上步骤才能再次进入到Ubuntu系统中)  
`sudo add-apt-repository ppa:yannubuntu/boot-repair && sudo apt-get update`  
`sudo apt-get install -y boot-repair && boot-repair`  
等待一会，就会出现Boot Repair的软件界面了，点击上面的“recommended repair”按钮，等待一会儿，就会跳出修复成功。  
哈哈,重启,出现引导界面,成功修复,问题解决!在此我要感谢我的室友,在我想日天日地日空气的时候给我指出了有效的解决办法~

# 二、科学上网配置
1. 安装`shadowsocks-qt5`
`sudo add-apt-repository ppa:hzwhuang/ss-qt5`
`sudo apt-get update`
`sudo apt-get install shadowsocks-qt5`

2. `shadowsocks-qt5`配置
输入ss服务器名、密码、端口号等。

3. 配置全局代理
打开系统设置，选择网络（Network）- 网络代理（Network proxy）  
选择自动，这里需要pac文件（下一步生成的pac文件）,把你下载的文件的路径填进去。文件格式如图片,我的设置是`file:///home/patrick/autoproxy.pac`

4. 使用`GenPAC`生成基于gfwlist.txt的pac文件
安装GenPAC:`sudo pip install genpac`(注意pip和python版本一致问题)
下载gfwlist:`sudo genpac -p "SOCKS5 127.0.0.1:1080" --gfwlist-proxy="SOCKS5 127.0.0.1:1080" --output="autoproxy.pac"`

也可以使用自定义的规则文件:`--user-rule-from="~/shadowsocks/user-rules.txt"`
genpac 的详细使用说明见 GitHub - Wiki：https://github.com/JinnLynn/GenPAC

5. 使用`SwitchyOmega`插件配置浏览器代理
参考这个教程:http://www.ihacksoft.com/chrome-switchyomega.html

6. 设置开机启动
参考的这个:http://www.afox.cc/archives/83

### git代理配置
```
git config --global http.proxy 'socks5://127.0.0.1:1080'
git config --global https.proxy 'socks5://127.0.0.1:1080'
```

### curl临时代理配置
```
export http_proxy=socks5://localhost:1080
export https_proxy=socks5://localhost:1080

unset http_proxy
unset https_proxy

$ curl ip.gs
当前 IP：125.39.112.14 来自：中国天津天津 联通
```
设置更长久的代理可以把上面这句加到～/.bashrc文件。


# 二、基本软件安装
三种不同安装方式的指令:
1. deb包
直接安装:`sudo dpkg -i xxx.deb`  
解决依赖问题:`sudo apt-get -f install`  

2. PPA
添加PPA源:`sudo add-apt-repository ppa:xxx`  
对源进行更新：`sudo apt-get update`    
安装：`sudo apt-get install xxx`   

3. 源码安装
解压源文件:`tar -zxvf **.tar.gz`  
进入文件目录:`cd **`  
配置:`./configure`    
编译:`make`  
安装:`make install`  

### 1. 娱乐软件
1. 网易云音乐  
2. Chrome  
注意Chrome在看视频时可能出现无法加载Adobe Flash Player的情况,请从Adobe官网下载`flash_player_ppapi_linux.x86_64.tar.gz`包后解压到`/usr/lib/adobe-flashplugin`中,重启电脑后即可享用.  
3. VLC(视频播放器)

### 2. 实用软件
1. WPS
2. uGet
3. Mutate(类似Alfred)
`https://github.com/qdore/Mutate`
4. unity tweak tool(桌面美化)
5. docky
6. 搜狗输入法
7. zsh + oh-my-zsh
8. Guake
9. Zeal(类似Dash的官方文档离线查询工具)
10. 坚果云
11. Albert

### 3. 编程软件
1. JetBrains系列(IntelliJ IDEA Pycharm Clion)
2. CodeBlocks
3. Atom/Visual Studio Code
4. TeXlive + TeXworks

### 4. 编程环境配置
1. npm
npm镜像设置为淘宝镜像：`npm config set registry https://registry.npm.taobao.org`  
验证是否设置成功:`npm config get registry`  
设置代理:`npm config set proxy=http://代理服务器ip:代理服务器端口`  
清理代理:`npm config delete http-proxy` `npm config delete https-proxy`  

# 三、atom常用插件
所有的插件都安装在`/home/patrick/.atom/packages`中
1. activate-power-mode-master  
2. markdown-preview-enhanced
3. language-latex-master + atom-latex-master + atom-pdf-view-master
4. terminal-plus
5. atom-script-master
6. atom-beautify
7. autocomplete-python
