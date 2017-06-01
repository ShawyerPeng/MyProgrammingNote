# 一、JDBC介绍
JDBC（Java Data Base Connectivity,java数据库连接)。简单说:就是可以直接通过java语言，去操作数据库。  
jdbc是一套标准,它是由一些接口与类组成的。  

学习中涉及到的类与接口：（它们主要在两个包下）
```
java.sql
    类:DriverManger
    接口  Connection Statement ResultSet  PreparedStatement
            CallableStatement（它是用于调用存储过程）
javax.sql
    接口 DataSource
```

# 二、快速入门
#### 1. 创建数据库
```sql
create table user(
   id int primary key auto_increment,
   username varchar(20) unique not null,
   password varchar(20) not null,
   email varchar(40) not null
);

INSERT INTO USER VALUES(NULL,'tom','123','tom@163.com');
INSERT INTO USER VALUES(NULL,'fox','123','fox@163.com');
```
#### 2. 下载驱动
将驱动jar包复制到lib下
#### 3. 创建一个JdbcDemo类
```java
import java.sql.*;
// import com.mysql.jdbc.Driver;

public class JdbcDemo{
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        // 1.注册驱动
        // DriverManager.registerDriver(new Driver());
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获取连接对象
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc?useSSL=false", "root", "pengxiaoye");
        // 3.通过连接对象获取操作sql语句Statement
        Statement st = con.createStatement();

        // 4.操作sql语句
        String sql = "select * from user";

        // 操作sql语句(select语句),会得到一个ResultSet结果集
        ResultSet rs = st.executeQuery(sql);

        // 5.遍历结果集
        // boolean flag = rs.next(); // 向下移动，返回值为true，代表有下一条记录.
        // int id = rs.getInt("id");
        // String username=rs.getString("username");
        // System.out.println(id);
        // System.out.println(username);

        while(rs.next()){
            int id=rs.getInt("id");
            String username=rs.getString("username");
            String password=rs.getString("password");
            String email=rs.getString("email");

            System.out.println(id+"  "+username+"  "+password+"  "+email);
        }

        //6.释放资源
        rs.close();
        st.close();
        con.close();

    }
}
```
JDBC操作详解：
```
1.注册驱动
    DriverManager.registDriver(new Driver());

    1.DriverManager类
        它是java.sql包下的一个驱动管理的工具类,可以理解成是一个容器(Vector),可以装入很多数据库驱动

        它的registDriver方法分析
              public static synchronized void registerDriver(java.sql.Driver driver)
              参数:java.sql.Driver
              我们传递的是  com.mysql.jdbc.Driver;

        在com.mysql.jdbc.Driver类中有一段静态代码块:

            static {
                try {
                    java.sql.DriverManager.registerDriver(new Driver());
                } catch (SQLException E) {
                    throw new RuntimeException("Can't register driver!");
                }
            }
        上述代码的问题:
            1.在驱动管理器中会装入两个mysql驱动.
                解决方案:使用反射
                        Class.forName("com.mysql.jdbc.Driver");

        分析:使用反射的方式来加载驱动有什么好处?
            1.只加载一次，装入一个驱动对象.
            2.降低耦合，不依赖于驱动.

2.可以通过DriverManager来获取连接对象
    Connection con=DriverManager.getConection(String url,String user,String password);
        url作用:就是用于确定使用哪一个驱动.
            mysql url:  jdbc:mysql://localhsot:3306/数据库名.
            oralce url: jdbc:oracle:thin:@localhost:1521:sid

总结:DriverManager作用:
    1.注册驱动
    2.获取连接Connection
-----------------------------------------------------------
关于url
        url格式
            主协议  子协议   主机 端口  数据库
            jdbc   :  mysql ://localhost:3306/day17

        mysql的url可以简写:
            前提：主机是localhost 端口是3306
            jdbc:mysql:///day17

        了解:在url后面可以带参数
            useUnicode=true&characterEncoding=UTF-8
------------------------------------------------------------------
2.Connection详解
	java.sql.Connection，它代表的是一个连接对象。简单说，就是我们程序与数据库连接。

	Connection作用:
		1.可以通过Connection获取操作sql的Statement对象。
			Statement createStatement() throws SQLException
			示例:
			Statement st=con.createStatement();

			了解:
				1.可以获取执行预处理的PreparedStatement对象.
					PreparedStatement prepareStatement(String sql) throws SQLException
				2.可以获取执行存储过程的 CallableStatement
					CallableStatement prepareCall(String sql) throws SQLException
		2.操作事务
			setAutoCommit(boolean flag);开启事务
			rollback();事务回滚
			commit();事务提交

------------------------------------------------------------------
3.Statement详解
	java.sql.Statement用于执行sql语句.
	Statement作用:
		1.执行sql
			DML:insert update delete
				int executeUpdate(String sql)

				利用返回值判断非0来确定sql语句是否执行成功。

			DQL:select
				ResultSet executeQuery(String sql)

			可以通过execute方法来执行任何sql语句.
				execute(String sql)：用于向数据库发送任意sql语句

		2.批处理操作
			addBatch(String sql); 将sql语句添加到批处理
			executeBatch();批量执行
			clearBatch();清空批处理.
---------------------------------------------------------------------
4.ResultSet详解
	java.sql.ResultSet它是用于封装select语句执行后查询的结果。

	常用API
		1.next()方法
			public boolean next();
				用于判断是否有下一条记录。如果有返回true,并且让游标向下移动一行。
				如果没有返回false.

		2.可以通过ResultSet提供的getXxx()方法来获取当前游标指向的这条记录中的列数据。
			常用:
				getInt()
				getString()
				getDate()
				getDouble()
				参数有两种
					1.getInt(int columnIndex);
					2.getInt(String columnName);

				如果列的类型不知道，可以通过下面的方法来操作
					getObject(int columnIndex);
					getObject(String columnName);

----------------------------------------------------------------
5.关闭资源
	Jdbc程序运行完后，切记要释放程序在运行过程中，创建的那些与数据库进行交互的对象，这些对象通常是ResultSet, Statement和Connection对象。

	特别是Connection对象，它是非常稀有的资源，用完后必须马上释放，如果Connection不能及时、正确的关闭，极易导致系统宕机。Connection的使用原则是尽量晚创建，尽量早的释放。

	为确保资源释放代码能运行，资源释放代码也一定要放在finally语句中。
```
对异常进行try-catch-finally：
```java
Connection con = null;
Statement st = null;
ResultSet rs = null;
try{
    ......
} catch(ClassNotFoundException e){
    e.printStackTrace();
} catch(SQLException e){
    e.printStackTrace();
} finally{ //必须要释放资源
    try{
        if(rs!=null) rs.close();
    } catch(SQLException e){
        e.printStackTrace();
    }
    try{
        if(st!=null) st.close();
    } catch(SQLException e){
        e.printStackTrace();
    }
    try{
        if(con!=null) con.close();
    } catch(SQLException e){
        e.printStackTrace();
    }
}
```
# 三、JDBC api详解(重点)
### 只抽取到Connection
    ```java
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获取连接
        Connection con = DriverManager.getConnection("jdbc:mysql:///day17","root", "abc");
        return con;
    }
    ```
