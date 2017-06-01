Gson对小文件处理比较快，Jackson处理大文件比较好

`jackson-core.jar`：核心包（必须），提供基于“流模式”解析的API。
`jackson-databind`：数据绑定包（可选），提供基于“对象绑定”和“树模型”相关API。
`jackson-annotations`：注解包（可选），提供注解功能。

```xml
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.9.0.pr3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.0.pr3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.9.0.pr3</version>
</dependency>
```

* **Tree model APIs**: 建立 tree representation of a JSON document
* **Data binding API**: JSON document与Java objects的转换
* **Streaming API**: 读写JSON document

# 示例
## Java对象转Json String
`ObjectMapper`类

`writeValue()`：把java对象转化成某个环境的输出，如System.out，Response.getWriter()
`writeValueAsString(Object obj)`：把java对象转化成json字符串
`writeValueAsBytes()`：把java对象转化成byte[]
`writeTree()`：把java对象转化成Tree

### 普通类型
```java
// User类转JSON
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(user);
System.out.println(json);
```

```java
InputStream inputStream = getClass().getResourceAsStream("/home/patrick/桌面/demo.json");
Reader reader = new InputStreamReader(inputStream, "UTF-8");
JsonReader jsonReader = Json.createReader(reader);
JsonArray employeeArray = jsonReader.readArray();
```

### List
```java
List<User> userList = new ArrayList<User>();

User user1 = new User();
User user2 = new User();
user1.setName("Shawyer");
user1.setAge(18);
user1.setEmail(new String[]{"1002097607@qq.com","patrickyateschn@gmail.com"});
user2.setName("Peng");
user2.setAge(20);
user2.setEmail(new String[]{"xxx@qq.com","xxx@gmail.com"});
userList.add(user1);
userList.add(user2);

// 1. writeValueAsString()，最常用
String json = objectMapper.writeValueAsString(userList);
System.out.println(json);

// 2. writeValueAsBytes()
byte[] bytes = objectMapper.writeValueAsBytes(userList);
System.out.println(Arrays.toString(bytes));

// 3. writeValue()
objectMapper.writeValue(System.out, userList);
//objectMapper.writeValue(Response.getWriter(), userList);

// 4. writeTree()
JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
JsonFactory jsonFactory = new JsonFactory();
// 根节点
ObjectNode rootNode = jsonNodeFactory.objectNode();
// 往根节点中添加普通键值对
rootNode.put("name","aaa");
rootNode.put("email","aaa@aa.com");
rootNode.put("age",24);
// 往根节点中添加一个子对象
ObjectNode petsNode = jsonNodeFactory.objectNode();
petsNode.put("petName","kitty").put("petAge",3);
rootNode.set("pets", petsNode);
//往根节点中添加一个数组
ArrayNode arrayNode = jsonNodeFactory.arrayNode();
arrayNode.add("java").add("linux").add("mysql");
rootNode.set("skills", arrayNode);
// 调用ObjectMapper的writeTree方法根据节点生成最终json字符串
JsonGenerator generator = jsonFactory.createGenerator(System.out);
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.writeTree(generator, rootNode);
```

### Map
```java
Map<String, String> map = new HashMap<String, String>();
map.put("name", "morris");
map.put("age", "22");
map.put("address", "hk");

String jsonString = objectMapper.writeValueAsString(map);
System.out.println(jsonString);
```

## JSON转Java类
```java
String json = "{\"name\":\"Shawyer\",\"age\":22,\"email\":[\"1002097607@qq.com\",\"patrickyateschn@gmail.com\"]}";

ObjectMapper mapper = new ObjectMapper();
User user = mapper.readValue(json, User.class);
System.out.println(user);

User user2 = mapper.readValue(new FileInputStream("user.json"), User.class);
System.out.println(user2);
```

