`#coding=utf-8` 防止报错：UnicodeEncodeError: 'gbk' codec can't encode character  
`from pyquery import PyQuery as pq`  
`from lxml import etree`

#### 可加载一段HTML字符串，或一个HTML文件，或是一个url地址，或lxml.etree：
```python
htmlblock = "<html><title>hello</title></html>"
filename = "path_to_html_file"
url = "http://www.baidu.com"

d = pq(htmlblock)
d = pq(filename)
d = pq(url)
d = pq(etree.fromstring("<html></html>"))
```

直接输出截取串的html对象，看着更加直观  

    v_source = pq(url='http://yunvs.com/list/mai_1.html')
    for data in v_source('tr'):
        print(pq(data).html())
        print(pq(data).text()) #以text文本的方式输出，这样就去掉了html标记

#### pq(d)('a[class= ""]').attr('')  
`Aurl= pq(data)('a[class= "j_th_tit " ]').attr('href')`

---

---

---


## 常用方法：
```python
#1.html()和text() ——获取相应的HTML块或文本块
d = pq("<head><title>hello</title></head>")
d('head').html()    #返回<title>hello</title>
d('head').text()    #返回hello

#2.根据HTML标签获取元素。注意：当获取到的元素不只一个时，html()、text()方法只返回首个元素的相应内容块
d = pq('<div><p>test 1</p><p>test 2</p></div>')
print(d('p'))           #返回<p>test 1</p><p>test 2</p>
print(d('p').html())    #返回test 1

#3.eq(index) ——根据给定的索引号得到指定元素。接上例，若想得到第二个p标签内的内容，则可以：
print(d('p').eq(1).html()) #返回test 2

#4.filter() ——根据类名、id名得到指定元素，例：
d = pq("<div><p id='1'>test 1</p><p class='2'>test 2</p></div>")
d('p').filter('#1') #返回[<p#1>]
d('p').filter('.2') #返回[<p.2>]

#5.find() ——查找嵌套元素，例：
d = pq("<div><p id='1'>test 1</p><p class='2'>test 2</p></div>")
d('div').find('p')#返回[<p#1>, <p.2>]
d('div').find('p').eq(0)#返回[<p#1>]

#6.直接根据类名、id名获取元素，例：
d = pq("<div><p id='1'>test 1</p><p class='2'>test 2</p></div>")
d('#1').html()#返回test 1
d('.2').html()#返回test 2

#7.获取属性值，例：
d = pq("<p id='my_id'><a href='http://hello.com'>hello</a></p>")
d('a').attr('href')#返回http://hello.com
d('p').attr('id')#返回my_id

#8.修改属性值，例：
d('a').attr('href', 'http://baidu.com')把href属性修改为了baidu


#9.addClass(value) ——为元素添加类，例：
d = pq('<div></div>')
d.addClass('my_class')#返回[<div.my_class>]

#10.hasClass(name) #返回判断元素是否包含给定的类，例：
d = pq("<div class='my_class'></div>")
d.hasClass('my_class')#返回True

#11.children(selector=None) ——获取子元素，例：
d = pq("<span><p id='1'>hello</p><p id='2'>world</p></span>")
d.children()#返回[<p#1>, <p#2>]
d.children('#2')#返回[<p#2>]

#12.parents(selector=None)——获取父元素，例：
d = pq("<span><p id='1'>hello</p><p id='2'>world</p></span>")
d('p').parents()            #返回[<span>]
d('#1').parents('span')     #返回[<span>]
d('#1').parents('p')        #返回[]

#13.clone() ——返回一个节点的拷贝

#14.empty() ——移除节点内容

#15.nextAll(selector=None) ——返回后面全部的元素块，例：
d = pq("<p id='1'>hello</p><p id='2'>world</p><img scr='' />")
d('p:first').nextAll()#返回[<p#2>, <img>]
d('p:last').nextAll()#返回[<img>]

#16.not_(selector) ——返回不匹配选择器的元素，例：
d = pq("<p id='1'>test 1</p><p id='2'>test 2</p>")
d('p').not_('#2')#返回[<p#1>]
```





### 与requests库结合使用：
```python
import requests
from pyquery import PyQuery as pq

r = requests.get('http://www.meipai.com/media/596371059')
d = pq(r.content)
print(d('meta[property="og:video:url"]').attr('content'))
```


---

---

---



## 参考资料
[PyQuery 1.2.4 complete API](http://pythonhosted.org//pyquery/api.html)  
[pyquery: 基于python和jquery语法操作XML](http://www.geoinformatics.cn/lab/pyquery/)  
[这一年Python总结](http://www.jianshu.com/p/036e589119d8)  
[Python爬虫利器六之PyQuery的用法](http://cuiqingcai.com/2636.html)