### JbdcUtils抽取
1. 将关于连接数据库的信息定义到配置文件中：
    ```
    driverClass=com.mysql.jdbc.Driver
    url=jdbc:mysql:///day17
    username=root
    password=abc

    #driverClass=oracle.jdbc.driver.OracleDriver
    #url=jdbc:oracle:thin:@localhost:1521:XE
    #username=system
    #password=system
    ```
2. 读取配置文件进行加载：
    ```java
    public class JdbcUtils {

    	private static final String DRIVERCLASS;
    	private static final String URL;
    	private static final String USERNAME;
    	private static final String PASSWORD;

    	static {   
    		DRIVERCLASS = ResourceBundle.getBundle("jdbc").getString("driverClass"); // ResourceBundle可直接获取配置文件名
    		URL = ResourceBundle.getBundle("jdbc").getString("url");
    		USERNAME = ResourceBundle.getBundle("jdbc").getString("username");
    		PASSWORD = ResourceBundle.getBundle("jdbc").getString("password");
    	}

    	static {
    		try {
    			// 1.将加载驱动操作，放置在静态代码块中.这样就保证了只加载一次.
    			Class.forName(DRIVERCLASS);
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		}
    	}

    	public static Connection getConnection() throws SQLException {

    		// 2.获取连接
    		Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

    		return con;
    	}

    	// 3.关闭操作
    	public static void closeConnection(Connection con) throws SQLException{
    		if(con!=null){
    			con.close();
    		}
    	}
    	public static void closeStatement(Statement st) throws SQLException{
    		if(st!=null){
    			st.close();
    		}
    	}
    	public static void closeResultSet(ResultSet rs) throws SQLException{
    		if(rs!=null){
    			rs.close();
    		}
    	}
    }
    ```

