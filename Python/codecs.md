#### 错误用例  

```python
# -*- coding: utf-8 -*-
#http://www.qiushibaike.com/8hr/page/1?s=4603425
import urllib2
from bs4 import BeautifulSoup
page=1
xiubai=open(r'xiubai.txt','w+')
for page in range(1,11):
    url="http://www.qiushibaike.com/8hr/page/"+str(page)+"?s=4603425"
    soup = BeautifulSoup(urllib2.urlopen(url).read())
    for result in soup.findAll("div", "content", title=True):
        xiubai.write(result.text)
```

#### 操作文件，读写数据，涉及到非ASCII的话，最好用codes模块操作，其会自动帮你处理不同的编码，效果最好。
```python
import codecs;
yourStrToSave = "your data string";
# 'a+': read,write,append
# 'w' : clear before, then write
outputFp = codecs.open("outputFile.txt", 'w', 'utf-8');
outputFp.write(yourStrToSave);
outputFp.flush();
outputFp.close();
```

#### 对于你此处获得html，并用bs处理html的过程，实际上，更好的做法是：
1. 搞清楚本身html的charset  
`<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />`
2. 传递给BeautifulSoup去解析为soup  
`soup = BeautifuSoup(yourHtml,fromEncoding="GBK")`
3. 从find处理的soup节点，通过get_text()获得对应的内容
4. 将获得的字符串内容，用codes保存到文件


#### 完整代码
```python
import codecs;

# 'a+': read,write,append
# 'w' : clear before, then write
outputFp = codecs.open("outputFile.txt", mode='w', encoding='utf-8');
for result in soup.findAll("div", "content", title=True):
    outputFp.write(result.get_text())
outputFp.flush();
outputFp.close();
```
其中，bs中通过get_text()得到的字符串，已经是unicode了
