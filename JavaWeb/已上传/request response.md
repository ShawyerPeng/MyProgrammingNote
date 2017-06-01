---
title: request & response
date: 2017-02-01 15:19:42
categories: JavaWeb
---

# 缺省的servlet（了解）
自己编写的servlet，不要配置/。  
在tomcat/conf/web.xml中的配置。

<!-- more --> 

```xml
<servlet>
    <servlet-name>default</servlet-name>
    <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
    <init-param>
        <param-name>debug</param-name>
        <param-value>0</param-value>
    </init-param>
    <init-param>
        <param-name>listings</param-name>
        <param-value>true</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
```

# 一、request对象（请求对象）
```
ServletRequest
    |
HttpServletRequest


获取客户机信息
    getRemoteAddr（*****）	获取IP地址
    getMethod()	（*****） 			获取请求方式
    getContextPath()（*****）		获取虚拟路径

获取请求头信息
    String getHeader(String name)  		
    long getDateHeader(String name)  	
    int getIntHeader(String name)  		

    * 请求头
        referer				记住当前网页的来源
        User-Agent			判断浏览器
        if-modified-since	控制缓存

获取请求参数（*****）
    String getParameter(String name) （*****）
    String[] getParameterValues(String name)（*****）  

    Map getParameterMap()（*****）

    Enumeration getParameterNames()（用的比较少）  

    乱码问题解决：
        * POST请求乱码 ：request.setCharacterEncoding("utf-8");

        * GET请求乱码
            解决方案一：修改tomcat/conf/server.xml
                <Connector port="80" protocol="HTTP/1.1"
                           connectionTimeout="20000"
                           redirectPort="8443" URIEncoding="utf-8"/>
            * 必须有修改tomcat服务器配置文件权限

            解决方案二：逆向编解码
            username = URLEncoder.encode(username, "ISO8859-1");
            username = URLDecoder.decode(username, "utf-8");

            解决方案三：简写的方式（推荐使用）
            username = new String(username.getBytes("ISO-8859-1"),"utf-8");

        * request获取中文数据乱码（总结：）
            * post提交
                * 设置request缓冲区的编码
                    request.setCharacterEncoding("utf-8");
            * get提交
                * String构造方法
                    username = new String(username.getBytes("ISO-8859-1"),"utf-8");


利用请求域传递对象（request域对象）
重定向和转发的区别（转发）（*****）

    * 域对象
        ServletContext：服务器一启动，为每个web应用创建一个ServletContext对象，所有servlet实例共享对象。
        request：一次请求的范围。

    * setAttribute("","");
    * getAttribute("");
    * removeAttribute("");


    * getRequestDispatcher(String path) ，返回是RequestDispatcher：对象
    * RequestDispatcher：
        * forward(ServletRequest request, ServletResponse response)（经常使用） 转发的方法
        * include(ServletRequest request, ServletResponse response)（了解） 包含
```

# 二、response对象（响应对象）
想要获取客户端的内容，使用request对象。对客户端做出响应使用response对象。

响应：
1. 响应行  
    #### 状态码
    void `setStatus(int sc)` 设置状态码

2. 响应头（key:value的形式，一个key对应一个value，一个key对应多个value）  
    #### 设置头信息  
    void `setHeader(String name, String value)`  （一个key对应一个value）经常使用的  
    void `setHeader("aa","bb")`  
    void `setHeader("aa","cc")`  
    结果：aa:cc  

    void `setIntHeader(String name, int value)`    
    void `setDateHeader(String name, long date)` 值是毫秒值（int 秒	long 毫秒）

    void `addHeader(String name, String value)`  （一个key对应多个value）  
    void `addHeader("aa","bb")`  
    void `addHeader("aa","cc")`  
    结果：aa:bb,cc

    void `addIntHeader(String name, int value)`   
    void `addDateHeader(String name, long date) ` 					

3. 响应体  
ServletOutputStream `getOutputStream()`  	字节输出流  
PrintWriter `getWriter()`  				字符输出流
