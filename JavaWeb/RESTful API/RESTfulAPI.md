# Json
## 格式
{
    "departmentName":"IT", 
    "employees":[
        {"firstName":"John", "lastName":"Chen"}, 
        {"firstName":"Ameya", "lastName":"Job"}, 
        {"firstName":"Pat", "lastName":"Fay"} 
    ],
    "location":["New York", "New Delhi"]
}

## 数据类型
* Number：`{"totalWeight": 123.456}`
* String：`{"firstName": "Jobinesh"}`
* Boolean：`{"isValidEntry": true}`
* Array：`{"fruits": ["apple", "banana", "orange"]}`
* Object：`{"departmentId":10,"departmentName":"IT","manager":"John Chen"}`
* null：`{"error":null}`

JSR 353 object model APIs
```xml
<!-- https://mvnrepository.com/artifact/javax.json/javax.json-api -->
<dependency>
    <groupId>javax.json</groupId>
    <artifactId>javax.json-api</artifactId>
    <version>1.1</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.32</version>
</dependency>
```





# JAX-RS
* [Jersey RESTful web service framework](https://jersey.java.net/)
* [Apache CXF](http://cxf.apache.org/)
* [RESTEasy](http://resteasy.jboss.org/)
* [Restlet](http://restlet.com/)

```xml
<!-- https://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api -->
<dependency>
    <groupId>javax.ws.rs</groupId>
    <artifactId>javax.ws.rs-api</artifactId>
    <version>2.0.1</version>
</dependency>
```

## Annotations for defining a RESTful resource
* `@Path`：指定资源路径
Basic:
```java
@Path("helloWorld")
public class HelloWorldResource {
    @GET
    @Produces("text/plain")
    public String sayHello() {
        return "Hello World!";
    }
}
```
Class-level:
```java
@Path("/helloWorld/{name}")
public class HelloWorldResource {
    @GET
    @Produces("text/plain")
    public String sayHello(@PathParam("name") String name) {
        return "Hello, " + name;
    }
}
```
Method-level:
```java
@Path("/helloWorld")
public class HelloWorldResource {
    @GET
    @Produces("text/plain")
    @Path("{name}")
    public String sayHello(@PathParam("name") String name) {
        return "Hello, " + name;
    }
}
```
with a regular expression:
```java
@Path("/helloWorld")
public class HelloWorldResource {
    @GET
    @Produces("text/plain")
    @Path("{name: ([a-zA-Z])*}")
    public String sayHello(@PathParam("name") String name) {
        return "Hello, " + name;
    }
}
```

* `@PathParam`：获取URI中指定规则的参数
* `@Produces`：指定将要返回给client端的数据标识类型（MIME），可以作为class注释，也可以作为方法注释
application/atom+xml
application/json
application/octet-stream
application/svg+xml
application/xhtml+xml
application/xml
text/html
text/plain
text/xml
```java
@Path("/helloWorld")
@Produces("text/plain")
public class HelloWorldResource {
    @GET
    public String greet() {}

    @GET
    @Produces("text/html")    // 覆盖类上指定的MIME type
    public String greetUser() {}
}
```

* `@Consumes`：指定可以接受client发送过来的MIME类型
application/atom+xml
application/json
application/octet-stream
application/svg+xml
application/xhtml+xml
application/xml
text/html
text/plain
text/xml
multipart/form-data
application/x-www-form-urlencoded
```java
@Path("/helloWorld")
@Consumes("multipart/related")
public class HelloWorldResource {
    @POST
    public String processMultipart(MimeMultipart multipartData) {}

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String processForm(FormURLEncodedProperties formData) {}
}
```

```java
@Path("departments")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentService {
    @GET
    @Path("count")
    @Produces("text/plain")
    public Integer getTotalDepartments() {}

    @DELETE
    @Path("{id}")
    public void removeDepartment(@PathParam("id") short id) {}

    // 正则表达式
    @Path("{name: [a-zA-Z][a-zA-Z_0-9]}")
    public void removeDepartmentByName(@PathParam("name") String deptName) {}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createDepartment(Department entity) {}
}
```



### Annotations for processing HTTP request methods
* `@GET`：读取
```java
@GET
public String getUser() {
  System.out.println("GET");
  return "Hello User";
}
```
* `@PUT`：更新、创建
```java
@PUT
public void updateUser(String userData) {
    System.out.println("PUT");
    System.out.println("User Data: " + userData);
}
```
* `@POST`：创建
```java
@POST
public void addUser(@FormParam("id") String id,@FormParam("name") String name){
   	 System.out.println("POST");
    System.out.println("Id: " + id);
    System.out.println("Name: " + name);
}
```
* `@DELETE`：删除
```java

```
* `@HEAD`
```java

```
* `@OPTIONS`
```java

```

### Annotations for accessing request parameters
* `@PathParam`：获取URI中指定规则的参数
```java

```
* `@QueryParam`：获取GET请求中的查询参数
```java

```
* `@MatrixParam`：绑定包含多个property (属性)=value(值)方法参数表达式
```java

```
* `@HeaderParam`：提取HTTP Header并将它绑定到一个方法的参数
```java

```
* `@CookieParam`：读取信息存储为一个cookie，并将它绑定到一个方法的参数
```java

```
* `@FormParam`：从POST请求的表单参数中获取数据
```java

```
* `@BeanParam`：当请求参数很多时，比如客户端提交一个修改用户的PUT请求，请求中包含很多项用户信息。
```java

```
* `@DefaultValue`：默认值
```java

```
* `@Context`：
```java

```
* `@Encoded`：
```java

```

```java
@Path("departments")
public class DepartmentService {
    @DELETE
    @Path("{id}")
    public void removeDepartment(@PathParam("id") Short deptId) {}

    @Produces(MediaType.APPLICATION_JSON)
    @Path("{country}/{city}")
    public List<Department> findAllDepartments(@PathParam("country")String countyCode, @PathParam("city") String cityCode) {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Department> 
    findAllDepartmentsByName(@QueryParam("name") String deptName) {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("matrix")
    public List<Department> findAllDepartmentsByNameWithMatrix(@MatrixParam("name") String deptName, @MatrixParam("city") String locationCode) {}

    @POST
    public void createDepartment(@HeaderParam("Referer") String referer, Department entity) {}

    @GET
    @Path("cook")
    @Produces(MediaType.APPLICATION_JSON)
    public Department getDefaultDepartment(@CookieParam("Default-Dept") short departmentId) {}

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void createDepartment(@FormParam("departmentId") short departmentId, @FormParam("departmentName") String departmentName) {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Department> findAllDepartmentsInRange(@DefaultValue("0") @QueryParam("from") Integer from, @DefaultValue("100") @QueryParam("to") Integer to) {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Department> findAllDepartmentsByName(@Context UriInfo uriInfo){}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Department> findAllDepartmentsByName(@QueryParam("name") String deptName) {}
}
```

```java
public class DepartmentBean {
    @FormParam("departmentId") 
    private short departmentId;
    @FormParam("departmentName") 
    private String departmentName;
}
```

### Returning additional metadata with responses
```java
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response findAllDepartmentsByName(@QueryParam("name") String deptName) {
    List<Department> depts= findAllMatchingDepartmentEntities(deptName); 
    //Sets cache control directive to the response
    CacheControl cacheControl = new CacheControl();
    //Cache the result for a day 
    cacheControl.setMaxAge(86400);
    return Response.ok().
    cacheControl(cacheControl).entity(depts).build();    
}
```

### Understanding data binding rules in JAX-RS

### Root Resource Classes的生命周期
`@RequestScope`：Request，默认
`@PerLookup`：Per-lookup，为每个请求创建root-resource实例
`@Singleton`：Singleton，一个JAX-RS应用只有一个实例，可以在类上使用@Singleton注解或者使用Application注册

### 注入规则
正常情况下，可以将请求的各种值注入到参数注解注解的对象，例如属性，方法参数，构造函数等。但是有一些特别的注入规则，会根据注入资源的生命周期有所不同，例如有些参数无法注入单例资源：
```java
// Root-resource
@Path("/userService")
public class UserResource {
    ...
}
// Fields  注入类的域中
@Path("/userService")
public class UserResource {
    ...
    @QueryParam("name")
    private String name;
    ...
}
// Constructor parameter 注入的值将用于调用构造函数
@Path("/userService")
public class UserResource {

    public UserResource(@PathParam("id") int id){
        ...
    }

    @GET
    @Path("{id}")
    public String getUser() {
        ...
    }
}
// Resource method 资源的各方法中注入
@Path("/userService")
public class UserResource {
    @GET
    public String getUser(@QueryParam("name")String name) {
        ...
    }
}
// Subresource method 子资源的各方法中注入
@Path("/userService")
public class UserResource {
    @GET
    @Path("/getUser/{name}")
    public String getUser(@PathParam("name")String name) {
        ...
    }
}
// Subresource locator method 不带方法注解的@Path
@Path("/userService")
public class UserResource {
    @Path("/getAddress")
    public AddressResource getAddress(@QueryParam("id") int id) {
        ...
    }
}
// Bean setter method 只能使用@Context注解
@Path("/userService")
public class UserResource {
    @GET
    @Path("{id}")
    public String getUser() {
         ...
    }

    @PathParam("id")
    public void setId( int id){
        ...
    }
}
```

# Jersey
```xml
<!-- https://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api -->
<dependency>
    <groupId>javax.ws.rs</groupId>
    <artifactId>javax.ws.rs-api</artifactId>
    <version>2.0.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/javax/javaee-web-api -->
<dependency>
    <groupId>javax</groupId>
    <artifactId>javaee-web-api</artifactId>
    <version>7.0</version>
</dependency>
```

```xml
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.containers/jersey-container-servlet-core -->
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet-core</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-client -->
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-client</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-server -->
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-server</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-common -->
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-common</artifactId>
    <version>2.25.1</version>
</dependency>
```