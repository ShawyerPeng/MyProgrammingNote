# 一、安装hexo
1. 安装：`npm install -g hexo-cli`

2. 建站：`hexo init <folder>` `cd <folder>` `npm install`  
新建完成后，指定文件夹的目录如下：
```
.
├── _config.yml     // 网站的配置信息，可在此配置大部分的参数
├── package.json    
├── scaffolds       // 模版文件夹。当您新建文章时，Hexo 会根据 scaffold 来建立文件
├── source          // 资源文件夹是存放用户资源的地方
|   ├── _drafts
|   └── _posts
└── themes          // 主题文件夹。Hexo 会根据主题来生成静态页面
```

3. 运行启动
`hexo g`：这是生成页面文件,必须先生成才能启动
`hexo s`：启动服务器,启动好之后,访问localhost:4000打开

# 二、安装next主题
1. git下载：`git clone https://github.com/iissnan/hexo-theme-next themes/next`
2. 启用主题：
需要修改/root/_config.yml配置项theme：
```
# Extensions
## Plugins: http://hexo.io/plugins/
## Themes: http://hexo.io/themes/
theme: next
```
在切换主题之后、验证之前， 我们最好使用 hexo clean 来清除 Hexo 的缓存。
3. 验证是否启用：`hexo s --debug`

# 三、使用next主题
1. 新建文章：`hexo new "first page"`  
在/source/_post里添加hello-world.md文件，之后新建的文章都将存放在此目录下。
2. 生成网站：`hexo generate`  
此时会将/source的.md文件生成到/public中，形成网站的静态文件。
3. 服务器：`hexo server -p 3000`  
输入http://localhost:3000即可查看网站。
4. 部署网站：`hexo deploy`  
部署网站之前需要生成静态文件，即可以用$ hexo generate -d直接生成并部署。此时需要在_config.yml中配置你所要部署的站点：
```
## Docs: http://hexo.io/docs/deployment.html
 deploy:
   type: git
   repo: git@github.com:YourRepository.git
   branch: master
```

# 四、个性化配置
### 1. 主题设定
Scheme 是 NexT 提供的一种特性，借助于 Scheme，NexT 为你提供多种不同的外观。同时，几乎所有的配置都可以 在 Scheme 之间共用。目前 NexT 支持三种 Scheme，他们是：

Muse - 默认 Scheme，这是 NexT 最初的版本，黑白主调，大量留白
Mist - Muse 的紧凑版本，整洁有序的单栏外观
Pisces - 双栏 Scheme，小家碧玉似的清新

```
#scheme: Muse
#scheme: Mist
scheme: Pisces
```

### 2. 设置语言
编辑站点配置文件，
```
language: zh-Hans
```


还需要在文章的Front-matter里打开mathjax开关
```
---
title: index.html
date: 2016-12-28 21:01:30
tags:
mathjax: true
--
```