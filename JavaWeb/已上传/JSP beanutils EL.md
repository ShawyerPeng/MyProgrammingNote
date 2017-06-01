# 一、JSP语法
```
* JSP的设置
    * 设置JSP文件的打开方式：window -- 选项 -- General -- Editors -- 文件关联 -- *.jsp -- 选择MyEclipse JSP Editor -- default
    * 设置JSP文件的编码：window -- 搜索JSP -- 找到JSP -- 选择UTF-8 -- OK

* 脚本元素
    <%! %>		成员方法、成员变量
    <%= %>		输出内容
    <%  %>		局部变量，语句

* JSP页面的注释		
    * HTML的注释	<!-- -->	JSP文件的阶段存在，在翻译成java文件也存在，	在页面存在
    * JAVA的注释	JSP文件的阶段存在，在翻译成java文件也存在，	在页面不存在
        <%
            // 		单行
            /**/	多行
            /**
            *		文档
            */
        %>
    * JSP的注释		<%-- JSP的注释 --%>		之后存在JSP文件的阶段

* JSP的指令元素
    * 语法：<%@ 指令元素 属性名=属性值  %>
    * page		：设置JSP的属性
        * 注意：import属性可以出现多次，其他属性只能出现一次。
        * 属性：
            * language	：JSP文件允许嵌入的语言。只支持一种JAVA语言。（不需要改变）
            * extends	：JSP翻译翻译Servlet文件，servlet继承的类。（不需要改变）
            * session	：默认值是true，可以直接使用session。设置false，不能直接使用。
            * import	：引入java的jar包（使用多次）
            * buffer	：设置缓冲区的大小	默认8kb
            * aotoFlush	：设置默认刷新缓冲区（不要修改）
            * errorPage		：指定错误页面
            * isErrorPage	：设置值，可以是exception对象，设置true，使用exception，设置不能使用exception对象
            * contentType	：服务器端输出所有内容采用编码。
            * pageEncoding	：JSP翻译成Servlet文件时采用的编码。
            * isELIgnored	：是否忽略EL表达式（默认false，不忽略，true，忽略）

        * 重点：
            * session	import	contentType	 pageEncoding	isELIgnored

    * include	：包含页面（页面布局）
        * 语法：<%@ include file="要包含文件的地址（静态包含）" %>
        * <%@ include file="/include/head.jsp" %>

    * taglib	：引入标签库文件
        * 语法：<%taglib  %>
        * 属性:
            * uri		：引入标签库文件的名称
            * prefix	：标签库的前缀名

* 配置全局的错误页面
    * 在web.xml中进行配置。
    <error-page>
        <error-code>404</error-code>
        <location>/404.jsp</location>
    </error-page>

    <error-page>
        <error-code>500</error-code>
        <location>/500.jsp</location>
    </error-page>
```

# 二、JSP的内置对象（9个）直接使用
`request`、`response`、`session`、`application`、`out`、`pageContext`、`page`、`config`、`exception`
```
内置对象					真实的对象					方法
request						HttpServletRequest			setAttribute()	getAttribute()
response					HttpServletResponse			addCookie()		getWriter()
session						HttpSession					setAttribute()	getAttribute()
application					ServletContext				setAttribute()	getAttribute()
config						ServletConfig				getInitParameter()	getInitParameterNames()

exception					Throwable					getMessage()
page						Object						（不使用对象）
out						    JspWriter					write()		print()
pageContext					PageContext					setAttribute()	getAttribute()


* exception
    * 和异常有关的
    * 前提条件：isErrorPage="true"，才能使用exception对象。
* page
    * 当前页面的对象。
* out对象
    JspWriter			  PrintWriter  response.getWriter()

    <%= "HELLO" %>
    <% out.print("AAAA"); %>
    <% response.getWriter().print("BBBB"); %>
    <% out.print("CCCC"); %>
    * 输出结果：

* pageContext对象
    * 域对象
        * 自己存取值
        * 向其他的域存取值。
            * setAttribute(String name, Object value, int scope)
            * getAttribute(String name, int scope)
            * findAttribute(String name)

    * 可以获取其他8个对象。
        * 编写框架或者通用性较高代码。

* 在JSP的时候（4个域对象）
	ServletContext	整个web应用
	session			一次会话
	request			一次请求
	pageContext		当前页面
```
```java
<html>
<body>
<%=request.getParameter("username") %>

<h4>pageContext向其他域中存入值</h4>
<%
	pageContext.setAttribute("name", "美美");
	// 下面这句等价于上面
	pageContext.setAttribute("name", "美美", pageContext.PAGE_SCOPE);

	request.setAttribute("name", "小凤");
	// 向request域中存入值
	pageContext.setAttribute("name", "小凤", pageContext.REQUEST_SCOPE);

	// 向session域存入值
	pageContext.setAttribute("name", "小苍", pageContext.SESSION_SCOPE);
	// 向ServletContext域存入值
	pageContext.setAttribute("name", "小班长", pageContext.APPLICATION_SCOPE);
%>

<%= pageContext.getAttribute("name", pageContext.SESSION_SCOPE)%>
<%= session.getAttribute("name") %>

${ pageScope.name }
${ requestScope.name }
${ sessionScope.name }
${ applicationScope.name }

</body>
</html>
```

