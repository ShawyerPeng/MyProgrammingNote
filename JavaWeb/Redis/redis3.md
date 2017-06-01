# 一、特点
redis是开源BSD许可高级的key-value存储系统(NoSQL)  
可以用来存储字符串，哈希结构，链表，集合，因此，常用来提供数据结构服务  

redis和memcached相比的独特之处：  
1、redis可以用来做存储(storage),而memcached是用来做缓存(cache)。这个特点主要因为其有持久化功能。  
2、redis中存储的数据有多种结构，而memcached存储的数据只有一种类型“字符串”。  
`redis-server.exe redis.windows.conf`
`redis-cli.exe -h 127.0.0.1 -p 6379`

# 安装
用如下命令下载、解压、编译redis：
```
cd /src   # cd /usr/local/src
wget http://download.redis.io/releases/redis-3.2.8.tar.gz
tar xzf redis-3.2.8.tar.gz
cd redis-3.2.8
make
```

为了将Redis二进制文件安装到/usr/local/bin，输入以下命令：
```
make install
```

二进制文件已经被编译到src目录下，用如下命令运行Redis：
```
src/redis-server
```

通过内置redis-cli使用redis：
```
src/redis-cli
```

# 主从备份
```
例如IP1和IP2都安装了redis,端口分别是6379和6380.  
如果想配置IP1为主备份,IP2为从备份,配置过程如下:  
在IP2的redis.conf配置文件中添加slaveof IP1 6379,即IP2为IP1的从.  
注意:默认情况下从redis不支持写操作,只支持读操作,看如下操作:  
主redis:  
IP1:6379> get "username"        第一步  
"IluckySi"  
IP1:6379> set "username" "test" 第四步  
OK  
IP1:6379> get "username"        第五步  
"test"  
IP1:6379>   
  
从redis:  
IP2:6380> get "username"        第二步  
"IluckySi"  
IP2:6380> set "username" "test" 第三步  
(error) READONLY You can't write against a read only slave.  
IP2:6380> get "username"        第六步  
"test"  
42.121.91.238:6380>   
通过如上测试,我们的redis主从备份配置生效了.  
主从之间切换可以参考此篇博客:http://blog.sina.com.cn/s/blog_67196ddc0101h8v0.html 
```

# 常见命令
`quit`：退出
`killall redis-server`：杀死进程
`redis-server redis/redis.conf`：开启进程
`redis-server -v`：查看版本信息
`redis-cli shutdown`：安全地关闭   `redis-cli -h 127.0.0.1 -p 6379 shutdown 指定redis 主机 和监听端口`
检查`ps -ef | grep redis`

