# 一、权限管理原理知识
## 1. 权限管理
只要有用户参与的系统一般都要有权限管理，权限管理实现对用户访问系统的控制，按照安全规则或者安全策略控制用户可以访问而且只能访问自己被授权的资源。
权限管理包括用户认证和授权两部分。

## 2. 用户认证

## 3. 用户授权


# 二、权限管理解决方案
## 1. 粗粒度和细粒度权限
## 2. 实现
## 3. 基于url拦截的方式实现
## 4. 使用权限管理框架实现

# 三、基于url的权限管理
## 1. 基于url权限管理流程
## 2. 搭建环境
## 3. 小结

# 四、shiro介绍
## 1. 概念
shiro是apache的一个开源框架，是一个权限管理的框架，实现 用户认证、用户授权。  

spring中有spring security (原名Acegi)，是一个权限框架，它和spring依赖过于紧密，没有shiro使用简单。  
shiro不依赖于spring，shiro不仅可以实现 web应用的权限管理，还可以实现c/s系统，分布式系统权限管理，shiro属于轻量框架，越来越多企业项目开始使用shiro。  

使用shiro实现系统 的权限管理，有效提高开发效率，从而降低开发成本。  

## 2. shiro架构
subject：主体，可以是用户也可以是程序，主体要访问系统，系统需要对主体进行认证、授权。

securityManager：安全管理器，主体进行认证和授权都 是通过securityManager进行。

authenticator：认证器，主体进行认证最终通过authenticator进行的。

authorizer：授权器，主体进行授权最终通过authorizer进行的。

sessionManager：web应用中一般是用web容器对session进行管理，shiro也提供一套session管理的方式。
SessionDao：  通过SessionDao管理session数据，针对个性化的session数据存储需要使用sessionDao。

cache Manager：缓存管理器，主要对session和授权数据进行缓存，比如将授权数据通过cacheManager进行缓存管理，和ehcache整合对缓存数据进行管理。

realm：域，领域，相当于数据源，通过realm存取认证、授权相关数据。

注意：在realm中存储授权和认证的逻辑。

cryptography：密码管理，提供了一套加密/解密的组件，方便开发。比如提供常用的散列、加/解密等功能。
比如 md5散列算法。

## 3. jar包
shiro-core是核心包必须选用，还提供了与web整合的shiro-web、与spring整合的shiro-spring、与任务调度quartz整合的shiro-quartz等，下边是shiro各jar包的maven坐标。
```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-web</artifactId>
    <version>1.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-ehcache</artifactId>
    <version>1.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-quartz</artifactId>
    <version>1.2.3</version>
</dependency>
```
也可以通过引入shiro-all包括shiro所有的包：
```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-all</artifactId>
    <version>1.2.3</version>
</dependency>
```


