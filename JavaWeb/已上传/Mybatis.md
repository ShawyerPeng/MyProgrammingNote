# 一、入门实例+配置文件
#### 1. log4j.properties(公用文件)
```
# Global logging configuration，建议开发环境中要用debug
log4j.rootLogger=DEBUG, stdout
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```
#### 2. SqlMapConfig.xml(公用文件)
通过`SqlMapConfig.xml`加载mybatis运行环境。
```xml
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 和spring整合后 environments配置将废除-->
    <environments default="development">
        <environment id="development">
        <!-- 使用jdbc事务管理-->
            <transactionManager type="JDBC" />
        <!-- 数据库连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <!-- 加载mapper.xml  -->
    <mappers>
        <mapper resource="sqlmap/User.xml" />
    </mappers>

</configuration>
```
#### 3. pojo（User.java）
```java
package cn.itcast.mybatis.po;

import java.util.Date;

public class User {
	private int id;
	private String username;// 用户姓名
	private String sex;// 性别
	private Date birthday;// 生日
	private String address;// 地址

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    // ......继续写getter setter方法，限于篇幅省略
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", sex=" + sex
				+ ", birthday=" + birthday + ", address=" + address + "]";
	}
}
```
#### 4. User.xml
建议命名规则：表名+mapper.xml
早期ibatis命名规则：表名.xml  
`User.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace命名空间，为了对sql语句进行隔离，方便管理 ，mapper开发dao方式，使用namespace有特殊作用
mapper代理开发时将namespace指定为mapper接口的全限定名 -->

<mapper namespace="test">
<!-- 在mapper.xml文件中配置很多的sql语句，执行每个sql语句时，封装为MappedStatement对象
mapper.xml以statement为单位管理sql语句 -->
    <!-- 根据id查询用户信息 -->
    <!--
        id：唯一标识 一个statement
        #{}：表示 一个占位符，如果#{}中传入简单类型的参数，#{}中的名称随意
        parameterType：输入 参数的类型，通过#{}接收parameterType输入 的参数
        resultType：输出结果 类型，不管返回是多条还是单条，指定单条记录映射的pojo类型
     -->
    <select id="findUserById" parameterType="int" resultType="cn.itcast.mybatis.po.User">
        SELECT * FROM USER WHERE id= #{id}
    </select>
```

#### 5.编码
创建SqlSessionFactory:
`MybatisFirst.java`
```java
package cn.itcast.mybatis.first;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.mybatis.po.User;

public class MybatisFirst {
	// 会话工厂
	private SqlSessionFactory sqlSessionFactory;
	// 创建工厂
	@Before
	public void init() throws IOException {
		// 配置文件（SqlMapConfig.xml）
		String resource = "SqlMapConfig.xml";
		// 加载配置文件到输入流
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 创建会话工厂
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

    // 测试根据id查询用户(得到单条记录)
    @Test
    public void testFindUserById() {
        // 通过sqlSessionFactory创建sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 通过sqlSession操作数据库
        // 第一个参数：statement的位置，等于namespace+statement的id
        // 第二个参数：传入的参数
        User user = null;
        try {
            user = sqlSession.selectOne("test.findUserById", 2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭sqlSession
            sqlSession.close();
        }
        System.out.println(user);
    }
}
```

# 二、mybatis开发dao的方式
### SqlSession作用范围
    * `SqlSessionFactoryBuilder`：SqlSessionFactoryBuilder是以工具类方式来使用，需要创建sqlSessionFactory就new一个SqlSessionFactoryBuilder。
    * `sqlSessionFactory`：正常开发时，以单例方式管理sqlSessionFactory，整个系统运行过程中sqlSessionFactory只有一个实例，将来和spring整合后由spring以单例方式管理sqlSessionFactory。
    * `SqlSession`：sqlSession是一个面向用户（程序员）的接口，程序员调用sqlSession的接口方法进行操作数据库。
        sqlSession能否以单例 方式使用？？  
        由于sqlSession是线程不安全，所以sqlSession最佳应用范围在方法体内，在方法体内定义局部变量使用sqlSession。
