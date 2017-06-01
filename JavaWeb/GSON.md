```xml
<!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.0</version>
</dependency>
```


```json
{
  "name":"Shawyer",
  "age":18,
  "email":[
    "1002097607@qq.com",
    "patrickyateschn@gmail.com"
  ]
}
```

```java
public class User {
    private String name;
    private Integer age;
    private String[] email;

    // getter、setter

    public static class Course {
        private String courseName;
        private String duration;

        public String getCourseName() {
            return courseName;
        }
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }
        public String getDuration() {
            return duration;
        }
        public void setDuration(String duration) {
            this.duration = duration;
        }
    }
}
```

`Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();`

```java
GsonBuilder gsonBuilder = new GsonBuilder();
gsonBuilder.setDateFormat("yyyy-MM-dd");
Gson gson = gsonBuilder.create();
// Gson gson = new Gson();

InputStream is = new FileInputStream("/emp.json");
BufferedReader br = new BufferedReader(new InputStreamReader(is));
//Converts JSON string to Employee object
Employee employee = gson.fromJson(br, Employee.class);

// Convert JSON array to List<Employee>
Type listType = new TypeToken<ArrayList<Employee>>(){}.getType();
List<Employee> employees = gson.fromJson(reader, listType);


Employee employee = getEmployee();
String jsonEmp = gson.toJson(employee);

List<Employee> employees = getEmployeeList();
Type typeOfSource = new TypeToken<List<Employee>>(){}.getType();
String jsonEmps = gson.toJson(employees, typeOfSource);

// Reading JSON data with Gson streaming APIs
InputStream is = new FileInputStream(("/emp-array.json"));
InputStreamReader isr = new InputStreamReader(is);
JsonReader reader = new JsonReader(inputStreamReader);
reader.beginArray();
while (reader.hasNext()) {
    // The method readEmployee(...) is listed below
    Employee employee = readEmployee(reader);
    employeeList.add(employee);
}
reader.endArray();
reader.close();
```

# Java Object to JSON
`String jsonInteger = gson.toJson(1);`
```java
// WrappedType
String jsonInteger = gson.toJson(1);
String jsonDouble = gson.toJson(12345.5432);
System.out.println(jsonInteger);
System.out.println(jsonDouble);

// JavaBean
User user = new User();
user.setName("Shawyer");
user.setAge(18);
user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1998-09-11"));
user.setEmail("patrickyateschn@gmail.com");

String userJson = gson.toJson(user);
System.out.println(userJson);

User anotherUser = gson.fromJson(userJson, User.class);
System.out.println(anotherUser instanceof  User);
```

# JSON to Java Object
`Integer javaInteger = gson.fromJson(jsonInteger, Integer.class);`
```java
String jsonStr = "{'brith':'2013-11-20','name':'lmw'}";

Integer javaInteger = gson.fromJson(jsonInteger, Integer.class);
Double javaDouble = gson.fromJson(jsonDouble, Double.class);
User user = gson.fromJson(jsonStr, User.class);

System.out.println(javaInteger);
System.out.println(javaDouble);
System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uu.brith));  
```

# 从`.json`文件读取
```java
Gson gson = new Gson();
User user = gson.fromJson(new FileReader("/home/patrick/桌面/user.json"), User.class);
System.out.println(user.getName());
```

# Java array to a JSON array
```java
// Wrapped Type
int[] numberArray = {121, 23, 34, 44, 52};
String[] fruitsArray = {"apple", "oranges", "grapes"};

String jsonNumber = gson.toJson(numberArray);
String jsonString = gson.toJson(fruitsArray);
System.out.println(jsonNumber);
System.out.println(jsonString);

int [] numCollectionArray = gson.fromJson(jsonNumber, int[].class);
String[] fruitBasketArray = gson.fromJson(jsonString, String[].class);
System.out.println("Number Array Length "+numCollectionArray.length);
System.out.println("Fruit Array Length "+fruitBasketArray.length);

// JavaBean
User user1 = new User();
user1.setName("Shawyer");
user1.setAge(18);
user1.setEmail(new String[]{"1002097607@qq.com","patrickyateschn@gmail.com"});

User user2 = new User();
user2.setName("Peng");
user2.setAge(20);
user2.setEmail(new String[]{"xxx@qq.com","xxx@gmail.com"});

List<User> listOfUser = new ArrayList<User>();
listOfUser.add(user1);
listOfUser.add(user2);

Gson gson = new GsonBuilder().setPrettyPrinting().create();
String prettyJsonString = gson.toJson(listOfUser);
System.out.println(prettyJsonString);
```

