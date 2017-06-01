![](http://p1.bqimg.com/567571/f0442504daf79e2e.jpg)

# 一、字符流(Reader和Writer)：纯文本
### 1. `FileWriter`和`FileReader`
```java
public class Test {
    public static void main(String[] args) {
        try (
            FileWriter fileWriter = new FileWriter("C:/Users/PatrickYates/Desktop/hello.txt",true);  // 加true续写，不覆盖前面的内容
            FileReader fileReader = new FileReader("C:/Users/PatrickYates/Desktop/hello.txt");
        ) {
            // 向文件中写入数据
            char array[] = "hello,world!".toCharArray();
            fileWriter.write(array);
            fileWriter.close(); // 这一句必须加，不然fileReader读不出来

            // 从文件中读取数据
            int n = 0;
            while((n = fileReader.read()) != -1){
                System.out.print((char)n);          // InputStreamReader代替FileReader，这样就不会产生乱码：Reader isr=new InputStreamReader(new FileInputStream(fileName),"UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 2. `OutputStreamWriter`和`InputStreamReader`
FileReader是使用默认码表读取文件, 如果需要使用指定码表读取, 那么可以使用InputStreamReader(字节流,编码表)  
FileWriter是使用默认码表写出文件, 如果需要使用指定码表写出, 那么可以使用OutputStreamWriter(字节流,编码表)

### 3. `BufferedWriter`和`BufferedReader`
BufferedReader的readLine()方法可以读取一行字符(不包含换行符号)  
BufferedWriter的newLine()可以输出一个跨平台的换行符号"\r\n"
```java
public class Test {
    public static void main(String[] args) {
        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:/Users/PatrickYates/Desktop/hello.txt",true));  // 加true续写，不覆盖前面的内容
                BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/PatrickYates/Desktop/hello.txt"))
        ) {
            // 向文件中写入数据
            char array[] = "hello,world!".toCharArray();
            bufferedWriter.write(array);

            // 从文件中读取数据
            int n = 0;
            while((n = bufferedReader.read()) != -1){
                System.out.print((char)n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
```java
public class Test {
    public static void main(String[] args) {
        try (
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:/Users/PatrickYates/Desktop/hello.txt",true));  // 加true续写，不覆盖前面的内容
                BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/PatrickYates/Desktop/hello.txt"))
        ) {
            // 向文件中写入数据
            char array[] = "hello,world!".toCharArray();
            bufferedWriter.write(array);

            // 从文件中读取数据
            String line;
            while((line = bufferedReader.readLine()) != null){
                System.out.println(new String(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


# 二、字节流(InputStream和OutputStream)：非纯文本
![](http://p1.bqimg.com/567571/19bbbbe055ddc94a.png)

`InputStream`：抽象类，所有InputStream类的父类

`FileInputStream`  
`ByteArrayInputStream`  
`ObjectInputStream`  
`BufferedInputStream`  
`DataInputStream`

`FilterInputStream`  
`PipedInputStream`：PipedInputStream和PipedOutputStream一般是结合使用的，这两个类用于在两个线程间进行管道通信，一般在一个线程中执行PipedOutputStream 的write操作，而在另一个线程中执行PipedInputStream的read操作。单独使用PipedInputStream或单独使用PipedOutputStream时没有任何意义的，必须将二者通过connect方法（或在构造函数中传入对应的流）进行连接绑定
`SequenceInputStream`  


### 1. `FileInputStream`
用于读取文件内容
```java
public class Test{
    public static void main(String[] args) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            // 向文件中写入数据
            byte array[] = "abc".getBytes();
            outputStream = new FileOutputStream("C:/Users/PatrickYates/Desktop/hello.txt");
            outputStream.write(array);

            // 从文件中读取数据
            inputStream = new FileInputStream("C:/Users/PatrickYates/Desktop/hello.txt");
            System.out.println(inputStream.available());
            byte data[] = new byte[inputStream.available()];    // 获取文件的总大小(长度)
            inputStream.read(data);
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

为了较为安全的释放IO流访问的资源，我们花费了将近一半的篇幅，来写释放资源的代码。  
所以，在JDK1.7中，我们可以用如下方法来实现：
```java
public class Test{
    public static void main(String[] args) {
        try ( 
            OutputStream outputStream = new FileOutputStream("C:/Users/PatrickYates/Desktop/hello.txt");
            InputStream inputStream = new FileInputStream("C:/Users/PatrickYates/Desktop/hello.txt");
        ) {
            // 向文件中写入数据
            byte array[] = "abc".getBytes();        
            outputStream.write(array);

            // 从文件中读取数据
            System.out.println(inputStream.available());
            byte data[] = new byte[inputStream.available()];    // 获取文件的总大小(长度)
            inputStream.read(data);
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
将资源的创建放到了try后面的小括号中，省去了资源释放的代码。

### 2. `ByteArrayInputStream`
ByteArrayInputStream类本身采用了适配器设计模式，它把字节数组类型转换为输入流类型，使得程序能够对字节数组进行读操作。 
把字节串(或叫字节数组)变成输入流的形式
主要是应对流的来源和目的地不一定是文件这种情况，比如说可能是内存，可能是数组。
```java
public class Test{  
    public static void main(String[] args) {  
    byte data[] = "abc".getBytes();  
    InputStream inputStream = new ByteArrayInputStream(data);  

    byte data0[] = new byte[data.length];   
    try {  
        inputStream.read(data0);  
        System.out.println(new String(data0));  
    } catch (IOException e) {  
        e.printStackTrace();  
    } finally {  
        if (inputStream != null) {  
        try {  
            inputStream.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        }  
    }  
    }  
}  
```
[字节数组输入流 ByteArrayInputStream](http://zzhangyx.iteye.com/blog/1530923)  
[java io系列02之 ByteArrayInputStream的简介,源码分析和示例(包括InputStream)](http://www.cnblogs.com/skywang12345/p/io_02.html)
```java
public class ByteArrayTester {  
    public static void main(String[] args) throws IOException {  
        byte[] buff = new byte[] { 2, 15, 67, -1, -9, 9 };  
        ByteArrayInputStream in = new ByteArrayInputStream(buff, 1, 4);  
        int data = in.read();  
        while (data != -1) {  
            System.out.println(data + " ");  
            data = in.read();  
        }  
        try {
            in.close();// ByteArrayInputSystem 的close()方法实际上不执行任何操作
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}  
```

### 3. `ObjectInputStream`
可以用于读取对象，但是读取的对象必须实现 Serializable 接口。objectOutputStream写入文件中的对象是乱码。
```java
public class Test {
    public static void main(String[] args) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:/Users/PatrickYates/Desktop/hello.txt"));
            User user1 = new User();
            user1.setNum(2);
            objectOutputStream.writeObject(user1);

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("C:/Users/PatrickYates/Desktop/hello.txt"));
            User user2 = (User)objectInputStream.readObject();
            System.out.println(user2.getNum());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class User implements Serializable {
    private static final long serialVersionUID = -8987587467273881932L;
    private int num;
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    @Override
    public String toString() {
        return "User [num=" + num + "]";
    }
}
```

### 4. `BufferedInputStream`
提供了一个缓冲的功能，可以避免大量的磁盘IO。因为像FileInputStream这种，每一次的读取，都是一次磁盘IO。
```java
public class Test{
    public static void main(String[] args) {
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream("C:/Users/PatrickYates/Desktop/hello.txt"));
            byte data[] = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(data);
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(bufferedInputStream != null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```


### 5. `DataInputStream`
可以返回一些基本类型或者是String类型，否则的话，只能返回byte类型的数据，利用该类，我们可以更好的操作数据。
```java
public class Test{
    public static void main(String[] args) {
        DataOutputStream dataOutputStream = null;
        try {
            // 向文件中写入数据
            dataOutputStream = new DataOutputStream(new FileOutputStream("C:/Users/PatrickYates/Desktop/hello.txt"));
            dataOutputStream.writeInt(666);

            // 从文件中读取数据
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream("C:/Users/PatrickYates/Desktop/hello.txt"));
            System.out.println(dataInputStream.readInt());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(dataOutputStream != null){
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```


# 参考资料
[Java IO：常见Java IO流介绍](https://github.com/pzxwhc/MineKnowContainer/issues/23)  
[Java IO流应用实例](http://www.yiibai.com/java/think10.5.html)  
[设计模式 -- 装饰器模式（主要用于为对象动态的添加功能）](https://github.com/pzxwhc/MineKnowContainer/issues/3)