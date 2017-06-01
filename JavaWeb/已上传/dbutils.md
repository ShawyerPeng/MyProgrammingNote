# 一、元数据
元数据(metaData)：指数据库中 库、表、列的定义信息
1. DataBaseMetaData 数据库元数据（了解）
```
问题:怎样获取一个DataBaseMetaData?
    Connection接口中定义了一个方法 getMetaData();

问题:常用API
    String driverName = dmd.getDriverName(); //获取驱动名称:getDriverName
    System.out.println(driverName);

    String userName = dmd.getUserName();    //获取用户名:getUserName
    System.out.println(userName);

    String url = dmd.getURL();              //获取url:getURL
    System.out.println(url);

    String databaseProductName = dmd.getDatabaseProductName(); //获取数据库名称:getDatabaseProductName
    System.out.println(databaseProductName);

    String databaseProductVersion = dmd.getDatabaseProductVersion();//获取数据库版本:getDatabaseProductVersion
    System.out.println(databaseProductVersion);

    ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException //获取表中主键相关描述
        每个主键列描述都有以下列：
        TABLE_CAT String   => 表类别（可为null）
        TABLE_SCHEM String => 表模式（可为null）
        TABLE_NAME String  => 表名称
        COLUMN_NAME String => 列名称
        KEY_SEQ short      => 主键中的序列号（值 1 表示主键中的第一列，值 2 表示主键中的第二列）。
        PK_NAME String     => 主键的名称（可为 null）
```

2. ParameterMetaData 参数元数据
```
参数元数据主要用于获取:sql语句中占位符的相关信息.

问题:怎样获取ParameterMetaData?
    在PreparedStatement中有一个方法getParameterMetaData可以获取.

问题:怎样使用?
    int count = pmd.getParameterCount(); // 获取参数 个数
    System.out.println(count);

    String type1 = pmd.getParameterTypeName(1);//获取参数的类型
    System.out.println(type1);

    注意：在获取参数类型时会产生异常
        java.sql.SQLException: Parameter metadata not available for the given statement
    解决方案:
        在url后添加参数
        jdbc:mysql:///day18?generateSimpleParameterMetadata=true
    添加这个参数后，我们在获取，它的结果也是varchar,原因:是mysql驱动的支持问题。
```
3. ResultSetMetaData 结果集元数据
```
问题:怎样获取结果集元数据?
    可以通过ResultSet的getMetaData()方法获取.

问题:怎样使用?
    System.out.println(rsmd.getColumnCount());      //获取结果集中列数量
    System.out.println(rsmd.getColumnName(2));      //获取结果集中指定列的名称.
    System.out.println(rsmd.getColumnTypeName(3));  //获取结果集中指定列的类型。
```

# 二、dbutils工具
它就是一个简单的jdbc封装工具.使用dbutils可以简化操作.要使用dbutils需要导入jar包.

dbutils核心：
1. `QueryRunner`类
    它是用于执行sql语句的类。
    1. `query` 用于执行select
    2. `update` 用于执行update delete insert
    3. `batch` 批处理
2. `ResultSetHandler`接口
    用于定义结果集的封装				
    它提供九个实现类，可以进行不同的封装。
3. `DbUtils`类
    它提供关于关闭资源`close`以及事务`rollback`,`commit`操作。

Dbutlis详解：
1. `QueryRunner`类
    1. QueryRunner怎样获取
        * `new QueryRunner()`
            如果是使用这种构造创建的QueryRunner,它的事务是手动控制。
            ```java
            String sql = "select * from account where id>? and name=?";
            QueryRunner runner = new QueryRunner(); //事务手动控制

            Connection con = DataSourceUtils.getConnection();
            // con.setAutoCommit(false);
            List<Account> list = runner.query(con,sql,new BeanListHandler<Account>(Account.class),2,"ccc");
            // con.rollback();

            System.out.println(list);
            ```
        * `new QueryRunner(DataSource ds);`
            如果是使用这种构造，它的事务是自动事务，简单说，一条sql一个事务。
            ```java
            String sql = "select * from account where id=?";
            QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());  //自动事务
            List<Account> list = runner.query(sql,new BeanListHandler<Account>(Account.class),2);

            System.out.println(list);
            ```

    2. QueryRunner中的三个核心方法
        `query`
        `update`
        `batch`
        对于上述三个方法，它们提供很多重载。
        如果QueryRunner在创建时，没有传递DataSource参数，那么在使用query,update,batch方法时，要传递Connection参数
        如果QueryRunner在创建时，传递了Dataource参数，那么在使用query,update,batch方法时，不需要传递Connection参数。

        总结:  
        怎样配套使用:  
        ```java
        QueryRunner runner=new QueryRunner();
        runner.query(Connection,sql,ResultSetHandler,Object... param);
        runner.update(Connection,sql,Object...param);
        runner.batch(Connection con,sql,Object[][] objs);

        QueryRunner runner=new QueryRunner(DataSource ds);
        runner.query(sql,ResultSetHandler,Object... param);
        runner.update(sql,Object...param);
        runner.batch(sql,Object[][] objs);
        ```
