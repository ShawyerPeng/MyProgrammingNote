# 基本操作
`mysql -u root -p` 不需要输入密码.就可以进入.  
`mysqld --skip-grant-tables` 开启一个mysql服务,不需要进行认证.  
`show databases` 查看数据库
`use mysql` 使用mysql数据库
`update user set password=password('root') WHERE user='root'` 修改密码

# 一、数据库
1. 创建数据库：`create database 数据名称;`  
`create database 数据库名称 character set 编码 collate 校对规则;`
2. 查看数据库：`show databases;`
3. 查询数据库的定义：`show create database 数据库;`
4. 删除数据库：`drop database 数据库名称;`  
5. 修改数据库：`alter database 数据库 character set 编码 collate 校对规则;`
6. 切换数据库：`use 数据库;`
7. 查看当前使用的数据库：`select database();`

# 二、表
1. 创建表：
    ```
    create table 表名(
                    字段1 类型(长度) 约束,
                    字段2 类型(长度) 约束,
                    字段3 类型(长度) 约束,
                    字段4 类型(长度) 约束
    );
    ```

    注意：  
    * 表名小括号，后面要有分号。
    * 每一行字段后面要有逗号，但是最后一行没有逗号。
    * 数据的类型后面有长度，如果是字符串类型，长度必须加。如果其他类型可以不加。默认长度。int 默认长度11
    * 数据的类型  

                字符串型
                VARCHAR、CHAR
                    * varchar和char区别：  
                        * varchar（经常使用）：长度可变。name varchar(8)存入数据hello，但如果存helloworld报错
                        * char：长度不可变的。   name char(8) 存入的数据hello，如果不够用空格补全。

                大数据类型（一般不用）
                BLOB、TEXT
                    BLOB：二进制文件
                    TEXT：字符

                数值型
                TINYINT 、SMALLINT、INT、BIGINT、FLOAT、DOUBLE

                逻辑性 对应boolean
                BIT

                日期型
                DATE、TIME、DATETIME、TIMESTAMP
                    * date	只包含日期
                    * time	只包含时分秒
                    * datetime和timestamp包含日期和时分秒区别：
                    * datetime需要手动录入时间。
                    * timestamp不传入数据，默认选择当前系统时间。
2. 查看表的信息：`desc 表名`
3. 查看当前库内所有表名：`show tables;`
4. 查看建表语句和字符集：`show create table 表名;`
5. 约束（单表）：
    ```
    * 主键约束（*****）
            * 标识标记该条记录。	通过pramary key声明主键。（默认唯一、非空）
            * auto_increment	数据库维护主键。自动增长。

            * 唯一约束
                * 值是唯一的。使用unique声明

            * 非空约束
                * 值不能为空	not null

            * 创建新的标签employee2，把约束加上。
                create table employee2(
                    id int primary key auto_increment,
                    name varchar(20) unique not null,
                    gender varchar(10) not null,
                    birthday date not null,
                    entry_date date not null,
                    job varchar(100) not null,
                    salary double not null,
                    resume text not null
                );
    ```
6. 删除表：`drop table employee2;`
7. 修改表：
```
    alter table 表名 add 字段 类型(长度) 约束;	 				-- 添加字段add
    alter table 表名 drop 字段;									 -- 删除字段drop
    alter table 表名 modify 字段 类型(长度) 约束;				    -- 修改类型或者约束modify
    alter table 表名 change 旧字段 新字段 类型(长度) 约束		     -- 修改字段的名称change
    rename table 表名 to 新表名;								    -- 修改表名to
    alter table 表名 character set utf8;						   -- 修改字符集character
```

# 三、数据
1. 添加数据：`insert into 表名 (字段1,字段2,字段3..) values(值1,值2,值3...);` 有几列就插入多少的值  
`insert into 表名 values(值1,值2,值3...);` 插入所有的列  
2. 修改数据：`update 表名 set 字段=值,字段=值... [where ]`如果没有where条件，默认更新所有的记录
3. 删除数据：`delete from 表名 [where ];` 删除数据  
`truncate 表名;` 删除所有的数据
truncate 和 delete的区别：truncate删除数据，先删除整个表。再创建一个新的空的表。（效率）/// delete删除数据，一条一条删除的。
4. 查询语句：`select * from 表名` 查询所有（字段）  
`select 字段名1,字段名2,字段名3 from 表名;` 显示查询字段名  
`select distinct 字段名 from 表名;` 去除重复的数据

        * 查询的列可以运算
        * 可以使用别名：使用as 别名		并且as可以省略。
            练习：
                在所有学生分数上加10分特长分。
                    select name,math+10,english+10,chinese+10 from stu;
                统计每个学生的总分。
                    select name,math+english+chinese from stu;

                使用别名表示学生分数。
                select name,(math+english+chinese) as sum from stu;
        * 使用where条件过滤
            查询姓名为班长的学生成绩
                select * from stu where name='班长';

            查询英语成绩大于90分的同学
                select name,english from stu where english < 15;

            查询总分大于200分的所有同学
                select name,math+english+chinese from stu where (math+english+chinese) > 200;

        * 常用的符号
            >   <   <=   >=   =    <>（不等于）
            in(范围内取内容)
            like		-- 模糊查询		写法：like '张_或者%';	_和%区别：占位符。_只一个%可以有多个
                                                %的写法	like '%张';		结果XXX张
                                                        like '张%';		结果张XXX
                                                        like '%张%';	只要有张就行
            is null		-- 判断是否为null
            and			-- 并且
            or			-- 或者
            not			-- 不成立
        * 练习
            查询英语分数在 80－90之间的同学。
                select * from stu where english >80 and english <90;
                select * from stu where english between 80 and 90;

            查询数学分数为18,78,46的同学。（in）
                select * from stu where math in(18,78,46);

            查询所有姓班的学生成绩。
                select * from stu where name like '班%';
            查询数学分>80，语文分>80的同学。
                select * from stu where math >80 or chinese > 80;

5. 排序
    出现select的语句末尾 order by 升序默认的(asc)/降序(desc)
    ```
    count       获取数量
    sum         求和（忽略null值）
    avg         平均数
    max         最大值
    min         最小值
    group by    分组（一起使用）.条件过滤需要是having，不能使用where
    ```
    小结：select 语句 ： S-F-W-G-H-O 组合 select ... from ... where ... group by... having... order by ... ; 顺序不能改变
