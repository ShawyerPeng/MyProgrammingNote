# AJAX应用的五个步骤
1. 建立XMLHttpRequest对象
2. 设置回调函数
3. 使用open方法与服务器建立连接
4. 向服务器端发送数据
5. 在回调函数针对不同响应状态进行处理

# 小练习
1. 去参数
2. 检查参数是否有问题
3. 校验操作
4. 这一步需要将用户感兴趣的数据返回给页面端，而不是将一个新的页面发送给用户

# 一、文本格式  
`AJAXServer.java`
```java
public class AJAXServer extends HttpServlet{
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try{
            httpServletResponse.setContentType("text/html;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();

            // 1.取参数
            String old = httpServletRequest.getParameter("name");
            // 2.检查参数是否有问题
            if (old == null || old.length() == 0) {
                out.println("用户名不能为空");
            } else {
                // 3.校验操作
                String name = old;
                if (name.equals("wangxingkui")) {
                    //4.和传统应用不同之处。这一步需要将用户感兴趣的数据返回给页面段，而不是将一个新的页面发送给用户
                    //写法没有变化，本质发生了改变
                    out.println("用户名[" + name + "]已经存在，请使用其他用户名, " + temp);
                } else {
                    out.println("用户名[" + name + "]尚未存在，可以使用该用户名注册, " + temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
```

`verify.js`  
```javascript
//定义用户名校验的方法
function verify(){
    // 1.获取文本框中的内容
    var jqueryObj = $("#userName");
    //获取节点的值
    var userName = jqueryObj.val();

    // 2.将文本框中的数据发送给服务器段的servlet
    //使用jquery的XMLHTTPrequest对象get请求的封装：get(url,data,callback)
    $.get("AJAXServer?name=" + userName,null,callback);
}

//回调函数
function callback(data) {
    // 3.接收服务器端返回的数据
    alert(data);
    // 4.将服务器段返回的数据动态的显示在页面上
    //找到保存结果信息的节点
    var resultObj = $("#result");
    //动态的改变页面中div节点中的内容
    resultObj.html(data);
}
```

# jQuery的总结与简化调用
```javascript
// 复杂版本
function verify() {
    //解决中文乱码问题的方法1，页面端发出的数据作一次encodeURI，服务器段使用new String(old.getBytes("iso8859-1"),"UTF-8");
    //解决中文乱码问题的方法2，页面端发出的数据作两次encodeURI，服务器段使用URLDecoder.decode(old,"UTF-8")
    var url = "AJAXServer?name=" + encodeURI(encodeURI($("#userName").val()));
    url = convertURL(url);

    $.get(url,null,function(data){
        $("#result").html(data);
});
}

// 简化版本
function verify() {
    $.get("AJAXServer?name=" + $("#userName").val(),null,function(data){
        $("#result").html(data);
    });
}
```

## 1. XHR对象的创建
```javascript
var xmlhttp;
function verify() {
    // 0.使用dom的方式获取文本框中的值
    var userName = document.getElementById("userName").value;

    // 1.创建XMLHttpRequest对象
    //这是XMLHttpReuquest对象无部使用中最复杂的一步，需要针对IE和其他类型的浏览器建立这个对象的不同方式写不同的代码
    if (window.XMLHttpRequest) {
        //针对FireFox，Mozillar，Opera，Safari，IE7，IE8
        xmlhttp = new XMLHttpRequest();
        //针对某些特定版本的mozillar浏览器的BUG进行修正
        if (xmlhttp.overrideMimeType) {
            xmlhttp.overrideMimeType("text/xml");
        }
    } else if (window.ActiveXObject) {
        //针对IE6，IE5.5，IE5
        //两个可以用于创建XMLHTTPRequest对象的控件名称，保存在一个js的数组中
        //排在前面的版本较新
        var activexName = ["MSXML2.XMLHTTP","Microsoft.XMLHTTP"];
        for (var i = 0; i < activexName.length; i++) {
            try{
                //取出一个控件名进行创建，如果创建成功就终止循环
                //如果创建失败，回抛出异常，然后可以继续循环，继续尝试创建
                xmlhttp = new ActiveXObject(activexName[i]);
                break;
            } catch(e){
            }
        }
    }
    //确认XMLHTtpRequest对象创建成功
    if (!xmlhttp) {
        alert("XMLHttpRequest对象创建失败!!");
        return;
    } else {
        alert(xmlhttp.readyState);
    }
}
```

