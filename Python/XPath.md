## 节点：XML文件的最小构成单位，一共分成7种
    - element（元素节点）
    - attribute（属性节点）
    - text （文本节点）
    - namespace （名称空间节点）
    - processing-instruction （处理命令节点）
    - comment （注释节点）
    - root （根节点）
-----

## xpath表达式的基本格式
    - 斜杠（/）作为路径内部的分割符。
    - 同一个节点有绝对路径和相对路径两种写法。
    - 绝对路径（absolute path）必须用"/"起首，后面紧跟根节点，比如/step/step/...。
    - 相对路径（relative path）则是除了绝对路径以外的其他写法，比如 step/step，也就是不使用"/"起首。
    - "."表示当前节点。
    - ".."表示当前节点的父节点
-----

## 选择节点的基本规则：
- nodename（节点名称）：表示选择该节点的所有子节点
- "/"：表示选择根节点
- "//"：表示选择任意位置的某个节点
- "@"： 表示选择某个属性
-----

## 通配符：
    # "*"表示匹配任何元素节点。
    # "@*"表示匹配任何属性值。
    # node()表示匹配任何类型的节点。
-----

## XPath轴(XPath Axes)可定义某个相对于当前节点的节点集：
    1. child  选取当前节点的所有子元素
    2. parent  选取当前节点的父节点
    3. descendant 选取当前节点的所有后代元素（子、孙等）
    4. ancestor  选取当前节点的所有先辈（父、祖父等）
    5. descendant-or-self  选取当前节点的所有后代元素（子、孙等）以及当前节点本身
    6. ancestor-or-self  选取当前节点的所有先辈（父、祖父等）以及当前节点本身
    7. preceding-sibling 选取当前节点之前的所有同级节点
    8. following-sibling 选取当前节点之后的所有同级节点
    9. preceding   选取文档中当前节点的开始标签之前的所有节点
    10. following   选取文档中当前节点的结束标签之后的所有节点
    11. self  选取当前节点
    12. attribute  选取当前节点的所有属性
    13. namespace 选取当前节点的所有命名空间节点
-----

## 功能函数：
    starts-with
    xpath('//div[starts-with(@id,'ma')]')
    选取id值以ma开头的div节点

    contains
    xpath('//div[contains(@id,'ma')]')
    选取id值包含ma的div节点

    and
    xpath('//div[contains(@id,'ma') and contains(@id,'in')]')
    选取id值包含ma和in的div节点

    text()
    xpath('//div[contains(text(),'ma')]')
    选取节点文本包含ma的div节点

-----

## 自己添加
```html
<div id="test3">
    我左青龙，
        <span id="tiger">右白虎，上朱雀，下玄武。老牛在当中，</span>龙头在胸口。
<div>```
解析text内容:  
使用xpath的string(.)  
以第三段代码为例：  
data = selector.xpath('//div[@id="test3"]')  
info = data.xpath('string(.)').extract()[0]  
这样，就可以把“我左青龙，右白虎，上朱雀，下玄武。老牛在当中，龙头在胸口”整个句子提取出来，赋值给info变量。  