# shiro认证
## 1. shiro认证流程
![](http://p1.bqimg.com/567571/aa8c2b805be4f224.png)

## 2. shiro入门程序工程环境

## 3. shiro认证入门程序
1. `shiro-first.ini`  
通过此配置文件创建securityManager工厂。  
需要修改eclipse的ini的编辑器:  

配置数据：  
```ini
#对用户信息进行配置
[users]
#用户账号和密码
zhangsan=111111
lisi=222222
```


2. `AuthenticationTest.java`  
```java
// 用户登陆和退出
@Test
public void testLoginAndLogout() {
    // 创建securityManager工厂，通过ini配置文件创建securityManager工厂
    Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-first.ini");

    //创建SecurityManager
    SecurityManager securityManager = factory.getInstance();
    
    //将securityManager设置当前的运行环境中
    SecurityUtils.setSecurityManager(securityManager);
    
    //从SecurityUtils里边创建一个subject
    Subject subject = SecurityUtils.getSubject();
    
    //在认证提交前准备token（令牌），这里是用户输入的
    UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "111111");

    try {
        //执行认证提交
        subject.login(token);
    } catch (AuthenticationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
    //是否认证通过
    boolean isAuthenticated =  subject.isAuthenticated();
    
    System.out.println("是否认证通过：" + isAuthenticated);
    
    //退出操作
    subject.logout();
    
    //是否认证通过
    isAuthenticated =  subject.isAuthenticated();
    
    System.out.println("是否认证通过：" + isAuthenticated);
}
```
## 2. 执行流程
1. 通过ini配置文件创建`securityManager`
2. 调用`subject.login`方法主体提交认证，提交的token
3. `securityManager`进行认证，securityManager最终由`ModularRealmAuthenticator`进行认证
4. `ModularRealmAuthenticator`调用`IniRealm`(给realm传入token) 去ini配置文件中查询用户信息
5. `IniRealm`根据输入的`token（UsernamePasswordToken）`从 `shiro-first.ini`查询用户信息，根据账号查询用户信息（账号和密码）  
	如果查询到用户信息，就给`ModularRealmAuthenticator`返回用户信息（账号和密码）  
	如果查询不到，就给`ModularRealmAuthenticator`返回null  
6. `ModularRealmAuthenticator`接收`IniRealm`返回Authentication认证信息  
	如果返回的认证信息是null，`ModularRealmAuthenticator`抛出异常（org.apache.shiro.authc.UnknownAccountException）  
	如果返回的认证信息不是null（说明inirealm找到了用户），对IniRealm返回用户密码（在ini文件中存在）和token中的密码进行对比，如果不一致抛出异常（org.apache.shiro.authc.IncorrectCredentialsException）

## 3. 小结
ModularRealmAuthenticator作用进行认证，需要调用realm查询用户信息（在数据库中存在用户信息）  
ModularRealmAuthenticator进行密码对比（认证过程）。
	
realm：需要根据token中的身份信息去查询数据库（入门程序使用ini配置文件），如果查到用户返回认证信息，如果查询不到返回null。

## 4. 自定义realm
将来实际开发需要realm从数据库中查询用户信息。
1. realm接口
![](http://i1.piimg.com/567571/4f256096ff000125.png)
`AuthorizingRealm`

2. 自定义realm  
`CustomRealm.java`  
```java
package cn.itcast.shiro.realm;
public class CustomRealm extends AuthorizingRealm {
	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("customRealm");
	}

	// 用于认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// token是用户输入的
		// 第一步从token中取出身份信息
		String userCode = (String) token.getPrincipal();

		// 第二步：根据用户输入的userCode从数据库查询
		// ....	

		// 如果查询不到返回null
		// 数据库中用户账号是zhangsansan
		/*if(!userCode.equals("zhangsansan")){//
			return null;
		}*/		
		
		// 模拟从数据库查询到密码
		String password = "111112";

		// 如果查询到返回认证信息AuthenticationInfo
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userCode, password, this.getName());
		return simpleAuthenticationInfo;
	}

	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}
}
```

3. 配置realm
需要在`shiro-realm.ini`配置realm注入到securityManager中。
```ini
[main]
#自定义realm
customRealm=cn.itcast.shiro.realm.customRealm
#将realm设置到securityManager，相当于spring中注入
securityManager.realm=$customRealm
```

4. 测试
同上边的入门程序，需要更改ini配置文件路径：
`Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");`

5. 散列算法
通常需要对密码 进行散列，常用的有md5、sha。 

对md5密码，如果知道散列后的值可以通过穷举算法，得到md5密码对应的明文。  
建议对md5进行散列时加salt（盐），进行加密相当于对原始密码+盐进行散列。  
正常使用时散列方法：在程序中对原始密码+盐进行散列，将散列值存储到数据库中，并且还要将盐也要存储在数据库中。

如果进行密码对比时，使用相同方法，将原始密码+盐进行散列，进行比对。

7.5.1	md5散列测试程序：
```java
public class MD5Test {
	public static void main(String[] args) {
		//原始 密码 
		String source = "111111";
		//盐
		String salt = "qwerty";
		//散列次数
		int hashIterations = 2;
		//上边散列1次：f3694f162729b7d0254c6e40260bf15c
		//上边散列2次：36f2dfa24d0a9fa97276abbe13e596fc
		
		//构造方法中：
		//第一个参数：明文，原始密码 
		//第二个参数：盐，通过使用随机数
		//第三个参数：散列的次数，比如散列两次，相当 于md5(md5(''))
		Md5Hash md5Hash = new Md5Hash(source, salt, hashIterations);
		
		String password_md5 =  md5Hash.toString();
		System.out.println(password_md5);
		//第一个参数：散列算法 
		SimpleHash simpleHash = new SimpleHash("md5", source, salt, hashIterations);
		System.out.println(simpleHash.toString());
	}
}
```


7.5.2	自定义realm支持散列算法   
7.5.2.1	新建realm(CustomRealmMd5)
`CustomRealmMd5.java`
```java
public class CustomRealmMd5 extends AuthorizingRealm {
	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("customRealmMd5");
	}

	// 用于认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		// token是用户输入的
		// 第一步从token中取出身份信息
		String userCode = (String) token.getPrincipal();

		// 第二步：根据用户输入的userCode从数据库查询
		// ....

		// 如果查询不到返回null
		// 数据库中用户账号是zhangsansan
		/*
		 * if(!userCode.equals("zhangsansan")){// return null; }
		 */

		// 模拟从数据库查询到密码,散列值
		String password = "f3694f162729b7d0254c6e40260bf15c";
		// 从数据库获取salt
		String salt = "qwerty";
		//上边散列值和盐对应的明文：111111

		// 如果查询到返回认证信息AuthenticationInfo
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userCode, password, ByteSource.Util.bytes(salt), this.getName());
		return simpleAuthenticationInfo;
	}

	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}
}
```
7.5.2.2	在realm中配置凭证匹配器
```
[main]
#定义凭证匹配器
credentialsMatcher=org.apache.shiro.authc.credential.HashedCredentialsMatcher
#散列算法
credentialsMatcher.hashAlgorithmName=md5
#散列次数
credentialsMatcher.hashIterations=1

#将凭证匹配器设置到realm
customRealm=cn.itcast.shiro.realm.CustomRealmMd5
customRealm.credentialsMatcher=$credentialsMatcher
securityManager.realms=$customRealm
```


# shiro授权  
`shiro-permission.ini`
```
#用户
[users]
#用户zhang的密码是123，此用户具有role1和role2两个角色
zhangsan=123,role1,role2
wang=123,role2

#权限
[roles]
#角色role1对资源user拥有create、update权限
role1=user:create,user:update
#角色role2对资源user拥有create、delete权限
role2=user:create,user:delete
#角色role3对资源user拥有create权限
role3=user:create
```

```java
public class AuthorizationTest {
	// 角色授权、资源授权测试
	@Test
	public void testAuthorization() {
		// 创建SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-permission.ini");

		// 创建SecurityManager
		SecurityManager securityManager = factory.getInstance();

		// 将SecurityManager设置到系统运行环境，和spring后将SecurityManager配置spring容器中，一般单例管理
		SecurityUtils.setSecurityManager(securityManager);

		// 创建subject
		Subject subject = SecurityUtils.getSubject();

		// 创建token令牌
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123");

		// 执行认证
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("认证状态：" + subject.isAuthenticated());
		// 认证通过后执行授权

		// 基于角色的授权
		// hasRole传入角色标识
		boolean ishasRole = subject.hasRole("role1");
		System.out.println("单个角色判断" + ishasRole);
		// hasAllRoles是否拥有多个角色
		boolean hasAllRoles = subject.hasAllRoles(Arrays.asList("role1",
				"role2", "role3"));
		System.out.println("多个角色判断" + hasAllRoles);

		// 使用check方法进行授权，如果授权不通过会抛出异常
		// subject.checkRole("role13");

		// 基于资源的授权
		// isPermitted传入权限标识符
		boolean isPermitted = subject.isPermitted("user:create:1");
		System.out.println("单个权限判断" + isPermitted);

		boolean isPermittedAll = subject.isPermittedAll("user:create:1","user:delete");
		System.out.println("多个权限判断" + isPermittedAll);

		// 使用check方法进行授权，如果授权不通过会抛出异常
		subject.checkPermission("items:create:1");
	}

	// 自定义realm进行资源授权测试
	@Test
	public void testAuthorizationCustomRealm() {

		// 创建SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");

		// 创建SecurityManager
		SecurityManager securityManager = factory.getInstance();

		// 将SecurityManager设置到系统运行环境，和spring后将SecurityManager配置spring容器中，一般单例管理
		SecurityUtils.setSecurityManager(securityManager);

		// 创建subject
		Subject subject = SecurityUtils.getSubject();

		// 创建token令牌
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan",
				"111111");

		// 执行认证
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("认证状态：" + subject.isAuthenticated());
		// 认证通过后执行授权

		// 基于资源的授权，调用isPermitted方法会调用CustomRealm从数据库查询正确权限数据
		// isPermitted传入权限标识符，判断user:create:1是否在CustomRealm查询到权限数据之内
		boolean isPermitted = subject.isPermitted("user:create:1");
		System.out.println("单个权限判断" + isPermitted);

		boolean isPermittedAll = subject.isPermittedAll("user:create:1",
				"user:create");
		System.out.println("多个权限判断" + isPermittedAll);

		// 使用check方法进行授权，如果授权不通过会抛出异常
		subject.checkPermission("items:add:1");
	}
}
```

# shiro与项目整合
将原来基于url的工程改成使用shiro实现。

1. 去除原工程的认证和授权的拦截
删除`springmvc.xml`中：`<mvc:interceptors>`
```xml
<!--拦截器 -->
<mvc:interceptors>
    <mvc:interceptor>
        <!-- 用户认证拦截 -->
        <mvc:mapping path="/**" />
        <bean class="cn.itcast.ssm.controller.interceptor.LoginInterceptor"></bean>
    </mvc:interceptor>
    <mvc:interceptor>
        <!-- 授权拦截 -->
        <mvc:mapping path="/**" />
        <bean class="cn.itcast.ssm.controller.interceptor.PermissionInterceptor"></bean>
    </mvc:interceptor>
</mvc:interceptors>
```

2. jar包
`shiro-web`、`shiro-spring`、`shiro-code`

3. web.xml中配置shiro的filter
在web系统中，shiro也通过filter进行拦截。filter拦截后将操作权交给spring中配置的filterChain（过滤链）。shiro提供很多filter。
```xml
<!-- shiro的filter -->
<!-- shiro过虑器，DelegatingFilterProxy通过代理模式将spring容器中的bean和filter关联起来 -->
<filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <!-- 设置true由servlet容器控制filter的生命周期 -->
    <init-param>
        <param-name>targetFilterLifecycle</param-name>
        <param-value>true</param-value>
    </init-param>
    <!-- 设置spring容器filter的bean id，如果不设置则找与filter-name一致的bean-->
    <init-param>
        <param-name>targetBeanName</param-name>
        <param-value>shiroFilter</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>shiroFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

4. 在spring中配置filter
在`applicationContext-shiro.xml`中配置web.xml中fitler对应spring容器中的bean。
```xml
<!-- web.xml中shiro的filter对应的bean -->
<!-- Shiro的Web过滤器 -->
<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
    <property name="securityManager" ref="securityManager" />
    <!-- loginUrl认证提交地址，如果没有认证将会请求此地址进行认证，请求此地址将由formAuthenticationFilter进行表单认证 -->
    <property name="loginUrl" value="/login.action" />
    <!-- 认证成功统一跳转到first.action，建议不配置，shiro认证成功自动到上一个请求路径 -->
    <property name="successUrl" value="/first.action"/>
    <!-- 通过unauthorizedUrl指定没有权限操作时跳转页面-->
    <property name="unauthorizedUrl" value="/refuse.jsp" />
    <!-- 自定义filter配置 -->
    <property name="filters">
        <map>
            <!-- 将自定义的FormAuthenticationFilter注入shiroFilter中-->
            <entry key="authc" value-ref="formAuthenticationFilter" />
        </map>
    </property>
    
    <!-- 过滤器链定义，从上向下顺序执行，一般将/**放在最下边 -->
    <property name="filterChainDefinitions">
        <value>
            <!-- 对静态资源设置匿名访问 -->
            /images/** = anon
            /js/** = anon
            /styles/** = anon
            <!-- 验证码，可匿名访问 -->
            /validatecode.jsp = anon
            
            <!-- 请求logout.action地址，shiro去清除session-->
            /logout.action = logout
            <!--商品查询需要商品查询权限，取消url拦截配置，使用注解授权方式 -->
            <!-- /items/queryItems.action = perms[item:query]
            /items/editItems.action = perms[item:edit] -->
            <!-- 配置记住我或认证通过可以访问的地址 -->
            /index.jsp  = user
            /first.action = user
            /welcome.jsp = user
            <!-- /** = authc 所有url都必须认证通过才可以访问-->
            /** = authc
            <!-- /** = anon所有url都可以匿名访问 -->
        </value>
    </property>
</bean>

<!-- securityManager安全管理器 -->
<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
		<property name="realm" ref="customRealm" />
		<!-- 注入缓存管理器 -->
		<property name="cacheManager" ref="cacheManager"/>
		<!-- 注入session管理器 -->
		<property name="sessionManager" ref="sessionManager" />
		<!-- 记住我 -->
		<property name="rememberMeManager" ref="rememberMeManager"/>
		
	</bean>

<!-- realm -->
<bean id="customRealm" class="cn.itcast.ssm.shiro.CustomRealm">
	<!-- 将凭证匹配器设置到realm中，realm按照凭证匹配器的要求进行散列 -->
	<property name="credentialsMatcher" ref="credentialsMatcher"/>
</bean>
```

# 登陆
## 1. 原理
使用`FormAuthenticationFilter`过滤器实现 ，原理如下：

将用户没有认证时，请求loginurl进行认证，用户身份和用户密码提交数据到loginurl
`FormAuthenticationFilter`拦截住取出request中的username和password（两个参数名称是可以配置的）
`FormAuthenticationFilter`调用`realm`传入一个`token（username和password）`
realm认证时根据username查询用户信息（在Activeuser中存储，包括 userid、usercode、username、menus）。
如果查询不到，realm返回null，FormAuthenticationFilter向request域中填充一个参数（记录了异常信息）

## 2. 登陆页面
由于FormAuthenticationFilter的用户身份和密码的input的默认值（username和password），修改页面的账号和密码 的input的名称为username和password

## 3. 登陆代码实现
```java
@Controller
public class LoginController {
	@Autowired
	private SysService sysService;
	
	//用户登陆提交方法
	/*@RequestMapping("/login")
	public String login(HttpSession session, String randomcode,String usercode,String password)throws Exception{
		
		//校验验证码，防止恶性攻击
		//从session获取正确验证码
		String validateCode = (String) session.getAttribute("validateCode");
		
		//输入的验证和session中的验证进行对比 
		if(!randomcode.equals(validateCode)){
			//抛出异常
			throw new CustomException("验证码输入错误");
		}
		
		//调用service校验用户账号和密码的正确性
		ActiveUser activeUser = sysService.authenticat(usercode, password);
		
		//如果service校验通过，将用户身份记录到session
		session.setAttribute("activeUser", activeUser);
		//重定向到商品查询页面
		return "redirect:/first.action";
	}*/
	
	//登陆提交地址，和applicationContext-shiro.xml中配置的loginurl一致
	@RequestMapping("login")
	public String login(HttpServletRequest request)throws Exception{
		//如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		//根据shiro返回的异常类路径判断，抛出指定异常信息
		if(exceptionClassName!=null){
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				//最终会抛给异常处理器
				throw new CustomException("账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(
					exceptionClassName)) {
				throw new CustomException("用户名/密码错误");
			} else if("randomCodeError".equals(exceptionClassName)){
				throw new CustomException("验证码错误 ");
			}else {
				throw new Exception();//最终在异常处理器生成未知错误
			}
		}
		//此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
		//登陆失败还到login页面
		return "login";
	}
}
```

## 5.8.4	认证拦截过滤器
在`applicationContext-shiro.xml`中配置：
```xml
<!-- /** = authc 所有url都必须认证通过才可以访问-->
/** = authc
```

# 退出
## 1. 使用LogoutFilter
不用我们去实现退出，只要去访问一个退出的url（该 url是可以不存在），由LogoutFilter拦截住，清除session。

在`applicationContext-shiro.xml`配置`LogoutFilter`：
```xml
<!-- 请求 logout.action地址，shiro去清除session-->
/logout.action = logout
```
可以删除原来的logout的controller方法代码。

# 5.10	认证信息在页面显示
5.10.1	修改realm设置完整认证信息
realm从数据库查询用户信息，将用户菜单、usercode、username等设置在`SimpleAuthenticationInfo`中。
```java

```

5.10.2	修改first.action将认证信息在页面显示

# 5.11	授权过滤器测试
5.11.1	使用PermissionsAuthorizationFilter
```java
// 用于授权
@Override
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    //从 principals获取主身份信息
    //将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
    ActiveUser activeUser =  (ActiveUser) principals.getPrimaryPrincipal();
    
    //根据身份信息获取权限信息
    //从数据库获取到权限数据
    List<SysPermission> permissionList = null;
    try {
        permissionList = sysService.findPermissionListByUserId(activeUser.getUserid());
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    //单独定一个集合对象 
    List<String> permissions = new ArrayList<String>();
    if(permissionList!=null){
        for(SysPermission sysPermission:permissionList){
            //将数据库中的权限标签 符放入集合
            permissions.add(sysPermission.getPercode());
        }
    }
    
    
/*	List<String> permissions = new ArrayList<String>();
    permissions.add("user:create");//用户的创建
    permissions.add("item:query");//商品查询权限
    permissions.add("item:add");//商品添加权限
    permissions.add("item:edit");//商品修改权限
*/		//....
    
    //查到权限数据，返回授权信息(要包括 上边的permissions)
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
    simpleAuthorizationInfo.addStringPermissions(permissions);
    return simpleAuthorizationInfo;
}
```

5.11.2	创建refuse.jsp
如果授权失败，跳转到refuse.jsp，需要在spring容器中配置：
```html
<!-- 通过unauthorizedUrl指定没有权限操作时跳转页面-->
<property name="unauthorizedUrl" value="/refuse.jsp" />
```

5.11.3	问题总结
1、在applicationContext-shiro.xml中配置过虑器链接，需要将全部的url和权限对应起来进行配置，比较发麻不方便使用。  
2、每次授权都需要调用realm查询数据库，对于系统性能有很大影响，可以通过shiro缓存来解决。


# 5.12	shiro的过滤器