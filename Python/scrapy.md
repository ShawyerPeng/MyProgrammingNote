## 具体用法
### 1. from scrapy.selector import Selector
参考：[Scrapy selector介绍](http://blog.csdn.net/dawnranger/article/details/50037703)  

1. 从text构建
    ```python
    body = '<html><body><span>good</span></body></html>'
    Selector(text=body).xpath('//span/text()').extract()
    ```

2. 从response构建
    ```
    response = HtmlResponse(url='http://example.com', body=body)
    Selector(response=response).xpath('//span/text()').extract()
    ```  
    快捷方法：`response.selector.xpath('//span/text()').extract()`  
    更快捷方法：`response.xpath('//title/text()')`  
    `response.css('title::text')`

### 2. from scrapy.selector import HtmlXPathSelector
```python
hxs = HtmlXPathSelector(response)
sites = hxs.path('//fieldset/ul/li')
for site in sites:
    title = site.select('a/text()').extract()
```

### 3.from scrapy.http import Request
```python
yield Request(complete_url,
              meta={'cookiejar': response.meta['cookiejar']},
              callback=self.parse_follow,
              errback=self.parse_err)
return [Request(
    "https://www.zhihu.com/#signin",
    meta={'cookiejar': 1},
    callback=self.post_login
)]
```

### 4.from scrapy.http import FormRequest
```python
yield FormRequest("abc.someurl.com",
        formdata=json.dumps({"referenceId":123,"referenceType":456}),
        headers={'content-type':'application/json'},
        callback=self.parseResult2)
        def parse(self, response):
return [FormRequest.from_response(response,
            formdata={'username': 'john', 'password': 'secret'},
            callback=self.after_login)]
request = Request( url, method='POST',
                   body=json.dumps(my_data),
                   headers={'Content-Type':'application/json'} )
```

### 5.scrapy 在不同的抓取级别的Request之间传递参数的办法
```python
import scrapy
from myproject.items import MyItem
class MySpider(scrapy.Spider):
    name = 'myspider'
    start_urls = (
        'http://example.com/page1',
        'http://example.com/page2',
        )

    def parse(self, response):
        # collect `item_urls`
        for item_url in item_urls:
            yield scrapy.Request(item_url, self.parse_item)

    def parse_item(self, response):
        item = MyItem()
        # populate `item` fields
        # and extract item_details_url
        yield scrapy.Request(item_details_url, self.parse_details, meta={'item': item})

    def parse_details(self, response):
        item = response.meta['item']
        # populate more `item` fields
        return item
```
parse是默认的callback, 它返回了一个Request列表，scrapy自动的根据这个列表抓取网页，每当抓到一个网页，就会调用parse_item，parse_item也会返回一个列表，scrapy又会根据这个列表去抓网页，并且抓到后调用parse_details。  

`def parse`是个迭代器，拿来多次执行迭代输出的内容，里面的第一个`for(yield)`是抓取当前response中所有的h3实体，第二个`for(yield)`是抓取当前response的里面链接，再进行子request，不断执行parse，是个递归。整个东西就是抓取所有链接的h3实体。

为了让这样的工作更容易，scrapy提供了另一个spider基类，利用它我们可以方便的实现自动抓取链接. 我们要用到CrawlSpider  

[scrapy使用yield返回Request的步骤是怎么样的](https://www.oschina.net/question/2254016_238539)

### 6.log
```python
def after_login(self, response):
    # check login succeed before going on
    if "authentication failed" in response.body:
        self.log("Login failed", level=log.ERROR)
        return
```



---

    scrapy.cfg: 项目的配置文件  
    tutorial/: 该项目的python模块。之后您将在此加入代码。  
    tutorial/items.py: 项目中的item文件.  
    tutorial/pipelines.py: 项目中的pipelines文件.  
    tutorial/settings.py: 项目的设置文件.  
    tutorial/spiders/: 放置spider代码的目录.
`cd myproject` -> 进入到项目目录中  


1. `scrapy startproject myproject` -> 创建一个新的Scrapy项目(在myproject目录中创建一个Scrapy项目)  
2. `scrapy genspider [-t template] <name> <domain>` -> 创建一个新的spider  
    `scrapy genspider mydomain mydomain.com`  
3. `scrapy crawl <spider>` -> 使用spider进行爬取  
    `scrapy crawl myspider`  
4. `scrapy check [-l] <spider>` -> 运行contract检查
    `scrapy check [-l] first_spider`
5. `scrapy list` -> 列出当前项目中所有可用的spider。每行输出一个spider  
    `scrapy list`  
6. `scrapy edit <spider>` -> 使用 EDITOR 中设定的编辑器编辑给定的spider.  
该命令仅仅是提供一个快捷方式。开发者可以自由选择其他工具或者IDE来编写调试spider
    `scrapy edit spider1`  
7. `scrapy fetch <url>` -> 使用Scrapy下载器(downloader)下载给定的URL，并将获取到的内容送到标准输出。  
该命令以spider下载页面的方式获取页面。例如，如果spider有 USER_AGENT 属性修改了 User Agent，该命令将会使用该属性。  
因此，您可以使用该命令来查看spider如何获取某个特定页面。  
该命令如果非项目中运行则会使用默认Scrapy downloader设定。  
    `scrapy fetch --nolog http://www.example.com/some/page.html`   
8. `scrapy view <url>` -> 在浏览器中打开给定URL，并以Scrapy spider获取到的形式展现。   
有些时候spider获取到的页面和普通用户看到的并不相同。因此该命令可以用来检查spider所获取到的页面，并确认这是您所期望的  
    `scrapy view http://www.example.com/some/page.html`
9. `scrapy shell [url]` -> 以给定的URL(如果给出)或者空(没有给出URL)启动Scrapy shell。 查看 Scrapy终端(Scrapy shell) 获取更多信息  
    `scrapy shell http://www.example.com/some/page.html`  
10. `scrapy parse <url> [options]` -> 获取给定的URL并使用相应的spider分析处理。  
如果您提供 --callback 选项，则使用spider的该方法处理，否则使用 parse  
    `scrapy parse http://www.example.com/ -c parse_item`  
11. `scrapy settings [options]` -> 获取Scrapy的设定。  
在项目中运行时，该命令将会输出项目的设定值，否则输出Scrapy默认设定  
    `scrapy settings --get BOT_NAME/DOWNLOAD_DELAY`  
12. `scrapy runspider <spider_file.py>` -> 在未创建项目的情况下，运行一个编写在Python文件中的spider  
    `scrapy runspider myspider.py`
13. `scrapy version [-v]` ->输出Scrapy版本。配合 -v 运行时，该命令同时输出Python, Twisted以及平台的信息，方便bug提交  
14. `scrapy deploy [ <target:project> | -l <target> | -L ]` -> 将项目部署到Scrapyd服务。查看、部署您的项目  
15. `scrapy bench` -> 运行benchmark测试，可用来检测scrapy是否安装成功    

--------------

scrapy crawl dmoz
`scrapy runspider somefile.py -o xx.csv`
`scrapy crawl dmoz -o items.json` -> 爬取并保存爬取到的数据(采用JSON格式对爬取的数据进行序列化，生成items.json文件)


response.xpath('//title/text()').extract()
response.css('title::text').extract()


------------
1. `scrapy startproject tutorial`
2. `cd tutorial`
3. `scrapy genspider dmoz_spider dmoz.org`  
4. 修改`items.py`
    ```python
    from scrapy.item import Item, Field

    class DmozItem(Item):
        name = Field()
        description = Field()
        url = Field()
    ```

5. 创建`spider.py`

    ```python
    import scrapy
    from scrapy.spider import BaseSpider
    from scrapy.selector import HtmlXPathSelector
    from tutorial.items import DmozItem

    class DmozSpider(scrapy.Spider):
        name = "dmoz"
        allowed_domains = ["dmoz.org"]
        start_urls = [
            "http://www.dmoz.org/Computers/Programming/Languages/Python/Books/",
            "http://www.dmoz.org/Computers/Programming/Languages/Python/Resources/"
        ]

        def parse(self, response):
            hxs = HtmlXPathSelector(response)
            sites = hxs.select('//ul/li')
            items = []  #Spiders希望将其抓取的数据存放到Item对象中。为了返回我们抓取数据，spider的最终代码应当是这样
            for site in sites:
                item = DmozItem()
                item['title'] = site.path('a/text()').extract()
                item['link'] = site.path('a/@href').extract()
                item['desc'] = site.path('text()').extract()
                items.append(item)
            return items
    ```



## txt保存数据
```python
f=open('liuyifei_pic_address.txt','wb')
def parse(self,response):
	hxs=HtmlXPathSelector(response)
	sites=hxs.select('//ul/li/div/a/img/@src').extract()
	items=[]
	for site in sites:
	    site=site.replace('thumb','raw')
	    self.f.write(site)
	    self.f.write('\r\n')
```

## json数据保存
直接在命令行里用参数执行即可：
`scrapy crawl liuyifei -o image.json -t json`









## 参考资料
1. [Scrapy0.24中文文档](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html)
2. [python爬虫----（scrapy框架提高（1），自定义Request爬取）](https://my.oschina.net/lpe234/blog/342741)
3. [使用scrapy框架爬取自己的博文](http://www.cnblogs.com/huhuuu/p/3706994.html)
4. [爬豆瓣](http://www.data321.com/yanfaguanli/2016051513/HtmlXPathSelector)
5. [python爬虫框架scrapy实例详解](http://www.pythontab.com/html/2013/pythonhexinbiancheng_0814/541.html)
6. [Python爬虫(六)--Scrapy框架学习](http://www.jianshu.com/p/078ad2067419)










```python
import scrapy
from scrapy.contrib.spiders import CrawlSpider
from scrapy.http import Request
from scrapy.selector import Selector

xxx = selector.xpath(xxxx).extract()
```