# 三、JSP的标签（JSP的动作）
```
<jsp:forward>	：转发
    属性：page	要转发的页面
<jsp:param>		：传递参数
    属性：name：参数名称	value：参数值
<jsp:include>  	：页面的包含（动态包含）
    属性：page	要包含的页面
```
```js
<h4>JSP的动作标签</h4>
<jsp:forward page="/jsp/pageContext.jsp">
	<jsp:param name="username" value="meimei"/>
</jsp:forward>
```
```js
<jsp:include page="/action/head.jsp"></jsp:include>
<jsp:include page="/action/menu.jsp"></jsp:include>

<h3>网站的新闻（数据变化）</h3>
<jsp:include page="/action/foot.jsp"></jsp:include>
```

# 四、JavaBean和内省（了解）有个工具类直接完成数据封装
```
* 什么是JavaBean？
    * 定义
        * 必须有一个无参的构造方法
        * 属性私有化
        * 私有化的属性必须通过public类型的方法暴露给其它程序，并且方法的命名也必须遵守一定的命名规范(set开头，后面接大写字母开头的属性)。

        public class User(){
            private String username;
            private String password;
            public void setUsername(){
            }
            public void getUsername(){
            }
            public void getPassword(){
            }
            public void getInfo(){
            }
        }

    * 作用：封装数据。

把数据封装到JavaBean中（JSP页面中完成的）
<jsp:useBean>
<jsp:setProperty>
<jsp:getProperty>

<jsp:useBean id="u" class="cn.itcast.vo.User"></jsp:useBean>
<jsp:setProperty property="username" name="u"/>
<jsp:setProperty property="password" name="u"/>
或者
<jsp:setProperty property="*" name="u"/>

<jsp:getProperty property="username" name="u"/>
<jsp:getProperty property="password" name="u"/>
```  

`User.java`  
```java
public class User {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
```

`login.jsp`
```java
<html>
<body>

<h4>1.表单提交到JSP的页面</h4>
<form action="/day12/bean/success.jsp" method="POST">
	姓名：<input type="text" name="username" /><br/>
	密码：<input type="password" name="password" /><br/>
	<input type="submit" value="登陆"/>
</form>


<h4>2.表单提交到Servlet程序</h4>
<form action="/day12/user" method="POST">
	姓名：<input type="text" name="username" /><br/>
	密码：<input type="password" name="password" /><br/>
	<input type="submit" value="登陆"/>
</form>


<h4>3.表单提交到Servlet（BeanUtils）程序</h4>
<form action="/day12/userBeanUtil" method="POST">
	姓名：<input type="text" name="username" /><br/>
	密码：<input type="password" name="password" /><br/>
	<input type="submit" value="登陆"/>
</form>

</body>
</html>
```

`success.jsp`
```js
<html>
<body>

<!-- 1.传统方式封装数据 -->
<%
	// 获取表单的内容
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	// 创建User对象，set设置值
	User user = new User();
	user.setUsername(username);
	user.setPassword(password);
%>

<!-- 2.使用jsp的标签封装数据 -->
<jsp:useBean id="u" class="cn.itcast.vo.User"></jsp:useBean>
<jsp:setProperty property="*" name="u"/>    // 当属性特别多时，直接只写一行*设置所有属性

<jsp:getProperty property="username" name="u"/>
<jsp:getProperty property="password" name="u"/>

</body>
</html>
```

# 五、内省（Introspector）
```java
public void run() throws Exception{
	User user = new User();
	// 获取类的信息
	BeanInfo info = Introspector.getBeanInfo(user.getClass());
	// 获取属性的描述
	PropertyDescriptor [] pds = info.getPropertyDescriptors();
	// 循环遍历，获取属性的名称
	for (PropertyDescriptor pd : pds) {
		// System.out.println(pd.getName());
		if(!"class".equals(pd.getName())){
			// 获取写的方法
			Method m = pd.getWriteMethod();
			m.invoke(user, "admin");
		}
	}
	System.out.println(user.getUsername());
	System.out.println(user.getPassword());
}
```
`UserServlet.jsp`
```java
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 6390620317553505800L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 获取请求参数，创建User对象，设置值。
		/**
		 * 	// 获取表单的内容
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			// 创建User对象，set设置值
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
		 */

		// 获取输入的数据
		Map<String, String []> map = request.getParameterMap();
		// 创建User对象
		User user = new User();
		// 自己编写封装数据的方法
		try {
			populate(map,user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 完成数据封装
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
	}

    // 完成的数据
	private void populate(Map<String, String[]> map, User user) throws Exception {
		BeanInfo info = Introspector.getBeanInfo(user.getClass());
		// 获取属性的描述
		PropertyDescriptor [] pds = info.getPropertyDescriptors();
		// 循环遍历
		for (PropertyDescriptor pd : pds) {
			// 获取到属性的名称
			String name = pd.getName();
			// map的key
			if(map.containsKey(name)){
				// 获取属性的写的方法
				Method m = pd.getWriteMethod();
				// 执行之
				m.invoke(user, map.get(name)[0]);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
```