## 2. 使用XHR对象发送和接受数据
```javascript
//--------------------------------------------------------------------------------------------第一步的代码
var xmlhttp;
function verify() {
    // 0.使用dom的方式获取文本框中的值
    var userName = document.getElementById("userName").value;

    // 1.创建XMLHttpRequest对象
    //这是XMLHttpReuquest对象无部使用中最复杂的一步，需要针对IE和其他类型的浏览器建立这个对象的不同方式写不同的代码
    if (window.XMLHttpRequest) {
        //针对FireFox，Mozillar，Opera，Safari，IE7，IE8
        xmlhttp = new XMLHttpRequest();
        //针对某些特定版本的mozillar浏览器的BUG进行修正
        if (xmlhttp.overrideMimeType) {
            xmlhttp.overrideMimeType("text/xml");
        }
    } else if (window.ActiveXObject) {
        //针对IE6，IE5.5，IE5
        //两个可以用于创建XMLHTTPRequest对象的控件名称，保存在一个js的数组中
        //排在前面的版本较新
        var activexName = ["MSXML2.XMLHTTP","Microsoft.XMLHTTP"];
        for (var i = 0; i < activexName.length; i++) {
            try{
                //取出一个控件名进行创建，如果创建成功就终止循环
                //如果创建失败，回抛出异常，然后可以继续循环，继续尝试创建
                xmlhttp = new ActiveXObject(activexName[i]);
                break;
            } catch(e){
            }
        }
    }
    //确认XMLHTtpRequest对象创建成功
    if (!xmlhttp) {
        alert("XMLHttpRequest对象创建失败!!");
        return;
    } else {
        alert(xmlhttp.readyState);
    }
//--------------------------------------------------------------------------------------------第一步的代码

    // 2.注册回调函数
    //注册回调函数时，之需要函数名，不要加括号
    //我们需要将函数名注册，如果加上括号，就会把函数的返回值注册上，这是错误的
    xmlhttp.onreadystatechange = callback;

    // 3.设置连接信息
    //第一个参数表示http的请求方式，支持所有http的请求方式，主要使用get和post
    //第二个参数表示请求的url地址，get方式请求的参数也在url中
    //第三个参数表示采用异步还是同步方式交互，true表示异步
    xmlhttp.open("GET","AJAXServer?name="+ userName,true);

    //POST方式请求的代码
    //xmlhttp.open("POST","AJAXServer",true);
    //POST方式需要自己设置http的请求头
    //xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    //POST方式发送数据
    //xmlhttp.send("name=" + userName);

    // 4.发送数据，开始和服务器端进行交互
    //同步方式下，send这句话会在服务器段数据回来后才执行完
    //异步方式下，send这句话会立即完成执行
    xmlhttp.send(null);
}

//回调函数
function callback() {
    // 5.接收响应数据
    //判断对象的状态是交互完成
    if (xmlhttp.readyState == 4) {
        //判断http的交互是否成功
        if (xmlhttp.status == 200) {
            //获取服务器端返回的数据
            //获取服务器端输出的纯文本数据
            var responseText = xmlhttp.responseText;
            //将数据显示在页面上
            //通过dom的方式找到div标签所对应的元素节点
            var divNode = document.getElementById("result");
            //设置元素节点中的html内容
            divNode.innerHTML = responseText;
        } else {
            alert("出错了！！！");
        }
    }
}
```


# 二、xml格式（利用XHR接受和处理XML数据）  
`AJAXXMLServer.java`
```javascript
public class AJAXXMLServer extends HttpServlet{
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try{
            //修改点1111111111111111111111111111111----响应的Content-Type必须是text/xml
            httpServletResponse.setContentType("text/xml;charset=utf-8");

            PrintWriter out = httpServletResponse.getWriter();
            //1.取参数
            String old = httpServletRequest.getParameter("name");
            //修改点2222222222222222222222222222222-----返回的数据需要拼装成xml格式
            StringBuilder builder = new StringBuilder();
            builder.append("<message>");
            //2.检查参数是否有问题
            if(old == null || old.length() == 0){
                builder.append("用户名不能为空").append("</message>");
            } else{
                //3.校验操作
                String name = old;

                if(name.equals("wangxingkui")){
                    //4。和传统应用不同之处。这一步需要将用户感兴趣的数据返回给页面段，而不是将一个新的页面发送给用户
                    //写法没有变化，本质发生了改变
                    builder.append("用户名[" + name + "]已经存在，请使用其他用户名").append("</message>");
                } else{
                    builder.append("用户名[" + name + "]尚未存在，可以使用该用户名注册").append("</message>");
                }
                out.println(builder.toString());
                System.out.println(builder.toString());

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
```

`verifyxml.js`
```javascript
function callback() {
    //alert(xmlhttp.readyState);
    // -----------------5.接收响应数据-----------------
    //判断对象的状态是交互完成
    if (xmlhttp.readyState == 4) {
        //判断http的交互是否成功
        if (xmlhttp.status == 200) {
            //使用responseXML的方式来接收XML数据对象的DOM对象
            var domObj = xmlhttp.responseXML;
            if (domObj) {
                //<message>123123123</message>
                //dom中利用getElementsByTagName可以根据标签名来获取元素节点，返回的是一个数组
                var messageNodes = domObj.getElementsByTagName("message");
                if (messageNodes.length > 0) {
                    //获取message节点中的文本内容
                    //message标签中的文本在dom中是message标签所对应的元素节点的字节点，firstChild可以获取到当前节点的第一个子节点
                    //通过以下方式就可以获取到文本内容所对应的节点
                    var textNode = messageNodes[0].firstChild;
                    //对于文本节点来说，可以通过nodeValue的方式返回文本节点的文本内容
                    var responseMessage = textNode.nodeValue;

                    //将数据显示在页面上
                    //通过dom的方式找到div标签所对应的元素节点
                    var divNode = document.getElementById("result");
                    //设置元素节点中的html内容
                    divNode.innerHTML = responseMessage;
                } else {
                    alert("XML数据格式错误，原始文本内容为：" + xmlhttp.responseText);
                }
            } else {
                alert("XML数据格式错误，原始文本内容为：" + xmlhttp.responseText);
            }
        } else {
            alert("出错了！！！");
        }
    }
}
```




