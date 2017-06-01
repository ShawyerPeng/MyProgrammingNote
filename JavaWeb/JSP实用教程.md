# JSP语法
`<% %>`：Java程序片
`<%! %>`：Java变量和方法的声明
`<%= %>`：Java表达式

`<!-- 我是注释 -->`：HTML注释
`<%-- 我是注释 --%>`：JSP注释

## JSP指令标记
### page指令标记
`<%@ page contentType="text/html;charset=gb2312" language="java" session="true" buffer="8KB" autoFlush="true" isThreadSafe="true" info="some infos" %>`
`<%@ page import="java.io.*", "java.util.*" %>`

### include指令标记
`<%@ include file="myfile/first.txt" %>`：include指令标记，在JSP页面内某处整体嵌入一个文件

## JSP动作标记
### include动作标记
`<jsp:include page="文件的URL"/>`：
或  
```jsp
<jsp:include page="文件的URL">
    param子标记
</jsp:include>
```

### forward动作标记
`<jsp:forward page="要转向的页面"/>`：
或  
```jsp
<jsp:forward page="要转向的页面">
    param子标记
</jsp:forward>
```

### param动作标记
`<jsp:param name="名字" value="指定给param的值">`

# JSP内置对象
## request对象
### 获取用户提交的信息
```jsp
<form action="目的地页面.jsp" method="post">
    <input type="text" name="param1" value="ok" size=6>
    <input type="submit" value="提交" name="submit">
</form>
```

`request.getParameter("param1");`：获取表单提交的信息

### 处理汉字信息
`request.setCharacterEncoding("gb2312");`：request设置编码

### 常用方法
`request.getProtocol();`：获取用户向服务器提交信息所使用的通信协议
`request.getServletPath();`：获取用户请求的JSP页面文件的目录
`request.getContextPath();`：获取用户请求的当前Web服务目录
`request.getContentLength();`：获取用户提交的这个信息的长度
`request.getMethod();`：获取用户提交信息的方式(get/post)
`request.getHeader(String s);`：获取HTTP头文件中由参数s指定的头名字的值
`request.getHeaderNames();`：获取头名字的一个枚举
`request.getHeaders(String s);`：获取头文件中指定头名字的全部值的一个枚举
`request.getRemoteAddr();`：获取用户的IP地址
`request.getRemoteHost();`：获取用户机的名称（如果获取不到，则获取IP地址）
`request.getServerName();`：获取服务器的名称
`request.getServerPort();`：获取服务器的端口号
`request.getParameterNames();`：获取用户提交的信息体部分中name参数值的一个枚举

### 处理HTML标记

## response对象
### 动态响应contentType属性
`response.setContentType("application/msword");`：改变ContentType属性值

### response的HTTP文件头
`response.addHeader(String head, String value);`
`response.setHeader(String head, String value);`

### response重定向
`response.sendRedirect("another.jsp");`：重定向到另一个页面

### response的状态行
`response.setStatus(408);`：设置响应状态为408

## session对象
### session对象的id
`session.getId();`：获取session对象的id

### session对象与URL重写
`session.setAttribute(String key, Object obj)`：将参数Object指定的对象obj添加到session对象中，并为添加的对象指定一个索引关键字
`session.getAttribute(String key)`：获取session对象索引关键字是key的对象
`session.getAttributeNames()`：产生一个枚举对象，该枚举对象使用nextElements()遍历session中的各个对象所对应的关键字
`session.removeAttribute(String name)`：移除关键字key对应的对象

```jsp
<form action="" method=post>
    <input type="text" name="name">
    <input type="submit" value="确定" name=submit>
</form>
<%  String name=request.getParameter("name");
   if(name==null)
      name="";
   else
      session.setAttribute("name",name); //将名字存入用户的session中
%>
```

```jsp
<%  String personName=(String)session.getAttribute("name");
    StringBuffer bookMess=null;
    if(personName==null||personName.length()== 0) {
      out.println("到输入名字页面输入姓名");
    }
    else {
       bookMess = (StringBuffer)session.getAttribute("book");
    }
 %>
```

## application对象


## out对象
`out.println()`




