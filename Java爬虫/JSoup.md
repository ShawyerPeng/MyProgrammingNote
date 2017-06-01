# 下载
```xml
<!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.10.2</version>
</dependency>
```

# 使用
## Input
`Document doc = Jsoup.parse(String html);`：String
`Document doc = Jsoup.parseBodyFragment(html);`：Body Fragment
`Document doc = Jsoup.connect(String url)`：URL
`Document doc = Jsoup.parse(File in, String charsetName, String baseUri)`：File

```java
String html = "<html><head><title>First parse</title></head>"
  + "<body><p>Parsed HTML into a doc.</p></body></html>";
// 1
Document doc = Jsoup.parse(html);
// 2
Document doc = Jsoup.parseBodyFragment(html);
Element body = doc.body();
// 3
Document doc = Jsoup.connect("http://example.com/").get();
String title = doc.title();
// 4
File input = new File("/tmp/input.html");
Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
// 5
```

## Extracting data
### Use DOM methods to navigate a document
1. Finding elements
* `getElementById(String id)`：根据id属性的属性值来获取元素
* `getElementsByTag(String tag)`：根据tag名来获取元素
* `getElementsByClass(String className)`：根据class名来获取元素
* `getElementsByAttribute(String key)`：根据属性名key来获取元素
* `getElementsByAttributeValue(String key,String value)`：根据属性名key=value这个属性值来获取元素
* `getElementByAttributeValueContaining(String key,String match)`：根据属性名key的属性值 包含 match属性值来获取元素
* `getElementByAttributeValueStarting(String key,String valuePrefix)`：根据属性名key的属性 以valuePrefix开头的属性值来获取元素
* Element siblings: `siblingElements()`, `firstElementSibling()`, `lastElementSibling()`; `nextElementSibling()`, `previousElementSibling()`
* Graph: `parent()`, `children()`, `child(int index)`

2. Element data
* `attr(String key)` to get and `attr(String key, String value)` to set attributes
* `attributes()` to get all attributes
* `id()`, `className()` and `classNames()`
* `text()` to get and `text(String value)` to set the text content
* `html()` to get and `html(String value)` to set the inner HTML content
* `outerHtml()` to get the outer HTML value
* `data()` to get data content (e.g. of script and style tags)
* `tag()` and `tagName()`

3. Manipulating HTML and text
* `append(String html)`, `prepend(String html)`
* `appendText(String text)`, `prependText(String text)`
* `appendElement(String tagName)`, `prependElement(String tagName)`
* `html(String value)`

### Use selector-syntax to find elements
`Element.select(String selector)`
`Elements.select(String selector)` 



```java
File input = new File("/tmp/input.html");
Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

Elements links = doc.select("a[href]"); // a with href
Elements pngs = doc.select("img[src$=.png]");
// img with src ending .png

Element masthead = doc.select("div.masthead").first();
// div with class=masthead

Elements resultLinks = doc.select("h3.r > a"); // direct a after h3
```

1. Selector overview
* `tagname`: find elements by tag, e.g. a
* `ns|tag`: find elements by tag in a namespace, e.g. fb|name finds <fb:name> elements
* `#id`: find elements by ID, e.g. #logo
* `.class`: find elements by class name, e.g. .masthead
* `[attribute]`: elements with attribute, e.g. [href]
* `[^attr]`: elements with an attribute name prefix, e.g. [^data-] finds elements with HTML5 dataset attributes
* `[attr=value]`: elements with attribute value, e.g. [width=500] (also quotable, like [data-name='launch sequence'])
* `[attr^=value]`, `[attr$=value]`, `[attr*=value]`: elements with attributes that start with, end with, or contain the value, e.g. [href*=/path/]
* `[attr~=regex]`: elements with attribute values that match the regular expression; e.g. img[src~=(?i)\.(png|jpe?g)]
* `*`: all elements, e.g. *

2. Selector combinations
* `el#id`: elements with ID
* `el.class`: elements with class, e.g. div.masthead
* `el[attr]`: elements with attribute, e.g. a[href]
Any combination, e.g. `a[href].highlight`
* `ancestor child`: child elements that descend from ancestor, e.g. .body p finds p elements anywhere under a block with class "body"
* `parent > child`: child elements that descend directly from parent, e.g. div.content > p finds p elements; and body > * finds the direct children of the body tag
* `siblingA + siblingB`: finds sibling B element immediately preceded by sibling A, e.g. div.head + div
* `siblingA ~ siblingX`: finds sibling X element preceded by sibling A, e.g. h1 ~ p
* `el, el, el`: group multiple selectors, find unique elements that match any of the selectors; e.g. div.masthead, div.logo

3. Pseudo selectors
* `:lt(n)`: find elements whose sibling index (i.e. its position in the DOM tree relative to its parent) is less than n; e.g. td:lt(3)
* `:gt(n)`: find elements whose sibling index is greater than n; e.g. div p:gt(2)
* `:eq(n)`: find elements whose sibling index is equal to n; e.g. form input:eq(1)
* `:has(seletor)`: find elements that contain elements matching the selector; e.g. div:has(p)
* `:not(selector)`: find elements that do not match the selector; e.g. div:not(.logo)
* `:contains(text)`: find elements that contain the given text. The search is case-insensitive; e.g. p:contains(jsoup)
* `:containsOwn(text)`: find elements that directly contain the given text
* `:matches(regex)`: find elements whose text matches the specified regular expression; e.g. div:matches((?i)login)
* `:matchesOwn(regex)`: find elements whose own text matches the specified regular expression
Note that the above indexed pseudo-selectors are 0-based, that is, the first element is at index 0, the second at 1, etc

### Extract attributes, text, and HTML from elements
`Node.attr(String key)`：获取属性值
`Element.text()`：获取元素的文本
`Element.html()`或`Node.outerHtml()`：获取HTML

```java
String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
Document doc = Jsoup.parse(html);
Element link = doc.select("a").first();

String text = doc.body().text(); // "An example link"
String linkHref = link.attr("href"); // "http://example.com/"
String linkText = link.text(); // "example""

String linkOuterH = link.outerHtml(); 
// "<a href="http://example.com"><b>example</b></a>"
String linkInnerH = link.html(); // "<b>example</b>"
```

* `Element.id()`
* `Element.tagName()`
* `Element.className()` and `Element.hasClass(String className)`


### Working with URLs
`attr("abs:href")`或`Node.absUrl(String key)`：获取网址的绝对路径
```java
Document doc = Jsoup.connect("http://jsoup.org").get();

Element link = doc.select("a").first();
String relHref = link.attr("href"); // == "/"
String absHref = link.attr("abs:href"); // "http://jsoup.org/"
```

## Modifying data
### Set attribute values
`Element.attr(String key, String value)`
`Elements.attr(String key, String value)`

### Set the HTML of an element

### Setting the text content of elements

## Cleaning HTML
### Sanitize untrusted HTML (to prevent XSS)


# 参考资料
[jsoup](https://jsoup.org/)
[HTML解析器软件](http://renegade24.iteye.com/blog/865197)  
