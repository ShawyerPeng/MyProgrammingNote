https://blogs.harvard.edu/rprasad/
## 从已有的Git仓库中克隆一个本地的镜像仓库：  

        `git clone https://github.com/YourAccountName/name.git`
------------------
## 自己新建一个仓库并上传到远程仓库：
1. `git init` -> 初始化新的仓库
2. `git add README.md` -> 将新加入的untracked状态的文件加入跟踪并放入暂存区///将已修改的文件放入暂存区  
`git add .` -> 操作所有文件

    ```
    On branch master

    Initial commit

    Changes to be committed:
      (use "git rm --cached <file>..." to unstage)

            new file:   a01.txt```

3. `git commit -m "第一次提交"` -> 将暂存区中的文件提交至HEAD所指向的分支，暂存区的文件将回到未修改状态  

    ```
    [master (root-commit) af05048] 第一次
     1 file changed, 3 insertions(+)
     create mode 100644 a01.txt```

     当修改文件时:
    ```
    On branch master
    Changes not staged for commit:
      (use "git add <file>..." to update what will be committed)
      (use "git checkout -- <file>..." to discard changes in working directory)

            modified:   a01.txt

    no changes added to commit (use "git add" and/or "git commit -a")
    Untracked files:
        (use "git add <file>..." to include in what will be committed)

        a02.txt

        no changes added to commit (use "git add" and/or "git commit -a")```  
      其他常见方法：
    `git status` -> 查看仓库中文件的状态  
    `git rm a01.txt` -> 将已跟踪的文件从Git中移除  
    `git checkout -- a01.txt` -> 恢复被删除的文件(未被commit)  
    `git reset HEAD readme.txt` -> 恢复被修改的文件(未被commit),并不会更改文件内容，只是使之回到已修改状态  
    `git commit --amend -m a01.txt` -> 恢复被删除的文件(已被commit)

4. `git remote add origin https://github.com/YourAccountName/name.git` -> 为远程Git更名为origin，这样就可以用origin这个名字来引用添加的远程仓库  

5. `git fetch origin` -> 并不能看到工作目录下有任何变化，只是把远程的数据抓取到本地，而不会把改动合并到当前的分支上  
`git pull https://github.com/YourAccountName/name.git` -> 把远程仓库抓取到本地，并合并本地master分支

6. `git push -u origin master` -> 将本地的数据更新到远程仓库中  
`git push` -> 以后直接使用


```
# 添加指定文件到暂存区
$ git add [file1] [file2] ...
# 添加指定目录到暂存区，包括子目录
$ git add [dir]
# 添加当前目录的所有文件到暂存区
$ git add *
# 添加每个变化前，都会要求确认
对于同一个文件的多处变化，可以实现分次提交
$ git add -p
# 删除工作区文件，并且将这次删除放入暂存区
$ git rm [file1] [file2] ...
# 停止追踪指定文件，但该文件会保留在工作区
$ git rm --cached [file]
# 改名文件，并且将这个改名放入暂存区
$ git mv [file-original] [file-renamed]
# 提交暂存区到仓库区
$ git commit -m [message]
# 提交暂存区的指定文件到仓库区
$ git commit [file1] [file2] ... -m [message]
# 提交工作区自上次commit之后的变化，直接到仓库区
$ git commit -a
# 提交时显示所有diff信息
$ git commit -v
# 使用一次新的commit，替代上一次提交
如果代码没有任何新变化，则用来改写上一次commit的提交信息
$ git commit --amend -m [message]
# 重做上一次commit，并包括指定文件的新变化
$ git commit --amend [file1] [file2] ...
# 提交更改到远程仓库
$ git push origin master
# 拉取远程更改到本地仓库默认自动合并
$ git pull origin master
```

## 分支操作
查看分支：`git branch`  
创建分支：`git branch <name>`  
切换分支：`git checkout <name>`  
创建+切换分支：`git checkout -b <name>`  
合并某分支到当前分支：`git merge <name>`  
删除分支：`git branch -d <name>`  