### 原始dao开发方式  
1. dao接口：
`UserDao.java`
    ```java
    public interface UserDao {
    	//根据id查询用户信息
    	public User findUserById(int id) throws Exception;
    }
    ```
2. dao接口实现类：
`UserDaoImpl.java`
    ```java
    package cn.itcast.mybatis.dao;

    import java.util.List;

    import org.apache.ibatis.session.SqlSession;
    import org.apache.ibatis.session.SqlSessionFactory;

    import cn.itcast.mybatis.po.User;

    public class UserDaoImpl implements UserDao {
    	private SqlSessionFactory sqlSessionFactory;

    	// 将SqlSessionFactory注入
    	public UserDaoImpl(SqlSessionFactory sqlSessionFactory) {
    		this.sqlSessionFactory = sqlSessionFactory;
    	}

    	@Override
    	public User findUserById(int id) throws Exception {
    		// 创建SqlSession
    		SqlSession sqlSession = sqlSessionFactory.openSession();
    		// 根据id查询用户信息
    		User user = sqlSession.selectOne("test.findUserById", id);
    		sqlSession.close();
    		return user;
    	}
    }
    ```
3. 测试类：
`UserDaoImplTest.java`
    ```java
    package cn.itcast.mybatis.dao;

    import java.io.IOException;
    import java.io.InputStream;

    import org.apache.ibatis.io.Resources;
    import org.apache.ibatis.session.SqlSessionFactory;
    import org.apache.ibatis.session.SqlSessionFactoryBuilder;
    import org.junit.Before;
    import org.junit.Test;

    import cn.itcast.mybatis.po.User;

    public class UserDaoImplTest {
    	// 会话工厂
    	private SqlSessionFactory sqlSessionFactory;

    	// 创建工厂
    	@Before
    	public void init() throws IOException {
    		// 配置文件（SqlMapConfig.xml）
    		String resource = "SqlMapConfig.xml";
    		// 加载配置文件到输入流
    		InputStream inputStream = Resources.getResourceAsStream(resource);
    		// 创建会话工厂
    		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    	}

    	@Test
    	public void testFindUserById() throws Exception {
    		UserDao userDao = new UserDaoImpl(sqlSessionFactory);
    		User user = userDao.findUserById(1);
    		System.out.println(user);
    	}
    }
    ```

### mapper代理的方式
程序员只需要写dao接口，dao接口实现对象由mybatis自动生成代理对象。
本身dao在三层架构中就是一个通用的接口。

#### 原始dao开发方式的问题：
1. dao的实现类中存在重复代码，整个mybatis操作的过程代码模板重复（先创建sqlsession、调用sqlsession的方法、关闭sqlsession）
2. dao的实现类中存在硬编码，调用sqlsession方法时将statement的id硬编码。

#### mapper开发规范：
要想让mybatis自动创建dao接口实现类的代理对象，必须遵循一些规则：  
1. mapper.xml中namespace指定为mapper接口的全限定名。此步骤目的：通过mapper.xml和mapper.java进行关联。    
2. mapper.xml中statement的id就是mapper.java中方法名  
3. mapper.xml中statement的parameterType和mapper.java中方法输入参数类型一致  
4. mapper.xml中statement的resultType和mapper.java中方法返回值类型一致

1. mapper.xml（映射文件）  
config文件夹下新建mapper目录。
mapper映射文件的命名方式建议：表名Mapper.xml
namespace指定为mapper接口的全限定名
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace命名空间，为了对sql语句进行隔离，方便管理 ，mapper开发dao方式，使用namespace有特殊作用
mapper代理开发时将namespace指定为mapper接口的全限定名
 -->