# Serialize and Deserialize
Serialize：使Java对象到Json字符串
Deserialize：使Json字符串到Java对象
```java
public class SerializeDemo implements JsonSerializer<User> {
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", user.getName());
        obj.addProperty("age", user.getAge());

        final JsonArray jsonAuthorsArray = new JsonArray();
        for (final String email : user.getEmail()) {
            final JsonPrimitive jsonEmail = new JsonPrimitive(email);
            jsonAuthorsArray.add(jsonEmail);
        }
        obj.add("email", jsonAuthorsArray);

        return obj;
    }
}
```

```java
public class DeserializeDemo implements JsonDeserializer<User> {
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        User user = new User();
        user.setName(jsonObject.get("name").getAsString());
        user.setAge(jsonObject.get("age").getAsInt());

        final JsonArray jsonAuthorsArray = jsonObject.get("email").getAsJsonArray();
        final String[] emails = new String[jsonAuthorsArray.size()];
        for (int i = 0; i < emails.length; i++) {
            final JsonElement jsonAuthor = jsonAuthorsArray.get(i);
            emails[i] = jsonAuthor.getAsString();
        }

        return user;
    }
}
```

```java
public class GsonDemo {
    public static void main(String[] args) {
        User user = new User();
        user.setName("Shawyer");
        user.setAge(18);
        user.setEmail(new String[]{"1002097607@qq.com","patrickyateschn@gmail.com"});

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new SerializeDemo());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String userJson = gson.toJson(user);
        System.out.println("Serializer : Json String Representation ");
        System.out.println(userJson);

        User anotherUser = gson.fromJson(userJson, User.class);
        System.out.println("Custom DeSerializer : Java Object Creation");
        System.out.println("Name：" + anotherUser.getName());
        System.out.println("Age：" + anotherUser.getAge());
        System.out.println("Email：" + Arrays.toString(anotherUser.getEmail()));
        System.out.println("anotherUser is type of Student? " + (anotherUser instanceof User));
    }
}
```

# Pretty printing
* `JsonCompactFormatter`：默认格式
* `JsonPrintFormatter`：用于pretty pringting，调用`GsonBuilder().setPrettyPrinting()`实现

# Use the GSON nested classes handling mechanism
## Static nested class
```java
Gson gson = new GsonBuilder().setPrettyPrinting().create();

User.Course aCourse = new User.Course();
aCourse.setCourseName("M.TECH.");
aCourse.setDuration("120 hr");

String jsonCourse = gson.toJson(aCourse);
System.out.println(jsonCourse);

User.Course anotherCourse = gson.fromJson(jsonCourse, User.Course.class);

System.out.println("Course : "+anotherCourse.getCourseName() + "Duration : " + anotherCourse.getDuration());
```

## Pure nested class(instance inner class)
A pure nested class in Java can be instantiated by using the outer class object.
```java
Gson gson = new GsonBuilder().setPrettyPrinting().create();

User user = new User();
User.Course instanceCourse = user.new Course();
instanceCourse.setCourseName("M.TECH.");
instanceCourse.setDuration("120 hr");

String jsonCourse = gson.toJson(instanceCourse);
System.out.println(jsonCourse);

User.Course anotherCourse = gson.fromJson(jsonCourse, User.Course.class);
System.out.println("Course : "+anotherCourse.getCourseName() + "Duration : " + anotherCourse.getDuration());
```

# generic-type classes
通过`TypeToken`类实现类型擦除，从而实现泛型类的serialization and deserialization。
```java
public class UserGeneric<T,E> {
    private T mark;
    private E name;

    public T getMark() {
        return mark;
    }
    public void setMark(T mark) {
        this.mark = mark;
    }
    public E getName() {
        return name;
    }
    public void setName(E name) {
        this.name = name;
    }
}
```

```java
Gson gson = new Gson();
UserGeneric<Integer, String> studGenericObj1 = new UserGeneric<Integer, String>();
studGenericObj1.setMark(25);
studGenericObj1.setName("Sandeep");

String json = gson.toJson(studGenericObj1);
System.out.println("Serialized Output :");
System.out.println(json);

UserGeneric<Integer, String> studGenericObj2 = gson.fromJson(json, UserGeneric.class);
System.out.println("DeSerialized Output :");
System.out.println("Mark : " + studGenericObj2.getMark());

Type studentGenericType = new TypeToken<UserGeneric<Integer, String>>() {
}.getType();
UserGeneric<Integer, String> studGenericObj3 = gson.fromJson(json,
        studentGenericType);
System.out.println("TypeToken Use DeSerialized Output :");
System.out.println("Mark : " + studGenericObj3.getMark());
```

# Handle a null object
`GsonBuilder().serializeNulls()`

```java
User user = new User();
user.setName("Shawyer");
user.setAge(null);
user.setEmail(new String[]{"1002097607@qq.com","patrickyateschn@gmail.com"});

Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
String studentJson = gson.toJson(user);
System.out.println(studentJson);

User javaStudentObject = gson.fromJson(studentJson, User.class);
System.out.println("Name：" + javaStudentObject.getName());
System.out.println("Age：" + javaStudentObject.getAge());
```

