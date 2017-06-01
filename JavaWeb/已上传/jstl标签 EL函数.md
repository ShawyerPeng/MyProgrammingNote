# 一、jstl标签
### 1.JSTL标签库概述
```
* JSP标准标签库。
* 作用：和EL表达式一起 取代<% %>
* 版本：1.0		1.1和1.2（区别不大）
    * 1.0EL表达式没有纳入规范
    * 1.1和1.2EL表达式纳入规范
```
导入Jar包：`jstl.jar`和`standard.jar`
标签库：`c.tld`(核心的标签库) 和 `fn.tld`(EL表达式标签库)

### 2.JSTL的快速入门
1. 导入相应jar包。
2. 新建JSP的文件，引入标签库:`<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>`   
3. 直接使用标签库

### 3.JSTL的标签
```
<c:out>		输出内容
    value	 ：输出的内容（常量或者变量）
    default	 : 默认值
    escapeXml：默认是true，进行转义，设置成false，不转义。

    代码：
        <c:out value="Hello"></c:out>
        <c:out value="${ name }"></c:out>
        <!-- "" -->
        <c:out value="${ city }" default="取不到"></c:out>
        <c:out value="<a href='#'>超链接</a>" escapeXml="false"/>

<c:set>
    * 属性
        var 	：定义属性
        value	：存入的值
        scope	：域范围

        target	：修改JavaBean对象
        property：修改的属性

    * 向4个web域对象存入值
        <c:set var="i" value="10" scope="request" ></c:set>

    * 修改JavaBean对象属性的值
        <c:set target="${ user }" property="username"  value="小凤"></c:set>

<c:remove>
    * 属性
        var		：删除的属性
        scope	：在域的范围

    * 代码
        <c:set var="name" value="小凤" scope="page"></c:set>
        ${ name }

        <c:remove var="name" scope="page"/>
        ${ name }

* <c:catch>
    * 属性：var	把异常的信息保存变量中
    * 代码
        <c:catch var="e">
            <%
                int a = 10/0;
            %>
        </c:catch>
        ${ e.message }

* <c:if>	没有<c:else>标签
    * 属性
        * test	判断的条件
        * var	计算的结果保存到变量中
        * scope	域的范围

    * 代码
        <c:set var="i" value="10" scope="page"></c:set>
        <c:if test="${ i ge 10 }" var="x" scope="page">
            i >= 10
        </c:if>
        <c:if test="${ i lt 10 }">
            i < 10
        </c:if>
        ${ x }

* <c:choose>标签
    <c:when>
    <c:otherwise>

    代码：
        <c:set var="i" value="10" scope="page"></c:set>
        <c:choose>
            <c:when test="${ i ge 10 }">
                i >= 10
            </c:when>
            <c:when test="${ i lt 10 }">
                i < 10
            </c:when>
            <c:otherwise>
                其他
            </c:otherwise>
        </c:choose>


* <c:forEach>（*****）
    * 循环遍历数据（数组，集合，Map集合）
    * 属性
        var 	：遍历数据的类型
        items	：要遍历的内容

        begin	：从哪开始
        end		：到哪结束
        step	：步长

        varStatus：记录循环遍历的信息
            * index
            * count（常用）
            * first
            * last

    * 代码
        <%
            String [] arrs = {"美美","小凤","芙蓉","小苍"};
            request.setAttribute("arrs", arrs);
        %>
        <!-- for(String s : arrs){ }  -->
        <c:forEach var="s" items="${ arrs }">
            ${ s }
        </c:forEach>

        <h4>遍历集合</h4>
        <%
            List<String> list = new ArrayList<String>();
            list.add("美美");
            list.add("小凤");
            list.add("芙蓉");
            list.add("小泽");
            request.setAttribute("list", list);
        %>
        <c:forEach var="s" items="${ list }">
            ${ s }
        </c:forEach>

        <h4>遍历Map集合</h4>
        <%
            Map<String,String> map = new HashMap<String,String>();
            map.put("aa", "美美");
            map.put("bb", "小凤");
            map.put("cc", "芙蓉");
            request.setAttribute("map", map);
        %>
        <c:forEach var="entry" items="${ map }">
            ${ entry.key } -- ${ entry.value }
        </c:forEach>

        <h4>遍历对象的集合</h4>
        <%
            List<User> uList = new ArrayList<User>();
            uList.add(new User("美美","123"));
            uList.add(new User("小风","234"));
            uList.add(new User("芙蓉","345"));
            request.setAttribute("uList", uList);
        %>
        <c:forEach var="user" items="${ uList }">
            ${ user.username } -- ${ user.password }
        </c:forEach>


        <h4>迭代数据</h4>
        <h4>迭代从1到10</h4>
        <c:forEach var="i" begin="1" end="10" step="2">
            ${ i }
        </c:forEach>


        <h4>计算从1加到100的和</h4>
        <c:set var="sum" value="0" scope="page"></c:set>
        <c:forEach var="i" begin="1" end="100" step="1">
            <c:set var="sum" value="${ sum + i }"></c:set>
        </c:forEach>
        ${ sum }

        <h4>遍历10到100的偶数，每到第3个数，显示红色</h4>
        <c:forEach var="i" begin="10" end="100" step="2" varStatus="status">
            <c:choose>
                <c:when test="${ status.first }">
                    <font color="blue">${ i }</font>
                </c:when>
                <c:when test="${ status.count % 3 eq 0 }">
                    <font color="red">${ i }</font>
                </c:when>
                <c:otherwise>
                    ${ i }
                </c:otherwise>
            </c:choose>
        </c:forEach>


* <c:param>	传递参数
    * 属性
        name	：参数名称
        value	：参数的值

* <c:import>	包含页面
    * url		：引入页面的地址
    * context	：虚拟路径
    * var		：引入页面保存到属性中
    * scope		：域的范围

    * 代码
        <c:import url="/jstl/choose.jsp" context="/day13" var="i" scope="page">
            <c:param name="username" value="meimei"></c:param>
        </c:import>
        ${ i }

* <c:url>
    <c:url>标签用于在JSP页面中构造一个URL地址，其主要目的是实现URL重写。
    URL重写就是将会话标识号以参数形式附加在URL地址后面
    http://localhost/day12/demo?jsessionid=xxxxxxxxxxxxxxxxxx;

    * 属性
        * var		：声明属性
        * value		：地址
        * scope		：域范围
        * context	：虚拟路径

    * 代码
        <c:url var="i" value="/jstl/choose.jsp" scope="request" context="/day13">
            <c:param name="username" value="xiaofeng"></c:param>
        </c:url>

        <a href="${ i }">choose</a>

* <c:redirect>重定向
    * 属性
        * url		：重定向的地址
        * context	：虚拟路径
    * 代码
        <c:redirect url="/jstl/choose.jsp" context="/day13">
            <c:param name="username" value="furong"></c:param>
        </c:redirect>

* <c:forTokens>分隔字符串（了解）
    <h4>分隔字符串</h4>
    <c:set var="i" value="aa,bb,cc" scope="page"></c:set>
    <c:forTokens items="${i }" delims="," var="x">
        ${ x }
    </c:forTokens>
```

