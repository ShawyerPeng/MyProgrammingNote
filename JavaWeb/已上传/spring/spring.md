## 1. IOC入门案例
1. 导入jar包
2. 创建类，在类里面创建方法`User.java`  
    ```java
    package cn.itcast.ioc;

    // 创建类，在类里面创建方法
    public class User {
    	private String username;

    	public User(String username) {
    		this.username = username;
    	}

    	public User() {    
            // 无参构造函数
    	}

    	public void add() {
    		System.out.println("add..........");
    	}

    	public static void main(String[] args) {
    		//原始做法
    //		User user = new User();
    //		user.add();
    	}
    }
    ```

3. 创建spring配置文件`applicationContext.xml`，配置创建类

    ```java
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:p="http://www.springframework.org/schema/p"
        xsi:schemaLocation="
            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    	<bean id="user" class="cn.itcast.ioc.User"></bean>

    </beans>
    ```

4. 写代码测试对象创建

    ```java
    package cn.itcast.ioc;

    import org.junit.Test;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class TestIOC {

    	@Test
    	public void testUser() {
    		//1 加载spring配置文件，根据创建对象
    		ApplicationContext context =
    				new ClassPathXmlApplicationContext("bean1.xml");
    		//2 得到配置创建的对象
    		UserService userService = (UserService) context.getBean("userService");
    		userService.add();
    	}
    }
    ```

## 2. bean管理
bean实例化的三种方式：使用类的无参构造创建（重点）、使用静态工厂创建、使用实例工厂创建

```java
public User() {    
    // 无参构造函数
}
```

## 3. Bean标签常用属性
1. id属性
2. class属性
3. name属性
4. scope属性  -singleton：默认值，单例   -prototype：多例   -request   -session   -globalSession

## 4. 属性注入
创建对象时候，向类里面属性里面设置值  
1. 使用set方法注入  

    ```java
    package cn.itcast.property;

    public class Book {

    	private String bookname;
    	//set方法
    	public void setBookname(String bookname) {
    		this.bookname = bookname;
    	}

    	public void demobook() {
    		System.out.println("book..........."+bookname);
    	}
    }

    ```
    ```
        <!-- 使用set方法注入属性 -->
        <!-- <bean id="book" class="cn.itcast.property.Book"> -->
            <!-- 注入属性值
                name属性值：类里面定义的属性名称
                value属性：设置具体的值
            -->
            <!-- <property name="bookname" value="易筋经"></property>
        </bean> -->
    ```

    ```java
    package cn.itcast.property;

    import org.junit.Test;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class TestIOC {

    	@Test
    	public void testUser() {
    		//1 加载spring配置文件，根据创建对象
    		ApplicationContext context =
    				new ClassPathXmlApplicationContext("bean1.xml");
    		//2 得到配置创建的对象
    		Book book = (Book) context.getBean("book");
    		person.demobook();
    	}
    }
    ```

2. 使用有参数构造注入  

    ```java
    package cn.itcast.property;

    public class PropertyDemo1 {

    	private String username;

    	public PropertyDemo1(String username) {
    		this.username = username;
    	}

    	public void test1() {
    		System.out.println("demo1.........."+username);
    	}

    }
    ```
    ```
        <!-- 使用有参数构造注入属性 -->
        <!-- <bean id="demo" class="cn.itcast.property.PropertyDemo1"> -->
            <!-- 使用有参构造注入 -->
            <!-- <constructor-arg name="username" value="小王小马"></constructor-arg>
        </bean> -->
    ```
    ```java
    package cn.itcast.property;

    import org.junit.Test;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.support.ClassPathXmlApplicationContext;

    public class TestIOC {

    	@Test
    	public void testUser() {
    		//1 加载spring配置文件，根据创建对象
    		ApplicationContext context =
    				new ClassPathXmlApplicationContext("bean1.xml");
    		//2 得到配置创建的对象
    		PropertyDemo1 demo1 = (PropertyDemo1) context.getBean("demo");
    		demo1.test1();
    	}
    }
    ```


3. 使用接口注入  


## 注入对象类型属性

```java
package cn.itcast.ioc;

public class UserDao {

	public void add() {
		System.out.println("dao.........");
	}
}
```

```
<!-- 注入对象类型属性 -->
<!-- 1 配置service和dao对象 -->
<!-- <bean id="userDao" class="cn.itcast.ioc.UserDao"></bean> -->

<!-- <bean id="userService" class="cn.itcast.ioc.UserService"> -->
    <!-- 注入dao对象
        name属性值：service类里面属性名称
                                               现在不要写value属性，因为刚才是字符串，现在是对象
        写ref属性：dao配置bean标签中id值
    -->
    <!-- <property name="userDao" ref="userDao"></property>
</bean> -->
```

```java
package cn.itcast.ioc;

public class UserService {

	//1 定义dao类型属性
	private UserDao userDao;
	//2 生成set方法
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void add() {
		System.out.println("service.........");
		//在service里面得到dao类对象，才能调用dao里面的方法
//		UserDao dao = new UserDao();
//		dao.add();
		userDao.add();
	}
}
```

## 注入复杂类型属性

## IOC和DI区别
1. IOC：控制反转，把对象创建交给spring进行配置
2. DI：依赖注入，向类里面的属性中设置值
3. 关系：依赖注入不能单独存在，需要在IOC基础上完成操作
