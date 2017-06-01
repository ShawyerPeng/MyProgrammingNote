# 1. `db.properties`和`log4j.properties`
这里是对数据库的参数进行一些配置，为了防止硬编码不利于系统的优化。（在与springmvc整合后，由spring管理）
# 2. `SqlMapConfig.xml`配置
只需定义别名
```xml
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

	<!-- 定义别名。配置批量扫描po包 -->
	<typeAliases>
		<!-- 批量别名定义。指定包路径，mybatis自动扫描包下边的pojo类，自动定义别名，别名默认为类名（首字母小写或大写） -->
		<package name="po" />
	</typeAliases>

</configuration>
```

### 解释
```xml
<typeAliases>
	<!--  <typeAlias type="zq.mybatis.test1.User" alias="_User"/> -->    
	<package name="zq.mybatis.test1"/>
</typeAliases>
```
使用package设置别名的手该如何运用这个别名呢？很简单，我指定了他的包名，那这个包下面的所有实体相当于已经被设置了别名，而这个别名实际上就是某一个实体自己的实体名。
在对应的映射文件中将使用别名：
```xml
<update id="updateUser" parameterType="User">   <!-- parameterType="User"就是使用了别名User -->
	update users set name=#{name},age=#{age} where id=#{id}
</update>
<select id="getAllUser" resultType="User">      <!-- resultType="User"就是使用了别名User -->
    select * from users
</select>
```

# 3. `applicationContext.xml`  
配置`数据源`、`SqlsessionFactory`、`MapperScannerConfigurer`(mapper扫描器)
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

<!-------------------------------1.dao层------------------------------->
	<!-- 1.配置数据源 -->
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

	<!-- 2.SqlsessionFactory -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 数据源 -->
		<property name="dataSource" ref="dataSource"/>
		<!-- mybatis配置文件 -->
		<property name="configLocation" value="classpath:mybatis/SqlMapConfig.xml"/>
	</bean>

	<!--3.MapperScannerConfigurer：mapper的扫描器，将包下边的mapper接口自动创建代理对象，
	自动创建到spring容器中，bean的id是mapper的类名（首字母小写） -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="mapper"/>
		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
	</bean>

<!-------------------------------2.service层------------------------------->
	<!--配置service的实现class-->
	<!-- 商品管理的service -->
	<bean id="itemsService" class="service.impl.ItemsServiceImpl"/>

<!-------------------------------3.transaction层------------------------------->
	 <!-- 事务管理器 -->
	 <bean id="transactionManager" 
	 		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	 	<property name="dataSource" ref="dataSource"/>
	 </bean>
	 
	 <!-- 通知 -->
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
	 
	 <!-- aop -->
	 <aop:config>
	 	<aop:advisor advice-ref="txAdvice" pointcut="execution(* service.impl.*.*(..))"/>
	 </aop:config>

</beans>
```

# 4. 使用myBatis-generator工具产生对应的`mapper.java`接口和`mapper.xml`实现  
分别粘贴到`cn.itcast.ssm.po`和`cn.itcast.ssm.mapper`包下

# 5. Junit测试  
`ItemsMapperTest.java`
```java
public class ItemsMapperTest {
	private ApplicationContext applicationContext;
	private ItemsMapper itemsMapper;

	@Before
	public void setUp() throws Exception {
		// 创建spring容器
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		itemsMapper = (ItemsMapper) applicationContext.getBean("itemsMapper");
	}

	// 根据主键查询
	@Test
	public void testSelectByPrimaryKey() {
		Items items = itemsMapper.selectByPrimaryKey(1);
		System.out.println(items);
	}
}
```

# 6. po和mapper重构  
继续在po下增加`ItemsCustom.java`、`ItemsQueryVo.java`
在mapper下增加`ItemsMapperCustom.java`、`ItemsMapperCustom.xml`

# 7. 完成service模块  
`UserService.java`
```java
a
```
`UserServiceImpl.java`
```java
b
```

# 8. 完成controller模块  
`ItemsController.java`

# 9. 配置`springmvc.xml`  
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

	<!-- 1.使用spring组件扫描 --><!-- 开启注解扫描功能 -->  
	<context:component-scan base-package="controller" />

	<!-- 2.通过annotation-driven可以替代下边的处理器映射器和适配器 -->
    <mvc:annotation-driven conversion-service="conversionService">
    </mvc:annotation-driven>

	 <!--*注解处理器映射器 -->
    <bean
		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
    </bean>
    <!-- *注解适配器 -->
    <bean
		class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    </bean>


	<!-- 3.配置视图解析器ViewResolver。要求将jstl的包加到classpath -->
	<!-- prefix:代表请求url的前缀 suffix:代表请求url的后缀
    设置了这两个属性值后我们在Controller中进行代码开发时返回的modelandview对象设置的页面路径值就不用带前缀名和后缀名了-->
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/jsp/" />
		<property name="suffix" value=".jsp" />
	</bean>

</beans>

```

# 10. 配置`web.xml`  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

<!-- 配置spring容器监听器 -->
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/classes/spring/applicationContext.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<!-- 前端控制器DispatcherServlet -->
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 加载springmvc配置 -->
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <!-- 配置文件的地址 如果不配置contextConfigLocation，默认查找的配置文件名称classpath下的：servlet名称+"-serlvet.xml"即：springmvc-serlvet.xml -->
        <param-value>classpath:spring/springmvc.xml</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>*.action</url-pattern>
</servlet-mapping>

<welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>

</web-app>
```

# 11. 写jsp页面