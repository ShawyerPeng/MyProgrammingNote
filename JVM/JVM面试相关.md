- Java字节码文件的格式
- 内部类的存储方式
- 垃圾回收器的分类及优缺点
- 类在虚拟机中的加载过程
- 即时编译器的前后端优化方法
- CMS垃圾回收器的工作过程
- CAS指令以及其他线程安全的方法
- 各种内存溢出的情况，包括JNI调用

1. 内存模型以及分区，需要详细到每个区放什么。
2. 堆里面的分区：Eden，survival from to，老年代，各自的特点。
3. 对象创建方法，对象的内存分配，对象的访问定位。
4. GC的两种判定方法：引用计数与引用链。
5. GC的三种收集方法：标记清除、标记整理、复制算法的原理与特点，分别用在什么地方，如果让你优化收集方法，有什么思路？
6. GC收集器有哪些？CMS收集器与G1收集器的特点。
7. Minor GC与Full GC分别在什么时候发生？
8. 几种常用的内存调试工具：jmap、jstack、jconsole。
9. 类加载的五个过程：加载、验证、准备、解析、初始化。
10. 双亲委派模型：Bootstrap ClassLoader、Extension ClassLoader、ApplicationClassLoader。
11. 分派：静态分派与动态分派。

- JVM内存分哪几个区，每个区的作用是什么?

- 如何判断一个对象是否存活?(或者GC对象的判定方法)

- java垃圾回收机制

- 垃圾收集的方法

- java内存模型

- java类加载过程

- java类加载机制

- 类加载器双亲委派模型机制

- 什么是类加载器，类加载器有哪些?

- 简述java内存分配与回收策率以及Minor GC和Major GC


http://blog.csdn.net/hsk256/article/details/49104955