<mapper namespace="cn.itcast.mybatis.mapper.UserMapper"></mapper>
```

2. mapper接口  
mybatis提出了mapper接口，相当于dao接口。    
mapper接口的命名方式建议：表名Mapper      
`UserMapper.java`
```java
public interface UserMapper {
	//根据用户id查询用户信息
	public User findUserById(int id) throws Exception;
}
```

3. 将mapper.xml在SqlMapConfig.xml中加载
```
<mappers>
    <mapper resource="sqlmap/User.xml" />
    <mapper resource="mapper/UserMapper.xml" />
</mappers>
```

4. 测试
```java
package cn.itcast.mybatis.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.mybatis.po.User;

public class UserMapperTest {
	// 会话工厂
	private SqlSessionFactory sqlSessionFactory;

	// 创建工厂
	@Before
	public void init() throws IOException {
		// 配置文件（SqlMapConfig.xml）
		String resource = "SqlMapConfig.xml";
		// 加载配置文件到输入流
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 创建会话工厂
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testFindUserById() throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		// 创建代理对象
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = userMapper.findUserById(1);
		System.out.println(user);
	}
}
```

mapper接口返回单个对象和集合对象
不管查询记录是单条还是多条，在 statement中resultType定义一致，都是单条记录映射的pojo类型。  
mapper接口方法返回值，如果是返回的单个对象，返回值类型是pojo类型，生成的代理对象内部通过selectOne获取记录，如果返回值类型是集合对象，生成的代理对象内部通过selectList获取记录。  
```java
//根据用户id查询用户信息
public User findUserById(int id) throws Exception;

//根据用户名称  查询用户信息
public List<User> findUserByName(String username) throws Exception;
```

# sqlMapConfig.xml
SqlMapConfig.xml中配置的内容和顺序如下：
```
properties（属性）
settings（全局配置参数）
typeAliases（类型别名）
typeHandlers（类型处理器）
objectFactory（对象工厂）
plugins（插件）
environments（环境集合属性对象）
    environment（环境子属性对象）
        transactionManager（事务管理）
        dataSource（数据源）
mappers（映射器）
```
### 1. properties属性定义
可以把一些`通用的属性值`配置在`属性文件`中，加载到mybatis运行环境内。  
比如：创建`db.properties`配置数据库连接参数。
```xml
<!-- 属性定义
加载一个properties文件，在properties标签中配置属性值
 -->
<properties resource="db.properties">
    <!-- <property name="" value=""/> -->
</properties>
```
注意： MyBatis 将按照下面的顺序来加载属性：
* 在`properties`元素体内定义的属性`property`首先被读取。
* 然后会读取`properties`元素中`resource`或`url`加载的属性，它会覆盖已读取的同名属性。
* 最后读取`parameterType`传递的属性，它会覆盖已读取的同名属性。
建议使用`properties`，不要在`properties`中定义属性，只引用定义的`properties`文件中属性，并且`properties`文件中定义的key要有一些特殊的规则。

### 2. settings全局参数配置
mybatis运行时可以调整一些全局参数（相当于软件的运行参数），参考：mybatis-settings.xlsx  
根据使用需求进行参数配置。  
注意：小心配置，配置参数会影响mybatis的执行。

ibatis的全局配置参数中包括很多的性能参数（最大线程数，最大待时间...），通过调整这些性能参数使ibatis达到高性能的运行，mybatis没有这些性能参数，由mybatis自动调节。

### 3. typeAliases类型别名(常用)
可以将`parameterType`、`resultType`中指定的类型 通过别名引用。

mybaits提供了很多别名：
```
别名	         映射的类型
_byte 	          byte
_long 	          long
_short 	         short
_int 	           int
_integer 	       int
_double 	    double
_float 	         float
_boolean 	   boolean
string 	        String
byte 	          Byte
long 	          Long
short 	         Short
int 	       Integer
integer 	   Integer
double 	        Double
float 	         Float
boolean 	   Boolean
date 	          Date
decimal 	BigDecimal
bigdecimal 	BigDecimal
```
自定义别名：
```xml
<!-- 定义 别名 -->
<typeAliases>
    <!--
    单个别名的定义
    alias：别名，type：别名映射的类型  -->
    <!-- <typeAlias type="cn.itcast.mybatis.po.User" alias="user"/> -->
    <!-- 批量别名定义：指定包路径，自动扫描包下边的pojo，定义别名，别名默认为类名（首字母小写或大写） -->
    <package name="cn.itcast.mybatis.po"/>