# Versioning support
在User.java中添加一行：`@Since(1.1) private String gender;`

```java
User user = new User();
user.setName("Shawyer");
user.setAge(null);
user.setEmail(new String[]{"1002097607@qq.com","patrickyateschn@gmail.com"});
user.setGender("Male");

System.out.println("Student json for Version 1.0 ");
Gson gson = new GsonBuilder().setVersion(1.0).setPrettyPrinting().create();
String jsonOutput = gson.toJson(user);
System.out.println(jsonOutput);

System.out.println("Student json for Version 1.1 ");
gson = new GsonBuilder().setVersion(1.1).setPrettyPrinting().create();
jsonOutput = gson.toJson(user);
System.out.println(jsonOutput);
```

# No argument constructor support
```java
public class Employee {
    private String name;
    private Salary salary;

    // getter、setter

    @Override
    public String toString() {
        return "servlet.Employee [name=" + name + ", salary=" + salary + " ]";
    }
}
```

```java
public class Salary {
    private int salaryAmount;
    Salary(int salary) {
        this.salaryAmount = salary;
    }
    @Override
    public String toString() {
        return "servlet.Salary [salaryAmount=" + salaryAmount + "]";
    }
}
```

```java
public class SalaryInstanceCreator implements InstanceCreator<Salary> {
    public Salary createInstance(Type type) {
        return new Salary(25000);
    }
}
```

```java
public class GsonDemo {
    public static void main(String[] args) throws ParseException {
        String jsonString = "{\"name\" :\"Sandeep\" , \"salary\": {}}";

        Gson gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(Salary.class, new SalaryInstanceCreator())
                .setPrettyPrinting().create();

        System.out.println(gson.fromJson(jsonString, Employee.class));
    }
}
```

# Create a custom field name from a Java object to JSON output string
序列化Java对象时自定义名字：`FieldNamingPolicy`类
```java
class College {
    @SerializedName("instituteName")
    private String name;

    private String[] coursesOffer;

    // getter、setter
}

public class FieldNamingFeature {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();

        College aCollege = new College();
        aCollege.setName("VIT University, Vellore");
        String[] courses = {"BTECH, MTECH, BSC, MSC"};
        aCollege.setCoursesOffer(courses);

        String jsonCollege = gson.toJson(aCollege);
        System.out.println(jsonCollege);
        College anotherCollege = gson.fromJson(jsonCollege, College.class);
        System.out.println("College Name : " + anotherCollege.getName());
    }
}
```

# User-defined field naming
create their own field naming policy：创建一个类实现`FieldNamingStrategy`接口，重写`translateName()`方法

```java
class CustomFieldStrategy implements FieldNamingStrategy {
    public String translateName(Field field) {
        String nameOfField = field.getName();
        return nameOfField.toUpperCase();
    }
}

public class CustomFieldNamingFeature {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
            .setFieldNamingStrategy(new CustomFieldStrategy())
            .setPrettyPrinting().create();

        College aCollege = new College();
        aCollege.setName("VIT University, Vellore");
        String[] courses = { "BTECH, MTECH, BSC, MSC" };
        aCollege.setCoursesOffer(courses);

        String jsonCollege = gson.toJson(aCollege);
        System.out.println(jsonCollege);
    }
}
```

# Exclude a JSON field while serializing
`GsonBuilder().excludeFieldsWithModifiers()`

## Configuring GsonBuilder方式
```java
public class Employee {
    private String name;
    private transient String gender;
    private static String designation;
    protected String department;

    public Employee() {
        this("Abcd Employee", "MALE", "Tech Lead", "IT Services");
    }

    public Employee(String name, String gender, String designation, String department) {
        this.name = name;
        this.gender = gender;
        this.designation = designation;
        this.department = department;
    }
}
```

```java
Gson gson = new Gson();
String json = gson.toJson(new Employee("Sandeep", "Male", "Tech Lead","IT Services"));
System.out.println(json);

Gson gson2 = new GsonBuilder().excludeFieldsWithModifiers().create();
json = gson2.toJson(new Employee("Sandeep", "MALE", "Tech Lead","IT Services"));
System.out.println(json);

Gson gson3 = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();
json = gson3.toJson(new Employee("Sandeep", "MALE", "Tech Lead","IT Services"));
System.out.println(json);
```

## Annotation方式
`@Expose`
```java
public class Vegetable {
    private String name;
    @Expose
    private int price;

    // getter、setter
}
```

```java
public class FieldExclusionAnnotationUse {
    public static void main(String[] args){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Vegetable aVegetable = new Vegetable();
        aVegetable.setName("Potato");
        aVegetable.setPrice(26);

        String jsonVegetable = gson.toJson(aVegetable);
        System.out.println("JSON Representation of Vegetable：" + jsonVegetable);
    }
}
```

