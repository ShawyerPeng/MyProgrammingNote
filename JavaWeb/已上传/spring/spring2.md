# 一、基于aspectj的注解aop操作
### 1. 使用注解方式实现aop操作
1. 创建对象  
`Book.java`
```java
package cn.itcast.aop;
public class Book {
	public void add() {
		System.out.println("add.............");
	}
}
```

2. 在spring核心配置文件中，开启aop操作  
`bean.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:aop="http://www.springframework.org/schema/aop" xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

	<!-- 开启aop操作 -->
	<aop:aspectj-autoproxy></aop:aspectj-autoproxy>

	<!-- 1  配置对象 -->
	<bean id="book" class="cn.itcast.aop.Book"></bean>
	<bean id="myBook" class="cn.itcast.aop.MyBook"></bean>
</beans>
```

3. 在增强类上面使用注解完成aop操作  
`MyBook.java`
```java
package cn.itcast.aop;
@Aspect
public class MyBook {
	// 在方法上面使用注解完成增强配置
	@Before(value="execution(* cn.itcast.aop.Book.*(..))")
	public void before1() {
		System.out.println("before..............");
	}
}
```
`TestAop.java`
```java
package cn.itcast.aop;
public class TestAop {
	@Test
	public void testDemo() {
		ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
		Book book = (Book) context.getBean("book");
		book.add();
	}
}
```
# 二、spring的jdbcTemplate操作
### 1. spring框架一站式框架
（1）针对javaee三层，每一层都有解决技术
（2）在dao层，使用 jdbcTemplate