</typeAliases>
```
使用别名：  
在parameterType、resultType中使用别名：  
`UserMapper.xml`  
```xml
<!-- 根据id查询用户信息 -->
<!--
    id：唯一标识 一个statement
    #{}：表示一个占位符，如果#{}中传入简单类型的参数，#{}中的名称随意
    parameterType：输入参数的类型，通过#{}接收parameterType输入 的参数
    resultType：输出结果类型，不管返回是多条还是单条，指定单条记录映射的pojo类型
 -->
<select id="findUserById" parameterType="int" resultType="user">
    SELECT * FROM USER WHERE id= #{id}
</select>
```
```java
public class UserMapperTest {
	// 会话工厂
	private SqlSessionFactory sqlSessionFactory;

	// 创建工厂
	@Before
	public void init() throws IOException {
		// 配置文件（SqlMapConfig.xml）
		String resource = "SqlMapConfig.xml";
		// 加载配置文件到输入 流
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 创建会话工厂
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testFindUserById() throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		// 创建代理对象
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		User user = userMapper.findUserById(1);
		System.out.println(user);
	}
}
```

### typeHandlers
类型处理器将java类型和jdbc类型进行映射。  
mybatis默认提供很多类型处理器，一般情况下够用了。

### mappers（映射器）
通过class引用mapper接口  
class：配置mapper接口全限定名  
要求：需要mapper.xml和mapper.java同名并且在一个目录中  
`SqlMapConfig.xml`
```xml
<!--加载mapper映射
如果将和spring整合后，可以使用整合包中提供的mapper扫描器，此处的mappers不用配置了。
 -->
<mappers>
    <mapper class="cn.itcast.mybatis.mapper.UserMapper"/>
</mappers>
```

批量mapper配置  
通过package进行自动扫描包下边的mapper接口  
要求：需要mapper.xml和mapper.java同名并且在一个目录中  
```xml
<mappers>
    <package name="cn.itcast.mybatis.mapper"/>
</mappers>
```

# 七、输入和输出映射
通过parameterType完成输入映射，通过resultType和resultMap完成输出映射。

### 1. parameterType传递pojo包装对象
可以定义pojo包装类型扩展mapper接口输入参数的内容。  

需求：自定义查询条件查询用户信息，需要向statement输入查询条件，查询条件可以有user信息、商品信息...
1. 包装类型  
    `UserQueryVo.java`
    ```java
    public class UserQueryVo {
    	//用户信息
    	private User user;
    	//自定义user的扩展对象
    	private UserCustom userCustom;
    }
    ```
    `UserCustom.java`
    ```java
    public class UserCustom extends User {
    	//添加一些扩展字段
    }
    ```
2. `mapper.xml`  
自定义查询条件查询用户的信息  
parameterType：指定包装类型  
`%${userCustom.username}%`：`userCustom`是`userQueryVo`中的属性，通过OGNL获取属性的值
```xml
<select id="findUserList" parameterType="userQueryVo" resultType="user">
    select * from user where username like '%${userCustom.username}%'
