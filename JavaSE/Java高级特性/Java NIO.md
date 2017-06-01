在标准的IO当中，都是基于字节流/字符流进行操作的，而在NIO中则是是基于Channel和Buffer进行操作，其中的Channel的虽然模拟了流的概念，实则大不相同。

区别	Stream	Channel
支持异步	不支持	支持
是否可双向传输数据	不能，只能单向	可以，既可以从通道读取数据，也可以向通道写入数据
是否结合Buffer使用	不	必须结合Buffer使用
性能	较低	较高

Java NIO的通道类似流，但又有些不同：
==>既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
==>通道可以异步地读写。
==>通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。
正如上面所说，从通道读取数据到缓冲区，从缓冲区写入数据到通道。如下图所示：  
![](http://i1.piimg.com/567571/90ade0c6cd9df574.png)

# 一、java的nio的channel的实现的作用
==>FileChannel 从文件中读写数据。
==>DatagramChannel 能通过UDP读写网络中的数据。
==>SocketChannel 能通过TCP读写网络中的数据。
==>ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
==>Pipe
对于Socket通道来说存在直接创建新Socket通道的方法，而对于文件通道来说，升级之后的FileInputStream、FileOutputStream和RandomAccessFile提供了getChannel（）方法来获取通道。

```java
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        // 把文件加载到内存
        RandomAccessFile aFile = new RandomAccessFile("D:/nio-data.txt", "rw");
        // 从文件中获取通道
        FileChannel inChannel = aFile.getChannel();
        // 从通道中注册一个缓冲区，每次48个字节
        ByteBuffer buf = ByteBuffer.allocate(48);
        // 读取第一次
        int bytesRead = inChannel.read(buf);
        // 看文件中是否有数据
        while (bytesRead != -1) {
            // 打印第一次读取字符的个数
            System.out.println("Read " + bytesRead);
            // 注意buf.flip()的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据。
            buf.flip();
            // 打印每一个字符到控制台
            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }
            // 清空这次buffer
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        // RandomAccessFile的close方法会将对应的非空channel关闭
        aFile.close();
    }
}
```

# Buffer
Java NIO中的Buffer用于和NIO通道进行交互。数据是从通道读入缓冲区，从缓冲区写入到通道中的。
缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。

### Buffer的基本用法
使用Buffer读写数据一般遵循以下四个步骤：
1. 写入数据到Buffer：当向buffer写入数据时，buffer会记录下写了多少数据
2. 调用flip()方法：一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式
3. 从Buffer中读取数据：在读模式下，可以读取之前写入到buffer的所有数据
4. 调用clear()方法或者compact()方法：一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。clear()方法会清空整个缓冲区，compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。

```java
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("D:/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();
            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
```


# Scatter/Gather


# 通道之间的数据传输
```java
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);
    }
}
```

# FileChannel
```java
public static void main(String[] args) throws IOException {
    RandomAccessFile aFile = new RandomAccessFile("data.text", "rw");
    FileChannel channel = aFile.getChannel();

    // (1)在FileChannel的某个特定位置进行数据的读/写操作
    long pos = channel.position();
    channel.position(pos + 4);

    // (2)返回该实例所关联文件的大小
    System.out.println(channel.size());

    // (3)截取一个文件。截取文件时，文件将中指定长度后面的部分将被删除
    channel.truncate(10);

    // (4)将通道里尚未写入磁盘的数据强制写到磁盘上
    channel.force(true);

    // 1. 数据读取
    ByteBuffer buf = ByteBuffer.allocate(48);
    int bytesRead = channel.read(buf);
    while (bytesRead != -1) {
        buf.flip();
        while(buf.hasRemaining()){
            System.out.print((char) buf.get());
        }
        buf.clear();
        bytesRead = channel.read(buf);
    }

    // 2. 数据写入
    // String newData = "New String to write to file..." + System.currentTimeMillis();
    // ByteBuffer buf = ByteBuffer.allocate(48);
    // buf.clear();
    // buf.put(newData.getBytes());
    // buf.flip();
    // while(buf.hasRemaining()) {
    //     channel.write(buf);
    //     System.out.println(channel.position());
    // }


    // 必须关闭
    channel.close();
}
```

# SocketChannel
```java
public static void main(String[] args) throws IOException {
    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.connect(new InetSocketAddress("http://jenkov.com", 30001));

    ByteBuffer buf = ByteBuffer.allocate(48);
    int bytesRead = socketChannel.read(buf);
    while (bytesRead != -1) {
        buf.flip();
        while(buf.hasRemaining()){
            System.out.print((char) buf.get());
        }
        buf.clear();
        bytesRead = socketChannel.read(buf);
    }

    // 写入SocketChannel
    // String newData = "New String to write to file..." + System.currentTimeMillis();
    // ByteBuffer buf = ByteBuffer.allocate(48);
    // buf.clear();
    // buf.put(newData.getBytes());
    // buf.flip();
    // while(buf.hasRemaining()) {
    //     channel.write(buf);
    // }

    socketChannel.close();
}
```

# ServerSocketChannel
```java

```

# DatagramChannel、
http://ifeve.com/datagram-channel/

```java
public static void main(String[] args) throws IOException {
    // 1. 打开DatagramChannel
    DatagramChannel channel = DatagramChannel.open();
    channel.socket().bind(new InetSocketAddress(9999));

    // 2. 接收数据
    ByteBuffer buf = ByteBuffer.allocate(48);
    buf.clear();
    channel.receive(buf);

    // 3. 发送数据
    String newData = "New String to write to file..." + System.currentTimeMillis();
    ByteBuffer buf2 = ByteBuffer.allocate(48);
    buf2.clear();
    buf2.put(newData.getBytes());
    buf2.flip();
    int bytesSent = channel.send(buf2, new InetSocketAddress("jenkov.com", 80));
}
```

# Pipe
Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。
http://ifeve.com/pipe/

```java
public static void main(String[] args) throws IOException {
    // 1. 创建管道
    Pipe pipe = Pipe.open();
    Pipe.SinkChannel sinkChannel = pipe.sink();

    // 2. 向管道写数据
    String newData = "New String to write to file..." + System.currentTimeMillis();
    ByteBuffer buf = ByteBuffer.allocate(48);
    buf.clear();
    buf.put(newData.getBytes());
    buf.flip();
    while(buf.hasRemaining()) {
        sinkChannel.write(buf);
    }

    // 3. 从管道读取数据
    Pipe.SourceChannel sourceChannel = pipe.source();
    ByteBuffer buf2 = ByteBuffer.allocate(48);
    int bytesRead = sourceChannel.read(buf2);
    while (bytesRead != -1) {
        buf.flip();
        while(buf.hasRemaining()){
            System.out.print((char) buf.get());
        }
        buf.clear();
        bytesRead = sourceChannel.read(buf);
    }

    // 4. 关闭Channel
    sinkChannel.close();
}
```
