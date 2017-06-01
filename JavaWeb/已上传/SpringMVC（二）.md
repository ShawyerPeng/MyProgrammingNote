# 1. 数据回显
### 1. 需求
表单提交出现错误，重新回到表单，用户重新填写数据，刚才提交的参数在页面上回显。
### 2. 对简单类型的数据回显
对商品修改数据回显：
注意在进入修改页面的controller方法中和提交修改商品信息方法model.addAttribute方法设置的key一致。

修改商品显示方法：  
`ItemsController.java`
```java
@RequestMapping(value="/editItems",method={RequestMethod.GET})
public String editItems(Model model, Integer id) throws Exception {
    // 将id传到页面
    model.addAttribute("id", id);
}
```
修改商品页面：  
```js
<form id="itemForm" action="${pageContext.request.contextPath }/items/editItemSubmit.action" method="post" enctype="multipart/form-data">
<input type="text" name="id" value="${ id }"/>
```
修改商品提交方法：  
```java
@RequestMapping("/editItemSubmit")
public String editItemSubmit(Model model, Integer id) throws Exception {
    // 进行数据回显
    model.addAttribute("id", id);
}
```
### 3. pojo类型数据回显
方法一：使用`Model.addtribute`方法进行数据回显  
```java
@RequestMapping("/editItemSubmit")
public String editItemSubmit(Model model, Integer id, ItemsCustom itemsCustom) throws Exception {
    // 进行数据回显
    model.addAttribute("item", itemsCustom);
}
```
方法二：使用`@ModelAttribute`，作用于将请求pojo数据放到Model中回显到页面
```java
public String editItemSubmit(Model model, Integer id, @ModelAttribute(value="itemsCustom") ItemsCustom itemsCustom) throws Exception {
    model.addAttribute("item", itemsCustom);
}
```
在ModelAttribute方法指定的名称就是要填充Model中的key，在页面中就要通过key取数据。

`@ModelAttribute`将方法返回值传到页面

# 四、参数绑定集合类型
### 1. 绑定数组
需求：在商品查询列表页面，用户选择要删除的商品，批量删除商品。  
在controller方法中如何将批量提交的数据绑定成数组类型。
1. 页面定义  
    `itemsList.jsp`
    ```html
    <html>
    <script type="text/javascript">
    function deleteItems(){
    	//将form的action指向删除商品的地址
    	document.itemsForm.action="${pageContext.request.contextPath }/items/deleteItems.action";
    	//进行form提交
    	document.itemsForm.submit();
    }
    </script>

    <body>
    当前用户：${usercode }  
    <c:if test="${ usercode!=null }">
    	<a href="${ pageContext.request.contextPath }/logout.action">退出</a>
    </c:if>

    <form name="itemsForm" action="${ pageContext.request.contextPath }/items/queryItem.action" method="post">
    查询条件：
    <table width="100%" border=1>
    <tr>
    <td>
    商品类别：
    <select>
    	<c:forEach items="${itemsType }" var="item">
    			<option value="${item.key }">${item.value }</option>

    	</c:forEach>

    </select>
    </td>
    <td><input type="submit" value="查询"/>
    <input type="button" value="批量删除" onclick="deleteItems()"/>
    </td>
    </tr>
    </table>
    商品列表：
    <table width="100%" border=1>
    <tr>
    	<td>选择</td>
    	<td>商品名称</td>
    	<td>商品价格</td>
    	<td>生产日期</td>
    	<td>商品描述</td>
    	<td>操作</td>
    	<td>rest连接</td>
    </tr>
    <c:forEach items="${itemsList }" var="item">
    <tr>
    	<td><input type="checkbox" name="delete_id" value="${item.id}" /></td>
    	<td>${item.name }</td>
    	<td>${item.price }</td>
    	<td><fmt:formatDate value="${item.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    	<td>${item.detail }</td>

    	<td><a href="${pageContext.request.contextPath }/items/editItems.action?id=${item.id}">修改</a></td>
    	<td><a href="${pageContext.request.contextPath }/items/viewItems/${item.id}">商品查看</a></td>

    </tr>
    </c:forEach>

    </table>
    </form>
    </body>
    </html>
    ```

