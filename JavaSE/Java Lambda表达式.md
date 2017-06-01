# 语法概要
```java
(int x, int y) -> return x + y;
(int x, int y) -> { return x + y; }
(x, y) -> x + y; x -> x * 2
(x, y) -> { return x + y; }
() -> System.out.println("Hey there!");
System.out::println;
```

## 例1
```java
// 传统
public void runThread() {
    new Thread(new Runnable() {
        public void run() {
            System.out.println("Run!");
        }
    }).start();
}

// Lambda
public void runThreadUseLambda() {
    new Thread(() -> {
        System.out.println("Run!");
    }).start();
}
```

```java
String[] atp = {"Rafael Nadal", "Novak Djokovic",  
       "Stanislas Wawrinka",  
       "David Ferrer","Roger Federer",  
       "Andy Murray","Tomas Berdych",  
       "Juan Martin Del Potro"};  
List<String> players =  Arrays.asList(atp);  
  
// 以前的循环方式  
for (String player : players) {  
     System.out.print(player + "; ");  
}  
  
// 使用 lambda 表达式以及函数操作(functional operation)  
players.forEach((player) -> System.out.print(player + "; "));  
   
// 在 Java 8 中使用双冒号操作符(double colon operator)  
players.forEach(System.out::println);  
```


## 例2
```java
List<Integer> values = Arrays.asList(4,5,6,7,8);
values.forEach(i -> System.out.println(i));

interface A {
    void show();
}
interface B {
    void show(int i);
}
class XYZ implements A {
    public void show() {
        System.out.println("Hello");
    }
}
public class LambdaTest {
    public static void main(String[] args) throws Exception {
        // A obj = new XYZ();
        // obj.show();

        A obj;
        obj = new A() {
            @Override
            public void show() {
                System.out.println("Inner class");
            }
        };

        obj = () -> System.out.println("Inner class");

        obj = () -> {
            System.out.println("Lambda");
        };

        B obj2 = i -> System.out.println("Lambda" + i);
        obj2.show(5);
    }
}
```

# 匿名类
```java
void anonymousClass() {
    final Server server = new HttpServer();
    waitFor(new Condition() {
        @Override
        public Boolean isSatisfied() {
            return !server.isRunning();
        }
    });
}

 void closure() {
     Server server = new HttpServer();
     waitFor(() -> !server.isRunning());
 }

class WaitFor {
    static void waitFor(Condition condition) throws InterruptedException {
        while (!condition.isSatisfied())
            Thread.sleep(250);
    }
}
```


## Type inference improvements


## 函数式接口
```java
@FunctionalInterface  // 使用函数式接口替代匿名内部
public interface Calculator {
    double calculate(int a, int b);
    
    public default int subtract(int a, int b) {
        return a - b;
    }
    public default int add(int a, int b) {
        return a * b;
    }
    
    @Override
    public String toString();
}

public class LambdaTest {
    public static void main(String[] args) throws Exception {
        Calculator addition = (int a, int b) -> (a + b);
        // Calculator addition = (a, b) -> (a + b);
        System.out.println(addition.calculate(5, 20));  // prints 25.0

        Calculator division = (int a, int b) -> (double) a / b;
        System.out.println(division.calculate(5, 2));   // prints 2.5
    }
}
```

所谓的函数式接口就是只有一个抽象方法的接口，注意这里说的是抽象方法，因为Java8中加入了默认方法的特性，但是函数式接口是不关心接口中有没有默认方法的。 一般函数式接口可以使用@FunctionalInterface注解的形式来标注表示这是一个函数式接口，该注解标注与否对函数式接口没有实际的影响， 不过一般还是推荐使用该注解，就像使用@Override注解一样。JDK1.8中提供了一些函数式接口如下：
函数式接口 | 描述
--- | ---
Predicate | 传递一个参数，基于参数的值返回boolean值。
Supplier | 代表一个供应者的结果。
Consumer | 传递一个参数但没有返回值。
Function | 传递一个参数返回一个结果。这个结果的类型可以与参数的类型不相同。
UnaryOperator | 代表一个操作符的操作，它的返回结果类型与操作符的类型一样。实际上它可以被看作是Function 它的返回结果跟参数一样，它是Function 的子接口。
BiPredicate | 
BinaryOperator | 
BiConsumer | 
BiFunction | 传递两个参数返回一个结果。这个结果的类型可以与任一参数的类型不相同。
BiOperator | 代表两个操作符的操作，它的返回结果类型必须与操作符相同。


## No Parameter
```java
interface Sayable{
    public String say();
}
public class LambdaExpressionExample{
    public static void main(String[] args) {
        Sayable s=()->{
            return "I have nothing to say.";
        };
        System.out.println(s.say());
    }
}
```

## Single Parameter
```java
interface Sayable{
    public String say(String name);
}
public class LambdaExpressionExample{
    public static void main(String[] args) {
        // Lambda expression with single parameter.  
        Sayable s1=(name)->{
            return "Hello, "+name;
        };
        System.out.println(s1.say("Sonoo"));

        // You can omit function parentheses    
        Sayable s2= name ->{
            return "Hello, "+name;
        };
        System.out.println(s2.say("Sonoo"));
    }
}
```

## Multiple Parameters
```java
interface Addable{
    int add(int a,int b);
}

public class LambdaExpressionExample{
    public static void main(String[] args) {
        // Multiple parameters in lambda expression  
        Addable ad1=(a,b)->(a+b);
        System.out.println(ad1.add(10,20));

        // Multiple parameters with data type in lambda expression  
        Addable ad2=(int a,int b)->(a+b);
        System.out.println(ad2.add(100,200));
    }
}
```