# 与java配合使用
参考：[Java中使用Jedis操作Redis](http://www.cnblogs.com/liuling/p/2014-4-19-04.html)
`jdies.jar`和`commons-pool.jar`

```java
Jedis jedis = new Jedis("localhost:6379");  // 连接redis服务器
jedis.auth("admin");                        // 权限认证
jedis.set("foo", "bar");                    // 添加数据
String value = jedis.get("foo");            // 执行结果
```

Jedis Cluster：
```java
Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//Jedis Cluster will attempt to discover cluster nodes automatically
jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7379));
JedisCluster jc = new JedisCluster(jedisClusterNodes);
jc.set("foo", "bar");
String value = jc.get("foo");
```

# 使用Spring + Jedis集成Redis
参考：[使用Spring + Jedis集成Redis](https://my.oschina.net/u/866380/blog/521658)  
## 配置资源池
如果你有多个数据源需要通过`<context:property-placeholder`管理，且不愿意放在一个配置文件里，那么一定要加上ignore-unresolvable=“true"
```xml
<!-- 0. 引入jedis配置文件 -->
<context:property-placeholder location="classpath:conf/properties/redis.properties" ignore-unresolvable="true" />

<!-- 1. 连接池配置 -->
<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
    <!-- 最大连接数 -->
    <property name="maxTotal" value="${redis.maxTotal}" />
    <!-- 最大空闲连接数 -->
    <property name="maxIdle" value="${redis.maxIdle}" />
    <!-- 每次释放连接的最大数目 -->
    <property name="numTestsPerEvictionRun" value="${redis.numTestsPerEvictionRun}" />
    <!-- 释放连接的扫描间隔（毫秒） -->
    <property name="timeBetweenEvictionRunsMillis" value="${redis.timeBetweenEvictionRunsMillis}" />
    <!-- 连接最小空闲时间 -->
    <property name="minEvictableIdleTimeMillis" value="${redis.minEvictableIdleTimeMillis}" />
    <!-- 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放 -->
    <property name="softMinEvictableIdleTimeMillis" value="${redis.softMinEvictableIdleTimeMillis}" />
    <!-- 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1 -->
    <property name="maxWaitMillis" value="${redis.maxWait}" />
    <!-- 在获取连接的时候检查有效性, 默认false -->
    <property name="testOnBorrow" value="${redis.testOnBorrow}" />
    <!-- 在空闲时检查有效性, 默认false -->
    <property name="testWhileIdle" value="${redis.testWhileIdle}" />
    <!-- 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true -->
    <property name="blockWhenExhausted" value="${redis.blockWhenExhausted}" />  
</bean>

<!-- 2. redis单节点数据库连接配置 -->
<bean id="jedisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
    <property name="hostName" value="${redis.host}" />
    <property name="port" value="${redis.port}" />
    <property name="password" value="${redis.password}" />
    <property name="timeout" value="${redis.timeout}" />
    <property name="poolConfig" ref="jedisPoolConfig" />
</bean>

<!-- 2. redisTemplate配置，redisTemplate是对Jedis的对redis操作的扩展，有更多的操作，封装使操作更便捷 -->
<bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">
    <property name="connectionFactory" ref="jedisConnectionFactory" />
</bean>







<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
    <constructor-arg index="0" ref="jedisPoolConfig" />
    <constructor-arg index="1" value="${redis.host}" />
    <constructor-arg index="2" value="${redis.port}" type="int" />
</bean>

<!-- jedis集群部署 -->
<bean id="shardedJedisPool" class="redis.clients.jedis.ShardedJedisPool"  scope="singleton">
    <constructor-arg index="0" ref="jedisPoolConfig" />
    <constructor-arg index="1">
        <list>
            <bean class="redis.clients.jedis.JedisShardInfo">
                <constructor-arg name="host" value="${redis.uri1}" />
            </bean>
            <bean class="redis.clients.jedis.JedisShardInfo">
                <constructor-arg name="host" value="${redis.uri2}" />
            </bean>
            <bean class="redis.clients.jedis.JedisShardInfo">
                <constructor-arg name="host" value="${redis.uri3}" />
            </bean>
        </list>
    </constructor-arg>
</bean>
```

## 准备`redis.properties`
这里要注意redis.uri的格式：`redis://:[密码]@[服务器地址]:[端口]/[db index]`
```
redis.pool.maxActive=200
redis.pool.maxIdle=50
redis.pool.minIdle=10
redis.pool.maxWaitMillis=20000
redis.pool.maxWait=300
redis.uri = redis:/:password@127.0.0.1:6379/0
redis.timeout=30000
```

```
redis.host=120.77.X.XXX     # redis服务器IP
redis.port=6379             # 端口地址
redis.password=123456       # 服务器密码
redis.maxTotal=10000        # 最大连接数
redis.maxIdle=100           # 最大空闲
redis.maxWait=1000          # 最大等待
redis.testOnBorrow=true     # 在获取连接的时候检查有效性, 默认false
redis.timeout=10000         # 超时(毫秒)
redis.numTestsPerEvictionRun=1024   # 每次释放连接的最大数目
redis.timeBetweenEvictionRunsMillis=30000   # 释放连接的扫描间隔（毫秒）
redis.minEvictableIdleTimeMillis=1800000    # 连接最小空闲时间
redis.softMinEvictableIdleTimeMillis=10000  # 连接空闲多久后释放
redis.testWhileIdle=true                    # 在空闲时检查有效性
redis.blockWhenExhausted=false              # 连接耗尽时是否阻塞

targetNames=            # 不需要加入缓存的类
methodNames=            # 不需要缓存的方法

#设置缓存失效时间
RecordManager= 60
SetRecordManager= 60
defaultCacheExpireTime=3600

cache.capacity =10000
```

## 添加到spring的总配置文件applicationContext.xml中
```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE" />
    <property name="ignoreResourceNotFound" value="true" />
    <property name="locations">
        <list>
            <value>classpath*:/META-INF/config/redis.properties</value>
        </list>
    </property>
</bean>

<import resource="spring-redis.xml" />
```

## 将spring-redis.xml加入web.xml的context中
如果你有多个数据源通过spring管理（如mysql），则同时加载，用逗号隔开
```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:conf/spring-mybatis.xml, classpath:config/spring-redis.xml</param-value>
</context-param>
```

## 单机模式
```java
public class RedisAPI {
    private static JedisPool pool = null;
    
    // 构建redis连接池
    public static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxActive(500);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWait(1000 * 100);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            pool = new JedisPool(config, "192.168.2.191", 8888);
        }
        return pool;
    }
    
    // 返还到连接池
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResourceObject(redis);
        }
    }
    
    // 获取数据

    public static String get(String key){
        String value = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return value;
    }
}
```

## 分布式模式(连接池)
推荐大家使用统一的类来管理Jedis实例的生成和回收：  
`JedisDataSourceImpl.java`  
```java
@Repository("jedisDS")
public class JedisDataSourceImpl implements JedisDataSource {
    private static final Logger LOG = LoggerFactory.getLogger(JedisDataSourceImpl.class);
    
    @Autowired
    private ShardedJedisPool shardedJedisPool;      // 切片连接池，注意与JedisPool的区别
    
    @Override
    public ShardedJedis getRedisClient() {
        ShardedJedis shardJedis = null;
        try {
            shardJedis = shardedJedisPool.getResource();
            return shardJedis;
        } catch (Exception e) {
            LOG.error("[JedisDS] getRedisClent error:" + e.getMessage());
            if (null != shardJedis)
                shardJedis.close();
        }
        return null;
    }

    @Override
    public void returnResource(ShardedJedis shardedJedis) {
        shardedJedis.close();
    }

    @Override
    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        shardedJedis.close();
    }
}
```

```java
public class ShardJedisTest {

    private ShardedJedisPool sharedPool;

    @Before
    public void initJedis(){
        JedisPoolConfig config =new JedisPoolConfig();  //Jedis池配置
        config.setTestOnBorrow(true);
        String hostA = "127.0.0.1";
        int portA = 6381;
        String hostB = "127.0.0.1";
        int portB = 6382;
        List<JedisShardInfo> jdsInfoList =new ArrayList<JedisShardInfo>(2);
        JedisShardInfo infoA = new JedisShardInfo(hostA, portA);
        infoA.setPassword("redis.360buy");
        JedisShardInfo infoB = new JedisShardInfo(hostB, portB);
        infoB.setPassword("redis.360buy");
        jdsInfoList.add(infoA);
        jdsInfoList.add(infoB);
        // 传入连接池配置、分布式redis服务器主机信息、分片规则（存储到哪台redis服务器）
        sharedPool =new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH,
Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    @Test
    public void testSetKV() throws InterruptedException {
        try {
            for (int i=0;i<50;i++){
                String key = "test"+i;
                ShardedJedis jedisClient = sharedPool.getResource();    // 获取连接资源
                System.out.println(key + ":"+ jedisClient.getShard(key).getClient().getHost() + ":" + jedisClient.getShard(key).getClient().getPort());
                System.out.println(jedisClient.set(key,Math.random() + ""));
                jedisClient.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        sharedPool.destroy();   // 应用退出时，关闭连接池
    }
}
```

具体的Jedis操作类：
`RedisClientTemplate.java`
```java
@Repository("redisClientTemplate")
public class RedisClientTemplate {
    private static final Logger log = LoggerFactory.getLogger(RedisClientTemplate.class);

    @Autowired
    private JedisDataSource redisDataSource;

    public void disconnect() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        shardedJedis.disconnect();
    }

    /**
     * 设置单个值
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        String result = null;

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }
```

# 二、redis键值操作



# 三、redis数据类型[string,link,set,order set,hash]
## 1.string
`APPEND key value`：如果该Key已经存在，APPEND命令将参数Value的数据追加到已存在Value的末尾。如果该Key不存在，APPEND命令将会创建一个新的Key/Value。	追加后Value的长度。

`DECR key`：将指定Key的Value原子性的递减1。如果该Key不存在，其初始值为0，在decr之后其值为-1。如果Value的值不能转换为整型值，如Hello，该操作将执行失败并返回相应的错误信息。注意：该操作的取值范围是64位有符号整型。	递减后的Value值。
`INCR key`：将指定Key的Value原子性的递增1。如果该Key不存在，其初始值为0，在incr之后其值为1。如果Value的值不能转换为整型值，如Hello，该操作将执行失败并返回相应的错误信息。注意：该操作的取值范围是64位有符号整型。 	递增后的Value值。 
`DECRBY key decrement`：将指定Key的Value原子性的减少decrement。如果该Key不存在，其初始值为0，在decrby之后其值为-decrement。如果Value的值不能转换为整型值，如Hello，该操作将执行失败并返回相应的错误信息。注意：该操作的取值范围是64位有符号整型。 	减少后的Value值。
`INCRBY key increment`：将指定Key的Value原子性的增加increment。如果该Key不存在，其初始值为0，在incrby之后其值为increment。如果Value的值不能转换为整型值，如Hello，该操作将执行失败并返回相应的错误信息。注意：该操作的取值范围是64位有符号整型。 	增加后的Value值。

`GET key`：获取指定Key的Value。如果与该Key关联的Value不是string类型，Redis将返回错误信息，因为GET命令只能用于获取string Value。 	与该Key相关的Value，如果该Key不存在，返回nil。
`SET key value`：设定该Key持有指定的字符串Value，如果该Key已经存在，则覆盖其原有值。	总是返回"OK"。
`GETSET key value`：原子性的设置该Key为指定的Value，同时返回该Key的原有值。和GET命令一样，该命令也只能处理string Value，否则Redis将给出相关的错误信息。	返回该Key的原有值，如果该Key之前并不存在，则返回nil。
`STRLEN key`：返回指定Key的字符值长度，如果Value不是string类型，Redis将执行失败并给出相关的错误信息。	返回指定Key的Value字符长度，如果该Key不存在，返回0。

`SETEX key seconds value`：原子性完成两个操作，一是设置该Key的值为指定字符串，同时设置该Key在Redis服务器中的存活时间(秒数)。该命令主要应用于Redis被当做Cache服务器使用时。	 
`SETNX key value`：如果指定的Key不存在，则设定该Key持有指定字符串Value，此时其效果等价于SET命令。相反，如果该Key已经存在，该命令将不做任何操作并返回。	1表示设置成功，否则0。

`SETRANGE key offset value`：替换指定Key的部分字符串值。从offset开始，替换的长度为该命令第三个参数value的字符串长度，其中如果offset的值大于该Key的原有值Value的字符串长度，Redis将会在Value的后面补齐(offset - strlen(value))数量的0x00，之后再追加新值。如果该键不存在，该命令会将其原值的长度假设为0，并在其后添补offset个0x00后再追加新值。鉴于字符串Value的最大长度为512M，因此offset的最大值为536870911。最后需要注意的是，如果该命令在执行时致使指定Key的原有值长度增加，这将会导致Redis重新分配足够的内存以容纳替换后的全部字符串，因此就会带来一定的性能折损。 	修改后的字符串Value长度。
`GETRANGE key start end`：如果截取的字符串长度很短，我们可以该命令的时间复杂度视为，否则就是O(N)，这里N表示截取的子字符串长度。该命令在截取子字符串时，将以闭区间的方式同时包含start(0表示第一个字符)和end所在的字符，如果end值超过Value的字符长度，该命令将只是截取从start开始之后所有的字符数据。	子字符串 

`SETBIT key offset value`：设置在指定Offset上BIT的值，该值只能为1或0，在设定后该命令返回该Offset上原有的BIT值。如果指定Key不存在，该命令将创建一个新值，并在指定的Offset上设定参数中的BIT值。如果Offset大于Value的字符长度，Redis将拉长Value值并在指定Offset上设置参数中的BIT值，中间添加的BIT值为0。最后需要说明的是Offset值必须大于0。 	在指定Offset上的BIT原有值。
`GETBIT key offset`：返回在指定Offset上BIT的值，0或1。如果Offset超过string value的长度，该命令将返回0，所以对于空字符串始终返回0。	在指定Offset上的BIT值。 

`MGET key [key ...]`：N表示获取Key的数量。返回所有指定Keys的Values，如果其中某个Key不存在，或者其值不为string类型，该Key的Value将返回nil。	返回一组指定Keys的Values的列表。
`MSET key value [key value ...]`：N表示指定Key的数量。该命令原子性的完成参数中所有key/value的设置操作，其具体行为可以看成是多次迭代执行SET命令。 	该命令不会失败，始终返回OK。  
`MSETNX key value [key value ...]`：N表示指定Key的数量。该命令原子性的完成参数中所有key/value的设置操作，其具体行为可以看成是多次迭代执行SETNX命令。然而这里需要明确说明的是，如果在这一批Keys中有任意一个Key已经存在了，那么该操作将全部回滚，即所有的修改都不会生效。	1表示所有Keys都设置成功，0则表示没有任何Key被修改。

## 2.list
在Redis中，List类型是按照插入顺序排序的字符串链表。和数据结构中的普通链表一样，我们可以在其头部(left)和尾部(right)添加新的元素。在插入时，如果该键并不存在，Redis将为该键创建一个新的链表。与此相反，如果链表中所有的元素均被移除，那么该键也将会被从数据库中删除。List中可以包含的最大元素数量是4294967295。  
从元素插入和删除的效率视角来看，如果我们是在链表的两头插入或删除元素，这将会是非常高效的操作，即使链表中已经存储了百万条记录，该操作也可以在常量时间内完成。然而需要说明的是，如果元素插入或删除操作是作用于链表中间，那将会是非常低效的。  

`LPUSH key value [value ...]`：在指定Key所关联的List Value的`头部插入`参数中给出的所有Values。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，之后再将数据从链表的头部插入。如果该键的Value不是链表类型，该命令将返回相关的错误信息。 	插入后链表中元素的数量。
`LPUSHX key value`：仅有当参数中指定的Key存在时，该命令才会在其所关联的List Value的`头部插入`参数中给出的Value，否则将不会有任何操作发生。	插入后链表中元素的数量。 
`LRANGE key start stop`：O(S+N)	时间复杂度中的S为start参数表示的偏移量，N表示元素的数量。该命令的参数start和end都是0-based。即0表示链表头部(leftmost)的第一个元素。其中start的值也可以为负值，-1将表示链表中的最后一个元素，即尾部元素，-2表示倒数第二个并以此类推。该命令在获取元素时，start和end位置上的元素也会被取出。如果start的值大于链表中元素的数量，空链表将会被返回。如果end的值大于元素的数量，该命令则获取从start(包括start)开始，链表中剩余的所有元素。	返回指定范围内元素的列表。
`LPOP key`：返回并弹出指定Key关联的链表中的第一个元素，即头部元素，。如果该Key不存，返回nil。	链表头部的元素。
`LLEN key`：返回指定Key关联的链表中元素的数量，如果该Key不存在，则返回0。如果与该Key关联的Value的类型不是链表，则返回相关的错误信息。	链表中元素的数量。

`LREM key count value`：O(N) 	时间复杂度中N表示链表中元素的数量。在指定Key关联的链表中，删除前count个值等于value的元素。如果count大于0，从头向尾遍历并删除，如果count小于0，则从尾向头遍历并删除。如果count等于0，则删除链表中所有等于value的元素。如果指定的Key不存在，则直接返回0。	返回被删除的元素数量。
`LSET key index value`：O(N) 	时间复杂度中N表示链表中元素的数量。但是设定头部或尾部的元素时，其时间复杂度为。设定链表中指定位置的值为新值，其中0表示第一个元素，即头部元素，-1表示尾部元素。如果索引值Index超出了链表中元素的数量范围，该命令将返回相关的错误信息。	 
`LINDEX key index`：O(N) 	时间复杂度中N表示在找到该元素时需要遍历的元素数量。对于头部或尾部元素，其时间复杂度为。该命令将返回链表中指定位置(index)的元素，index是0-based，表示头部元素，如果index为-1，表示尾部元素。如果与该Key关联的不是链表，该命令将返回相关的错误信息。	返回请求的元素，如果index超出范围，则返回nil。
`LTRIM key start stop`：O(N) 	N表示被删除的元素数量。该命令将仅保留指定范围内的元素，从而保证链接中的元素数量相对恒定。start和stop参数都是0-based，0表示头部元素。和其他命令一样，start和stop也可以为负值，-1表示尾部元素。如果start大于链表的尾部，或start大于stop，该命令不错报错，而是返回一个空的链表，与此同时该Key也将被删除。如果stop大于元素的数量，则保留从start开始剩余的所有元素。	 
`LINSERT key BEFORE|AFTER pivot value` 	O(N) 	时间复杂度中N表示在找到该元素pivot之前需要遍历的元素数量。这样意味着如果pivot位于链表的头部或尾部时，该命令的时间复杂度为。该命令的功能是在pivot元素的前面或后面插入参数中的元素value。如果Key不存在，该命令将不执行任何操作。如果与Key关联的Value类型不是链表，相关的错误信息将被返回。	成功插入后链表中元素的数量，如果没有找到pivot，返回-1，如果key不存在，返回0。

`RPUSH key value [value ...]`：在指定Key所关联的List Value的尾部插入参数中给出的所有Values。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，之后再将数据从链表的尾部插入。如果该键的Value不是链表类型，该命令将返回相关的错误信息。 	插入后链表中元素的数量。 
`RPUSHX key value`：仅有当参数中指定的Key存在时，该命令才会在其所关联的List Value的尾部插入参数中给出的Value，否则将不会有任何操作发生。 	插入后链表中元素的数量。 
`RPOP key`：返回并弹出指定Key关联的链表中的最后一个元素，即尾部元素，。如果该Key不存，返回nil。 	链表尾部的元素。 
`RPOPLPUSH source destination`：原子性的从与source键关联的链表尾部弹出一个元素，同时再将弹出的元素插入到与destination键关联的链表的头部。如果source键不存在，该命令将返回nil，同时不再做任何其它的操作了。如果source和destination是同一个键，则相当于原子性的将其关联链表中的尾部元素移到该链表的头部。	返回弹出和插入的元素。

# 四、事务
和众多其它数据库一样，Redis作为NoSQL数据库也同样提供了事务机制。在Redis中，MULTI/EXEC/DISCARD/WATCH这四个命令是我们实现事务的基石。相信对有关系型数据库开发经验的开发者而言这一概念并不陌生，即便如此，我们还是会简要的列出Redis中事务的实现特征：
1. 在事务中的所有命令都将会被串行化的顺序执行，事务执行期间，Redis不会再为其它客户端的请求提供任何服务，从而保证了事物中的所有命令被原子的执行。
2. 和关系型数据库中的事务相比，在Redis事务中如果有某一条命令执行失败，其后的命令仍然会被继续执行。
3. 我们可以通过MULTI命令开启一个事务，有关系型数据库开发经验的人可以将其理解为"BEGIN TRANSACTION"语句。在该语句之后执行的命令都将被视为事务之内的操作，最后我们可以通过执行EXEC/DISCARD命令来提交/回滚该事务内的所有操作。这两个Redis命令可被视为等同于关系型数据库中的COMMIT/ROLLBACK语句。
4. 在事务开启之前，如果客户端与服务器之间出现通讯故障并导致网络断开，其后所有待执行的语句都将不会被服务器执行。然而如果网络中断事件是发生在客户端执行EXEC命令之后，那么该事务中的所有命令都会被服务器执行。
5. 当使用Append-Only模式时，Redis会通过调用系统函数write将该事务内的所有写操作在本次调用中全部写入磁盘。然而如果在写入的过程中出现系统崩溃，如电源故障导致的宕机，那么此时也许只有部分数据被写入到磁盘，而另外一部分数据却已经丢失。Redis服务器会在重新启动时执行一系列必要的一致性检测，一旦发现类似问题，就会立即退出并给出相应的错误提示。此时，我们就要充分利用Redis工具包中提供的redis-check-aof工具，该工具可以帮助我们定位到数据不一致的错误，并将已经写入的部分数据进行回滚。修复之后我们就可以再次重新启动Redis服务器了。

`MULTI`：用于标记事务的开始，其后执行的命令都将被存入命令队列，直到执行EXEC时，这些命令才会被原子的执行。	始终返回OK
`EXEC`：执行在一个事务内命令队列中的所有命令，同时将当前连接的状态恢复为正常状态，即非事务状态。如果在事务中执行了WATCH命令，那么只有当WATCH所监控的Keys没有被修改的前提下，EXEC命令才能执行事务队列中的所有命令，否则EXEC将放弃当前事务中的所有命令。	原子性的返回事务中各条命令的返回结果。如果在事务中使用了WATCH，一旦事务被放弃，EXEC将返回NULL-multi-bulk回复。
`DISCARD`：回滚事务队列中的所有命令，同时再将当前连接的状态恢复为正常状态，即非事务状态。如果WATCH命令被使用，该命令将UNWATCH所有的Keys。	始终返回OK。
`WATCH key [key ...]`：在MULTI命令执行之前，可以指定待监控的Keys，然而在执行EXEC之前，如果被监控的Keys发生修改，EXEC将放弃执行该事务队列中的所有命令。	始终返回OK。
`UNWATCH`：取消当前事务中指定监控的Keys，如果执行了EXEC或DISCARD命令，则无需再手工执行该命令了，因为在此之后，事务中所有被监控的Keys都将自动取消。	始终返回OK。
三、命令示例：


# 五、消息订阅

# 持久化

# 集群

# 运维