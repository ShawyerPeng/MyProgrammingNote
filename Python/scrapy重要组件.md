Spider是一个类，它定义了怎样爬取一个网站，包括怎样跟踪链接、怎样提取数据  
generating the initial Requests
parse the response
using selector
store item


基类：  
属性：-------------
* scrapy.Spider  
* name:spider的名称，唯一  
* allowed_domains:允许的域名  
* start_urls:初始urls  
* custom_settings:个性化设置，会覆盖全局设置  
* crawler:抓取器，spider将绑定到它上面  
* settings:配置实例，包含工程中所有的配置变量  
* logger:日志实例

方法：--------------
* from_crawler():创建spiders  
* start_requests():生成初始的requests  
* make_requests_from_url(url):根据url生成一个request  
* parse(response):解析网页内容  
* log(message[,level,component]):记录日志，这里请使用logger属性记录日志，self.logger.info("visited success")  
* closed(reason):当spider关闭时调用的方法  


子类：CrawlSpider  XMLFeedSpider  CSVFeedSpider SitemapSpider  

CrawlSpider:最常用的spider，用于抓取普通网页
新成员：rules  parse_start_url(response)