参见：[廖雪峰-创建与合并分支](http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000/001375840038939c291467cc7c747b1810aab2fb8863508000)


## 解决冲突
准备新的feature1分支：`git checkout -b feature1`  
修改readme.txt最后一行，改为：Creating a new branch is quick AND simple.   
在feature1分支上提交：`git add readme.txt` `git commit -m "AND simple"`  
切换到master分支：`git checkout master`  
在master分支上把readme.txt文件的最后一行改为：Creating a new branch is quick & simple.    
提交：`git add readme.txt` `git commit -m "& simple"`    
现在，master分支和feature1分支各自都分别有新的提交这种情况下，Git无法执行“快速合并”，只能试图把各自的修改合并起来，但这种合并就可能会有冲突：`git merge feature1`   
Git用<<<<<<<，=======，>>>>>>>标记出不同分支的内容，我们修改如下后保存：`Creating a new branch is quick and simple.`  
再提交：`git add readme.txt` `git commit -m "conflict fixed"`    
用带参数的git log也可以看到分支的合并情况：`git log --graph --pretty=oneline --abbrev-commit`  
最后，删除feature1分支：`git branch -d feature1`  

----------------
##遇到的错误(慢慢更新......)
### 1. git push origin master
```
To https://github.com/PatrickYates/patrickyates.github.com.git
 ! [rejected]        master -> master (non-fast-forward)
error: failed to push some refs to 'https://github.com/PatrickYates/patrickyates
.github.com.git'
hint: Updates were rejected because the tip of your current branch is behind
hint: its remote counterpart. Integrate the remote changes (e.g.
hint: 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.```
* 解决办法：当前的本地仓库不是最新的，应该pull到本地



### 2. git pull https://github.com/YourAccountName/name.git
```
From https://github.com/YourAccountName/name.git
 * branch            HEAD       -> FETCH_HEAD
fatal: refusing to merge unrelated histories```

```
>git pull https://github.com/YourAccountName/name.git
warning: no common commits
remote: Counting objects: 60, done.
remote: Compressing objects: 100% (51/51), done.
remote: Total 60 (delta 1), reused 60 (delta 1), pack-reused 0
Unpacking objects: 100% (60/60), done.
From https://github.com/YourAccountName/name.git
 * branch            HEAD       -> FETCH_HEAD
fatal: refusing to merge unrelated histories```
* 解决办法：本地代码与远程仓库代码完全不同，无法合并  

--------------------

## 其他Github精华教程：  
1. [交互编程-15分钟学会github](http://try.github.io/levels/1/challenges/1)  
2. [书籍-重量级教程progit](https://github.com/numbbbbb/progit-zh-pdf-epub-mobi)  
3. [书籍-git magic](https://github.com/blynn/gitmagic/tree/master/zh_cn)  
4. [教程-如何高效利用GitHub](http://www.yangzhiping.com/tech/github.html)  
5. [教程-git immersion](http://gitimmersion.com/)  
[中文版](http://igit.linuxtoy.org/contents.html)
6. [廖雪峰Git教程](http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000)
## 参考资料
1. [必须要会的Git基本使用及常用命令操作](http://www.jianshu.com/p/555e7a188312)





------------------
## Git链接到自己的Github
1. 配置本机的git
`git config --global user.name "abcd"`
`git config --global user.email abcd@efgh.com`
2. 生成密钥
`ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`
3. 提交密钥
vim /home/linx/.ssh/id_rsa.pub //复制里面的密钥
4. 检验是否链接上了github
`ssh git@github.com`
5. 首次推送
```
mkdir tmp      //创建推送目录
cd tmp         //进入推送目录    
git init       //设置该目录为推送
touch README   //生成readme
git add README //加入修改列表
git commit -m 'first commit' //递交修改声明
git remote add origin git@github.com:abcd/tmp.git //为远程Git更名为origin
git push -u origin master //推送此次修改
```

















1