## 常见类
`JsonFactory`：这个类是Jackson项目主要的工厂方法，主要用于配置和构建解析器(比如 JsonParser)和生成器(比如 JsonGenerator)，这个工厂实例是线程安全的，如果有配置的话，可以重复使用。
`JsonGenerator`：这个类主要是用来生成Json格式的内容的，我们可以使用JsonFactory的方法生成一个实例。
`JsonParser`：这个主要是用来读取Json格式的内容，并且完成解析操作的，我们可以使用JsonFactory的方法生成一个实例。
`ObjectMapper`：这个类提供了Java对象和Json之间的转化，主要通过JsonParser和JsonGenerator实例来完成实际的对Json数据的读写操作。这个类是ObjectCodec的子类，主要的实现细节都在ObjectCodec里面。而且这个类可以重复使用，所以一般会创建这个类的一个单例模式，比如下面的代码

## 复杂对象的Json数据的生成
### Object格式
```java
JsonGenerator jsonGenerator = objectMapper.getFactory().
        createGenerator(new FileOutputStream("user.json"));
jsonGenerator.writeStartObject();
jsonGenerator.writeStringField("name", "Shawyer");
jsonGenerator.writeNumberField("age", 22);
jsonGenerator.writeObjectField("sex", "male");
jsonGenerator.writeEndObject();
jsonGenerator.flush();
jsonGenerator.close();

// 将生成的json数据写入StringWriter
StringWriter sw = new StringWriter();
JsonGenerator jsonGenerator2 = objectMapper.getFactory().createGenerator(sw);
jsonGenerator2.writeStartObject();
jsonGenerator2.writeStringField("name", "Shawyer");
jsonGenerator2.writeNumberField("age", 22);
jsonGenerator2.writeObjectField("sex", "male");
jsonGenerator2.writeEndObject();
jsonGenerator2.flush();
jsonGenerator2.close();
System.out.println(sw.toString());
```

如果要生成如下格式的json字符，可以这样写：  
`{"name":"Shawyer","age":22,"email":["1002097607@qq.com","patrickyateschn@gmail.com"]}`
```java
JsonGenerator jsonGenerator = objectMapper.getFactory().
        createGenerator(new FileOutputStream("user.json"));
jsonGenerator.writeStartObject();
jsonGenerator.writeStringField("name", "Shawyer");
jsonGenerator.writeNumberField("age", 22);

jsonGenerator.writeArrayFieldStart("email");
jsonGenerator.writeString("1002097607@qq.com");
jsonGenerator.writeString("patrickyateschn@gmail.com");
jsonGenerator.writeEndArray();

jsonGenerator.writeEndObject();
jsonGenerator.flush();
jsonGenerator.close();
```

## Array格式
```java
User user = new User("Shayer", 22, new String[]{"1002097607@qq.com"});

// 将生成的json数据写入.json文件
JsonGenerator jsonGenerator = objectMapper.getFactory().
        createGenerator(new FileOutputStream("user.json"));
jsonGenerator.writeStartArray();
jsonGenerator.writeString("Shawyer");
jsonGenerator.writeNumber(22);
jsonGenerator.writeObject(user);
jsonGenerator.writeEndArray();
jsonGenerator.flush();
jsonGenerator.close();

// 将生成的json数据写入StringWriter
StringWriter sw = new StringWriter();
JsonGenerator jsonGenerator2 = objectMapper.getFactory().createGenerator(sw);
jsonGenerator2.writeStartArray();
jsonGenerator2.writeString("Shawyer");
jsonGenerator2.writeNumber(22);
jsonGenerator2.writeObject(user);
jsonGenerator2.writeEndArray();
jsonGenerator2.flush();
jsonGenerator2.close();
System.out.println(sw.toString());
```

## model APIs
### Using Jackson tree model APIs to query and update data
```java
InputStream inputStream = new FileInputStream("/emp-array.json");
ObjectMapper objectMapper = new ObjectMapper();

//Read JSON content in to tree 
JsonNode rootNode = objectMapper.readTree(inputStream);

//Check if the json content is in array form
if (rootNode.isArray()) {
//Iterate over each element in the array
    for (JsonNode objNode : rootNode) {
        //Find out the email node by traversing tree
        JsonNode emailNode = objNode.path("email");
        //if email is null, then update with 
        //a system generated email
        if(emailNode.textValue() == null ){
            String generatedEmail = "007@gmail.com";
            ((ObjectNode)objNode).put("email", generatedEmail );
        }
    }
}
//Write the modified tree to a json file
objectMapper.writeValue(new File("emp-modified-array.json"), rootNode);
if(inputStream != null) inputStream.close();
```