# User-defined field exclusion annotation
`ExclusionStrategy`接口有两个方法：`shouldSkipField`和`shouldSkipClass`

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@interface MyExclude {
}

public class CustomExclusionStrategy implements ExclusionStrategy {
    private final Class<?> typeToExclude;

    public CustomExclusionStrategy(Class<?> typeToExclude) {
        this.typeToExclude = typeToExclude;
    }

    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(MyExclude.class) != null;
    }

    public boolean shouldSkipClass(Class<?> aClass) {
        return (aClass == typeToExclude);
    }
}
```

```java
public class Vegetable {
    private String name;
    @MyExclude
    private int price;

    // getter、setter
}
```

```java
public class GsonDemo {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new CustomExclusionStrategy(MyExclude.class)).create();

        Vegetable aVegetable = new Vegetable();
        aVegetable.setName("Potato");
        aVegetable.setPrice(26);

        String jsonVegetable = gson.toJson(aVegetable);
        System.out.println(jsonVegetable);
    }
}
```

# 应用
```java
@WebServlet("/StudentJsonDataServlet")
public class StudentJsonDataServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public StudentJsonDataServlet() {}

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Gson gson = new Gson();

    List<Student> listOfStudent = getStudentData();

    String jsonString = gson.toJson(listOfStudent);

    response.setContentType("application/json");

    response.getWriter().write(jsonString);
  }

/**
 * Returns List of Static Student data
 */
  private List<Student> getStudentData(){

    Student s1 = new Student();
    s1.setName("Sandeep");
    s1.setSubject("Computer");
    s1.setMark(85);

    Student s2 = new Student();
    s2.setName("John");
    s2.setSubject("Science");
    s2.setMark(85);

    Student s3 = new Student();
    s3.setName("Ram");
    s3.setSubject("Computer");
    s3.setMark(85);

    List<Student> listOfStudent = new ArrayList<Student>();
    listOfStudent.add(s1);
    listOfStudent.add(s2);
    listOfStudent.add(s3);

    return listOfStudent;
  }
}
```

```jsp
<html>
  <head>
  <title>Students JSON Table View</title>
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  </head>
  <body>
    <div id="student-data-container"></div>
    <script>
    $(document).ready(function(){

      var getStudentTableHtml, html, 

      htmlStudent, container =$('#student-data-container'),

      ajaxRequest = $.ajax({
        url: "StudentJsonDataServlet",

        dataType: "JSON",

        success: function(data){

          htmlStudent = getStudentTableHtml(data);

          container.html(htmlStudent)
        }
    }),

    getStudentTableHtml = function(data){

      html = [];

      html.push("<TABLE border='2px' cellspacing='2px'>");
      html.push("<TR>");
      html.push("<TH>NAME</TH>");
      html.push("<TH>SUBJECT</TH>");
      html.push("<TH>MARK</TH>");
      html.push("</TR>");

      $.each(data,function(index, aStudent){
        html.push("<TR>");
        html.push("<TD>");
        html.push(aStudent.name);
        html.push("</TD>");
        html.push("<TD>");
        html.push(aStudent.subject);
        html.push("</TD>");
        html.push("<TD>");
        html.push(aStudent.mark);
        html.push("</TD>");
        html.push("</TR>");
      });
      html.push("</TABLE>")

      return html.join("");
    }
  })
    </script>
  </body>
</html>
```

# 参考资料
## Official sites
Home page: http://code.google.com/p/google-gson/  
Manual and documentation: http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html  
Wiki: http://en.wikipedia.org/wiki/GSON  
Source code: http://code.google.com/p/google-gson/source/checkout  
Design document: https://sites.google.com/site/gson/gson-design-document  
Feed: http://code.google.com/p/google-gson/feeds  

## Articles and tutorials
User guide from Google: https://sites.google.com/site/gson/gson-user-guide  
Mkyong's blog: http://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/  
[Instant GSON](https://www.packtpub.com/mapt/book/application-development/9781783282036)  
[Gson全解析（上）](http://blog.csdn.net/u014315849/article/details/51890494)  
[通过Gson解析Json数据](http://www.cnblogs.com/tianzhijiexian/p/4246497.html)  
[JSON解析之Gson](http://www.cnblogs.com/zhangminghui/p/4109539.html)  
[Gson Deserialization Cookbook](http://www.baeldung.com/gson-deserialization-guide)  

## Community
Official forums: https://groups.google.com/forum/?fromgroups#!forum/google-gson  
Unofficial forums: http://stackoverflow.com/questions/tagged/gson  

## Blogs
Java code geek blog post: http://examples.javacodegeeks.com/core-java/gson/convert-java-object-to-from-json-using-gson-example/  
GSON applications: http://www.tutorialsavvy.com/search/label/gson  