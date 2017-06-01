# 一、SpringMVC架构的概念及应用
![](http://p1.bpimg.com/567571/1d1ab9c7ff7efa04.jpg)
![](http://p1.bpimg.com/567571/eb59573b94f97474.jpg)
```
DispatcherServlet前端控制器-----------------------ViewResolver视图解析器、View视图
            |
            |----请求查找Handler
            |                                                               
            \/                                                              
HandlerMapping处理器映射器                                                 
            |                                                               
            |----请求执行Handler                                             
            |                                                               
            \/                                                              
HandlerAdapter处理器适配器-----------------------Handler处理器            
```
### 步骤
1. 用户发起request请求，请求至`DispatcherServlet`前端控制器
2. `DispatcherServlet`前端控制器请求HandlerMapping处理器映射器查找Handler  
    (`DispatcherServlet`：前端控制器，相当于中央调度器，各个组件都和前端控制器进行交互，降低各个组件之间耦合度)  
3. `HandlerMapping`处理器映射器，根据url及一些配置规则（xml配置、注解配置）查找Handler，将Handler返回给DispatcherServlet前端控制器
4. `DispatcherServlet`前端控制器调用`适配器`执行`Handler`，有了适配器通过适配器去扩展对不同Handler执行方式（比如：原始servlet开发，注解开发）
5. `适配器`执行`Handler`
	(Handler：后端控制器，当成模型)
6. `Handler`执行完成返回`ModelAndView`
	(`ModelAndView`：springmvc的一个对象，对Model和view进行封装)
7. 适配器将`ModelAndView`返回给`DispatcherServlet`
8. `DispatcherServlet`调用`视图解析器`进行视图解析，解析后生成view
	视图解析器根据逻辑视图名解析出真正的视图。
	(`View`：springmvc视图封装对象，提供了很多view，jsp、freemarker、pdf、excel)
9. `ViewResolver`视图解析器给前端控制器返回view
10. `DispatcherServlet`调用`view`的渲染视图的方法，将模型数据填充到`request`域
11. `DispatcherServlet`向用户响应结果(jsp页面、json数据...)

### 组成
* `DispatcherServlet`：前端控制器，相当于中央调度器，可以降低组件之间的耦合度。由springmvc提供
* `HandlerMappting`：处理器映射器，负责根据url查找Handler。由springmvc提供
* `HandlerAdapter`：处理器适配器，负责根据适配器要求的规则去执行处理器，可以通过扩展适配器支持不同类型的Handler。由springmvc提供
* `Handler`：处理器，需要程序员开发
* `ViewResolver`：视图解析器，根据逻辑视图名解析成真正的视图，可配置视图解析器的前缀和后缀，真正视图地址==前缀+逻辑视图名+后缀。由springmvc提供
* `View`：真正视图页面需要由程序编写

# 二、入门程序
### 1. 前端控制器
在`web.xml`中配置：
```xml
<!-- 前端控制器 -->
<servlet>
      <servlet-name>springmvc</servlet-name>
      <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      <!-- 加载springmvc配置 -->
      <init-param>
              <param-name>contextConfigLocation</param-name>
              <!-- 配置文件的地址
                  如果不配置contextConfigLocation，
                  默认查找的配置文件名称classpath下的：servlet名称+"-serlvet.xml"即：springmvc-serlvet.xml
               -->
               <!-- 一定要加classpath:，不然服务器运行会报错 -->
              <param-value>classpath:springmvc.xml</param-value>
      </init-param>

</servlet>
<servlet-mapping>
      <servlet-name>springmvc</servlet-name>
      <!--
      可以配置/ ，此工程 所有请求全部由springmvc解析，此种方式可以实现 RESTful方式，需要特殊处理对静态文件的解析不能由springmvc解析
      可以配置*.do或*.action，所有请求的url扩展名为.do或.action由springmvc解析，此种方法常用
      不可以/*，如果配置/*，返回jsp也由springmvc解析，这是不对的。
       -->
      <url-pattern>*.action</url-pattern>
</servlet-mapping>
```

### 2. springmvc.xml
在config目录下的`springmvc.xml`中配置springmvc架构三大组件（处理器映射器、处理器适配器、视图解析器）和Handler
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/aop
		http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
		http://www.springframework.org/schema/tx
		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd ">

	<!-- 1.配置处理器映射器，根据 HandlerMapping 接口判断是否是处理器映射器 -->
	<!-- 根据bean的name进行查找Handler 将action的url配置在bean的name中 -->
	<bean
		class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping" />

	<!-- 2.配置处理器适配器，根据 HandlerAdapter 接口判断是否是处理器适配器 -->
	<bean
		class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter" />

    <!-- 3.配置Handler，由于使用了BeanNameUrlHandlerMapping处理映射器，name配置为url -->
    <bean name="/itemList.action"
        class="cn.itcast.springmvc.first.ItemController1" />

	<!-- 4.配置视图解析器 要求将jstl的包加到classpath -->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/jsp/" />  
		<property name="suffix" value=".jsp" />
	</bean>
</beans>
```

#### (1)处理器映射器
在`springmvc.xml`中配置：  
`BeanNameUrlHandlerMapping`： 根据请求url（XXXX.action）匹配spring容器bean的name找到对应的bean（程序编写的Handler）
```xml
<!-- 根据bean的name进行查找Handler 将action的url配置在bean的name中 -->
<bean
	class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"
/>
```
所有处理器映射器都实现HandlerMapping接口

#### (2)处理器适配器
在`springmvc.xml`配置，根据`HandlerAdapter`接口判断是否是处理器适配器。
```xml
<bean		
    class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"
/>
```
所有的适配器都是实现了`HandlerAdapter`接口  

程序编写Handler根据适配器的要求编写。
`SimpleControllerHandlerAdapter`适配器要求：通过`supports`方法知道`Handler`必须要实现哪个接口：
```java
public boolean supports(Object handler){
    return (handler instanceof Controller);
}
```

#### (3)Handler编写
返回一个ModelAndView。
```java
public class ItemController1 implements Controller {
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// 使用静态数据将商品信息列表显示在jsp页面
		// 商品列表
		List<Items> itemsList = new ArrayList<Items>();

		Items items_1 = new Items();
		items_1.setName("联想笔记本");
		items_1.setPrice(6000f);
		items_1.setCreatetime(new Date());
		items_1.setDetail("ThinkPad T430 联想笔记本电脑！");

		Items items_2 = new Items();
		items_2.setName("苹果手机");
		items_2.setPrice(5000f);
		items_2.setDetail("iphone6苹果手机！");

		itemsList.add(items_1);
		itemsList.add(items_2);

		ModelAndView modelAndView = new ModelAndView();
		// 1.将数据填充到request域
        // request.setAttribute("itemsList", itemsList);
		modelAndView.addObject("itemsList", itemsList);
		// 2.指定转发的jsp页面
		modelAndView.setViewName("/WEB-INF/jsp/itemsList.jsp");
		return modelAndView;
	}
}
```

#### (4)配置Handler
在`springmvc.xml`配置`Handler`由`spring`管理`Handler`。
```xml
<!-- 配置Handler 由于使用了BeanNameUrlHandlerMapping处理映射器，name配置为url -->
<bean id="itemController1" name="/itemList.action"
    class="cn.itcast.springmvc.first.ItemController1"
/>
```

#### (5)配置视图解析器
配置视图解析，能够解析jsp视图。（要求将jstl的包加到classpath）
```xml
<!-- 配置视图解析器 要求将jstl的包加到classpath -->
<!-- ViewResolver -->
<bean
    class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/jsp/" />
    <property name="suffix" value=".jsp" />
</bean>
```

# 三、其它非注解处理器映射器和适配器
1. `BeanNameUrlHandlerMapping`(映射器)  
根据请求url（XXXX.action）匹配spring容器bean的name
找到对应的bean（程序编写的Handler）  

2. `SimpleUrlHandlerMapping`(映射器)
```xml
<!--简单url映射 集中配置bean的id对应 的url -->
<bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
    <property name="mappings">
        <props>
            <prop key="/itemsTest1.action">itemController1</prop>
            <prop key="/itemsTest2.action">itemController1</prop>
        </props>
    </property>
</bean>
```
注意：在springmvc.xml配置了多个处理器映射器，多个处理器映射器可以共存。

3. `SimpleControllerHandlerAdapter`(适配器)  
要求程序编写的`Handler(Controller)`需要实现`Controller`接口。

4. `HttpRequestHandlerAdapter`（适配器）  
在`springmvc.xml`配置：`HttpRequestHandlerAdapter`  
要求`Handler`实现`HttpRequestHandler`接口
```xml
<!-- HttpRequestHandlerAdapter适配器 -->
<bean class="org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter" />
```
    1. 开发Handler  
        ```java
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;

        import org.springframework.web.HttpRequestHandler;
        import org.springframework.web.servlet.ModelAndView;
        import org.springframework.web.servlet.mvc.Controller;

        import cn.itcast.springmvc.po.Items;

        public class ItemController2 implements HttpRequestHandler {

        	@Override
        	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
        			throws ServletException, IOException {

        		// 使用静态数据将商品信息列表显示在jsp页面
        		// 商品列表
        		List<Items> itemsList = new ArrayList<Items>();

        		Items items_1 = new Items();
        		items_1.setName("联想笔记本");
        		items_1.setPrice(6000f);
        		items_1.setCreatetime(new Date());
        		items_1.setDetail("ThinkPad T430 联想笔记本电脑！");

        		Items items_2 = new Items();
        		items_2.setName("苹果手机");
        		items_2.setPrice(5000f);
        		items_2.setDetail("iphone6苹果手机！");

        		itemsList.add(items_1);
        		itemsList.add(items_2);

        		request.setAttribute("itemsList", itemsList);
        		// 转发到jsp页面
        		request.getRequestDispatcher("/WEB-INF/jsp/itemsList.jsp").forward(request, response);
        	}
        }
        ```
    2. 配置Handler  
    `BeanNameUrlHandlerMapping`或`SimpleUrlHandlerMapping`都可配置
    ```xml
    <!-- handler2 -->
    <bean id="itemController2" class="cn.itcast.springmvc.first.ItemController2"></bean>

    <!--简单url映射 集中配置bean的id对应 的url -->
    <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="mappings">
            <props>
                <prop key="/itemsTest2.action">itemController2</prop>
            </props>
        </property>
    </bean>
    ```

# 四、DispatcherServlet.properoties
`DispatcherServlet`前端控制器加载`DispatcherServlet.properoties`配置文件，从而默认加载各个组件，如果在`springmvc.xml`中配置了处理器映射器和适配器，以`sprintmvc.xml`中配置的为准。

# 五、注解映射器和适配器
### 1.注解映射器
`spring3.1之前`默认加载映射器是`org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping`
`spring3.1之后`要使用：`org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping`

在`springmvc.xml`中配置`RequestMappingHandlerMapping`：  
使用RequestMappingHandlerMapping需要在`Handler`中使用`@controller`标识此类是一个控制器，使用`@requestMapping`指定Handler方法所对应的url。
```xml
<!-- 注解处理器映射器 -->
<bean
    class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"></bean>
```

### 2.注解适配器
`spring3.1之前`默认加载映射器是`org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter`
`spring3.1之后`要使用：`org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter`

RequestMappingHandlerAdapter，不要求Handler实现任何接口，它需要和RequestMappingHandlerMapping注解映射器配对使用，主要解析Handler方法中的形参。

### 3.注解开发Handler
```java
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.springmvc.po.Items;

@Controller
public class ItemController3 {
	//商品列表，@RequestMapping中url建议和方法名一致，方便开发维护
	@RequestMapping("/queryItems")
	public ModelAndView queryItems(){
		// 使用静态数据将商品信息列表显示在jsp页面
		// 商品列表
		List<Items> itemsList = new ArrayList<Items>();

		Items items_1 = new Items();
		items_1.setName("联想笔记本");
		items_1.setPrice(6000f);
		items_1.setCreatetime(new Date());
		items_1.setDetail("ThinkPad T430 联想笔记本电脑！");

		Items items_2 = new Items();
		items_2.setName("苹果手机");
		items_2.setPrice(5000f);
		items_2.setDetail("iphone6苹果手机！");

		itemsList.add(items_1);
		itemsList.add(items_2);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("itemsList", itemsList);
		// 指定逻辑视图名
		modelAndView.setViewName("itemsList");

		return modelAndView;
	}
	//商品添加
	//商品删除
}
```

### 4.配置Handler
建议使用组件扫描，组件扫描可以扫描`@Controller`、`@Service`、`@Component`、`@Repsitory`
```xml
<!-- 注解的handler，单个配置 -->
<bean class="cn.itcast.springmvc.first.ItemController3"/>
<!-- 使用spring组件扫描 -->
<context:component-scan base-package="cn.itcast.springmvc.first" />
```

### 总结
```xml
<!-- sss -->
```
1.`DispatcherServlet`通过`HandlerMapping`查找Handler  
2.DispatcherServlet通过适配器去执行Handler，得到ModelAndview  
3.视图解析，解析完成得到一个view  
4.进行视图渲染，将Model中的数据 填充到request域  


# 六、springmvc和mybatis整合
### 1. 整合思路
在mybatis和spring整合的基础上添加springmvc。

spring要管理springmvc编写的Handler（controller）、mybatis的SqlSessionFactory、mapper

第一步：整合`dao`，spring和mybatis整合  
第二步：整合`service`，spring管理service接口，service中可以调用spring容器中dao(mapper)  
第三步：整合`controller`，spring管理controller接口，在controller调用service

### 2. jar包
mybatis：3.2.7  
spring：3.2.0  

mybatis的jar  
mybatis和spring整合包  
spring的所有jar包(包括 springmvc的包)  
数据库驱动包  
log4j日志..  

### 3. 工程结构
#### 配置文件
1. `applicationContext-dao.xml`---配置数据源、SqlSessionFactory、mapper扫描器
    ```xml
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    		http://www.springframework.org/schema/mvc
    		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
    		http://www.springframework.org/schema/context
    		http://www.springframework.org/schema/context/spring-context-3.2.xsd
    		http://www.springframework.org/schema/aop
    		http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
    		http://www.springframework.org/schema/tx
    		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd ">

    	<!-- 加载配置文件 -->
    	<context:property-placeholder location="classpath:db.properties" />
    	<!-- 数据库连接池 -->
    	<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
    		destroy-method="close">
    		<property name="driverClassName" value="${jdbc.driver}" />
    		<property name="url" value="${jdbc.url}" />
    		<property name="username" value="${jdbc.username}" />
    		<property name="password" value="${jdbc.password}" />
    		<property name="maxActive" value="10" />
    		<property name="maxIdle" value="5" />
    	</bean>

    	<!-- SqlsessionFactory -->
    	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    		<!-- 数据源 -->
    		<property name="dataSource" ref="dataSource"/>
    		<!-- mybatis配置文件 -->
    		<property name="configLocation" value="classpath:mybatis/SqlMapConfig.xml"/>
    	</bean>

    	<!--
    	MapperScannerConfigurer：mapper的扫描器，将包下边的mapper接口自动创建代理对象，
    	自动创建到spring容器中，bean的id是mapper的类名（首字母小写）
    	 -->
    	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    		<!-- 配置扫描包的路径
    		如果要扫描多个包，中间使用半角逗号分隔
    		要求mapper.xml和mapper.java同名且在同一个目录
    		 -->
    		<property name="basePackage" value="cn.itcast.ssm.mapper"/>
    		<!-- 使用sqlSessionFactoryBeanName -->
    		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    	</bean>

    </beans>
    ```

2. `applicationContext-service.xml`---配置service接口
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/mvc
		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/aop
		http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
		http://www.springframework.org/schema/tx
		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd ">
</beans>
```
这样配置：
```xml
<!-- 商品管理的service -->
<bean id="itemsService" class="cn.itcast.ssm.service.impl.ItemsServiceImpl"/>
```

3. `applicationContext-transaction.xml`--事务管理
    ```xml
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    		http://www.springframework.org/schema/mvc
    		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
    		http://www.springframework.org/schema/context
    		http://www.springframework.org/schema/context/spring-context-3.2.xsd
    		http://www.springframework.org/schema/aop
    		http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
    		http://www.springframework.org/schema/tx
    		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd ">
    </beans>
    ```
这样配置：
    ```xml
    <!-- 1.事务管理器 -->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 2.通知 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="save*" propagation="REQUIRED"/>
        <tx:method name="insert*" propagation="REQUIRED"/>
        <tx:method name="update*" propagation="REQUIRED"/>
        <tx:method name="delete*" propagation="REQUIRED"/>
        <tx:method name="find*" propagation="SUPPORTS" read-only="true"/>
        <tx:method name="select*" propagation="SUPPORTS" read-only="true"/>
        <tx:method name="get*" propagation="SUPPORTS" read-only="true"/>
    </tx:attributes>
    </tx:advice>

    <!-- 3.aop(切点) -->
    <aop:config>
    <aop:advisor advice-ref="txAdvice"
                <!-- impl包下任意类任意方法，不管输入参数和返回值是什么 -->
                 pointcut="execution(* cn.itcast.ssm.service.impl.*.*(..))"/>
    </aop:config>
    ```

4. 前端控制器配置  
    `web.xml`
    ```xml
    <!-- 前端控制器 -->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 加载springmvc配置 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <!-- 配置文件的地址 如果不配置contextConfigLocation， 默认查找的配置文件名称classpath下的：servlet名称+"-serlvet.xml"即：springmvc-serlvet.xml -->
            <param-value>classpath:/spring/springmvc.xml</param-value>
        </init-param>

    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!-- 可以配置/，此工程所有请求全部由springmvc解析，此种方式可以实现RESTful方式，需要特殊处理对静态文件的解析不能由springmvc解析。
             可以配置*.do或*.action，所有请求的url扩展名为.do或.action由springmvc解析，此种方法常用。
             不可以/*，如果配置/*，返回jsp也由springmvc解析，这是不对的。 -->
        <url-pattern>*.action</url-pattern>
    </servlet-mapping>
    ```

5. `springmvc.xml`---springmvc的配置，配置处理器映射器、适配器、视图解析器
    ```xml
    <beans xmlns="http://www.springframework.org/schema/beans"
    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
    	xmlns:context="http://www.springframework.org/schema/context"
    	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
    	xsi:schemaLocation="http://www.springframework.org/schema/beans
    		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
    		http://www.springframework.org/schema/mvc
    		http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
    		http://www.springframework.org/schema/context
    		http://www.springframework.org/schema/context/spring-context-3.2.xsd
    		http://www.springframework.org/schema/aop
    		http://www.springframework.org/schema/aop/spring-aop-3.2.xsd
    		http://www.springframework.org/schema/tx
    		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd ">
    </beans>
    ```
这样配置：
    ```xml
    <!-- 通过annotation-driven可以替代下边的处理器映射器和适配器 -->
    <!-- <mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven> -->
    ```
    基本配置：
    ```xml
    <!-- 1.使用spring组件扫描 -->
    <context:component-scan base-package="cn.itcast.ssm.controller" />

    <!-- 2.注解处理器映射器 -->
    <bean
    	class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
    </bean>

    <!-- 3.注解适配器 -->
    <bean
    	class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    	<!-- 在webBindingInitializer中注入自定义属性编辑器、自定义转换器 -->
    	<property name="webBindingInitializer" ref="customBinder"></property>
    </bean>

    <!-- 4.配置视图解析器 要求将jstl的包加到classpath -->
    <bean
    	class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    	<property name="prefix" value="/WEB-INF/jsp/" />
    	<property name="suffix" value=".jsp" />
    </bean>
    ```
    增加的更多的配置：
    ```xml
    <!-- 自定义webBinder -->
    <bean id="customBinder"
    	class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer">
    	<!-- 使用converter进行参数转 -->
    	<property name="conversionService" ref="conversionService" />

    	<!-- propertyEditorRegistrars用于属性编辑器 -->
    	<!-- <property name="propertyEditorRegistrars">
    		<list>
    			<ref bean="customPropertyEditor" />
    		</list>
    	</property> -->
    </bean>

    <!-- 注册属性编辑器 -->
    <bean id="customPropertyEditor" class="cn.itcast.ssm.controller.propertyeditor.CustomPropertyEditor"></bean>

    <!-- 转换器 -->
    <bean id="conversionService"
    	class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    	<property name="converters">
    		<list>
    			<bean class="cn.itcast.ssm.controller.converter.CustomDateConverter"/>
    			<bean class="cn.itcast.ssm.controller.converter.StringTrimConverter"/>
    		</list>
    	</property>
    </bean>
    ```

5. `SqlMapConfig.xml`---mybatis的配置文件，配置别名、settings、mapper
    ```xml
    <!DOCTYPE configuration
    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
    	<!-- 定义 别名 -->
    	<typeAliases>
    		<!-- 批量别名定义 指定包路径，自动扫描包下边的pojo，定义别名，别名默认为类名（首字母小写或大写） -->
    		<package name="cn.itcast.ssm.po" />
    	</typeAliases>

    	<!--
    	由于使用spring和mybatis整合的mapper扫描器，这里可以不用配置了
    	<mappers>
    		<package name="cn.itcast.ssm.mapper" />
    	</mappers> -->
    </configuration>
    ```

# 七、商品列表开发
从底层即dao数据层开始写
### 1. mapper
功能描述：根据条件查询商品信息，返回商品列表  
一般情况下针对查询mapper需要自定义mapper。

首先针对单表进行逆向工程，生成代码。
1. mapper.xml  
    `ItemsMapperCustom.xml`
    ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="cn.itcast.ssm.mapper.ItemsMapperCustom">
    	<!-- 商品查询的sql片段
    	建议是以单表为单位定义查询条件 建议将常用的查询条件都写出来
    	 -->
    	<sql id="query_items_where">
    		<if test="itemsCustom!=null">
    			<if test="itemsCustom.name!=null and itemsCustom.name!=''">
    				and  name like '%${itemsCustom.name}%'
    			</if>
    			<if test="itemsCustom.id!=null">
    				and  id = #{itemsCustom.id}
    			</if>
    		</if>
    	</sql>

        <!-- 商品查询 -->
    	<select id="findItemsList" parameterType="cn.itcast.ssm.po.ItemsQueryVo"
    			resultType="cn.itcast.ssm.po.ItemsCustom">
    		SELECT * FROM items
    		<where>
    			<include refid="query_items_where"/>
    		</where>
    	</select>

    </mapper>
    ```

2. 包装类：  
    `ItemsCustom.java`
    ```java
    package cn.itcast.ssm.po;
    public class ItemsCustom extends Items{
    }
    ```
    `ItemsQueryVo.java`
    ```java
    package cn.itcast.ssm.po;
    public class ItemsQueryVo {
    	//商品信息
    	private ItemsCustom itemsCustom;

    	public ItemsCustom getItemsCustom() {
    		return itemsCustom;
    	}

    	public void setItemsCustom(ItemsCustom itemsCustom) {
    		this.itemsCustom = itemsCustom;
    	}
    }
    ```
3. mapper.java  
    `ItemsMapperCustom.java`
    ```java
    package cn.itcast.ssm.mapper;

    import java.util.List;

    import cn.itcast.ssm.po.ItemsCustom;
    import cn.itcast.ssm.po.ItemsQueryVo;

    // 商品自定义mapper
    public interface ItemsMapperCustom {
    	// 商品查询列表
    	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;    // 把包装类作参数
    }
    ```

### 2. service  
`ItemsService.java`
```java
package cn.itcast.ssm.service;

import java.util.List;

import cn.itcast.ssm.po.Items;
import cn.itcast.ssm.po.ItemsCustom;
import cn.itcast.ssm.po.ItemsQueryVo;

// 商品service接口
public interface ItemsService {
	// 商品查询列表
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;

	// 根据商品id查询商品信息
	public ItemsCustom findItemsById(int id) throws Exception;

	// 更新商品信息
    // 定义service接口，遵循单一职责，将业务参数细化 （不要使用包装类型，比如map）
	public void updateItems(Integer id, ItemsCustom itemsCustom) throws Exception;
}
```
`ItemsServiceImpl.java`
```java
package cn.itcast.ssm.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemsServiceImpl implements ItemsService {
	// 注入mapper
	@Autowired
	private ItemsMapperCustom itemsMapperCustom;

	@Autowired
	private ItemsMapper itemsMapper;

	// 商品查询列表
    // service接口，然后用serviceImpl实现接口
	@Override
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception {
		return itemsMapperCustom.findItemsList(itemsQueryVo);
	}

	@Override
	public ItemsCustom findItemsById(int id) throws Exception {

		Items items = itemsMapper.selectByPrimaryKey(id);
		//在这里随着需求的变量，需要查询商品的其它的相关信息，返回到controller

		ItemsCustom itemsCustom = new ItemsCustom();
		//将items的属性拷贝到itemsCustom
		BeanUtils.copyProperties(items, itemsCustom);

		return itemsCustom;
	}

	@Override
	public void updateItems(Integer id,ItemsCustom itemsCustom) throws Exception {
		// 写业务代码

		// 对于关键业务数据的非空校验
		if(id == null){
			// 抛出异常，提示调用接口的用户，id不能为空
			//...
		}
		// itemsMapper.updateByPrimaryKey(itemsCustom);
		itemsMapper.updateByPrimaryKeyWithBLOBs(itemsCustom);
	}
}
```

### 3. 在applicationContext-service.xml中配置service
```xml
<!-- 商品管理 的service -->
<bean id="itemsService" class="cn.itcast.ssm.service.impl.ItemsServiceImpl"/>
```

### 4. controller
```java
@Controller
//定义url的根路径，访问时根路径+方法的url
@RequestMapping("/items")
public class ItemsController {
	// 注入service
	@Autowired
	private ItemsService itemsService;

	@RequestMapping("/queryItems")
	public ModelAndView queryItems(HttpServletRequest request) throws Exception {
		System.out.println(request.getParameter("id"));

		//调用service查询商品列表
		List<ItemsCustom> itemsList = itemsService.findItemsList(null);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("itemsList", itemsList);
		// 指定逻辑视图名
		modelAndView.setViewName("itemsList");

		return modelAndView;
	}
}
```
### 5. jsp
`itemsList.jsp`
```js
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询商品列表</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/items/queryItem.action" method="post">
查询条件：
<table width="100%" border=1>
<tr>
<td><input type="submit" value="查询"/></td>
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
<c:forEach items="${itemsList }" var="item">
<tr>
	<td>${item.name }</td>
	<td>${item.price }</td>
	<td><fmt:formatDate value="${item.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	<td>${item.detail }</td>

	<td><a href="${ pageContext.request.contextPath }/items/editItems.action?id=${item.id}">修改</a></td>
</tr>
</c:forEach>

</table>
</form>
</body>

</html>
```
### 6. 在web.xml配置spring监听器
```xml
<!-- 配置spring容器监听器 -->
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/classes/spring/applicationContext-*.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

# 八、注解开发基础
功能描述：商品信息修改  
操作流程：  
1、在商品列表页面点击修改连接。  
2、打开商品修改页面，显示了当前商品的信息。  
	根据商品id查询商品信息  
3、修改商品信息，点击提交。  
	更新商品信息  

### 1. mapper和service
`ItemsMapper.java`
```java
public interface ItemsMapper {
    int countByExample(ItemsExample example);

    int deleteByExample(ItemsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Items record);

    int insertSelective(Items record);

    List<Items> selectByExampleWithBLOBs(ItemsExample example);

    List<Items> selectByExample(ItemsExample example);

    Items selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Items record, @Param("example") ItemsExample example);

    int updateByExampleWithBLOBs(@Param("record") Items record, @Param("example") ItemsExample example);

    int updateByExample(@Param("record") Items record, @Param("example") ItemsExample example);

    int updateByPrimaryKeySelective(Items record);

    int updateByPrimaryKeyWithBLOBs(Items record);

    int updateByPrimaryKey(Items record);
}
```
`ItemsService.java`
```java
public interface ItemsService {
    //根据商品id查询商品信息
    public ItemsCustom findItemsById(int id) throws Exception;
    //更新商品信息
    public void updateItems(Integer id,ItemsCustom itemsCustom)throws Exception;
}
```
### 2. `@RequestMapping`
1. 设置方法对应的url（完成url映射）  
一个`方法`对应一个url。  
`ItemsController.java`
```java
@RequestMapping("/queryItems")
public ModelAndView queryItems(HttpServletRequest request) throws Exception {}
```

2. 窄化请求映射  
在`class`上定义根路径。
```java
@Controller
//定义url的根路径，访问时根路径+方法的url
@RequestMapping("/items")
public class ItemsController {}
```
好处：更新规范系统 的url，避免 url冲突。

3. 限制http请求的方法  
通过`requestMapping`限制url请求的http方法，如果限制请求必须是post，如果get请求就抛出异常。商品修改方法，限制为http的get：
```java
//使用method=RequestMethod.GET限制使用get方法
@RequestMapping(value="/editItems",method={RequestMethod.GET})
// GET和POST都可以：@RequestMapping(value="/editItems",method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView editItems()throws Exception{
	ModelAndView modelAndView = new ModelAndView();

	//调用 service查询商品信息
	ItemsCustom itemsCustom = itemsService.findItemsById(1);
	//将模型数据传到jsp
	modelAndView.addObject("item", itemsCustom);
	//指定逻辑视图名
	modelAndView.setViewName("editItem");

	return modelAndView;
}
```

### 3. controller方法返回值
1. 返回ModelAndView  
```java
@RequestMapping("/queryItems")
public ModelAndView queryItems(HttpServletRequest request) throws Exception {
    System.out.println(request.getParameter("id"));

    //调用service查询商品列表
    List<ItemsCustom> itemsList = itemsService.findItemsList(null);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("itemsList", itemsList);
    // 指定逻辑视图名
    modelAndView.setViewName("itemsList");

    return modelAndView;
}
```
2. 返回字符串  
如果controller方法返回jsp页面，可以简单将方法返回值类型定义为字符串，最终返回逻辑视图名。
```java
//方法返回字符串，字符串就是逻辑视图名，Model作用是将数据填充到request域，在页面展示
@RequestMapping(value="/editItems",method={RequestMethod.GET})
public String editItems(Model model,Integer id)throws Exception{
    //调用 service查询商品信息
    ItemsCustom itemsCustom = itemsService.findItemsById(id);

    model.addAttribute("item", itemsCustom);

    return "editItem";
}
```

3. 返回void  
```java
@RequestMapping(value="/editItems",method={RequestMethod.GET})
public void editItems(HttpServletRequest request, HttpServletResponse response, Integer id) throws Exception {
	//调用service查询商品信息
	ItemsCustom itemsCustom = itemsService.findItemsById(id);
	request.setAttribute("item", itemsCustom);
	//注意如果使用request转向页面，这里指定页面的完整路径
	request.getRequestDispatcher("/WEB-INF/jsp/editItem.jsp").forward(request, response);
}
```
使用此方法，容易输出json、xml格式的数据：
通过response指定响应结果，例如响应json数据如下：  
`response.setCharacterEncoding("utf-8");`  
`response.setContentType("application/json;charset=utf-8");`  
`response.getWriter().write("json串");`  

4. redirect重定向  
如果方法重定向到另一个url，方法返回值为`redirect:url路径`  
使用redirect进行重定向，request数据无法共享，url地址栏会发生变化的。

5. forward转发  
使用forward进行请求转发，request数据可以共享，url地址栏不会。方法返回值为`forward:url路径`
```java
//商品修改提交
@RequestMapping("/editItemSubmit")
public String editItemSubmit() throws Exception{
    // 1.请求重定向
    return "redirect:queryItems.action";
    // 2.转发
    return "forward:queryItems.action";
}
```  
`edititem.jsp`
```js
<form id="itemForm" action="${ pageContext.request.contextPath }/items/editItemSubmit.action" method="post" >
```

### 4. 参数绑定
#### 1.默认支持的参数类型
处理器形参中添加如下类型的参数处理适配器会默认识别并进行赋值。  
`HttpServletRequest`：通过request对象获取请求信息  
`HttpServletResponse`：通过response处理响应信息  
`HttpSession`：通过session对象得到session中存放的对象  
`Model`：通过model向页面传递数据，如下：
```java
//调用service查询商品信息
Items item = itemService.findItemById(id);
model.addAttribute("item", item);
```
页面通过`${item.XXXX}`获取item对象的属性值。
1. `@RequestParam`  
如果request请求的参数名和controller方法的形参数名称一致，适配器自动进行参数绑定。如果不一致可以通过`@RequestParam`指定request请求的参数名绑定到哪个方法形参上。  
对于必须要传的参数，通过`@RequestParam`中属性`required`设置为`true`，如果不传此参数则报错。  
对于有些参数如果不传入，还需要设置默认值，使用`@RequestParam`中属性`defaultvalue`设置默认值。  
    ```java
    @RequestMapping(value="/editItems",method={RequestMethod.GET})
    public void editItems(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "item_id", required = false, defaultValue = "1")) throws Exception {
    	//调用service查询商品信息
    	ItemsCustom itemsCustom = itemsService.findItemsById(id);
    	request.setAttribute("item", itemsCustom);
    	//注意如果使用request转向页面，这里指定页面的完整路径
    	request.getRequestDispatcher("/WEB-INF/jsp/editItem.jsp").forward(request, response);
    }
    ```

2. 可以绑定简单类型  
可以绑定整型、字符串、单精/双精度、日期、布尔型。

3. 可以绑定简单pojo类型  
简单pojo类型只包括简单类型的属性。  
绑定过程：request请求的参数名称和pojo的属性名一致，就可以绑定成功。
```java
//商品修改提交
@RequestMapping("/editItemSubmit")
public String editItemSubmit(Integer id,ItemsCustom itemsCustom)throws Exception{
    //调用service接口更新商品信息
    itemsService.updateItems(id, itemsCustom);
    //请求重定向
    return "redirect:queryItems.action";
}
```
问题：如果controller方法形参中有多个pojo且pojo中有重复的属性，使用简单pojo绑定无法有针对性的绑定。  
比如：方法形参有items和User，pojo同时存在name属性，从http请求过程的name无法有针对性的绑定到items或user。

4. 可以绑定包装的pojo  
包装的pojo里边包括了pojo。
```java
@RequestMapping("/editItemSubmit")
public String editItemSubmit(Integer id,ItemsCustom itemsCustom, ItemsQueryVo itemsQueryVo)throws Exception{
    //调用service接口更新商品信息
    itemsService.updateItems(id, itemsCustom);
    //请求重定向
    return "redirect:queryItems.action";
}
```
页面参数定义：
```js
<tr>
	<td>商品名称</td>
	<td><input type="text" name="itemsCustom.name" value="${item.name }"/></td>
</tr>
<tr>
	<td>商品价格</td>
	<td><input type="text" name="itemsCustom.price" value="${item.price }"/></td>
</tr>
<tr>
	<td>商品简介</td>
	<td>
	<textarea rows="3" cols="30" name="itemsCustom.detail">${item.detail }</textarea>
	</td>
</tr>
```
包装类型的属性也是itemsCustom：  
`ItemsQueryVo.java`
```java
public class ItemsQueryVo {
	// 商品信息
	private ItemsCustom itemsCustom;
    // getter setter方法省略......
}
```
按照上边的规则进行包装类型的绑定。

5. 自定义绑定使用属性编辑器  
springmvc没有提供默认的对日期类型的绑定，需要自定义日期类型的绑定。
    * 使用`WebDataBinder`（了解）  
    在controller类中定义：  
    `ItemsController.java`
    ```java
    // 自定义属性编辑器
    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        // Date.class必须是与controler方法形参pojo属性一致的date类型，这里是java.util.Date
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"), true));
    }
    ```
    使用这种方法问题是无法在多个controller共用。

    * 使用`WebBindingInitializer`（了解）  
    使用WebBindingInitializer让多个controller共用 属性编辑器。    
    自定义WebBindingInitializer，注入到处理器适配器中。  
    如果想多个controller需要共同注册相同的属性编辑器，可以实现PropertyEditorRegistrar接口，并注入webBindingInitializer中。

    如下：编写`CustomPropertyEditor.java`：  
    ```java
    package cn.itcast.ssm.controller.propertyeditor;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    import org.springframework.beans.PropertyEditorRegistrar;
    import org.springframework.beans.PropertyEditorRegistry;
    import org.springframework.beans.propertyeditors.CustomDateEditor;

    public class CustomPropertyEditor implements PropertyEditorRegistrar {
    	@Override
    	public void registerCustomEditors(PropertyEditorRegistry binder) {
    		binder.registerCustomEditor(Date.class, new CustomDateEditor(
    				new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"), true));

    	}
    }
    ```
    配置如下：  
    `springmvc.xml`
    ```xml
    <!-- 注册属性编辑器 -->
    	<bean id="customPropertyEditor" class="cn.itcast.ssm.propertyeditor.CustomPropertyEditor"></bean>
    <!-- 自定义webBinder -->
    	<bean id="customBinder"
    		class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer">
    		<property name="propertyEditorRegistrars">
    			<list>
    				<ref bean="customPropertyEditor"/>
    			</list>
    		</property>
    	</bean>

    <!--注解适配器 -->
    	<bean
    		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    		 <property name="webBindingInitializer" ref="customBinder"></property>
    	</bean>
    ```

6. 自定义参数绑定使用转换器(架构师掌握)
    1. 实现Converter接口：  
    定义日期类型转换器和字符串去除前后空格转换器。  
    `CustomDateConverter.java`
    ```java
    package cn.itcast.ssm.controller.converter;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    import org.springframework.core.convert.converter.Converter;

     // 自定义日期转换器
    public class CustomDateConverter implements Converter<String, Date> {
    	@Override
    	public Date convert(String source) {
    		try {
    			//进行日期转换
    			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return null;
    	}
    }
    ```
    `StringTrimConverter.java`
    ```java
    package cn.itcast.ssm.controller.converter;

    import java.text.SimpleDateFormat;
    import java.util.Date;

    import org.springframework.core.convert.converter.Converter;

    public class StringTrimConverter implements Converter<String, String> {
    	@Override
    	public String convert(String source) {
    		try {
    			//去掉字符串两边空格，如果去除后为空设置为null
    			if(source!=null){
    				source = source.trim();
    				if(source.equals("")){
    					return null;
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return source;
    	}
    }
    ```
    2. 配置转换器  
    配置方式1针对不使用`<mvc:annotation-driven>`  
    `springmvc.xml`
    ```xml
    <!--注解适配器 -->
	<bean
		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
		 <property name="webBindingInitializer" ref="customBinder"></property>
	</bean>

	<!-- 自定义webBinder -->
	<bean id="customBinder"
		class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer">
		<property name="conversionService" ref="conversionService" />
	</bean>
	<!-- conversionService -->
	<bean id="conversionService"
		class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
		<!-- 转换器 -->
		<property name="converters">
			<list>
				<bean class="cn.itcast.ssm.controller.converter.CustomDateConverter"/>
			</list>
		</property>
	</bean>
    ```
    配置方式2针对使用`<mvc:annotation-driven>`的配置  
    `springmvc.xml`
    ```xml
    <mvc:annotation-driven conversion-service="conversionService">
    </mvc:annotation-driven>
    <!-- conversionService -->
    	<bean id="conversionService"
    		class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    		<!-- 转换器 -->
    		<property name="converters">
    			<list>
    				<bean class="cn.itcast.ssm.controller.converter.CustomDateConverter"/>
    			</list>
    		</property>
    	</bean>
    ```

### 5.问题处理
1. post乱码  
    在`web.xml`中加入：
    ```xml
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ```

2. get乱码  
    对于get请求中文参数出现乱码解决方法有两个：  
    1. 修改tomcat配置文件添加编码与工程编码一致     
    `<Connector URIEncoding="utf-8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>`

    2. 对参数进行重新编码：
    ```java
    String userName new
    String(request.getParamter("userName").getBytes("ISO8859-1"),"utf-8")
    ```
    ISO8859-1是tomcat默认编码，需要将tomcat编码后的内容按utf-8编码