# 二、EL函数
### 1.自定义EL函数（入门）
1. 编写一个类，方法必须是静态方法。
2. 在WEB-INF目录下创建.tld的文件，配置。(选择2.0)
3. 完成配置
```xml
 <!-- 配置自定义的EL函数 -->
 <function>
    <!-- 配置方法名称 -->
    <name>sayHi</name>
    <!-- 方法所在的类 -->
    <function-class>cn.itcast.el.ElDemo1</function-class>
    <!-- 配置方法的签名 -->
    <function-signature>java.lang.String sayHello(java.lang.String)</function-signature>
 </function>
```

### 2.自定义标签
1. 实现`SimpleTag`接口：编写一个类，继承`SimpleTagSupport`类。
2. 重写5个方法
```
void setJspContext(JspContext pc)
void setParent(JspTag parent)
void setJspBody(JspFragment jspBody)  
void doTag()
JspTag getParent()
```


#### (1).快速入门的步骤（自定义没有标签体的标签）
* 编写一个类，继承`SimpleTagSupport`
* 选择重写的方法，`doTag()`必须有的
    ```java
    public class TagDemo1 extends SimpleTagSupport{
        private PageContext pc;

        public void doTag() throws JspException, IOException {
            pc.getOut().write("Hello");
        }

        // 服务器默认先执行该方法
        public void setJspContext(JspContext pc) {
            this.pc = (PageContext) pc;
        }
    }
    ```
