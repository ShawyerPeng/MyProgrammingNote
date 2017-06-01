`import requests` -> 引用模块  
## 1. 发送请求与传递参数
带参数请求：
```python

#GET参数实例
requests.get('http://www.dict.baidu.com/s', params={'wd': 'python'})  
#或
url = 'http://www.baidu.com'
payload = {'key1': 'value1', 'key2': 'value2'}
headers = { "Accept":"text/html,application/xhtml+xml,application/xml;",
            "Accept-Encoding":"gzip",
            "Accept-Language":"zh-CN,zh;q=0.8",
            "Referer":"http://www.example.com/",
            "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36"
            }  
res1 = requests.get(url, params=payload, headers=headers, timeout=1)

#POST参数实例
requests.post('http://www.itwhy.org/wp-comments-post.php', data={'comment': '测试post'})
files = {'file': open('touxiang.png', 'rb')} #用于发送文件的post属性
files = {'file': ('xxxx,jpg',open('/home/lyb/sjzl.mpg','rb'))}  #设置文件名
#或
url = 'http://www.baidu.com'
data={"user":"user","password":"pass"}
res2 = requests.post(url1, data=data, headers=headers ,files=files)
```


POST发送JSON数据：
```python
import json

r = requests.post('https://api.github.com/some/endpoint', data=json.dumps({'some': 'data'}))

print(r.json())
```

```python
r = requests.get('http://ip.taobao.com/service/getIpInfo.php?ip=122.88.60.28')
print (r.json()['data']['country'])
```

添加代理：
```python
proxies = {
  "http": "http://10.10.1.10:3128",
  "https": "http://10.10.1.10:1080",
}
requests.get("http://www.zhidaow.com", proxies=proxies)
```

一些操作requests返回值的方法：
```python
r.text  #字符串方式的响应体，会自动根据响应头部的字符编码进行解码
r.content   #获得二进制响应内容
r.raw   #获得原始响应内容,需要stream=True
r.raw.read(50)
type(r.text)    #返回解码成unicode的内容
r.url
r.history   #追踪重定向
r.cookies
r.cookies['example_cookie_name']
r.headers   #以字典对象存储服务器的响应头，但该字典比较特殊，字典键不区分大小写，若键不存在返回None
r.headers['Content-Type']
r.headers.get('content-type')
r.json  #讲返回内容编码为json
r.encoding  #返回内容编码
r.status_code   #返回http状态码
r.raise_for_status()    #返回错误状态码
```
若编码出错，则`r.text.encode('utf-8')`

------

### Session()

```python
#初始化一个session对象
s = requests.Session()

#使用这个session对象来进行访问
prepped1 = requests.Request('POST', url1,
    data=data,
    headers=headers
).prepare()
s.send(prepped1)
#或 r = s.post(url,data = user)
```

其他的一些访问方式：
    >>> r = requests.put("http://httpbin.org/put")
    >>> r = requests.delete("http://httpbin.org/delete")
    >>> r = requests.head("http://httpbin.org/get")
    >>> r = requests.options("http://httpbin.org/get")



-----

----

----

## 总结
该笔记描述那么多方面，好像只是讲了requests模块的参数而已。这也说明了它的强大，但是前提是你必须懂对应的原理。参数如下：  

    json: json数据传到requests的body
    headers: HTTP Headers的字典传到requests的header
    cookies: 可以使用字典或者CookieJar object
    files: 字典{'name': file-tuple} 来实现multipart encoding upload, 2参数元组 ('filename', fileobj), 3参数元组 ('filename', fileobj, 'content_type')或者 4参数元组 ('filename', fileobj, 'content_type', custom_headers), 其中'content-type' 用于定于文件类型和custom_headers文件的headers
    auth: Auth元组定义用于Basic/Digest/Custom HTTP Auth
    timeout: 连接等待时长
    allow_redirects: 布尔型， True代表POST/PUT/DELETE只有的重定向是允许的
    proxies: 代理的地址
    verify: 用于认证SSL证书
    stream: False代表返回内容立刻下载
    cert: String代表ssl client证书地址(.pem) Tuple代表('cert', 'key')键值对


## 其他参考资料：
[网页数据压缩deflate&gzip](http://www.jianshu.com/p/2c2781462902)
