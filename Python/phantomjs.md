#### hello,world!
1. `console.log()`打印到终端
2. `phantom.exit()`中止执行

#### Page Loading
```
var page = require('webpage').create();
page.open('http://example.com', function(status) {
  console.log("Status: " + status);
  if(status === "success") {
    page.render('example.png');
  }
  phantom.exit();
});
```
终端输入参数：
```
address = system.args[1];
page.open(address, function(status)
```

`t = Date.now()`返回当前时间

#### Code Evaluation
利用 evaluate 方法我们可以获取网页的源代码。这个执行是“沙盒式”的，它不会去执行网页外的 JavaScript 代码。evalute 方法可以返回一个对象，然而返回值仅限于对象，不能包含函数（或闭包）
```javascript
var page = require('webpage').create();
page.open(url, function(status) {
  var title = page.evaluate(function() {
    return document.title;
  });
  console.log('Page title is ' + title);
  phantom.exit();
});
```
获取网页的源代码：`page.evaluate()`
获取网站标题：`document.title`







































## 页面加载
```
var page = require('webpage').create();
page.open('http://github.com/', function() {
    page.render('github.png');
    phantom.exit();
});
```

```javascript
var page = require('webpage').create();
page.open('http://cuiqingcai.com', function (status) {
    console.log("Status: " + status);
    if (status === "success") {
        page.render('example.png');
    }
    phantom.exit();
});
```

## 测试页面加载速度
```javascript
var page = require('webpage').create(),
  system = require('system'),
  t, address;

if (system.args.length === 1) {
  console.log('Usage: loadspeed.js <some URL>');
  phantom.exit();
}

t = Date.now();
address = system.args[1];
page.open(address, function(status) {
  if (status !== 'success') {
    console.log('FAIL to load the address');
  } else {
    t = Date.now() - t;
    console.log('Loading ' + system.args[1]);
    console.log('Loading time ' + t + ' msec');
  }
  phantom.exit();
});
```

## 页面截图
viewportSize 是视区的大小，你可以理解为你打开了一个浏览器，然后把浏览器窗口拖到了多大。  
clipRect 是裁切矩形的大小，需要四个参数，前两个是基准点，后两个参数是宽高。  
`page.viewportSize = { width: 1024, height: 768 };`  
`page.clipRect = { top: 0, left: 0, width: 1024, height: 768 };`