2. controller方法定义  
    `ItemsController.java`
    ```java
    //删除 商品
    @RequestMapping("/deleteItems")
    public String deleteItems(Integer[] delete_id) throws Exception {
        //调用service方法删除 商品
        //....
        return "success";
    }
    ```

### 2. 绑定List<Object>
需求：批量修改商品信息提交。  
先进入批量修改商品页面，填写信息，点击提交。
1. 页面定义  
    `itemsList.jsp`
    ```html
    <html>
    <title>批量修改商品查询</title>
    <script type="text/javascript">
    //修改商品提交
    function updateItems(){
    	//将form的action指向删除商品的地址
    	document.itemsForm.action="${pageContext.request.contextPath }/items/editItemsListSubmit.action";
    	//进行form提交
    	document.itemsForm.submit();
    }
    </script>

    <body>
    <form name="itemsForm" action="${pageContext.request.contextPath }/items/queryItem.action" method="post">
    查询条件：
    <table width="100%" border=1>
    <tr>
    <td>
    商品类别：
    <select>
    	<c:forEach items="${itemsType }" var="item">
    			<option value="${item.key }">${item.value }</option>

    	</c:forEach>

    </select>
    </td>
    <td><input type="submit" value="查询"/>
    <input type="button" value="批量修改提交" onclick="updateItems()"/>
    </td>
    </tr>
    </table>
    商品列表：
    <table width="100%" border=1>
    <tr>
    	<td>商品名称</td>
    	<td>商品价格</td>
    	<td>生产日期</td>
    	<td>商品描述</td>
    	<td>操作</td>
    </tr>
    <c:forEach items="${itemsList }" var="item" varStatus="s">
    <tr>
    	<td><input type="text" name="itemsList[${s.index }].name" value="${item.name }"/></td>
    	<td><input type="text" name="itemsList[${s.index }].price" value="${item.price }"/></td>
    	<td><fmt:formatDate value="${ item.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    	<td>${ item.detail }</td>

    	<td><a href="${ pageContext.request.contextPath }/items/editItems.action?id=${item.id}">修改</a></td>

    </tr>
    </c:forEach>

    </table>
    </form>
    </body>
    </html>
    ```