## with or without return keyword
```java
interface Addable{
    int add(int a,int b);
}
public class lambdaExpression {
    public static void main(String[] args) {
        // Lambda expression without return keyword.  
        Addable ad1=(a,b)->(a+b);
        System.out.println(ad1.add(10,20));

        // Lambda expression with return keyword.    
        Addable ad2=(int a,int b)->{
            return (a+b);
        };
        System.out.println(ad2.add(100,200));
    }
}
```

## Foreach Loop
```java
public class LambdaExpressionExample{
    public static void main(String[] args) {
        List<String> list=new ArrayList<String>();
        list.add("ankit");
        list.add("mayank");
        list.add("irfan");
        list.add("jai");

        list.forEach(
                (n)->System.out.println(n)
        );
    }
}
```

## Multiple Statements
```java
@FunctionalInterface
interface Sayable{
    String say(String message);
}
public class LambdaExpressionExample{
    public static void main(String[] args) {
        // You can pass multiple statements in lambda expression  
        Sayable person = (message)-> {
            String str1 = "I would like to say, ";
            String str2 = str1 + message;
            return str2;
        };
        System.out.println(person.say("time is precious."));
    }
}
```

## Creating Thread
```java
public class LambdaExpressionExample{
    public static void main(String[] args) {
        //Thread Example without lambda  
        Runnable r1=new Runnable(){
            public void run(){
                System.out.println("Thread1 is running...");
            }
        };
        Thread t1=new Thread(r1);
        t1.start();
        //Thread Example with lambda  
        Runnable r2=()->{
            System.out.println("Thread2 is running...");
        };
        Thread t2=new Thread(r2);
        t2.start();
    }
}
```

## Comparator
```java
class Product{
    int id;
    String name;
    float price;
    public Product(int id, String name, float price) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
public class LambdaExpressionExample{
    public static void main(String[] args) {
        List<Product> list=new ArrayList<Product>();

        //Adding Products  
        list.add(new Product(1,"HP Laptop",25000f));
        list.add(new Product(3,"Keyboard",300f));
        list.add(new Product(2,"Dell Mouse",150f));

        System.out.println("Sorting on the basis of name...");

        // implementing lambda expression  
        Collections.sort(list,(p1, p2)->{
            return p1.name.compareTo(p2.name);
        });
        for(Product p:list){
            System.out.println(p.id+" "+p.name+" "+p.price);
        }

    }
}
```

## Filter Collection Data
```java
class Product{
    int id;
    String name;
    float price;
    public Product(int id, String name, float price) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
public class LambdaExpressionExample{
    public static void main(String[] args) {
        List<Product> list=new ArrayList<Product>();
        list.add(new Product(1,"Samsung A5",17000f));
        list.add(new Product(3,"Iphone 6S",65000f));
        list.add(new Product(2,"Sony Xperia",25000f));
        list.add(new Product(4,"Nokia Lumia",15000f));
        list.add(new Product(5,"Redmi4 ",26000f));
        list.add(new Product(6,"Lenevo Vibe",19000f));

        // using lambda to filter data  
        Stream<Product> filtered_data = list.stream().filter(p -> p.price > 20000);

        // using lambda to iterate through collection  
        filtered_data.forEach(
                product -> System.out.println(product.name+": "+product.price)
        );
    }
}
```

## Event Listener
```java
public class LambdaEventListenerExample {
    public static void main(String[] args) {
        JTextField tf=new JTextField();
        tf.setBounds(50, 50,150,20);
        JButton b=new JButton("click");
        b.setBounds(80,100,70,30);

        // lambda expression implementing here.  
        b.addActionListener(e-> {tf.setText("hello swing");});

        JFrame f=new JFrame();
        f.add(tf);f.add(b);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.setSize(300, 200);
        f.setVisible(true);
    }
}
```

# 参考资料
[Java Lambda表达式入门](http://blog.csdn.net/sdqdzc/article/details/48029273)  
[Java 8 Lambda表达式入门](https://nkcoder.github.io/2016/01/16/java-8-lambda-expression-guide/)  
[Java 8十个lambda表达式案例](http://www.jdon.com/idea/java/10-example-of-lambda-expressions-in-java8.html)  
[深入浅出 Java 8 Lambda 表达式](http://blog.oneapm.com/apm-tech/226.html)  
[掌握 Java 8 Lambda 表达式](http://blog.chengyunfeng.com/?p=902)  
https://wizardforcel.gitbooks.io/java8-tutorials/content/Java%208%20lambda%20%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5.html  
[掌握 Java 8 Lambda 表达式的更多相关文章](https://www.bbsmax.com/R/lk5aG094z1/)  
https://www.coursera.org/learn/java-chengxu-sheji/lecture/eHjdD/5-6-lambdabiao-da-shi-lambdabiao-da-shi-jiao-gao-yao-qiu  
http://liuxing.info/attachment/Java%208%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BC%96%E7%A8%8B.pdf  
http://how2j.cn/k/lambda/lambda-lamdba-tutorials/697.html  
[Java Lambda Expressions](https://www.javatpoint.com/java-lambda-expressions)  
[Java8新特性第1章(Lambda表达式)](https://zhuanlan.zhihu.com/p/20540175)  
[JAVA LAMBDA 表达式教程](http://how2j.cn/k/lambda/lambda-lamdba-tutorials/697.html?tid=75#nowhere)  
[《Java 8函数式编程》读书笔记](http://ginobefunny.com/post/java8_lambda_notes/)  
[java8函数式接口](https://wuzhaoyang.me/2016/10/26/java-functional-program.html)  
[JAVA 8 学习总结](http://yangsui.me/JAVA8-OverView/)  