* 需要配置
    ```xml
    <!-- 配置自定义标签 -->
        <tag>
        <!-- 配置标签名称 -->
        <name>print</name>
        <!-- 配置标签的类 -->
        <tag-class>cn.itcast.tag.TagDemo1</tag-class>
        <!-- 配置标签主体 -->
        <body-content>empty</body-content>
        </tag>
        ```
* 在JSP页面上，引入标签库：`<%@ taglib uri="http://www.itcast.cn/1110/myc" prefix="myc" %>`  
* 使用标签了

#### (2).带有标签主体的标签
* 编写类，继承`SimpleTagSupport`
* 重写`doTag()`
* 获取标签主体对象：`JspFragment jf = getJspBody();` `jf.invoke(null);`
    ```java
    public class TagDemo2 extends SimpleTagSupport{
        private PageContext pc;

        public void doTag() throws JspException, IOException {
            JspFragment jf = getJspBody();
            jf.invoke(null);
        }

        public void setJspContext(JspContext pc) {
            this.pc = (PageContext)pc;
        }
    }
    ```
* 配置
    ```xml
    <!-- 配置自定义标签 -->
        <tag>
        <!-- 配置标签名称 -->
        <name>out</name>
        <!-- 配置标签的类 -->
        <tag-class>cn.itcast.tag.TagDemo2</tag-class>
        <!-- 配置标签主体 -->
        <body-content>scriptless</body-content>
        </tag>
        ```
        ```
    <body-content>元素的可选值有：
        empty：不能有标签体内容。
        JSP：标签体内容可以是任何东西：EL、JSTL、<%=%>、<%%>，以及html；但不建议使用Java代码段，SimpleTag已经不再支持使用<body-content>JSP</body-content>；
        scriptless：标签体内容不能是Java代码段，但可以是EL、JSTL等；
        tagdependent：标签体内容不做运算，由标签处理类自行处理，无论标签体内容是EL、JSP、JSTL，都不会做运算。
    ```

#### (3).带有属性的标签
* 编写类，继承`SimpleTagSupport`
* 重写`doTag()`
* 编写一个属性，属性必须和标签中的属性是相同
* 提供`set()`方法
* 获取标签主体对象：`JspFragment jf = getJspBody();` `jf.invoke(null);`
    ```java
    public class TagDemo3 extends SimpleTagSupport{
        private boolean test;

        public void setTest(boolean test) {
            this.test = test;
        }

        public void doTag() throws JspException, IOException {
            if(test){
                getJspBody().invoke(null);
            }
        }
    }
    ```
* 配置
    ```xml
    <!-- 配置自定义标签 -->
        <tag>
        <!-- 配置标签名称 -->
        <name>if</name>
        <!-- 配置标签的类 -->
        <tag-class>cn.itcast.tag.TagDemo3</tag-class>
        <!-- 配置标签主体 -->
        <body-content>scriptless</body-content>
        <!-- 配置属性 -->
        <attribute>
            <!-- 配置属性名称 -->
            <name>test</name>
            <!-- 属性是否是必须的 -->
            <required>true</required>
            <!-- 是否支持EL表达式 -->
            <rtexprvalue>true</rtexprvalue>
            <!-- 属性的类型 -->
            <type>boolean</type>
        </attribute>
        </tag>
        ```