2. `ResultSetHandler`接口
    用于封装结果集.
    ```java
    // 将结果封装到一个javaBean
    String sql = "select * from account where id=?";
    QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
    Account a = runner.query(sql,
                            new ResultSetHandler<Account>() {
                                //ResultSetHandler上的泛型就是我们执行query方法后得到的结果.

                                //重写handle方法，在这个方法中确定，怎样将结果集封装。
                                public Account handle(ResultSet rs) throws SQLException {
                                    Account a = null;
                                    if (rs.next()) {
                                        a = new Account();
                                        a.setId(rs.getInt("id"));
                                        a.setName(rs.getString("name"));
                                        a.setMoney(rs.getDouble("money"));
                                    }
                                    return a;
                                }
                            }, 2);
    System.out.println(a);
    ```

### 模仿QueryRunner实现
```java
public class MyQueryRunner {
	private DataSource ds;

	public MyQueryRunner() {
		super();
	}

	public MyQueryRunner(DataSource ds) {
		this.ds = ds;
	}

	// 执行select操作
	public <T> T query(Connection con, String sql, MyResultSetHandler<T> mrs,
			Object... params) throws SQLException {

		PreparedStatement pst = con.prepareStatement(sql); // 得到一个预处理的Statement.
		// 问题:sql语句中可能存在参数，需要对参数赋值。

		ParameterMetaData pmd = pst.getParameterMetaData();
		// 可以得到有几个参数
		int count = pmd.getParameterCount();
		for (int i = 1; i <= count; i++) {
			pst.setObject(i, params[i - 1]);
		}

		ResultSet rs = pst.executeQuery(); // 得到了结果集，要将结果集封装成用户想要的对象，但是，工具不可能知道用户需求。

		return mrs.handle(rs);
	}

	// 执行update操作
	public int update(Connection con, String sql, Object... params) throws SQLException {
		PreparedStatement pst = con.prepareStatement(sql); // 得到一个预处理的Statement.
		// 问题:sql语句中可能存在参数，需要对参数赋值。

		ParameterMetaData pmd = pst.getParameterMetaData();
		// 可以得到有几个参数
		int count = pmd.getParameterCount();
		for (int i = 1; i <= count; i++) {
			pst.setObject(i, params[i - 1]);
		}

		int row = pst.executeUpdate();
		// 关闭资源
		pst.close();
		return row;
	}

	public int update(String sql, Object... params) throws SQLException {
		Connection con = ds.getConnection();
		PreparedStatement pst = con.prepareStatement(sql); // 得到一个预处理的Statement.
		// 问题:sql语句中可能存在参数，需要对参数赋值。

		ParameterMetaData pmd = pst.getParameterMetaData();
		// 可以得到有几个参数
		int count = pmd.getParameterCount();
		for (int i = 1; i <= count; i++) {
			pst.setObject(i, params[i - 1]);
		}

		int row = pst.executeUpdate();
		// 关闭资源
		pst.close();
		con.close();
		return row;
	}
}
```

### ResulSetHandler九个实现类介绍
`ArrayHandler`, 将结果集中`第一条`记录封装到Object[],数组中的每一个元素就是记录中的字段值。
`ArrayListHandler`, 将结果集中`每一条`记录封装到Object[],数组中的每一个元素就是记录中的字段值。在将这些数组装入到List集合

`BeanHandler`（重点）, 将结果集中`第一条`记录封装到一个`javaBean`中。
`BeanListHandler`(重点), 将结果集中`每一条`记录封装到`javaBean`中，在将javaBean封装到`List集合`.

`ColumnListHandler`, 将结果集中指定列的值封装到`List集合`.(无参默认第一列)

`MapHandler`, 将结果集中`第一条`记录封装到`Map集合`中，集合的key就是字段名称，value就是字段值
`MapListHandler`, 将结果集中`每一条`记录封装到`Map集合`中，集合的key就是字段名称，value就是字段值，再将这些Map封装到`List集合`

`KeyedHandler`,在使用`指定的列的值`做为一个`Map集合的key`,值为每一条记录的Map集合封装。
`ScalarHandler` 进行单值查询 `select count(*) from account;`