</select>
```
3. `mapper.java`（接口）  
```java
public interface UserMapper {
    //自定义查询条件查询用户信息
    public List<User> findUserList(UserQueryVo userQueryVo) throws Exception;
}
```
4. 测试  
`UserMapperTest.java`
```java
// 通过包装类型查询用户信息
@Test
public void testFindUserList() throws Exception {

    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 创建代理对象
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    // 构造查询条件
    UserQueryVo userQueryVo = new UserQueryVo();
    UserCustom userCustom = new UserCustom();
    userCustom.setUsername("小明");
    userCustom.setSex("1");
    userQueryVo.setUserCustom(userCustom);

    List<User> list = userMapper.findUserList(userQueryVo);

    sqlSession.close();

    System.out.println(list);
}
```
5. 异常
如果parameterType中指定属性错误，异常，找不到getter方法。  
注意：如果将来和spring整合后，不是通过调用getter方法来获取属性值，通过反射强读取pojo的属性值。

### 2. resultType
指定输出结果的类型（pojo、简单类型、hashmap...），将sql查询结果映射为java对象。  
1. 返回简单类型
输出简单类型  
功能：自定义查询条件，返回查询记录个数，通常用于实现查询分页  
`UserMapper.xml`
```xml
 <select id="findUserCount" parameterType="userQueryVo" resultType="int">
    select count(*) from user where username like '%${userCustom.username}%'
 </select>
```
`UserMapper.java`
```java
//查询用户，返回记录个数
public int findUserCount(UserQueryVo userQueryVo) throws Exception;
```
`UserMapperTest.java`
```java
// 返回查询记录总数
@Test
public void testFindUserCount() throws Exception {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 创建代理对象
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    // 构造查询条件
    UserQueryVo userQueryVo = new UserQueryVo();
    UserCustom userCustom = new UserCustom();
    userCustom.setUsername("小明");
    userQueryVo.setUserCustom(userCustom);

    int count = userMapper.findUserCount(userQueryVo);

    sqlSession.close();

    System.out.println(count);
}
```
注意：如果查询记录结果集为一条记录且一列再使用返回简单类型。

### 3. resultMap(入门)
`resultType` ：指定输出结果的类型（pojo、简单类型、hashmap..），将sql查询结果映射为java对象 。  
注意：sql查询的列名要和resultType指定pojo的属性名相同，指定相同属性方可映射成功，如果sql查询的列名要和resultType指定pojo的属性名全部不相同，list中无法创建pojo对象的。

`resultMap`：将sql查询结果映射为java对象。  
如果sql查询列名和最终要映射的pojo的属性名不一致，使用resultMap将列名和pojo的属性名做一个对应关系 （列名和属性名映射配置）

1. resultMap配置
```xml
<!-- 定义resultMap，列名和属性名映射配置 id：mapper.xml中的唯一标识 type：最终要映射的pojo类型 -->
<resultMap id="userListResultMap" type="user" >
    <!-- 列名
    id_,username_,birthday_
    id：要映射结果集的唯 一标识 ，称为主键
    column：结果集的列名
    property：type指定的哪个属性中
     -->
     <id column="id_" property="id"/>
     <!-- result就是普通列的映射配置 -->
     <result column="username_" property="username"/>
     <result column="birthday_" property="birthday"/>
</resultMap>
```
2. resultMap使用
使用resultMap作结果映射  
resultMap：如果引用resultMap的位置和resultMap的定义在同一个mapper.xml，直接使用resultMap的id，如果不在同一个mapper.xml要在resultMap的id前边加namespace
```xml
<select id="findUserListResultMap" parameterType="userQueryVo" resultMap="userListResultMap">
    select id id_,username username_,birthday birthday_ from user where username like '%${userCustom.username}%'
</select>
```
3. mapper.java
```java
//查询用户，使用resultMap进行映射
public List<User> findUserListResultMap(UserQueryVo userQueryVo)throws Exception;
```


# 十一、mybatis和spring整合
### 1. mybaits和spring整合的思路
1、让spring管理SqlSessionFactory  
2、让spring管理mapper对象和dao  
	使用spring和mybatis整合开发mapper代理及原始dao接口。  
	自动开启事务，自动关闭 sqlsession.  
3、让spring管理数据源( 数据库连接池)  
### 2. 创建整合工程
### 3. 加入jar包
1、mybatis3.2.7本身的jar包
2、数据库驱动包
3、spring3.2.0
4、spring和mybatis整合包
	从mybatis的官方下载spring和mybatis整合包
### 4. log4j.properties
### 5. SqlMapconfig.xml
mybatis配置文件：别名、settings，数据源不在这里配置
### 6. applicationContext.xml
1、数据源（dbcp连接池）
2、SqlSessionFactory
3、mapper或dao
### 7. 整合开发原始dao接口
1. 配置SqlSessionFactory
`applicationContext.xml`
```xml
<!-- SqlsessionFactory -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <!-- 数据源 -->
    <property name="dataSource" ref="dataSource"/>
    <!-- mybatis配置文件 -->
    <property name="configLocation" value="classpath:mybatis/SqlMapConfig.xml"/>