#### 1.查询
查询全部：  
条件查询：根据id
```java
public void findByIdTest() {
    // 定义sql
    String sql = "select * from user where id=1";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    try {
        // 1.注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获取连接
        con = DriverManager.getConnection("jdbc:mysql:///day17", "root", "abc");
        // 3.获取操作sql语句对象Statement
        st = con.createStatement();
        // 4.执行sql
        rs = st.executeQuery(sql);
        // 5.遍历结果集
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            System.out.println(id + "  " + username + "  " + password + "  " + email);
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally { // 6.释放资源
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

#### 2.修改
```java
public void update() {
    // 将id=3的人的password修改为456
    String password = "456";
    String sql = "update user set password='" + password + "' where id=3";
    // 1.得到Connection
    Connection con = null;
    Statement st = null;
    try {
        con = JdbcUtils.getConnection();
        // 3.获取操作sql语句对象Statement
        st = con.createStatement();
        // 4.执行sql
        int row = st.executeUpdate(sql);
        if (row != 0) {
            System.out.println("修改成功");
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally { // 关闭资源
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

#### 3.删除
```java
public void delete() {
    // 将id=3的人删除
    String sql = "delete from user where id=2";
    // 1.得到Connection
    Connection con = null;
    Statement st = null;
    try {
        con = JdbcUtils.getConnection();
        // 3.获取操作sql语句对象Statement
        st = con.createStatement();
        // 4.执行sql
        int row = st.executeUpdate(sql);
        if (row != 0) {
            System.out.println("删除成功");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally { // 关闭资源
        try {
            JdbcUtils.closeStatement(st);
            JdbcUtils.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

#### 4.添加
```java
public void add() {
    // 定义sql
    String sql = "insert into user values(null,'张三','123','zs@163.com')";
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    try {
        // 1.注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获取连接
        con = DriverManager.getConnection("jdbc:mysql:///day17", "root",
                "abc");
        // 3.获取操作sql语句对象Statement
        st = con.createStatement();
        // 4.执行sql
        int row = st.executeUpdate(sql);
        if (row != 0) {
            System.out.println("添加成功");
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally { // 6.释放资源
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
滚动结果集：
```
默认得到的ResultSet它只能向下遍历(next()),对于ResultSet它可以设置成是滚动的，可以向上遍历，
或者直接定位到一个指定的物理行号.

问题:怎样得到一个滚动结果集?

    Statement st=con.createStatement();
    ResultSet rs=st.executeQuery(sql);
    这是一个默认结果集:只能向下执行，并且只能迭代一次。

     Statement stmt = con.createStatement(
                              ResultSet.TYPE_SCROLL_INSENSITIVE,
                              ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery(sql);
    这个就可以创建滚动结果集.
    简单说，就是在创建Statement对象时，不使用createStatement();
    而使用带参数的createStatement(int,int)


    Statement createStatement(int resultSetType,
                  int resultSetConcurrency)
                  throws SQLException

    resultSetType - 结果集类型，它是 ResultSet.TYPE_FORWARD_ONLY、ResultSet.TYPE_SCROLL_INSENSITIVE 或 ResultSet.TYPE_SCROLL_SENSITIVE 之一
    resultSetConcurrency - 并发类型；它是 ResultSet.CONCUR_READ_ONLY 或 ResultSet.CONCUR_UPDATABLE 之一

    第一个参数值
    ResultSet.TYPE_FORWARD_ONLY    该常量指示光标只能向前移动的 ResultSet 对象的类型。
    ResultSet.TYPE_SCROLL_INSENSITIVE  该常量指示可滚动但通常不受 ResultSet 底层数据更改影响的 ResultSet 对象的类型。
    ResultSet.TYPE_SCROLL_SENSITIVE  该常量指示可滚动并且通常受 ResultSet 底层数据更改影响的 ResultSet 对象的类型。

    第二个参数值
    ResultSet.CONCUR_READ_ONLY    该常量指示不可以更新的 ResultSet 对象的并发模式。
    ResultSet.CONCUR_UPDATABLE    该常量指示可以更新的 ResultSet 对象的并发模式。

    以上五个值，可以有三种搭配方式
        ResultSet.TYPE_FORWARD_ONLY   ResultSet.CONCUR_READ_ONLY   默认
        ResultSet.TYPE_SCROLL_INSENSITIVE   ResultSet.CONCUR_READ_ONLY 				
        ResultSet.TYPE_SCROLL_SENSITIVE  ResultSet.CONCUR_UPDATABLE


    常用API
        next()：移动到下一行
        previous()：移动到前一行
        absolute(int row)：移动到指定行
        beforeFirst()：移动resultSet的最前面
        afterLast() ：移动到resultSet的最后面
        updateRow() ：更新行数据
```

# 四、dao模式
DAO模式（Data Access Object 数据访问对象）：在持久层通过DAO将数据源操作完全封装起来，业务层通过操作Java对象，完成对数据源操作  
业务层无需知道数据源底层实现，通过java对象操作数据源

DAO模式结构 ：
1. 数据源（MySQL数据库）
2. Business Object 业务层代码，调用DAO完成 对数据源操作
3. DataAccessObject 数据访问对象，持久层DAO程序，封装对数据源增删改查，提供方法参数都是Java对象
4. TransferObject 传输对象（值对象） 业务层通过向数据层传递 TO对象，完成对数据源的增删改查

### 1.使用dao模式登录操作
```
1.web层
    login.jsp  LoginServlet  User
2.service层
    UserService
3.dao层
    UserDao
```

### 2.sql注入
```
由于没有对用户输入进行充分检查，而SQL又是拼接而成，在用户输入参数时，在参数中添加一些SQL关键字，达到改变SQL运行结果的目的，也可以完成恶意攻击。

示例:
	在输入用户名时  tom' or '1'='1
	这时就不会验证密码了。
解决方案:
	PreparedStatement(重点)
	它是一个预处理的Statement，它是java.sql.Statement接口的一个子接口。

总结PreparedStatement使用:
	1.在sql语句中，使用"?"占位
		String sql="select * from user where username=? and password=?";
	2.得到PreparedStatement对象
		PreparedStatement pst=con.prepareStatement(String sql);
	3.对占位符赋值
		pst.setXxx(int index,Xxx obj);
		例如:
			setInt()
			setString();
		参数index,代表的是"?"的序号.注意：从1开始。

	4.执行sql
		DML：  pst.executeUpdate();
		DQL:   pst.executeQuery();
		注意：这两方法无参数

关于PreparedStatement优点:
	1.解决sql注入(具有预处理功能)
	2.不需要在拼sql语句。
```

# 五、JDBC处理大数据
```
mysql中有大数据
    blob 大二进制
        TINYBLOB(255)、BLOB(64kb)、MEDIUMBLOB(16m)和LONGBLOB(4g)
    text(clob) 大文本
        TINYTEXT(255)、TEXT(64kb)、MEDIUMTEXT(16m)和LONGTEXT(4g)

    对于大数据操作，我们一般只有两种 insert  select
    演示1:
        大二进制操作
        create table myblob(
            id int primary key auto_increment,
            content longblob
        )

        向表中插入数据


        问题1:java.lang.AbstractMethodError: com.mysql.jdbc.PreparedStatement.setBinaryStream(ILjava/io/InputStream;)V
            原因:mysql驱动不支持setBinaryStream(int,InputStream);

        修改成
            pst.setBinaryStream(1, fis,file.length());
            原因:因为mysql驱动不支持setBinaryStream(int,InputStream,long);

        解决:
            mysql驱动支持setBinaryStream(int,InputStream,int);


        注意：如果文件比较大，那么需要在my.ini文件中配置
            max_allowed_packet=64M

    总结:
        存
                pst.setBinaryStream(1, fis, (int) (file.length()));
        取
                InputStream is = rs.getBinaryStream("content");
    --------------------------------------------------------------
    演示:存储大文本
        create table mytext(
            id int primary key auto_increment,
            content longtext
        )
    存储
        File file = new File("D:\\java1110\\workspace\\day17_3\\a.txt");
        FileReader fr = new FileReader(file);
        pst.setCharacterStream(1, fr, (int) (file.length()));
    获取:
        Reader r = rs.getCharacterStream("content");
```
# 六、JDBC批处理
```
一次可以执行多条sql语句.

在jdbc中可以执行sql语句的对象有Statement,PreparedStatement，它们都提供批处理.

1.Statement执行批处理
    addBatch(String sql);  将sql语句添加到批处理
    executeBatch(); 执行批处理
    clearBatch();

2.PreparedStatement执行批处理
    addBatch();
    executeBatch();
    clearBatch();

以上两个对象执行批处理区别?
    1.Statement它更适合执行不同sql的批处理。它没有提供预处理功能，性能比较低。		
    2.PreparedStatement它适合执行相同sql的批处理，它提供了预处理功能，性能比较高。

注意;mysql默认情况下，批处理中的预处理功能没有开启，需要开启
    1.在 url下添加参数
        url=jdbc:mysql:///day17?useServerPrepStmts=true&cachePrepStmts=true&rewriteBatchedStatements=true
    2.注意驱动版本
        Mysql驱动要使用mysql-connector-java-5.1.13以上
```
