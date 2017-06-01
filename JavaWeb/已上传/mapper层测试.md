### 1. po层  
`User.java`
```java
package po;
public class User {
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
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

### 2. mapper层  
`UserMapper.java`
```java
package mapper;
import po.User;
public interface UserMapper {
    User getUserByName(String username);
    User getUserById(int id);
    void insertUser(User user);
}
```
`UserMapper.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="mapper.UserMapper">
    <resultMap type="po.User" id="userMap">
        <id property="id" column="id" />
        <result property="username" column="username" />
        <result property="password" column="password" />
    </resultMap>
    <!-- select标签的id值对应Mapper类中方法名 -->

    <select id="getUserByName" parameterType="string" resultMap="userMap">
        <!-- 此处写sql语句,#{Mapper类传入的参数} -->
        select * from T_USER where username = #{username}
    </select>

    <select id="getUserById" parameterType="int" resultMap="userMap">
        SELECT * FROM user WHERE id = #{id}
    </select>

    <insert id="insertUser" parameterType="po.User" useGeneratedKeys="true" keyProperty="id"> <!--插入时获取自增主键id-->
        insert into user (id, username, password)
        values (#{id,jdbcType=INTEGER}, #{username,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR})
    </insert>
</mapper>
```

### 3. 测试
`UserMapperTest.java`
```java
package mapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import po.User;
public class UserMapperTest {
    private ApplicationContext applicationContext;
    private UserMapper userMapper;

    @Before
    public void setUp() throws Exception {
        // 创建spring容器
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        userMapper = (UserMapper) applicationContext.getBean("userMapper");
    }

    @Test
    public void testGetUserById() {
        User user = userMapper.getUserById(1);
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("user8");
        user.setPassword("psw8");
        userMapper.insert(user);
        System.out.println(user);
    }
}
```