## data binding APIs
### Processing JSON with Jackson data binding APIs
* `TypeReference`
* `CollectionType`
* `TypeFactory`

```java
// readValue()获得Java对象
ObjectMapper objectMapper = new ObjectMapper(); 
Employee employee = objectMapper.readValue(new File("emp.json"), Employee.class);

// getTypeFactory().constructCollectionType()
CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Employee.class);

// "emp-array.json" file contains JSON array of employee data
List<Employee> emp = objectMapper.readValue(new File("emp-array.json"), collectionType);
```

## streaming APIs
### Processing JSON with Jackson streaming APIs
* JsonParser
* JsonGenerator
* JsonFactory

```java
InputStream inputStream = getClass().getResourceAsStream("/emp-array.json");
JsonParser jsonParser = new JsonFactory().createParser(inputStream);
ObjectMapper objectMapper = new ObjectMapper();

while (!jsonParser.isClosed()) {
    JsonToken jsonToken = jsonParser.nextToken();
    // 如果是最后一个token就退出循环
    if (jsonToken == null) break;
    //If this is start of the object, then create 
    // 如果是对象的开始，就创建Employee实例，把它添加到result list中
    if (jsonToken.equals(JsonToken.START_OBJECT)) {
        // 使用objectMapper复制当前的JSON对象到Employee对象中
        Employee employee = objectMapper.readValue(jsonParser, Employee.class);
        // 把新复制的实例添加到list中
        employeeList.add(employee);
    }
}
if (inputStream != null) inputStream.close();
if (jsonParser != null) jsonParser.close();
```


```java

public void parseJsonObject() throws Exception{  
    JsonFactory jsonFactory = new JsonFactory(); //创建json工厂，主要用来创建json生成器，json解析器的工具  
        
    // 解析器是用于标记JSON内容到令牌和相关数据的对象。这是最低级的JSON内容的读访问
    JsonParser jsonParse = jsonFactory.createJsonParser(new File("D:/test.json"));  
    //循环判断下一个令牌是否到结束令牌  
    while(jsonParse.nextToken() != JsonToken.END_OBJECT){  
        String fieldName = jsonParse.getCurrentName();  
        if("name".equals(fieldName)) {  
            jsonParse.nextToken();  
            System.out.println(jsonParse.getText());  
        }  
        if("sex".equals(fieldName)) {  
            jsonParse.nextToken();  
            System.out.println(jsonParse.getText());  
        }  
        if("Address".equals(fieldName)) { //数组判断有没有到数组结束位置  
            jsonParse.nextToken();  
            while (jsonParse.nextToken() != JsonToken.END_ARRAY) {    
                System.out.println(jsonParse.getText());     
            }    
        }  
    }  
    jsonParse.close();  
}
```

### Using Jackson streaming APIs to generate JSON
```java
OutputStream outputStream = new FileOutputStream("emp-array.json");
JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream, JsonEncoding.UTF8);

jsonGenerator.writeStartArray();
List<Employee> employees = getEmployeesList();
for (Employee employee : employees) {
  jsonGenerator.writeStartObject();
  jsonGenerator.writeNumberField("employeeId", employee.getEmployeeId());
  jsonGenerator.writeStringField("firstName", employee.getFirstName());
  jsonGenerator.writeStringField("lastName", employee.getLastName());
  jsonGenerator.writeEndObject();

}
// JsonGenerator把JSON内容写到指定的OutputStream输出流中
jsonGenerator.writeEndArray();
// 关闭流
jsonGenerator.close();
outputStream.close();
```