# Javabean
## 编写JavaBean
1. 如果类的成员变量的名字是xxx，类中提供两个方法：getXxx()，用来获取属性xxx。setXxx()，用来修改属性xxx。后缀是将成员变量名字的首字母大写的字符序列。
2. 对于boolean类型的成员变量，即布尔逻辑类型的属性，允许使用“is”代替上面的“get”和“set”。
3. 类中声明的方法的访问属性都必须是public的。
4. 类中需提供public、无参数的构造方法。


## 动作标记useBean
`<jsp:useBean id="bean的名字" class="创建beans的字节码" scope="bean有效范围"/>`
或
```jsp
<jsp:useBean id= "bean的名字" class="创建beans的类" scope= "bean有效范围">
</jsp:useBean>
```

## bean的有效范围
scope取值范围给出了bean的存活时间(生命周期)，即scope取值决定了JSP引擎分配给用户的bean的存活时间  

`page`：有效范围是当前页面。不同用户的scope取值是page的bean也是互不相同的。
`request`：bean的有效范围是request期间。不同用户的scope取值是request的bean也是互不相同的。
`session`：有效范围是用户的session(会话)期间。不同用户(即不同浏览器)的scope取值是session的bean是互不相同的（占有不同的内存空间）。
`application`：bean的有效范围是application期间。JSP引擎为Web服务目录下所有的JSP页面分配一个共享的bean，不同用户的scope取值是application的bean也都是相同的一个。

## 获取和修改beans的属性
使用getProperty动作标记可以获得bean的属性值，并将这个值用串的形式发送给用户的浏览器。使用getProperty动作标记之前，必须使用useBean动作标记获得相应的bean。

`<jsp:getProperty  name="id" property="bean属性" />`
或
```jsp
<jsp:getProperty  name=“id"  property="bean属性"/>
</jsp:getProperty>
```
该指令相当于Java表达式：`<%= bean.getXxx() %>`

使用setProperty动作标记可以设置bean的属性值。使用这个标记之前，必须使用useBean标记得到一个相应的bean。
将bean属性的值设置为一个表达式的值或字符串：
`<jsp:setProperty name=“id" property="bean的属性" value= "<%=expression%>"/>`
`<jsp:setProperty name="id" property="bean的属性" value=字符串/>`

通过HTTP表单的参数的值来设置bean的相应属性（所有参数/某个参数）的值：
`<jsp:setProperty name= "id" property="*" />`
`<jsp:setProperty name= "bean的名字" property="bean属性名" param= "表单中的参数名" />`

## beans的辅助类


# Servlet
需要`servlet-api.jar`

## 生命周期
init方法只被调用一次，即在servlet第一次被请求加载时调用该方法。当后续的用户请求servlet服务时，Web服务将启动一个新的线程，在该线程中，servlet调用service方法响应用户的请求，也就是说，每个用户的每次请求都导致service方法被调用执行，其执行过程分别运行在不同的线程中。
### init方法
servlet第一次被请求加载时，服务器创建一个servlet，这个对象调用init方法完成必要的初始化工作。 init方法只被调用一次，即在servlet第一次被请求加载时调用该方法。
无论有多少客户机访问 Servlet，都不会重复执行 init() 。 

管理服务器端资源/初始化数据库连接
```java
public void init(ServletConfig config) throws ServletException {
    super.init(config);
}
```

### service方法
当后续的用户请求该servlet时，服务器将启动一个新的线程，在该线程中，servlet调用service方法响应用户的请求，即每个用户的请求都导致service方法被调用执行，调用过程运行在不同的线程中，互不干扰。
```java
public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.service(req, resp);
}
```

### destroy方法
当服务器终止服务时，比如关闭服务器等，destroy()方法会被执行，消灭servlet。

## 通过JSP页面访问servlet
如果web.xml文件中<servlet-mapping>标记的子标记<url-pattern>指定的请求servlet的格式是“/lookHello”，那么JSP页面请求servlet时，必须要写成 “lookHello”，不可以写成“/lookHello”，否则将变成请求root服务目录下的某个servlet。


## doGet doPost


## 重定向与转发
重定向：将用户从当前页面或servlet定向到另一个JSP页面或servlet；
转发：将用户对当前JSP页面或servlet的请求转发给另一个JSP页面或servlet。

### sendRedirect方法
```java
response.sendRedirect("index.jsp");
```
当用户请求一个servlet时，该servlet在处理数据后，可以使用重定向方法将用户重新定向到另一个JSP页面或servlet。重定向方法仅仅是将用户从当前页面或servlet定向到另一个JSP页面或servlet，但不能将用户对当前页面或servlet的请求（HttpServletRequest对象）转发给所定向的资源。也就是说，重定向的目标页面或servlet无法使用request获取用户提交的数据。