</bean>
```

2. 开发dao  
`UserDaoImpl.java`
```java
package cn.itcast.mybatis.dao;
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {
	@Override
	public User findUserById(int id) throws Exception {
		// 创建SqlSession
		SqlSession sqlSession = this.getSqlSession();

		// 根据id查询用户信息
		User user = sqlSession.selectOne("test.findUserById", id);

		return user;
	}
}
```

3. 配置dao  
`applicationContext.xml`
```xml
<!-- 配置dao(先开发daoUserDaoImpl.java) -->
<bean id="userDao" class="cn.itcast.mybatis.dao.UserDaoImpl">
    <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
</bean>
```

4. 测试dao接口  
`UserDaoImplTest.java`
```java
public class UserDaoImplTest {
	private ApplicationContext applicationContext;
	@Before
	public void setUp() throws Exception {
		//创建spring容器
		applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	}

	@Test
	public void testFindUserById() throws Exception {
		//从spring容器中获取UserDao这个bean
		UserDao userDao = (UserDao) applicationContext.getBean("userDao");
		User user = userDao.findUserById(1);
		System.out.println(user);
	}
}
```

### 8. 整合开发mapper代理方法
1. 开发`mapper.xml`和`mapper.java`  
`UserMapper.java`和`UserMapper.xml`

2. 使用`MapperFactoryBean`（繁琐，不推荐）    
`applicationContext.xml`
```xml
<!-- 配置mapper (MapperFactoryBean：用于生成mapper代理对象) -->
<bean id="userMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
    <property name="mapperInterface" value="cn.itcast.mybatis.mapper.UserMapper"/> 指定接口类型
    <property name="sqlSessionFactory" ref="sqlSessionFactory"/> 因为它也集成了SqlSessionDaoSupport，所以把它也注入进去
</bean>
<bean id="itemMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
    <property name="mapperInterface" value="cn.itcast.mybatis.mapper.UserMapper"/> 指定接口类型
    <property name="sqlSessionFactory" ref="sqlSessionFactory"/> 因为它也集成了SqlSessionDaoSupport，所以把它也注入进去
</bean>
<!-- ......这样很繁琐，每个mapper都要配置 -->
```
使用此方法对于每个mapper都需要配置，比较繁琐。

3. 使用`MapperScannerConfigurer`（扫描mapper，推荐）  
`applicationContext.xml`
```xml
<!-- MapperScannerConfigurer：mapper的扫描器，将包下边的mapper接口自动创建代理对象，自动创建到spring容器中，bean的id是mapper的类名（首字母小写） -->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <!-- 配置扫描包的路径(如果要扫描多个包，中间使用半角逗号分隔) -->
    <property name="basePackage" value="cn.itcast.mybatis.mapper"/>
    <!-- 使用sqlSessionFactoryBeanName而不使用sqlSessionFactory -->
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
</bean>
```
使用扫描器自动扫描mapper，生成代理对象，比较方便。

4. 测试`mapper接口`    
`UserMapperTest.java`
```java
public class UserMapperTest {
	private ApplicationContext applicationContext;

	@Before
	public void setUp() throws Exception {
		// 创建spring容器
		applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	}