## Tree Model
`JsonNodeFactory`：用来构造各种 JsonNode 节点，例如对象节点 ObjectNode、数组节点 ArrayNode 等；
`JsonNode`：表示 json 节点，可以往里添加各种 json 值，从而构造出 json 树；
`ObjectMapper`：将 JsonNode 节点转换成最终的 json 字符串；

### ->Json
假设要生成如下json字符串：
```json
{
    "name": "aaa",
    "email": "aaa@aa.com",
    "age": 24,
    "pets": {
        "petName": "kitty",
        "petAge": 3
    },
    "skills": [ "java","linux","mysql"]
}
```

```java
// 4. writeTree()
JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
JsonFactory jsonFactory = new JsonFactory();
// 根节点
ObjectNode rootNode = jsonNodeFactory.objectNode();
// 往根节点中添加普通键值对
rootNode.put("name","aaa");
rootNode.put("email","aaa@aa.com");
rootNode.put("age",24);
// 往根节点中添加一个子对象
ObjectNode petsNode = jsonNodeFactory.objectNode();
petsNode.put("petName","kitty").put("petAge",3);
rootNode.set("pets", petsNode);
//往根节点中添加一个数组
ArrayNode arrayNode = jsonNodeFactory.arrayNode();
arrayNode.add("java").add("linux").add("mysql");
rootNode.set("skills", arrayNode);
// 调用ObjectMapper的writeTree方法根据节点生成最终json字符串
JsonGenerator generator = jsonFactory.createGenerator(System.out);
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.writeTree(generator, rootNode);
```

### <-Json
```java
jsonString = "{\"name\":\"aaa\",\"email\":\"aaa@aa.com\",\"age\":24,\"pets\":{\"petName\":\"kitty\",\"petAge\":3},\"skills\":[\"java\",\"linux\",\"mysql\"]}";

ObjectMapper objectMapper = newObjectMapper();  
//使用ObjectMapper的readValue方法将json字符串解析到JsonNode实例中  
JsonNode rootNode = objectMapper.readTree(jsonString);  
//直接从rootNode中获取某个键的值，  
//这种方式在如果我们只需要一个长json串中某个字段值时非常方便  
JsonNode nameNode = rootNode.get("name");  
String name = nameNode.asText();  
System.out.println(name);  
//从 rootNode 中获取数组节点  
JsonNode skillsNode = rootNode.get("skills");  
for(inti = 0;i < skillsNode.size();i++) {  
    System.out.println(skillsNode.get(i).asText());  
}  
//从 rootNode 中获取子对象节点  
JsonNode petsNode = rootNode.get("pets");  
String petsName = petsNode.get("petName").asText();  
System.out.println(petsName);  
```

## 注解
`@JsonIgnore`：此注解用于属性上，进行JSON操作时忽略该属性
`@JsonIgnoreProperties`：主要作用于类，可以忽略类中指定的属性，如@JsonIgnoreProperties (value = { "hibernateLazyInitializer" , "password" })
`@JsonFormat`：此注解用于属性上，把Date类型直接转化为想要的格式，如@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
`@JsonProperty`：此注解用于属性上，把该属性的名称序列化为另外一个名称。如把trueName属性序列化为name，@JsonProperty("name")
`@JsonSerialize`
`@JsonDeserialize`

`@JsonCreator`：使用构造器或工厂方法。给参数绑定名称
`@JsonTypeInfo`：处理多态类型
`@JsonAutoDetect`：改变可见级别
自动发现所有字段：@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
禁用所有字段的自动发现：@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)

# 参考资料
[java – 如何使用Jackson反序列化对象数组](http://codeday.me/bug/20170221/3548.html)  
[Jackson Json进行JSON解析和序列化](http://blog.csdn.net/luckykapok918/article/details/53888255)  
[jackson解析的基本使用](http://www.jianshu.com/p/14e975248a9f)  
[利用Jackson对Object,Map,List,数组,枚举,日期类等转化为json](http://zhangyou1010.iteye.com/blog/1049259)  
