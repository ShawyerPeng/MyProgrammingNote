# Rewrite




















# SSI
## 概念
SSI：Server Side Include，是一种基于服务端的网页制作技术，大多数（尤其是基于Unix平台）的web服务器如Netscape Enterprise Server等均支持SSI命令。

在页面内容发送到客户端之前，使用SSI指令将文本、图片或代码信息包含到网页中。对于在多个文件中重复出现内容，使用SSI是一种简便的方法，将内容存入一个包含文件中即可，不必将其输入所有文件。通过一个非常简单的语句即可调用包含文件，此语句指示 Web 服务器将内容插入适当网页。而且，使用包含文件时，对内容的所有更改只需在一个地方就能完成。

## 使用
在要使用SSI的页面添加一行：`<!--# include file="quote.txt" -->`，即可完成插入
```html
<html>
<head><title>SSI Example</title></head>
<body>
<center>
  <!--# block name="error_footer" -->Sorry, the footer file was not found.<!--# endblock -->
  <h1>Welcome to nginx</h1>
  <!--# include virtual="footer.html" stub="error_footer" -->
</center>
</body>
</html>
```

## 配置
```
location ~* \.shtml$ {
    ssi on;
    ssi_silent_errors on;
    ssi_types text/shtml;
    ssi_silent_errors off;
    ssi_value_length 256;
    ssi_ignore_recycled_buffers off;
    ssi_min_file_chunk 1024;
    ssi_last_modified off;

    root /usr/local/web/wwwroot;
    index index.shtml;
}
```



# FastCGI
## Python and Nginx
### 准备
1. 安装Python：`apt-get install python python-dev`
2. 安装pip：`apt-get install python-pip`
3. 安装Django：`pip install Django==1.8.2`
`pip install -e django-trunk/`
4. 安装flup：`apt-get install python-flup`

## Starting the FastCGI process manager
`django-admin startproject mysite`
`python manage.py runfcgi method=prefork host=127.0.0.1 port=9000 pidfile=/var/run/django.pid`

## 配置
```
server {
    server_name .website.com;
    listen 80;
    # Insert the path of your Python project public files below
    root /home/website/www;
    index index.html;

    location / {
        fastcgi_pass 127.0.0.1:9000;
        fastcgi_param SCRIPT_FILENAME  $document_root$fastcgi_script_name;
        fastcgi_param PATH_INFO $fastcgi_script_name;
        include fastcgi_params;
    }
}
```


# Index









# Log











# Limits and restrictions
















# Content and encoding












# About your visitors







# Split Clients





# SSL and security