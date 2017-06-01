# 安装
## APT Repository
[Install MySQL on Ubuntu 16.04 (Xenial Xerus)](https://howtoprogram.xyz/2016/09/22/install-mysql-ubuntu-16-04/)  
```
wget https://repo.mysql.com//mysql-apt-config_0.8.5-1_all.deb
sudo dpkg -i mysql-apt-config_0.8.5-1_all.deb
sudo apt-get update
sudo apt-get install mysql-server mysql-client
```

## dpkg

## apt


# Create Table
1. create
```SQL
CREATE TABLE countries(
);
```

```SQL
CREATE TABLE IF NOT EXISTS countries (
);
```

只会完整复制原表的建表语句，但不会复制数据
```SQL
CREATE TABLE IF NOT EXISTS dup_countries LIKE countries;
```

将原表中的数据完整复制一份，但表结构中的索引会丢失
```SQL
CREATE TABLE IF NOT EXISTS dup_countries AS SELECT * FROM countries;
```

2. 约束
```SQL
NOT NULL：强制列不接受 NULL 值
UNIQUE：唯一标识数据库表中的每条记录
PRIMARY KEY：唯一标识数据库表中的每条记录，主键必须包含唯一的值且主键列不能包含 NULL 值。每个表都应该有一个主键，并且每个表只能有一个主键。
FOREIGN KEY：一个表中的 FOREIGN KEY 指向另一个表中的 PRIMARY KEY
CHECK：限制列中的值的范围。
DEFAULT：向列中插入默认值。
AUTO_INCREMENT：自增值
```

```SQL
CHECK(MAX_SALARY<=25000)
CHECK(COUNTRY_NAME IN('Italy','India','China')),
CHECK (END_DATE LIKE '--/--/----'),
```

```SQL
FOREIGN KEY (job_id) REFERENCES jobs(job_id)
FOREIGN KEY(DEPARTMENT_ID,MANAGER_ID) REFERENCES departments(DEPARTMENT_ID,MANAGER_ID)

FOREIGN KEY(JOB_ID) REFERENCES jobs(JOB_ID) ON UPDATE CASCADE ON DELETE RESTRICT
FOREIGN KEY(JOB_ID) REFERENCES jobs(JOB_ID) ON DELETE CASCADE ON UPDATE RESTRICT

FOREIGN KEY(JOB_ID) REFERENCES jobs(JOB_ID) ON DELETE SET NULL ON UPDATE SET NULL

FOREIGN KEY(JOB_ID) REFERENCES jobs(JOB_ID) ON DELETE NO ACTIONON UPDATE NO ACTION
```

# insert
```SQL
INSERT INTO countries VALUES('C1','India',1001);
INSERT INTO countries (country_id,country_name) VALUES('C1','India');
INSERT INTO countries VALUES('C0001','India',1001),('C0002','USA',1007),('C0003','UK',1003);
INSERT INTO countries SELECT * FROM country_new;
```

```SQL

```

```SQL

```










# 参考资料
[重设 MySQL root 密码](http://hellojinjie.com/2013/12/15/%E9%87%8D%E8%AE%BE-mysql-root-%E5%AF%86%E7%A0%81/)  























```SQL

```
