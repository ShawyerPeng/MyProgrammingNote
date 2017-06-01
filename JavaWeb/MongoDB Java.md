`java -classpath ".;mongo-2.10.1.jar" MongoDBJDBC`
# 安装
1. 导入public key：
`sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6`
2. 创建一个list：
`echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list`
3. Reload local package database：
`sudo apt-get update`
4. `sudo apt-get install -y mongodb-org`

# 卸载
1. 删除包：
`sudo apt-get purge mongodb-org*`
2. 删除数据目录：
`sudo rm -r /var/log/mongodb`
`sudo rm -r /var/lib/mongodb`

# 运行
`sudo service mongod start`
`sudo service mongod stop`
`sudo service mongod restart`

`ps -ef | grep mongo`

`mongo`：打开shell

# 配置
`/etc/mongodb.conf`

# 基本操作
All MongoDB executable files are stored at /usr/bin/
1. 创建数据库
`use db`
`show dbs` – List all databases.
`use db_name` – Switches to db_name.
`show collections` – List all tables in the current selected database.

2. insert
~~`db.tablename.insert({data})`或`db.tablename.save({data})`~~
`db.tablename.insertOne({data})`或`db.tablename.insertMany({data})`

向users表(collection)中插入：
`db.users.insertOne({username:"mkyong",password:"123456"})`
`db.users.insertMany({username:"a",password:"123456"},{username:"b",password:"654321"})`

3. Update
`db.tablename.update({criteria},{$set: {new value}})`
`db.users.update({username:"mkyong"},{$set:{password:"hello123"}})`

4. Find
`db.tablename.find({criteria})`
```
db.users.find()
db.users.find({username:"google"})
db.users.find({$where:"this.username.length<=2"})
db.users.find({username:{$exists : true}})
```

5. Delete
`db.tablename.remove({criteria})`
```
db.users.remove({username:"google"})
```
Drop Table：`db.tablename.drop()`

6. Indexing
List all indexes of table "users"：`db.users.getIndexes()`
create an index：`db.tablename.ensureIndex(column)`
drop an index：`db.tablename.dropIndex(column)`
create an unique index：`db.tablename.ensureIndex({column},{unique:true})`







1. 列出所有数据库：
`mongoClient.getDatabaseNames().forEach(System.out::println);`
2. 创建Collection：
    ```java
    database.createCollection("customers", null);
    database.getCollectionNames().forEach(System.out::println);
    ```
3. Save – Insert：
```java
DBCollection collection = database.getCollection("customers");
BasicDBObject document = new BasicDBObject();
document.put("name", "Shubham");
document.put("company", "Baeldung");
collection.insert(document);
```
4. Save – Update：
```java

```

# 与Java结合
```xml
<!-- https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongo-java-driver</artifactId>
    <version>3.4.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.mongodb/bson -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>bson</artifactId>
    <version>3.4.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.mongodb/mongodb-driver-core -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-core</artifactId>
    <version>3.4.2</version>
</dependency>
```

```java
public class MongoDBJDBC {
    public static void main(String args[]) {
        try{
            // Connect to mongodb server
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // Connect to databases
            DB db = mongoClient.getDB("test");
            // Authenticate
            boolean auth = db.authenticate("myUserName", "myPassword");
            System.out.println("Authentication: "+auth);

            // Create a Collection
            DBCollection coll = db.createCollection("mycol");

            // Getting/selecting a collection
            DBCollection coll = db.getCollection("mycol");

            // Insert a document
            BasicDBObject doc = new BasicDBObject("title", "MongoDB").
                                    append("description", "database").
                                    append("likes", 100).
                                    append("url", "http://www.tutorialspoint.com/mongodb/").
                                    append("by", "tutorials point");
            coll.insert(doc);

            // Update a Document
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
               DBObject updateDocument = cursor.next();
               updateDocument.put("likes","200")
               col1.update(updateDocument);
            }

            // Delete a Document
            DBObject myDoc = coll.findOne();
            col1.remove(myDoc);

        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }
   }
}
```



# 参考资料
[官方手册](https://docs.mongodb.com/manual/)
[MongoDB hello world example](http://www.mkyong.com/mongodb/mongodb-hello-world-example/)  
[MongoDB Java CRUD Example Tutorial](http://www.journaldev.com/3963/mongodb-java-crud-example-tutorial)  
[A Guide to MongoDB with Java](http://www.baeldung.com/java-mongodb)  
[MongoDB Java 学习笔记](http://blog.csdn.net/ererfei/article/details/50857103)  
