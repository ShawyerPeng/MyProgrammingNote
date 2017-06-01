得到达某处```python
from bs4 import BeautifulSoup  
soup = BeautifulSoup(html_doc, 'html.parser')
1. soup.prettify()


```

------------------------
浏览结构化数据的方法:
```python
soup.title  
# <title>The Dormouse's story</title>```
```python
soup.title.name
# u'title'```
```python,
soup.title.string
# u'The Dormouse's story'```
```python
soup.title.parent.name
# u'head'```
```python
soup.p
# <p class="title"><b>The Dormouse's story</b></p>```
```python
soup.p['class']
# u'title'```
```python
soup.a
# <a class="sister" href="http://example.com/elsie" id="link1">Elsie</a>```
```python
soup.find_all('a')
# [<a class="sister" href="http://example.com/elsie" id="link1">Elsie</a>,
#  <a class="sister" href="http://example.com/lacie" id="link2">Lacie</a>,
#  <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>]```
```python
soup.find(id="link3")
# <a class="sister" href="http://example.com/tillie" id="link3">Tillie</a>```



-----------------------

## 输出soup对象的html标签的方法：
1. `soup.prettify()`
2. `soup.html`
3. `soup.contents`
4. `soup`  

另外使用soup+标签名称可以获取html标签中第一个匹配的标签内容，举例：  
`print soup.p`输出结果为：`<p class="title"><b>The Dormouse's story</b></p>`  
`print soup.p.string` 输出标签的内容结果为：`The Dormouse's story`

另外输出标签内容还可以使用get_text()函数：
```python
pid = soup.find(href=re.compile("^http:")) #使用re正则匹配 后面有讲
p1=soup.p.get_text()
The Dormouse's story```

## 通过get函数获得标签的属性：
```python
soup=BeautifulSoup(html,'html.parser')
pid = soup.findAll('a',{'class':'sister'})
for i in pid:
    print i.get('href') #对每项使用get函数取得tag属性值```


------------------

findAll(name, attrs, recursive, text, limit, **kwargs)
