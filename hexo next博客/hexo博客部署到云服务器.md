> 本文参考了四弦同学的文章：[在Linux服务器上搭建Hexo：OS X、Windows与Linux本地环境](https://zby.io/how-to-set-up-hexo-blog-on-linux.html#more)，讲解得非常详细，在遇到这篇文章之前，踩了太多坑还没有头绪，现在终于解决了部署问题，非常非常感谢他~

那我们开始吧~

# 在本地安装hexo
## 安装nvm
参考[官方文档](https://github.com/creationix/nvm/blob/master/README.markdown)
1. 安装：`curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash`或  
`wget -qO- https://raw.githubusercontent.com/creationix/nvm/v0.33.1/install.sh | bash`
2. 使环境变量生效：`export NVM_DIR="$HOME/.nvm" [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"`
3. 测试nvm是否安装生效：`command -v nvm`

## 通过nvm安装node.js
1. 安装指定版本的node.js：`nvm install 6.10.0`
可以通过`nvm ls-remote`查看远程所有版本以选择需要的版本

## 安装hexo
1. 安装：`npm install -g hexo-cli`
注意：如果安装过程中有报错：
```
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
请重新输入一下命令再重新安装即可得到解决：  
`npm config set user 0`   
`npm config set unsafe-perm true`

参考：`http://www.cnblogs.com/lidonghao/p/3543747.html`  
`https://segmentfault.com/q/1010000006038485`  
`http://www.07net01.com/2015/04/825142.html`  `https://www.liquidweb.com/kb/how-to-install-nvm-node-version-manager-for-node-js-on-ubuntu-12-04-lts/`

# hexo建站
参考官网：[Hexo官方文档](https://hexo.io/zh-cn/docs/index.html)
## 初始化
1. 初始化主目录：`hexo init <folder>`
2. 进入hexo主目录：`cd <folder>` 
3. 安装node_modules：`npm install` 

## 站点配置文件_config.yml的配置
对一个新站点来说，需要编辑的项目有：
```
title: Hexo #站点的标题
subtitle: #站点的副标题
description: #站点的描述，写一段话来介绍你的博客吧:)，主要为SEO使用
author: John Doe #显示的文章作者名字，例如我配置的是fourstring
language: #语言。简体中文是zh-Hans
timezone: #时区，可以不配置，默认以本地时区为准
url: http://yoursite.com #你的站点地址，如果是全站HTTPS记得写成https://domain.com
root: / #如果您的网站存放在子目录中，例如 http://yoursite.com/blog，则请将您的 url 设为 http://yoursite.com/blog 并把 root 设为 /blog/。（引用自官方文档）
permalink: :year/:month/:day/:title/ #固定链接格式。这项配置的格式为：变量1/变量2/变量3...，其中合法的变量格式为“:变量名”（注意，:是变量的组成部分！）这样生成的效果为/2016/08/10/文章标题。默认的固定链接格式存在一些问题，下文讲解
per_page: 10 #设置每页文章篇数，设为0可以关闭分页功能
theme: #使用的主题。下文讲解
deploy: #部署配置，其值是一个杂凑表，注意缩进，下文详细讲解
```

限于篇幅，省略部分无关注释

```yml
# Site
title: Patrick's Blog
subtitle: Love Coding, Enjoy Life
description: PatrickPhang
author: Patrick Phang
language: zh-Hans
timezone: Asia/Shanghai
avatar: /avatar.png

# URL
url: http://your_server_ip  # 此处修改为你的服务器IP
root: /
permalink: :year/:month/:day/:title/
permalink_defaults:

# Directory
source_dir: source
public_dir: public
tag_dir: tags
archive_dir: archives
category_dir: categories
code_dir: downloads/code
i18n_dir: :lang
skip_render:

# Writing
new_post_name: :title.md
default_layout: post
titlecase: false
external_link: true
filename_case: 0
render_drafts: false
post_asset_folder: false
relative_link: false
future: true
highlight:
  enable: true
  line_number: true
  auto_detect: false
  tab_replace:

# Category & Tag
default_category: uncategorized
category_map:
tag_map:

# Date / Time format
date_format: YYYY-MM-DD
time_format: HH:mm:ss

# Pagination
per_page: 10
pagination_dir: page

# Extensions
theme: next

# Deployment
deploy:
  type: git
  repo: git@your_server_ip:/home/blog/hexo.git  # 此处为你服务器的git目录，先忽略，下面再解释
  branch: master
```

## 新建文章
`hexo new post "标题"`

## 预览
生成静态文件，位于public目录：`hexo generate`  
本地开启服务器，localhost:4000 访问：`hexo server`

## 个性化你的主题
本文主要介绍怎么在服务器上部署你的博客，这部分省略，若想了解请参考如下链接：  
[IIssNan's Notes](http://notes.iissnan.com/)  
[hexo+NEXT主题之博客优化](http://www.mway.top/2016/10/17/hexo-NEXT%E4%B8%BB%E9%A2%98%E4%B9%8B%E5%8D%9A%E5%AE%A2%E4%BC%98%E5%8C%96/)  
[hexo的私人订制](http://blog.sunnyxx.com/2014/03/07/hexo_customize/)  
[使用 Hexo 搭建博客的深度优化与定制](http://blog.tangxiaozhu.com/p/45374067/)
[Hexo的NexT主题个性化：添加文章阅读量](http://www.jeyzhang.com/hexo-next-add-post-views.html)  
[如何使用 Hexo 部署个人博客](http://www.jialunliu.com/how-to-build-a-blog-with-hexo/)  
[为NexT主题添加文章阅读量统计功能](https://notes.wanghao.work/2015-10-21-%E4%B8%BANexT%E4%B8%BB%E9%A2%98%E6%B7%BB%E5%8A%A0%E6%96%87%E7%AB%A0%E9%98%85%E8%AF%BB%E9%87%8F%E7%BB%9F%E8%AE%A1%E5%8A%9F%E8%83%BD.html#%E9%85%8D%E7%BD%AELeanCloud)
[Hexo 主题配置](http://www.shuang0420.com/2016/05/12/Github-Pages-Hexo%E4%B8%BB%E9%A2%98%E9%85%8D%E7%BD%AE/)  
[Hexo-NexT搭建个人博客（二）](https://neveryu.github.io/2016/09/30/hexo-next-two/)  
[hexo-wordcount实现文章标题栏显示更多的文章信息(精)](http://www.joryhe.com/2016-06-06-hexo_wordcount_setting_your_post.html)  
[动动手指，给你的Hexo站点添加最近访客（多说篇）](http://www.arao.me/2015/hexo-next-theme-optimize-duoshuo/)  
[Hexo中播放网易云音乐的实践](http://weqeo.com/2016/10/11/Hexo%E4%B8%AD%E6%92%AD%E6%94%BE%E7%BD%91%E6%98%93%E4%BA%91%E9%9F%B3%E4%B9%90%E7%9A%84%E5%AE%9E%E8%B7%B5/)  
[一步步在GitHub上创建博客主页(1)](http://www.pchou.info/ssgithubPage/2013-01-03-build-github-blog-page-01.html)  
[Hexo-用自己的页面做首页](https://jacklightchen.github.io/blog/2016/10/27/HexoOverview/)  
[hexo搭建个人博客如何设置自定义的页面为主页](https://segmentfault.com/q/1010000008326088)  
[自动随机切换Hexo博客的banner图片](http://kuangqi.me/tricks/hexo-banner-auto-switcher/)  
[Hexo 博客插入 B 站 html5 播放器](https://login926.github.io/2016/12/24/Bilibilihtml5Player/)  
[Hexo搭建博客的个性化设置](https://segmentfault.com/a/1190000008618841)  
[Hexo-设置阅读全文](http://www.jianshu.com/p/78c218f9d1e7)  
[hexo-NexT主题添加炫酷的动画插件](http://www.she.vc/article/18-101737-0.html)  
[Hexo主题Yilia](http://litten.me/2014/08/31/hexo-theme-yilia/)  
[部署 hexo 静态博客到自有服务器](http://www.dullong.com/deploy-hexo-blog-to-my-own-server.html)  


## 遇到的问题
[-bash: hexo: command not found解决办法](http://www.jianshu.com/p/ffcd663f40c2)  
[hexo 部署至Git遇到的坑](http://www.jianshu.com/p/67c57c70f275)  
[fatal: Unable to create temporary file '/home/username/git/myrepo.git/./objects/pack/tmp_pack_XXXXXX': Permission denied](http://stackoverflow.com/questions/13146992/fatal-unable-to-create-temporary-file-home-username-git-myrepo-git-objects)  
[Why am I getting git error “remote: error: unable to create temporary file: No such file or directory” on push?](http://unix.stackexchange.com/questions/105261/why-am-i-getting-git-error-remote-error-unable-to-create-temporary-file-no-s)



# 部署到服务器
## Git版本控制系统
### 准备（建立SSH信任关系）
参考：[《ssh-copy-id帮你建立信任》–linux命令五分钟系列之四十一](http://roclinux.cn/?p=2551)  
[SSH如何通过公钥连接云服务器](http://blog.csdn.net/u014743697/article/details/52678916)

1. 在本地生成公钥和密钥：`ssh-keygen -t rsa`
2. 将本机生成的公钥发送到服务器上（建立信任关系）：  
`ssh-copy-id -i C:/Users/UserName/.ssh/id_rsa.pub root@server_ip`
3. 测试ssh远程登录是否成功：`ssh root@server_ip`

### 服务端配置
1. 安装nginx（也可以通过编译安装）：`apt-get install nginx`

2. 新建git用户并添加权限：`adduser git`
编辑配置文件，加入git到sudo用户组：`nano /etc/sudoers`  
    ```
    # User privilege specification
    root    ALL=(ALL:ALL) ALL
    git     ALL=(ALL:ALL) ALL   # 新增这一行
    ```

3. 生成一对ssh认证密钥：
    ```
    su git
    cd /home/git
    mkdir /blog/.ssh
    cd blog/.ssh
    ssh-keygen -t rsa
    ```

4. 配置Git仓库：
    将网站目录放到/home/git/
    ```
    mkdir -p /home/git/blog/hexo.git #Git仓库，不存储网站文件
    mkdir /home/git/blog/hexo #实际存储网站文件目录
    ```

    初始化空的Git仓库：`git init --bare /home/git/blog/hexo.git`

    进入该仓库，配置post-update hooks（有的可能是post-receive）：  
    `cd /home/git/blog/hexo.git/hooks`
    `sudo nano /home/git/blog/hexo.git/hooks/post-update.sample`
    ```
    git --work-tree=/home/git/blog/hexo --git-dir=/home/git/blog/hexo.git checkout -f
    ```

    赋予可执行权限：`chmod +x post-update`

5. nginx web server配置
    修改配置文件：`nano /etc/nginx/sites-enabled/default`
    将root改为你的hexo主目录
    ```
    server {
        ......
        # root /var/www/html;
        root /home/git/blog/hexo;
    ```

### 本地配置
1. 本地SSH配置  
    打开Git Bash，建立配置文件
    ```
    mkdir ~/.ssh
    touch ~/.ssh/config
    ```

    编辑config文件，写入如下配置：
    ```
    Host hexo #SSH主机配置的识别名，配置好后直接"ssh 识别名"即可快速连接
    HostName your_server_ip #SSH主机的地址
    Port 22 #SSH主机端口
    User git #用户，本例是Git
    IdentityFile ~/.ssh/id_rsa #私钥文件的存放地址，建议复制到~/.ssh下统一管理
    ```

    然后执行：
    ```
    chmod 0600 ~/.ssh/testkey #换成你自己的私钥路径
    ssh hexo
    ```
    如果可以正常连接，说明我们的Git服务端已经配置成功。

2. 配置部署选项  
    编辑home/_config.yml文件，找到deploy项目，修改如下：
    ```
    deploy:
    type: git #用户名
    repo: git@hexo:/home/git/blog/hexo.git #Git仓库地址，:符号后为Git仓库服务器路径
    branch: master #分支，由于我们只用Git进行发布，master即可。
    ```

    保存，进入home目录，执行：
    ```
    hexo clean
    hexo deploy -g
    ```
    即可将静态文件发布到服务端了。

<font face="黑体" color=#0099ff size=3>至此，一切（woc终于结束了）搭建步骤完成，快访问你的域名看看效果吧~（解析请自行完成）</font>

## rsync远程同步工具
暂未使用过，过两天填坑
[静态博客遭遇持续部署](https://blog.jamespan.me/2015/11/01/ci-your-hexo-blog)