	@Test
	public void testFindUserById() throws Exception {
		UserMapper userMapper = (UserMapper) applicationContext.getBean("userMapper");
		User user  = userMapper.findUserById(1);
		System.out.println(user);
	}
}
```

# 十二、逆向工程(MyBatis Generator)
mybatis官方为了提高开发效率，提高自动对单表生成sql，包括 ：mapper.xml、mapper.java、表名.java(po类)

在企业开发中通常是在设计阶段对表进行设计、创建。  
在开发阶段根据表结构创建对应的po类。

mybatis逆向工程的方向：由数据库表----》java代码
### 1. 使用配置
1. xml配置
需要使用用配置的地方：
1、连接数据库的地址和驱动  
`Mysql`配置
```xml
<jdbcConnection driverClass="com.mysql.jdbc.Driver"
    connectionURL="jdbc:mysql://localhost:3306/mybatis" userId="root" password="123">
</jdbcConnection>
```
`Oracle`配置
```xml
<jdbcConnection driverClass="oracle.jdbc.OracleDriver"
    connectionURL="jdbc:oracle:thin:@127.0.0.1:1521:yycg"
    userId="yycg"
    password="yycg">
</jdbcConnection>
```
2、需要配置po类的包路径  
```xml
<!-- targetProject:生成PO类的位置 -->
<javaModelGenerator targetPackage="cn.itcast.mybatis.po"
    targetProject=".\src">
    <!-- enableSubPackages:是否让schema作为包的后缀 -->
    <property name="enableSubPackages" value="false" />
    <!-- 从数据库返回的值被清理前后的空格 -->
    <property name="trimStrings" value="true" />
</javaModelGenerator>
```
3、需要配置mapper包的路径  
```xml
<!-- targetProject:mapper映射文件生成的位置 -->
<sqlMapGenerator targetPackage="cn.itcast.mybatis.mapper"
    targetProject=".\src">
    <!-- enableSubPackages:是否让schema作为包的后缀 -->
    <property name="enableSubPackages" value="false" />
</sqlMapGenerator>
<!-- targetPackage：mapper接口生成的位置 -->
<javaClientGenerator type="XMLMAPPER"
    targetPackage="cn.itcast.mybatis.mapper"
    targetProject=".\src">
    <!-- enableSubPackages:是否让schema作为包的后缀 -->
    <property name="enableSubPackages" value="false" />
</javaClientGenerator>
```
4、指定数据表  
```xml
<table tableName="items"></table>
<table tableName="orders"></table>
<table tableName="orderdetail"></table>
```

`generatorConfig.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
	<context id="testTables" targetRuntime="MyBatis3">
		<commentGenerator>
			<!-- 是否去除自动生成的注释 true：是 ： false:否 -->
			<property name="suppressAllComments" value="true" />
		</commentGenerator>
		<!--1.数据库连接的信息：驱动类、连接地址、用户名、密码 -->
		<jdbcConnection driverClass="com.mysql.jdbc.Driver"
			connectionURL="jdbc:mysql://localhost:3306/mybatis" userId="root" password="123">
		</jdbcConnection>

		<!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
			NUMERIC 类型解析为java.math.BigDecimal -->
		<javaTypeResolver>
			<property name="forceBigDecimals" value="false" />
		</javaTypeResolver>

		<!-- 2.targetProject:生成PO类的位置 -->
		<javaModelGenerator targetPackage="cn.itcast.mybatis.po"
			targetProject=".\src">
			<!-- enableSubPackages:是否让schema作为包的后缀 -->
			<property name="enableSubPackages" value="false" />
			<!-- 从数据库返回的值被清理前后的空格 -->
			<property name="trimStrings" value="true" />
		</javaModelGenerator>
        <!-- 3.targetProject:mapper映射文件生成的位置 -->
		<sqlMapGenerator targetPackage="cn.itcast.mybatis.mapper"
			targetProject=".\src">
			<!-- enableSubPackages:是否让schema作为包的后缀 -->
			<property name="enableSubPackages" value="false" />
		</sqlMapGenerator>
		<!-- 3.targetPackage：mapper接口生成的位置 -->
		<javaClientGenerator type="XMLMAPPER"
			targetPackage="cn.itcast.mybatis.mapper"
			targetProject=".\src">
			<!-- enableSubPackages:是否让schema作为包的后缀 -->
			<property name="enableSubPackages" value="false" />
		</javaClientGenerator>
		<!-- 4.指定数据库表 -->
		<table tableName="items"></table>
		<table tableName="orders"></table>
		<table tableName="orderdetail"></table>
		<!-- <table schema="" tableName="sys_user"></table>
		<table schema="" tableName="sys_role"></table>
		<table schema="" tableName="sys_permission"></table>
		<table schema="" tableName="sys_user_role"></table>
		<table schema="" tableName="sys_role_permission"></table> -->

		<!-- 有些表的字段需要指定java类型
		 <table schema="" tableName="">
			<columnOverride column="" javaType="" />
		</table> -->
	</context>
