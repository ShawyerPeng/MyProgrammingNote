# 一、JSP、EL表达式的入门
Servlet/JSP	是两种动态的WEB资源的两种技术。
* 使用Servlet生成HTML的页面
    `response.getWriter("<form action='' method='post'>");`
    `response.getWriter("<input type='text' name='username'>");`
* JSP简介
    * java server pages		java服务器端的网页们，是在服务器端执行。
    * JSP不仅可以写HTML+java代码+JSP自己的代码
    * JSP的运行原理（*****）：JSP -- 翻译成Servlet -- 编译 -- 访问

* JSP的脚本元素（JSP的页面可以编写java代码）
    ```
    <%!  %>		：定义类、定义变量、定义方法（不常用）	成员变量。
    <%=  %>		：输出语句（输出到页面，不能有分号）
    <%   %>		：定义变量、语句
    ```
    ```js
    <html>
        <body>
        	<table border="1" width="60%">
                <% for(int i=1;i<=10;i++){ %>
            	<tr>
            	<% for(int j=1;j<=10;j++){ %>
            		<td>1</td>
            	<% } %>
            	</tr>
                <% } %>
            </table>
        </body>
    </html>
    ```

* EL快速入门
    * 获取域对象中的内容：`request.setAttribute("xx","yy");`
    `${xx}`
    ```js
    <html>
        <body>
        <% request.setAttribute("aa", "苍老师"); %>
        <%= request.getAttribute("aa") %>
        ${ aa }
        </body>
    </html>
    ```

# 二、JSP的脚本元素

# 三、cookie和session的原理
### cookie
1. 显示上次的访问时间（案例）
    * 第一次访问，输出欢迎，在服务器端，记录当前的时间，把当前的时间通过cookie回写到浏览器。
    * 第二次访问，会带着上次的时间，获取时间，可以把时间显示到页面上，记录当前的时间，再把回写浏览器。
    输出上次的访问时间。

2. Cookie的API
	```
	* cookie的构造方式	Cookie(String name, String value)
	* String getName()  获取cookie的名称
	* String getValue() 获取cookie的值

	* void setMaxAge(int expiry)  			：设置有效时间
		* 失效cookie	setMaxAge(0);	前提条件：设置有效路径（和之前设置过相同）

	* void setPath(String uri)  			：设置有效路径
		* 默认的有效路径（）
			* 配置	/last				默认路径	/day11
			* 配置	/servlet/last		默认路径	/day11/servlet

	* void setDomain(String pattern)  		：设置有效域名
		* www.sina.com.cn
		* sports.sina.com.cn
		* xxx.sina.com.cn
		* 设置有效域名	setDomain(".sian.com.cn");

	* 会话级别的cookie：默认保存到浏览器的内存中。
	* 持久的cookie：把cookie保存到磁盘上。通过setMaxAge(int a)进行设置。
	```

3. 显示用户上次访问过的商品信息（需求）
	```
	1.获取请求参数
	2.获取cookie数组，通过指定的名称（自己指定）查找cookie
	3.如果cookie==null，第一次访问
			* 如果是第一次访问，创建cookie，回写浏览器
	4.如果cookie!=null，不是第一次访问
			* 如果不是第一次访问，说明我的cookie中已经存在id
				* 判断，当前的id是否已经存在cookie的中value
				* 如果存在，不用操作
				* 如果不存在，在后面追加（product=1,2）
	5.重定向到商品页面
	```

	```java
	public class LastServlet extends HttpServlet {
		private static final long serialVersionUID = -5604481158386227221L;

		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			/**
			* 	1.获取所有的cookie，判断是否是第一次访问
			* 	2.如果是第一次访问
			* 		* 输出欢迎，记录当前的时间，回写到浏览器
			* 	3.如果不是第一次访问
			* 		* 获取时间，输出到浏览器，记录当前的时间，回写到浏览器。		
			* 	记录当前的时间，回写到浏览器。
			*/
			// 设置字符中文乱码问题
			response.setContentType("text/html;charset=UTF-8");
			// 获取所有的cookie
			Cookie [] cookies = request.getCookies();
			// 通过指定cookie名称来查找cookie		Cookie c = new Cookie("last","当前的时间");
			Cookie cookie = MyCookieUtil.getCookieByName(cookies,"last");
			// 判断，如果cookie==null，说明是第一次访问
			if(cookie == null){
				// 输出欢迎，记录当前的时间，回写到浏览器
				response.getWriter().write("<h3>亲，欢迎再来哦！！</h3>");
			}else{
				// 获取cookie的值，输出浏览器，记录当前的时间，回写到浏览器
				String value = cookie.getValue();
				// 输出浏览器
				response.getWriter().write("<h3>亲，您又来了，上次的时间是"+value+"</h3>");
			}
			// 记录当前的时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sDate = sdf.format(date);
			// 回写到浏览器
			// 使用cookie回写
			Cookie c = new Cookie("last", sDate);
			// 设置有效时间
			c.setMaxAge(60*60);	// 秒
			// 设置有效路径
			c.setPath("/day11");
			// 回写
			response.addCookie(c);
		}

		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doGet(request, response);
		}
	}
	```

### session（服务器）
```
* cookie基于客户端，不安全，并且大小和个数的限制。
* session域对象，范围一次会话范围，存个人相关的数据。
    * setAttribute(String name, Object value)
    * Object getAttribute(String name)  

    * String getId()  获取seesion的唯一的ID
    * void invalidate()  销毁的seesion


* 完成简单的购物车
    * 购物车	Map<String,Integer> cart	购物车，把购物车存入seesion中。

    * 获取数据
    * 判断是否是第一次访问    session.getAttribute("cart");
      * 如果第一次访问，cart	创建一个购物车，放入商品的名称和数量

      * 如果不是第一次访问，cart!=null
        判断是否包含该商品？
           * 如果包含，数量拿出来，++，放回去，存入seesion中

           * 如果不包含，正常放入到购物车中。存入seesion中
```