# 六、beanutils工具类
导入jar包：`commons-beanutils-1.8.3.jar` `commons-logging-1.1.1.jar`
```java
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

public class UserBeanUtilServlet extends HttpServlet {

	private static final long serialVersionUID = 3625882115495534032L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取数据
		Map<String, String []> map = request.getParameterMap();
		// 创建User对象
		User user = new User();

		// 完成注册
		ConvertUtils.register(new MyDateConverter(), Date.class);

		// 完成封装
		try {
			BeanUtils.populate(user, map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		// 打印
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getMoney());
		System.out.println(user.getBirthday());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
```
默认把字符串转换成日期类型：
`MyDateConverter.java`
```
* 编写一个类，实现Converter接口。重写该方法。把字符串转换日期。
* 在封装数据之前进行注册。ConvertUtils.register(Converter converter, Class clazz) 	Date.class
```
`MyDateConverter.java`
```java
import org.apache.commons.beanutils.Converter;

public class MyDateConverter implements Converter{
	// 字符串转换成日期
	public Object convert(Class clazz, Object obj) {
		// 把输入的字符串，转换成日期类型，返回
		String dDate = (String) obj;
		// 把字符串转换成日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sdf.parse(dDate);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("转换日期错误");
		}
		return date;
	}
}
```

# 七、EL表达式
### 语法：${ }
### 获取（域对象中的）数据
```
<h4>获取域对象中的值</h4>
<%
    pageContext.setAttribute("name", "黄海波");
    request.setAttribute("name", "美美");
%>

${ pageScope.name }
${ requestScope.name }
```

```
<h4>域中数组的值</h4>
<%
    String [] arrs = {"美美","波波","东东","名名"};
    request.setAttribute("arrs", arrs);
%>
${ arrs[2] }
```

```
<h4>域中集合的值</h4>
<%
    List<String> list = new ArrayList<String>();
    list.add("美美");
    list.add("小凤");
    list.add("芙蓉");
    request.setAttribute("list", list);
%>
${ list[1] }
```

```
<h4>域中Map集合的值</h4>
<%
    Map<String,String> map = new HashMap<String,String>();
    map.put("aa", "美美");
    map.put("bb.cc", "小凤");                     //含特殊字符.用中括号取出
    request.setAttribute("map", map);
%>
${ map.bb }                                      /${ map["bb.cc"] }
```

```
<h4>域中集合中有对象的值</h4>
<%
    List<User2> uList = new ArrayList<User2>();
    uList.add(new User2("banzhang","123"));
    uList.add(new User2("美美","abc"));
    request.setAttribute("uList", uList);
%>
${ uList[1].username }
```

### 执行运算
#### 1.加法运算
`${ n1 + n2 }`

#### 2.关系运算
大于：`${ n1 > n2 }	  ${ n1 gt n2 }`  
小于：`${ n1 < n2 }	  ${ n1 lt n2 }`  
等于：`${ n1 == n2 }	  ${ n1 eq n2 }`  
不等于：`${ n1 != n2 }	 ${ n1 ne n2 }`  
大于等于：`${ n1 >= n2 }	${ n1 ge n2 }`  
小于等于：`${ n1 <= n2 }	${ n1 le n2 }`    

#### 3.逻辑运算
与：`${ n1 > n2 && n3 > n4 }		${ n1 > n2 and n3 > n4 }`  
或：`${ n1 > n2 || n3 > n4 }		${ n1 > n2 or n3 > n4 }`  
非：`${ !(n1 > n2) }		${ not (n1 > n2) }`  


### 获取WEB开发中的对象
```
pageScope
requestScope
sessionScope
applicationScope

param 			获取请求的参数	getParameter()
paramValues		获取请求的参数	getParameterValues()

header			获取请求头的信息
headerValues	获取请求头的信息

initParam		获取全局的初始化参数
cookie			获取cookie

pageContext
```

1. EL的WEB对象
`${ param.username }`
2. 获取请求头
`${ header.referer }`
3. 获取全局初始化参数
`${ initParam.username }`
4. pageContext对象
`${ pageContext.request.contextPath }`

* 调用java的方法
