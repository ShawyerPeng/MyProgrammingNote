# 一、快速入门
* 编写一个类，实现Servlet接口，重写5个方法。
    * 编写一个类，继承GenericServlet类，重写一个方法。
* 配置文件，配置Servlet信息。
```xml
<!-- 先配置Servlet信息 -->
<servlet>
    <!-- 配置Servlet名称，名称必须唯一 -->
    <servlet-name>ServletDemo1</servlet-name>
    <!-- 配置Servlet的完全路径（包名+类名） -->
    <servlet-class>cn.itcast.servlet.ServletDemo1</servlet-class>
</servlet>

<!-- 配置Servlet映射（访问路径） -->
<servlet-mapping>
    <!-- 配置Servlet名称，和上面的名称必须相同 -->
    <servlet-name>ServletDemo1</servlet-name>
    <!-- 配置虚拟路径（访问路径） -->
    <url-pattern>/demo1</url-pattern>
</servlet-mapping>
```

# 二、Servlet的生命周期
生命周期：实例被创建，对外提供服务，销毁。
* Servlet被创建后，调用init方法进行初始化（不是服务器一启动实例就被创建，第一次请求访问时servlet实例才被创建）  
    `void init(ServletConfig config)`
* 从客户端发送所有的请求是service方法进行处理的。（有一次请求就调用一次service方法）  
    `void service(ServletRequest req, ServletResponse res)`  
* 从服务器中移除服务，调用destroy方法。（服务器关闭，手动移除时调用，所以只调用一次）  
    `void destroy()`  

Servlet的生命周期：第一次请求的时候，Servlet实例被创建，立即调用init方法进行初始化。实例通过service方法提供服务。服务器关闭或者移除服务时，调用destroy方法进行销毁。

# 三、Servlet自动加载
* Servlet默认是第一次访问时候创建实例。通过配置，服务器启动，创建实例。
* init做初始化的操作，非常消耗时间的。

* 在<servlet>标签下
    `<load-on-startup>3</load-on-startup>`
* 值是正整数
    * 如果值越小，优先级越高。

# 四、配置虚拟路径（访问路径）
```xml
<servlet-mapping>
	<url-pattern>/demo5</url-pattern>
</servlet-mapping>
```

```
* 完全路径匹配
	* 以/开头的			/demo5		/servlet/demo5
	* 访问：http://localhost/day09/demo5
* 目录匹配
	* 以/开头的			/*
	* 访问：http://localhost/day09/demo5可以访问
* 扩展名匹配
	* 不能以/开头的		*.do	*.action
	* 访问：http://localhost/day09/demo5.do

* 优先级：完全路径匹配	> 目录匹配 > 扩展名匹配（*****）
```

# 五、WEB开发中路径的问题
```
* 相对路径
    * 一个文件相对于另一个文件的位置的关系。
    * 不能以/开头	写法：	./demo等价于demo(当前目录)	  ../demo(上一级目录)

    * 访问1.html：	http://localhost/day09/1.html
    * 访问demo5：	http://localhost/day09/demo5
    * 从1.html中去访问demo5：./demo5	demo5

    * 访问2.html：	http://localhost/day09/html/2.html
    * 访问demo5：	http://localhost/day09/demo5
    * 从2.html访问demo5：../demo5

* 绝对路径（推荐使用）
    * 以/开头的
    * 访问demo5：	http://localhost/day09/demo5
    * 从1.html使用绝对路径访问demo5：http://localhost/day09/demo5
    * 简写方式：/day09/demo5

    * 客户端绝对路径
        * /day09/demo5	需要写项目名

    * 服务器绝对路径
        * /demo5	不能写项目名
```

# 六、ervletConfig对象和配置文件相关
* 配置初始化参数
    * 需要在<servlet></servlet>标签下配置。
    * 如果要是配置在某个servlet的标签下，那么只能在该servlet中获取初始化参数。
    <init-param>
        <param-name>username</param-name>
        <param-value>root</param-value>
    </init-param>

* String `getServletName`()  					获取配置文件中servlet的名称
* String `getInitParameter(String name)`  	获取初始化参数
* Enumeration `getInitParameterNames()`  	    获取初始化参数的名称们

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 测试ServletConfig对象的ApplicationContext
    // 先获取ServletConfig对象
    ServletConfig config = getServletConfig();
    // 获取配置文件中Servlet的名称
    System.out.println("Servlet的名称"+config.getServletName());
    // 获取初始化的参数
    String username = config.getInitParameter("username");

    Enumeration<String> e = config,getInitParameterNames();
    while(e.hasMoreElements()){
        String name = e.nextElement();
        String value = config.getInitParameter(name);
        System.out.println(name+" : "+value);
    }
}

```


# 七、ServletContext对象（域对象）
```
* 定义：WEB容器在启动时，它会为每个WEB应用程序都创建一个对应的ServletContext对象，它代表当前web应用。
一个WEB应用对应一个ServletContext对象
一个WEB应用下有多个Servlet程序
所有的servlet程序都共享同一个ServletContext对象

demo1存入内容	ServletContext	demo2中取出来  实现数据共享

* 作用：
    * 获取WEB应用全局初始化参数
        * 在web.xml中配置
            <context-param>
                <param-name>encoding</param-name>
                <param-value>GBK</param-value>
            </context-param>

        String getInitParameter(String name)	  
        getInitParameterNames() 					

    * 实现数据的共享（*****）
        void setAttribute(String name, Object object)   存入数据
        void removeAttribute(String name)  				删除数据
        Object getAttribute(String name)  				获取数据

    * 读取资源文件（*****）
        InputStream getResourceAsStream(String path)  	通过文件的地址获取输入流
        String getRealPath(String path)  		通过文件的地址获取文件的绝对磁盘路径
```

# 八、HTTP的协议
* 请求
	* referer		记住当前网页的来源
	* user-agent	浏览器版本信息
	* if-modefied-since

* 响应
	* 响应头
	* location		和302一起来完成重定向的操作
	* refresh		页面的定时刷新
	* last-modefied	和 if-modefied-since和304状态码一起来控制缓存。