2. controller方法定义  
    `ItemsController.java`
    ```java
    //批量修改商品查询
    @RequestMapping("/editItemsList")
    public ModelAndView editItemsList(HttpServletRequest request) throws Exception {
        System.out.println(request.getParameter("id"));
        //调用service查询商品列表
        List<ItemsCustom> itemsList = itemsService.findItemsList(null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("itemsList", itemsList);
        // 指定逻辑视图名
        modelAndView.setViewName("editItemsList");

        return modelAndView;
    }
    //批量修改商品提交
    @RequestMapping("/editItemsListSubmit")
    public String editItemsListSubmit(ItemsQueryVo itemsQueryVo) throws Exception {
        return "success";
    }
    ```
    ```html
    <c:forEach items="${itemsList }" var="item" varStatus="s">
    <tr>
        <td><input type="text" name="itemsList[${s.index }].name" value="${item.name }"/></td>
        <td><input type="text" name="itemsList[${s.index }].price" value="${item.price }"/></td>
    ```
    注释：
    `itemsList`：controller方法形参包装类型中list的属性名。
    `itemsList[0]`或`itemsList[1]`...：[]中是序号，从0开始。
    `itemsList[].name`：name就是controller方法形参包装类型中list中pojo的属性名
    ![](http://i1.piimg.com/567571/ee0b0d2398ab9e0b.png)

3. 在包装类型里定义一个list  
    `ItemsQueryVo.java`
    ```java
    public class ItemsQueryVo {
        //商品信息
        private ItemsCustom itemsCustom;
        //定义一个list
    	private List<ItemsCustom> itemsList;
    }
    ```


# 五、springmvc和struts的区别
springmvc是通过方法的形参接收参数，在使用时可以以单例方式使用，建议使用单例。  
struts是通过成员变量接收参数，在使用时必须以多例方式使用。  

springmvc是基于方法开发，struts基于类开发。  
springmvc将一个请求的Method和Handler进行关联绑定，一个method对应一个Handler。  

springmvc开发以方法为单位进行开发，方法更帖进service(业务方法)。  

经过实际测试，发现struts标签解析速度比较慢，建议在实际开发时使用jstl。  

# 六、图片上传
### 需求  
在商品修改页面，增加图片上传的功能。  
操作流程：  
	用户进入商品修改页面  
	上传图片  
	点击提交（提交的是图片和商品信息）  
	再次进入修改页面，图片在商品修改页面展示  
### 1. 图片存储问题   
切记：不要把图片上传到工程目录 ，不方便进行工程维护。  
实际电商项目中使用专门图片服务器(http，比如apache、tomcat)。  

本教程使用图片虚拟目录，通过虚拟目录 访问硬盘上存储的图片目录。  

虚拟目录设置：  

注意：图片目录中尽量进行目录分级存储，提高访问速度（提交i/o）。
### 2. 配置图片上传解析器  
springmvc使用`commons-fileupload`进行图片上传。commons-fileupload对应的springmvc的图片上传解析器：`org.springframework.web.multipart.commons.CommonsMultipartResolver`  
`springmvc.xml`
```xml
<!-- 文件上传 -->
<bean id="multipartResolver"
    class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <!-- 设置上传文件的最大尺寸为5MB -->
    <property name="maxUploadSize">
        <value>5242880</value>
    </property>
</bean>
```
加入commons-fileupload的jar包：`commons-fileupload-1.3.2.jar`和`commons-io-2.5.jar`
### 3. 编写上传图片的页面  
`editItem.jsp`
```html
<tr>
	<td>商品图片</td>
	<td>
		<c:if test="${itemsCustom.pic !=null}">
			<img src="/pic/${itemsCustom.pic}" width=100 height=100/>
			<br/>
		</c:if>
		<input type="file"  name="pictureFile"/>
	</td>
</tr>
```
### 4. 编写controller方法  
`ItemsController.java`  
```java
@RequestMapping("/editItemSubmit")
public String editItemSubmit(Model model,Integer id,
            @ModelAttribute(value="itemsCustom") ItemsCustom itemsCustom,
        //上传图片
        MultipartFile pictureFile
        ) throws Exception {
        //进行数据回显
    	model.addAttribute("id", id);

    	//进行图片上传
    	if(pictureFile!=null && pictureFile.getOriginalFilename()!=null && pictureFile.getOriginalFilename().length()>0){
    		//图片上传成功后，将图片的地址写到数据库
    		String filePath = "F:\\develop\\upload\\temp\\";
    		//上传文件原始名称
    		String originalFilename = pictureFile.getOriginalFilename();
    		//新的图片名称
    		String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
    		//新文件
    		File file = new java.io.File(filePath+newFileName);

    		//将内存中的文件写入磁盘
    		pictureFile.transferTo(file);

    		//图片上传成功，将新图片地址写入数据库
    		itemsCustom.setPic(newFileName);
    	}
```










# 七、json数据的交互
### 需求
json数据格式是比较简单容易理解，json数据格式常用于远程接口传输，http传输json数据，非常方便页面进行提交/请求结果解析，对json数据的解析。

### 2. springmvc解析json加入json解析包
Springmvc默认用`MappingJacksonHttpMessageConverter`对json数据进行转换，需要加入jackson的包，如下：
`jackson-core-asl-1.9.13.jar`  
`jackson-mapper-asl-1.9.13.jar`

### 3. 在处理器适配器中注入MappingJacksonHttpMessageConverter
让`处理器适配器`支持json数据解析，需要注入`MappingJacksonHttpMessageConverter`。  
`springmvc.xml`
```xml
<!-- 注解适配器 -->
<!-- 加入 json数据的消息转换器 MappingJacksonHttpMessageConverter依赖Jackson的包 -->
<property name="messageConverters">
    <list>
        <bean
            class="org.springframework.http.converter.json.MappingJacksonHttpMessageConverter"></bean>
    </list>
</property>
```

### 4. @RequestBody和@ResponseBody
`@RequestBody`：将请求的json数据转成java对象
`@ResponseBody`：将java对象转成json数据输出。
![](http://i1.piimg.com/567571/c5ed1081d6f3358c.jpg)

### 5. 请求json响应json
controller方法：  
`JsonTest.java`
```java
@Controller
public class JsonTest {
    //请求的json响应json，请求商品信息，商品信息用json格式，输出商品信息
    @RequestMapping("/requestJson")
    public @ResponseBody ItemsCustom requestJson(@RequestBody ItemsCustom itemsCustom) throws Exception{
    	return itemsCustom;
    }
}
```

页面：  
`JsonTest.jsp`
```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>json测试</title>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript">
    //请求json响应json
    function requestJson(){
    	$.ajax({
    		url:"${pageContext.request.contextPath }/requestJson.action",
    		type:"post",
    		contentType:"application/json;charset=utf-8",
    		//请求json数据,使用json表示商品信息
    		data:'{"name":"手机","price":1999}',
    		success:function(data){
    			alert(data.name);
    		}
    	});
    }
    //请求key/value响应json
    function responseJson(){
    	$.ajax({
    		url:"${pageContext.request.contextPath }/responseJson.action",
    		type:"post",
    		//请求key/value数据
    		data:"name=手机&price=1999",
    		success:function(data){
    			alert(data.name);
    		}
    	});
    }
    </script>
</head>

<body>
    <input type="button" value="请求json响应json" onclick="requestJson()"/>
    <input type="button" value="请求key/value响应json" onclick="responseJson()"/>
</body>
</html>
```

测试跟踪：
![](http://p1.bpimg.com/567571/dd07ad24c143fdad.png)

### 6. 请求key/value响应json
controller方法：  
`JsonTest.java`
```java
@Controller
public class JsonTest {
    //请求key/value响应json
    @RequestMapping("/responseJson")
    public @ResponseBody ItemsCustom responseJson(ItemsCustom itemsCustom) throws Exception{
        return itemsCustom;
    }
}
```

测试跟踪：
![](http://i1.piimg.com/567571/1121094aec7e6ee8.png)

### 7. 小结
如果前端处理没有特殊要求，`建议使用第二种`，请求`key/value`，响应`json`，方便客户端解析请求结果。

# 八、validation校验（了解）

# 九、统一异常处理
要在一个统一异常处理的类中要处理系统抛出的所有异常，根据异常类型来处理。

统一异常处理的类是什么？

前端控制器DispatcherServlet在进行HandlerMapping、调用HandlerAdapter执行Handler过程中，如果遇到异常，进行异常处理。

在系统中自定义统一的异常处理器，写系统自己的异常处理代码。
1. 定义统一异常处理器类  
    统一异常处理器实现`HandlerExceptionResolver`接口。  
    `CustomException.java`
    ```java
    package cn.itcast.ssm.exception;
    public class CustomException extends Exception {
    	//异常信息
    	private String message;

    	public CustomException(String message){
    		super(message);
    		this.message = message;
    	}

    	public String getMessage() {
    		return message;
    	}

    	public void setMessage(String message) {
    		this.message = message;
    	}
    }
    ```
    `CustomExceptionResolver.java`
    ```java
    public class CustomExceptionResolver implements HandlerExceptionResolver  {
    	//前端控制器DispatcherServlet在进行HandlerMapping、调用HandlerAdapter执行Handler过程中，如果遇到异常就会执行此方法
    	@Override
    	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    		//输出异常
    		ex.printStackTrace();
    		//统一异常处理代码
    		//......
            return null;
        }
    }
    ```

2. 配置统一异常处理器  
    `springmvc.xml`
    ```xml
    <!-- 定义统一异常处理器 -->
    <bean class="cn.itcast.ssm.exception.CustomExceptionResolver"></bean>
    ```

3. 异常处理逻辑
根据不同的异常类型进行异常处理。

系统自定义的异常类是`CustomException` ，在`controller`方法中、`service`方法中手动抛出此类异常。

针对系统自定义的`CustomException`异常，就可以直接从异常类中获取异常信息，将异常处理在错误页面展示。
针对`非CustomException`异常，对这类重新构造成一个`CustomException`，异常信息为“未知错误”，此类错误需要在系统测试阶段去排除。

在统一异常处理器CustomExceptionResolver中实现上边的逻辑。  
    `CustomExceptionResolver.java`
    ```java
    public class CustomExceptionResolver implements HandlerExceptionResolver  {
    	//前端控制器DispatcherServlet在进行HandlerMapping、调用HandlerAdapter执行Handler过程中，如果遇到异常就会执行此方法
    	//handler最终要执行的Handler，它的真实身份是HandlerMethod
    	//Exception ex就是接收到异常信息
    	@Override
    	public ModelAndView resolveException(HttpServletRequest request,
    			HttpServletResponse response, Object handler, Exception ex) {
    		//输出异常
    		ex.printStackTrace();

    		//统一异常处理代码
    		// 1.针对系统自定义的CustomException异常，就可以直接从异常类中获取异常信息，将异常处理在错误页面展示
    		//异常信息
    		String message = null;
    		CustomException customException = null;
    		//如果ex是系统自定义的异常，直接取出异常信息
    		if(ex instanceof CustomException){
    			customException = (CustomException)ex;
    		} else{
    			// 2.针对非CustomException异常，对这类重新构造成一个CustomException，异常信息为“未知错误”
    			customException = new CustomException("未知错误");
    		}

    		//错误信息
    		message = customException.getMessage();
    		request.setAttribute("message", message);
    		try {
    			//转向到错误页面
    			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
    		} catch (ServletException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		return new ModelAndView();
    	}
    }
    ```

4. 测试抛出异常由统一异常处理器捕获
    可以在controller方法、service方法、dao实现类中抛出异常，要求dao、service、controller遇到异常全部向上抛出异常，方法向上抛出异常throws Exception
    `ItemsServiceImpl.java`
    ```java
    package cn.itcast.ssm.service.impl;
    public class ItemsServiceImpl implements ItemsService {
        @Override
        public ItemsCustom findItemsById(int id) throws Exception {
            Items items = itemsMapper.selectByPrimaryKey(id);
            //如果查询的商品信息为空，抛出系统自定义的异常
            if(items==null){
                    throw new CustomException("修改商品信息不存在");
            }
            //在这里随着需求的变量，需要查询商品的其它的相关信息，返回到controller
            ItemsCustom itemsCustom = new ItemsCustom();
            //将items的属性拷贝到itemsCustom
            BeanUtils.copyProperties(items, itemsCustom);

            return itemsCustom;
        }
    }
    ```
    `ItemsController.java`
    ```java
    //方法返回字符串，字符串就是逻辑视图名，Model作用是将数据填充到request域，在页面展示
    @RequestMapping(value="/editItems",method={RequestMethod.GET})
    public String editItems(Model model,Integer id)throws Exception{
        //将id传到页面
        model.addAttribute("id", id);

        //调用 service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);
        if(itemsCustom == null){
            throws new CustomException("修改商品信息不存在");
        }
        model.addAttribute("itemsCustom", itemsCustom);

        return "editItem";
    }
    ```

图解：
![](http://p1.bqimg.com/567571/b2c2de3d9ae41482.jpg)

# 十、RESTful架构
### 1. 什么是RESTful
RESTful软件开发理念，RESTful对http进行非常好的诠释。  
RESTful即Representational State Transfer的缩写。
综合上面的解释，我们总结一下什么是RESTful架构：  
（1）每一个URI代表一种资源；  
（2）客户端和服务器之间，传递这种资源的某种表现层；  
（3）客户端通过四个HTTP动词，对服务器端资源进行操作，实现"表现层状态转化"。  
### 2. url的RESTful实现
非RESTful的http的url：http://localhost:8080/items/editItems.action?id=1&....

RESTful的url是简洁的：http:// localhost:8080/items/editItems/1
参数通过url传递，rest接口返回json数据

### 3. 需求
根据`id`查看商品信息，商品信息查看的链接使用`RESTful`方式实现，商品信息以`json`返回。  
1. 更改DispatcherServlet配置   
    `web.xml`
    ```xml
    <!-- restful的配置 -->
    <servlet>
        <servlet-name>springmvc_rest</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 加载springmvc配置 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <!-- 配置文件的地址 如果不配置contextConfigLocation， 默认查找的配置文件名称classpath下的：servlet名称+"-serlvet.xml"即：springmvc-serlvet.xml -->
            <param-value>classpath:spring/springmvc.xml</param-value>
        </init-param>

    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc_rest</servlet-name>
        <!-- rest方式配置为/ -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    ```

2. 参数通过url传递
    `ItemsController.java`
    ```java
    // 根据商品id查看商品信息rest接口
    // @RequestMapping中指定restful方式的url中的参数，参数需要用{}包起来
    // @PathVariable将url中的{}包起参数和形参进行绑定
    @RequestMapping("/viewItems/{id}")
    public @ResponseBody ItemsCustom viewItems(@PathVariable("id") Integer id) throws Exception{
        //调用 service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);
        return itemsCustom;
    }
    ```
    `itemsList.jsp`
    ```js
    <c:forEach items="${itemsList }" var="item">
    <tr>
    	<td><input type="checkbox" name="delete_id" value="${item.id}" /></td>
    	<td>${ item.name }</td>
    	<td>${ item.price }</td>
    	<td><fmt:formatDate value="${item.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    	<td>${ item.detail }</td>

    	<td><a href="${pageContext.request.contextPath }/items/editItems.action?id=${item.id}">修改</a></td>
    	<td><a href="${pageContext.request.contextPath }/items/viewItems/${item.id}">商品查看</a></td>
    </tr>
    </c:forEach>
    ```

3. 设置静态资源解析
    当DispatcherServlet拦截/开头的所有请求，对静态资源的访问就报错404，需要通过设置对静态资源进行解析.  
    ![](http://i1.piimg.com/567571/a5056866c3d1742b.png)
    `springmvc.xml`
    ```xml
    <!-- 静态资源 解析 -->
    <mvc:resources location="/js/" mapping="/js/**" />
    <mvc:resources location="/img/" mapping="/img/**" />
    ```
    访问`/js/**`的url从工程下`/js/`下解析。


# 十一、springmvc拦截器
### 1. 拦截器的异常场合
用户请求到DispatherServlet中，DispatherServlet调用HandlerMapping查找Handler，HandlerMapping返回一个拦截的链儿（多个拦截），springmvc中的拦截器是通过HandlerMapping发起的。
在企业开发，使用拦截器实现用户认证（用户登陆后进行身份校验拦截），用户权限拦截。
### 2. springmvc拦截器方法
### 3. 测试拦截器
### 4. 拦截器应用（用户认证拦截）