# jQuery处理XML数据
`verifyjqueryxml.js`
```javascript
function verify(){
    // 1.获取文本框中的内容
    // document.getElementById("userName");  dom的方式
    var jqueryObj = $("#userName");
    // 获取节点的值
    var userName = jqueryObj.val();

    // 2.将文本框中的数据发送给服务器段的servlet
    var obj = {name:"123",age:20};
    // 使用jquery的XMLHTTPrequest对象get请求的封装
    $.ajax({
        type: "POST",               //http请求方式
        url: "AJAXXMLServer",       //服务器端url地址（必须）
        data: "name=" + userName,   //发送给服务器端的数据
        dataType: "xml",            //告诉JQuery返回的数据格式
        success: callback           //定义交互完成，并且服务器正确返回数据时调用的回调函数
    });
}

//回调函数
function callback(data) {
    // 3.接收服务器端返回的数据
    // 需要将data这个dom对象中的数据解析出来
    // 首先需要将dom的对象转换成JQuery的对象
    var jqueryObj = $(data);
    // 获取message节点
    var message = jqueryObj.children();
    // 获取文本内容
    var text = message.text();
    // 4.将服务器段返回的数据动态的显示在页面上
    //找到保存结果信息的节点
    var resultObj = $("#result");
    //动态的改变页面中div节点中的内容
    resultObj.html(text);
    alert("");


    // 由于从文本格式变成xml格式所以就不能就这样写了
    // var resultObj = $("#result");
    // resultObj.html(data);
}
```


# 解决XHR与图片缓存问题  
`verify.js`
```javascript
// 给url地址增加时间戳，骗过浏览器，不读取缓存
function convertURL(url) {
    //获取时间戳
    var timstamp = (new Date()).valueOf();
    //将时间戳信息拼接到url上
    if (url.indexOf("?") >= 0) {
        url = url + "&t=" + timstamp;
    } else {
        url = url + "?t=" + timstamp;
    }
    return url;
}
```

# 解决Ajax中文乱码与跨域访问  
``
```java
public class AJAXServer extends HttpServlet{
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try{
//            request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=gb18030");

            httpServletResponse.setContentType("text/html;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();

            Integer inte = (Integer) httpServletRequest.getSession().getAttribute("total");
            int temp = 0;
            if (inte == null) {
                temp = 1;
            } else {
                temp = inte.intValue() + 1;
            }
            httpServletRequest.getSession().setAttribute("total",temp);

            //1.取参数
            String old = httpServletRequest.getParameter("name");
            //String name = new String(old.getBytes("iso8859-1"),"UTF-8");
            String name = URLDecoder.decode(old,"UTF-8");
            //2.检查参数是否有问题
            if(old == null || old.length() == 0){
                out.println("用户名不能为空");
            } else{
//                String name = URLDecoder.decode(old,"UTF-8");
//                byte[] by = old.getBytes("ISO8859-1");
//                String name = new String(by,"utf-8");
//                String name = URLDecoder.decode(old,"utf-8");
                //3.校验操作
//                String name = old;
                if(name.equals("wangxingkui")){
                    //4。和传统应用不同之处。这一步需要将用户感兴趣的数据返回给页面段，而不是将一个新的页面发送给用户
                    //写法没有变化，本质发生了改变
                    out.println("用户名[" + name + "]已经存在，请使用其他用户名, " + temp);
                } else{
                    out.println("用户名[" + name + "]尚未存在，可以使用该用户名注册, " + temp);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
}
```

`verify.js`
```javascript
function verify() {
    //解决中文乱码问题的方法1：页面端发出的数据作一次encodeURI，服务器端使用new String(old.getBytes("iso8859-1"),"UTF-8");
    //解决中文乱码问题的方法2：页面端发出的数据作两次encodeURI，服务器端使用URLDecoder.decode(old,"UTF-8")
    var url = "AJAXServer?name=" + encodeURI(encodeURI($("#userName").val()));
    url = convertURL(url);
    $.get(url,null,function(data){
        $("#result").html(data);
});
}
```

# jQuery的Json支持
```javascript
function getInfo() {
    $.get("GetStockInfo", null, function(data) {
        ......
    },"json")  // 直接在这里加上json就可以将数据转换成json格式  
}
```