</generatorConfiguration>
```

2. java程序
通过java程序生成mapper类、po类  
`GeneratorSqlmap.java`
```java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class GeneratorSqlmap {
	public void generator() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		//指定逆向工程配置文件
		File configFile = new File("generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}

	public static void main(String[] args) throws Exception {
		try {
			GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
			generatorSqlmap.generator();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

3. 使用逆向工程生成的代码
    1. 第一步配置`generatorConfig.xml`
    2. 第二步配置执行java程序
    3. 第三步将生成的代码拷贝到工程中`Items.java` `ItemsExample.java` `ItemsMapper.java` `ItemsMapper.xml`
    4. 测试生成的代码

`ItemsMapperTest.java`
```java
package cn.itcast.mybatis.mapper;

public class ItemsMapperTest {
	private ApplicationContext applicationContext;
	private ItemsMapper itemsMapper;

	@Before
	public void setUp() throws Exception {
		// 创建spring容器
		applicationContext = new ClassPathXmlApplicationContext(
				"spring/applicationContext.xml");
		itemsMapper = (ItemsMapper) applicationContext.getBean("itemsMapper");
	}

	// 根据主键删除
	@Test
	public void testDeleteByPrimaryKey() {
		itemsMapper.deleteByPrimaryKey(4);
		// 自定义条件删除
		// itemsMapper.deleteByExample(example)
	}

	@Test
	public void testInsert() {
		Items items = new Items();
		items.setName("手机");
		items.setPrice(3000.0f);

		itemsMapper.insert(items);
	}

	// 自定义条件查询
	@Test
	public void testSelectByExample() {
		ItemsExample itemsExample = new ItemsExample();
		ItemsExample.Criteria criteria = itemsExample.createCriteria();
		criteria.andNameEqualTo("笔记本2");
		// criteria.andNameLike("笔记本");

		List<Items> list = itemsMapper.selectByExample(itemsExample);
		System.out.println(list);
		// 将大文本字段也查询出来
		// itemsMapper.selectByExampleWithBLOBs(example)
	}

	// 根据主键查询
	@Test
	public void testSelectByPrimaryKey() {
		Items items = itemsMapper.selectByPrimaryKey(1);
		System.out.println(items);
	}

	@Test
	public void testUpdateByPrimaryKey() {
		// 将更新对象的内容全部更新到数据库
		// updateByPrimaryKey通过是先查询再设置更新字段的值
		Items items = itemsMapper.selectByPrimaryKey(1);
		items.setName("笔记 本4");

		itemsMapper.updateByPrimaryKey(items);
		//自定义条件更新，example设置条件，record是更新对象
//		itemsMapper.updateByExample(record, example)
		//可以更新大文本字段
//		itemsMapper.updateByExampleWithBLOBs(record, example)
	}

	@Test
	public void testUpdateByPrimaryKeySelective() {
		// 如果更新对象的属性不为空才更新到数据库
		// 常用于指定字段更新，不用先查询出，可以直接new一个对象，此对象一定要设置id主键，再设置更新字段的值
		Items items = new Items();
		items.setId(1);
		items.setName("笔记 本5");
		itemsMapper.updateByPrimaryKeySelective(items);
	}
}
```
