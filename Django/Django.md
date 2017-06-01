# 一.安装Django
安装到python3中：
`pip3 install Django`  (若遇到python2和3共存时，可能会遇到安装目录的问题，可以换成`python3 -m pip install Django`)  
安装到python2中：`pip install Django`
查看Django版本:在cmd中输入
```
python3
import django
django.VERSION
```
--------------------------
# 二.在虚拟环境中创建Django项目
1.创建新项目  

![](C:\Users\asus-pc\Desktop\01创建新项目.jpg)

2.新建虚拟环境

![](C:\Users\asus-pc\Desktop\02新建虚拟环境.jpg)
![](C:\Users\asus-pc\Desktop\02新建虚拟环境2.jpg)

3.新建Django项目和Django App

![](C:\Users\asus-pc\Desktop\03新建Django项目和Django App.jpg)

---------------------------
`django-admin startproject firstsite`
# 三.Django的设置与管理
`python manage.py <command> [options]`
## 1.使用manage.py启动服务器
1. `python manage.py migrate`  
`python manage.py runserver`  
-> 打开服务器(输入http://127.0.0.1:8000/ 或 http://localhost:8000/会看到Django网站已经在web server上生成了)  
记住，以后每次修改了文件，并且想要提交到服务器上时，都需要runserver,也就是输入`python manage.py runserver`


## 2.把Django app加入设置
2. 在PyCharm中打开mysite/settings.py,找到INSTALLED_APPS，把ganji(app名字)加在最后面


## 3.设置Django管理后台
1. 建立管理员账号:在PyCharm的Terminal中输入`python manage.py createsuperuser`，然后输入用户名、邮箱、密码  
2. 使用管理后台:执行runserver指令，然后进入http://127.0.0.1:8000/admin，可以看到后台管理的登录界面
-------------------------






1