### RequestDispatcher对象
```java
RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
dispatcher.forward(request,response);
```
转发的目标页面或servlet可以使用request获取用户提交的数据。
用户在浏览器的地址栏中不能看到forward方法转发的页面的地址或servlet的地址，只能看到该页面或servlet的运行效果

## 使用session
```java
HttpSession session = request.getSession(true);
session.setAttribute("message", "我是一条消息");
```

```html
<% session.setAttribute("message", "我是一条消息"); %>
<% String message = session.getAttribute("message"); %>
```
记得将jsp页面的scope写成session



# MVC模式


## request周期的Javabean
```java
BeanClass bean=new BeanClass();             // 创建bean对象
request.setAttribute("keyWord",bean);       // 将所创建的bean对象存放到request中
// servlet使用转发方式让JSP页面，显示bean中的数据
```

然后在jsp页面中使用Javabean：
`<jsp:useBean id="名字" class="创建bean的类" scope="生命周期"/>`或
`<jsp:useBean id="名字" type="创建bean的类" scope="生命周期"/>`


## session周期的Javabean
```java
BeanClass bean=new BeanClass();             // 创建bean对象
HttpSession session = request.getSession(true);
session.setAttribute("keyWord",bean);       // 将所创建的bean对象存放到session中
// servlet使用转发或重定向方法让JSP页面来显示bean中的数据
```

## application周期的Javabean
```java
BeanClass bean=new BeanClass();                         // 创建bean对象
getServletContext().setAttribute("keyword", bean);      // 将所创建的bean对象存放到appication中
// servlet使用转发或重定向方法让JSP页面来显示bean中的数据
```


# JSP中使用数据库
## JDBC
`mysql-connector-java-5.1.28-bin.jar`
```java
public class JDBCServlet extends HttpServlet {
    private static String DBurl = "jdbc:mysql://localhost:3306/database?useSSL=false";
    private static String DBname = "root";
    private static String DBpwd = "123";

    private static Connection conn = null;
    private static Statement st = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 获取连接对象
            conn = DriverManager.getConnection(DBurl, DBname, DBpwd);
            // 方法二
            // String uri = "jdbc:mysql://localhost:3306/database?user=root&password=123&useSSL=false&characterEncoding=gb2312";
            // conn = DriverManager.getConnection(uri);


            // 通过连接对象获取操作sql语句Statement
            st = conn.createStatement();

            // 操作sql语句,得到ResultSet结果集
            ResultSet rs = st.executeQuery("SELECT * FROM iuser");

            // 遍历结果集
            while (rs.next()) {
                row++;
                for (int j = 0; j < columnCount; j++) {
                    String value = rs.getString(j+1);
                    // 新建标签Label
                    Label label = new Label(j, row, value);
                    sheet.addCell(label);
                }
            }
            // 释放资源
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 结果集与查询
```java
ResultSetMetaData metaData = rs.getMetaData();      // 得到元数据对象metaData
int columnCount = metaData.getColumnCount();        // 得到结果集的列的个数，即共有几列
String columnName = metaData.getColumnName(i);      // 得到结果集rs中的第i列的名字

