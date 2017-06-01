## 1. 创建python虚拟环境：  
1. 最简单地创建python虚拟环境：
```
virtualenv [虚拟环境名称]```

2. 如果你的系统里安装有不同版本的python，可以使用--python参数指定虚拟环境的python版本
```
>> virtualenv [虚拟环境名称] --no-site-packages --python=C:/Users/asus-pc/AppData/Local/Programs/Python/Python35/python.exe```

3. 依赖系统环境的第三方软件包：  
```
>> virtualenv --system-site-packages [虚拟环境名称]
```

## 2.  进入虚拟环境目录，启动虚拟环境：    
```
>> cd env1/
>> source bin/activate 或 ./Scripts/activate   #前面为Linux 后面为Windows
>> python -V
```

## 3. 退出虚拟环境
```
>> deactivate
或
>> ./Scripts/deactivate
```


-----

-----

-----

# 使用virtualenvwrapper

1. 安装virtualenvwrapper

    1. 创建一个文件夹，用于存放所有的虚拟环境：`mkdir ~/workspaces`  
    2. 设置环境变量，把下面两行添加到~/.bashrc里,然后就可以使用virtualenvwrapper了：  
    ```
    export WORKON_HOME=~/workspaces
    source /usr/bin/virtualenvwrapper.sh
    ```
2. 创建虚拟环境：`mkvirtualenv [虚拟环境名称]`  
3. 列出虚拟环境：`lsvirtualenv -b`
4. 切换虚拟环境：`workon [虚拟环境名称]`
5. 查看环境里安装了哪些包：`lssitepackages`
6. 进入当前环境的目录：`cdvirtualenv [子目录名]`  
    进入当前环境的site-packages目录：`cdsitepackages [子目录名]`  
    控制环境是否使用global site-packages：`toggleglobalsitepackages`  
7. 复制虚拟环境：`cpvirtualenv [source] [dest]`    
8. 退出虚拟环境：`deactivate`  
9. 删除虚拟环境：`rmvirtualenv [虚拟环境名称]`