spring对不同的持久化层技术都进行封装：
![](http://p1.bpimg.com/567571/4021824f43fab3a1.png)  

jdbcTemplate对jdbc进行封装，它的使用和dbutils使用很相似，都数据库进行crud操作。

# 三、spring配置连接池
### 1. spring配置c3p0连接池
1. 导入jar包
2. 创建spring配置文件，配置连接池
代码实现：  
```java
import com.mchange.v2.c3p0.ComboPooledDataSource;
public class JdbcTemplateDemo1 {
    ComboPooledDataSource dataSource = new ComboPooledDataSource();
    dataSource.setDriverClass("com.mysql.jdbc.Driver");
    dataSource.setJdbcUrl("jdbc:mysql:///spring_day03");
    dataSource.setUser("root");
    dataSource.setPassword("123");
}
```
配置实现：   
`bean.xml`
```xml
<!-- 配置c3p0连接池 -->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <!-- 注入属性值 -->
    <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
    <property name="jdbcUrl" value="jdbc:mysql:///spring_day03"></property>
    <property name="user" value="root"></property>
    <property name="password" value="root"></property>
</bean>
```

### 2. dao使用jdbcTemplate
1. 创建service和dao，配置service和dao对象，在service注入dao对象  
`bean.xml`
```xml
<!-- 创建service和dao对象，在service注入dao对象 -->
<bean id="userService" class="cn.itcast.c3p0.UserService">
    <!-- 注入dao对象 -->
    <property name="userDao" ref="userDao"></property>
</bean>

<bean id="userDao" class="cn.itcast.c3p0.UserDao">
        <!-- 注入jdbcTemplate对象 -->
        <property name="jdbcTemplate" ref="jdbcTemplate"></property>
</bean>
```

2. 创建jdbcTemplate对象，把模板对象注入到dao里面  
`UserDao.java`
```java
public class UserDao {
	//得到JdbcTemplate对象
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//添加操作
	public void add() {
        // 代码实现：创建jdbcTemplate对象
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "insert into user values(?,?)";
		jdbcTemplate.update(sql, "李雷","520");
	}
}
```

3. 在jdbcTemplate对象里面注入dataSource
`bean.xml`
```xml
<!-- 创建jdbcTemplate对象 -->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <!-- 把dataSource传递到模板对象里面 -->
    <property name="dataSource" ref="dataSource"></property>
</bean>
```

# 四、spring事务管理  
### 1. spring事务管理两种方式  
1. 编程式事务管理（不用）  
2. 声明式事务管理  
    （1）基于xml配置文件实现  
    （2）基于注解实现（最简单方便）  
### 2. spring事务管理的api介绍  
spring针对不同的dao层框架，提供接口不同的实现类  
![](http://i1.piimg.com/567571/c279bd45d32074be.png)
### 3. 声明式事务管理（xml配置）  
1. 创建service和dao类，完成注入关系
`OrdersDao.java`
```java
public class OrdersDao {
	//注入jdbcTemplate
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 做对数据库操作的方法，不写业务操作
	// 小王少钱的方法
	public void lessMoney() {
		String sql = "update account set salary=salary-? where username=?";
		jdbcTemplate.update(sql, 1000,"小王");
	}
	// 小马多钱的方法
	public void moreMoney() {
		String sql = "update account set salary=salary+? where username=?";
		jdbcTemplate.update(sql, 1000,"小马");
	}
}
```
`OrdersService.java`
```java
public class OrdersService {
	private OrdersDao ordersDao;
	public void setOrdersDao(OrdersDao ordersDao) {
		this.ordersDao = ordersDao;
	}

	//调用dao的方法
	//业务逻辑层，写转账业务
	public void accountMoney() {
		//小王少1000
		ordersDao.lessMoney();
		//出现异常，应该进行回滚操作
		int i = 10/0;
		//小马多1000
		ordersDao.moreMoney();
	}
}
```
`bean.xml`
```xml
<!-- 配置c3p0连接池 -->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <!-- 注入属性值 -->
    <property name="driverClass" value="com.mysql.jdbc.Driver"></property>
    <property name="jdbcUrl" value="jdbc:mysql:///spring_day03"></property>
    <property name="user" value="root"></property>
    <property name="password" value="root"></property>
</bean>

<bean id="ordersService" class="cn.itcast.service.OrdersService">
    <property name="ordersDao" ref="ordersDao"></property>
</bean>
<bean id="ordersDao" class="cn.itcast.dao.OrdersDao">
    <property name="jdbcTemplate" ref="jdbcTemplate"></property>
</bean>
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"></property>
</bean>
```

2. 配置事务管理器
```xml
<!-- 第一步 配置事务管理器 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!-- 注入dataSource，知道对哪个数据库进行操作 -->
    <property name="dataSource" ref="dataSource"></property>
</bean>
```

3. 配置事务增强
```xml
<!-- 第二步 配置事务增强 -->
<tx:advice id="txadvice" transaction-manager="transactionManager"> <!-- transaction-manager指定事务管理器id -->
    <!-- 做事务操作 -->
    <tx:attributes>
        <!-- 设置进行事务操作的方法匹配规则  -->
        <tx:method name="account*" propagation="REQUIRED"/> <!-- 如果方法都以account开头则都进行操作 -->
    </tx:attributes>
</tx:advice>
```

4. 配置切面
```xml
<!-- 第三步 配置切面 -->
<aop:config>
    <!-- 切入点 -->
    <aop:pointcut expression="execution(* cn.itcast.service.OrdersService.*(..))" id="pointcut1"/>
    <!-- 切面 -->
    <aop:advisor advice-ref="txadvice" pointcut-ref="pointcut1"/> <!-- 指定哪个增强用在哪个切入点上 -->
</aop:config>
```

### 总结
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd
	http://www.springframework.org/schema/aop
	http://www.springframework.org/schema/aop/spring-aop.xsd
	http://www.springframework.org/schema/tx
	http://www.springframework.org/schema/tx/spring-tx.xsd">

	<!-- 配置c3p0连接池 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<!-- 注入属性值 -->
		<property name="driverClass" value="com.mysql.jdbc.Driver"></property>
		<property name="jdbcUrl" value="jdbc:mysql:///spring_day03"></property>
		<property name="user" value="root"></property>
		<property name="password" value="root"></property>
	</bean>

	<!-- 第一步 配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<!-- 注入dataSource -->
		<property name="dataSource" ref="dataSource"></property>
	</bean>

	<!-- 第二步 配置事务增强 -->
	<tx:advice id="txadvice" transaction-manager="transactionManager">
		<!-- 做事务操作 -->
		<tx:attributes>
			<!-- 设置进行事务操作的方法匹配规则  -->
			<tx:method name="account*" propagation="REQUIRED"/>
			<!-- <tx:method name="insert*" /> -->
		</tx:attributes>
	</tx:advice>

	<!-- 第三步 配置切面 -->
	<aop:config>
		<!-- 切入点 -->
		<aop:pointcut expression="execution(* cn.itcast.service.OrdersService.*(..))" id="pointcut1"/>
		<!-- 切面 -->
		<aop:advisor advice-ref="txadvice" pointcut-ref="pointcut1"/>
	</aop:config>

	<bean id="ordersService" class="cn.itcast.service.OrdersService">
		<property name="ordersDao" ref="ordersDao"></property>
	</bean>
	<bean id="ordersDao" class="cn.itcast.dao.OrdersDao">
		<property name="jdbcTemplate" ref="jdbcTemplate"></property>
	</bean>
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
</beans>
```

### 4. 声明式事务管理（注解）
1. 配置事务管理器
```xml
<!-- 第一步配置事务管理器 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"></property>
</bean>
```

2. 配置事务注解
```xml
<!-- 第二步 开启事务注解 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

3. 在要使用事务的方法所在类上面添加注解
`OrdersService.java`
```java
@Transactional
public class OrdersService {
	private OrdersDao ordersDao;

	public void setOrdersDao(OrdersDao ordersDao) {
		this.ordersDao = ordersDao;
	}

	//调用dao的方法
	//业务逻辑层，写转账业务
	public void accountMoney() {
		//小王少1000
		ordersDao.lessMoney();
		//出现异常
		int i = 10/0;
		//小马多1000
		ordersDao.moreMoney();
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd
	http://www.springframework.org/schema/aop
	http://www.springframework.org/schema/aop/spring-aop.xsd
	http://www.springframework.org/schema/tx
	http://www.springframework.org/schema/tx/spring-tx.xsd">

	<!-- 配置c3p0连接池 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<!-- 注入属性值 -->
		<property name="driverClass" value="com.mysql.jdbc.Driver"></property>
		<property name="jdbcUrl" value="jdbc:mysql:///spring_day03"></property>
		<property name="user" value="root"></property>
		<property name="password" value="root"></property>
	</bean>

	<!-- 第一步配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"></property>
	</bean>

	<!-- 第二步 开启事务注解 -->
	<tx:annotation-driven transaction-manager="transactionManager"/>


	<bean id="ordersService" class="cn.itcast.service.OrdersService">
		<property name="ordersDao" ref="ordersDao"></property>
	</bean>
	<bean id="ordersDao" class="cn.itcast.dao.OrdersDao">
		<property name="jdbcTemplate" ref="jdbcTemplate"></property>
	</bean>
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
</beans>
```
