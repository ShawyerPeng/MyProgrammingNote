## 安装travis
Travis安装需要Ruby环境，所有需要安装Ruby,并且需要安装rubygems插件。
```
gem install travis
```

## 在github上生成Access Token
首先我们来到github的设置界面，点击到Personal access tokens页面，点击右上角的Generate new token按钮会重新生成一个，点击后他会叫你输入密码，然后来到如下界面，给他去一个名字，下面是勾选一些权限

![](http://7qnc6h.com1.z0.glb.clouddn.com/somyqjl13rhbm2zjgg47hlu3an.png)

生成完后，你需要拷贝下来，只有这时候他才显示，下载进来为了安全他就不会显示了，如果忘了只能重新生成一个了，拷贝完以后我们需要到Travis CI网站配置下

## 在Travis CI配置Github的Access Token
配置界面还是在项目的setting里面，如下图
![](http://7qnc6h.com1.z0.glb.clouddn.com/asl8qurxkanst1uus1wc1opxk1.png)  
至于为什么我们要在这里配置，我想大家肯定应该明白了，写在程序里不安全，配置到这里相当于一个环境变量，我们在构建的时候就可以引用他。
到这里我已经配置了要构建的仓库和要访问的Token，但是问题来了，他知道怎么构建，怎么生成静态文件吗，怎么push的gitpages，又push到那个仓库吗，所以这里我们还需要在源代码的仓库里创建一个.travis.yml配置文件，放到源代码的根目录，如下图
![](http://7qnc6h.com1.z0.glb.clouddn.com/z2wwfwluzv6ajgx6qwbjtlf2yb.png)


## 新建配置文件
首先打开博客项目文件夹，在项目根目录新建`.travis.yml`配置文件。
```
`cd 博客项目文件夹根目录`
`touch .travis.yml`
```

## 加密操作
加密travis私钥，`--add`将解密命令添加到`.travis.yml`
```
travis encrypt 'REPO_TOKEN=<TOKEN>' --add 
```

## 配置文件
修改git config你的用户名和邮箱，修改GH_REF为你的github项目地址

```yml
language: node_js
node_js: stable
before_install:
  - npm install -g hexo
install:
  - npm install
script:
  - hexo generate
after_script:
  - cd ./public
  - git init
  - git config user.name "PatrickPhang"
  - git config user.email "patrickyateschn@gmail.com"
  - git add .
  - git commit -m "Update docss"
  - git push --force --quiet "https://${GH_TOKEN}@${GH_REF}" master:master
branches:
  only:
    - hexo
env:
  global:
  - GH_REF: https://github.com/PatrickPhang/patrickphang.github.io.git
  - secure: xxxxxx
```

## Push文章到Github
```
git push origin hexo:hexo
```

# 参考
[手把手教你使用Travis CI自动部署你的Hexo博客到Github上](http://i.woblog.cn/2016/05/04/%E6%89%8B%E6%8A%8A%E6%89%8B%E6%95%99%E4%BD%A0%E4%BD%BF%E7%94%A8Travis%20CI%E8%87%AA%E5%8A%A8%E9%83%A8%E7%BD%B2%E4%BD%A0%E7%9A%84Hexo%E5%8D%9A%E5%AE%A2%E5%88%B0Github%E4%B8%8A/)
[Continuous Integration Your Hexo Blog With Travis CI](http://blog.bigruan.com/2015-03-09-Continuous-Integration-Your-Hexo-Blog-With-TravisCI/)  
[Hexo 博客 travis-ci 自动部署到VPS](https://uedsky.com/2016-06/travis-deploy/)  
[hexo教程系列——使用Travis自动部署hexo](http://blog.csdn.net/xuezhisdc/article/details/53130423)  
[用 Travis CI 自动部署 hexo](http://blog.acwong.org/2016/03/20/auto-deploy-hexo-with-travis-CI/)