```

### 随机查询
`Statement st = conn.createStatement(int type, int concurrency);`
type取值：`ResultSet.TYPE_SCROLL_INSENSITIVE`或`ResultSet.TYPE_SCROLL_SENSITIVE`
Concurrency取值：`ResultSet.CONCUR_READ_ONLY`或`ResultSet.CONCUR_UPDATABLE`

滚动查询经常用到ResultSet的下述方法：
`first()`：将游标移到结果集的第一行
`last()`：将游标移到结果集的最后一行
`getRow()`：得到当前游标所指行的行号（行号从1开始）
`absolute(int row)`：将游标移到参数row指定的行号


### 更新、添加与删除记录
`st.executeUpdate("");`
```java
st.executeUpdate("UPDATE product SET price = 6866 WHERE name='海尔电视机'");               // 更新数据库表中记录的字段值
st.executeUpdate("INSERT INTO students VALUES ('012','神通手机’,'2015-2-26',2687)");       // 向数据库表中添加新的记录
st.executeUpdate("DELETE  FROM product WHERE number = '888' ");                           // 删除数据库表中的记录
```


## 预处理语句对象PreparedStatement
`PreparedStatement pre = conn.prepareStatement("sql");`
对参数sql指定的SQL语句进行预编译处理

pre调用下列方法都可以使得该底层内部命令被数据库执行：`executeQuery()`、`execute()`、`executeUpdate()`

只要编译好了PreparedStatement对象pre，那么pre可以随时地执行上述方法，提高了访问数据库的速度。


## 使用通配符
使用通配符“?”来代替字段的值

```java
prepareStatement pre = con.prepareStatement("SELECT * FROM product WHERE price < ? ");
pre.setDouble(1, 6565);     // 指定上述预处理语句pre中第1个通配符"?"代表的值是6565
```

## 事务
```java
Connection conn = null;
Statement sql; 
ResultSet rs;
try {
    Class.forName("com.mysql.jdbc.Driver");
    conn = DriverManager.getConnection("xxx");
    conn.setAutoCommit(false);       // 关闭自动提交模式
    sql=con.createStatement();

    rs = sql.executeQuery("SELECT userMoney FROM user WHERE name='geng'");
    rs.next();

    rs=sql.executeQuery("SELECT userMoney FROM user WHERE name='zhang'");
    rs.next();

    sql.executeUpdate("UPDATE user SET userMoney=" + gengMoney + " WHERE name='geng'");
    sql.executeUpdate("UPDATE user SET userMoney=" + zhangMoney + " WHERE name='zhang'");
    conn.commit();                  // 开始事务处理

    rs=sql.executeQuery("SELECT * FROM user WHERE name='zhang'||name='geng'");
    while(rs.next()) {
        out.print(rs.getString(1)+"	");
        out.print(rs.getString(2)); 
        out.print("<br>");
    }
    con.close();



} catch (SQLException e) {
    try { 
        conn.rollback();             //撤消事务所做的操作
    } catch(SQLException e) {
        e.printStackTrace();
    }
}
```

## 常见数据库连接
1. Microsoft SQL Server
```java
Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

String uri = "jdbc:sqlserver://localhost:1433;DatabaseName=warehouse";
String user="root";
String password="123";
con=DriverManager.getConnection(uri, user, password);
```

2. Oracle
```java
Class.forName("oracle.jdbc.driver.OracleDriver").newInstance(); 

String user="root";
String password="123";
con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.96.1:1521:oracle9i", user, password);
```

3. Microsoft Access
```java
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

Connection con = DriverManager.getConnection("jdbc:odbc:数据源名字", "root"," 123 ")
```





# JSP中的文件操作
## 文件上传
```html
<form action="xxx.jsp" method="post" ENCTYPE="multipart/form-data">
    <input type="file" name="picture">
    <input type="submit" value="提交">
</form>
```

```java
try{  
    InputStream in=request.getInputStream();
    File dir=new File("C:/1000");
    dir.mkdir();
    File f=new File(dir,"B.txt");
    FileOutputStream o=new FileOutputStream(f);
    byte b[]=new byte[1000];
    int n;
    while((n=in.read(b))!=-1)
        o.write(b,0,n);
    o.close();
    in.close();
    System.out.println("文件已上传");
}
catch(IOException e){
    e.printStackTrace();
}
```


## 文件下载
`download.jsp`
```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/loadFile" method=post name=form>
    选择要下载的文件：<br>
    <select name="filePath" size=3>
        <option selected value="D:/hello.java">hello.java
        <option value="D:/world.jsp">world.jsp
    </select>
    <br><input type="submit" value="提交" >
</form>
</body>
</html>
```

`FileServlet.java`
```java
public class FileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String filePath = request.getParameter("filePath");
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        try{
            // 读取文件,并发送给用户下载
            File f = new File(filePath);
            FileInputStream in = new FileInputStream(f);
            OutputStream out = response.getOutputStream();
            int n = 0;
            byte b[] = new byte[500];
            while((n=in.read(b)) != -1)
                out.write(b,0,n);
            out.close();
            in.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
```

`web.xml`
```xml
<servlet>
    <servlet-name>FileServelt</servlet-name>
    <servlet-class>servlet.FileServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>FileServelt</servlet-name>
    <url-pattern>/loadFile</url-pattern>
</servlet-mapping>
```