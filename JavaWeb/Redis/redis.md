# 下载
```
wget http://download.redis.io/releases/redis-3.2.9.tar.gz
tar xzf redis-3.2.9.tar.gz
cd redis-3.2.9
make
```

# 运行
```
src/redis-server        # 启动redis
src/redis-cli           # 交互式redis
```

`redis-server.exe redis.conf`

# Jedis
```xml
<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.9.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-pool2 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.4.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.data/spring-data-commons -->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-commons</artifactId>
    <version>1.13.3.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.springframework.data/spring-data-redis -->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version>1.8.3.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>21.0</version>
</dependency>
```

## keys
```java
// 判断key999键是否存在
shardedJedis.exists("key999");
// 新增键值对
shardedJedis.set("key001", "value001");
// 删除键值对
jedis.del("key002");
// 设置key001的过期时间为5秒
jedis.expire("key001", 5);
// 查看key001的剩余生存时间
jedis.ttl("key001");
// 移除key001的生存时间
jedis.persist("key001");
// 查看key所储存的值的类型
jedis.type("key001");
// 修改键名
jedis.rename("key6", "key0");
// 将当前db的key移动到给定的db当中
jedis.move("foo", 1);

// 打印系统中所有键
Set<String> keys = jedis.keys("*");
Iterator<String> it = keys.iterator();
while(it.hasNext()){
    String key = it.next();
    System.out.println(key);
}

// 清空库中所有数据
jedis.flushDB();
```

## Strings
```java
// 新增key
jedis.set("key001","value001");
// 获取值
System.out.println(jedis.get("key001"));
// 删除键值对
jedis.del("key001")
// 改
jedis.set("key001","value001-update");
// 追加
jedis.append("key001","+appendString")

// mset,mget同时新增，修改，查询多个键值对
jedis.mset("key201","value201", "key202","value202", "key203","value203", "key204","value204");
jedis.del(new String[]{"key201", "key202"})
jedis.mget("key201","key202","key203","key204");

// 新增键值对时防止覆盖原先值（当key301存在时，尝试新增key301将失败）
shardedJedis.setnx("key301", "value301");
// 设置key的有效期，并存储数据（新增key303，并指定过期时间为2秒）
shardedJedis.setex("key303", 2, "key303-2second")

// 获取原值，更新为新值一步完成
shardedJedis.getSet("key302", "value302-after-getset");

// 获取子串
shardedJedis.getrange("key302", 5, 7);
```

## Lists
```java
// 新增
shardedJedis.lpush("stringlists", "vector");
// 所有元素
shardedJedis.lrange("stringlists", 0, -1);
// 成功删除指定元素个数（删除列表指定的值 ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈）
shardedJedis.lrem("stringlists", 2, "vector");
// 删除下标0-3区间之外的元素（删除区间以外的数据）
shardedJedis.ltrim("stringlists", 0, 3);
// 出栈元素（列表元素出栈）
shardedJedis.lpop("stringlists");

// 修改列表中指定下标的值
shardedJedis.lset("stringlists", 0, "hello list!");
// 数组长度
shardedJedis.llen("stringlists");

// 排序
SortingParams sortingParameters = new SortingParams();
sortingParameters.alpha();
sortingParameters.limit(0, 3);
System.out.println("返回排序后的结果-stringlists："+shardedJedis.sort("stringlists",sortingParameters));

// 获取下标为2的元素（获取列表指定下标的值）
shardedJedis.lindex("stringlists", 2);
```

## Sets
```java
// 向sets集合中加入元素
jedis.sadd("sets", "element001");
// 查看sets集合中的所有元素
jedis.smembers("sets");

// 判断element001是否在集合sets中
jedis.sismember("sets", "element001");

// 循环查询获取sets中的每个元素
Set<String> set = jedis.smembers("sets");
Iterator<String> it=set.iterator() ;
while(it.hasNext()){
    Object obj=it.next();
    System.out.println(obj);
}

// 交集运算
jedis.sinter("sets1", "sets2");
// 并集运算
jedis.sunion("sets1", "sets2");
// 差集运算（set1中有，set2中没有的元素）
jedis.sdiff("sets1", "sets2");
```

## Sorted sets
```java
// zset中添加元素
shardedJedis.zadd("zset", 7.0, "element001");
// zset集合中的所有元素 //按照权重值排序
shardedJedis.zrange("zset", 0, -1);

// zset中删除元素
shardedJedis.zrem("zset", "element002");
// zset集合中的元素个数
shardedJedis.zcard("zset");
// zset集合中权重某个范围内（1.0——5.0）元素个数
shardedJedis.zcount("zset", 1.0, 5.0);
// 查看zset集合中element004的权重
shardedJedis.zscore("zset", "element004");
// 查看下标1到2范围内的元素值
shardedJedis.zrange("zset", 1, 2);
```

## Hashes
```java
// hashs中添加键值对
shardedJedis.hset("hashs", "key001", "value001");
// 新增key004和4的整型键值对
shardedJedis.hincrBy("hashs", "key004", 4l);

// hashs中删除key002键值对
shardedJedis.hdel("hashs", "key002");

// key004整型键值的值增加100
shardedJedis.hincrBy("hashs", "key004", 100l);

// 判断key003是否存在
shardedJedis.hexists("hashs", "key003");
// 获取key004对应的值
shardedJedis.hget("hashs", "key004");
// 批量获取key001和key003对应的值
shardedJedis.hmget("hashs", "key001", "key003");
// 获取hashs中所有的key
shardedJedis.hkeys("hashs");
// 获取hashs中所有的value
shardedJedis.hvals("hashs");
```

## Bit arrays
```java

```

## HyperLogLogs
```java

```

# Spring Cache
```java

```