参数名称 | 含义 | 默认值 | 示例 | 说明
|---|---|---|---|---|
-Xms | 初始堆大小 | 物理内存的1/64(<1GB) ，Server端JVM最好将-Xms和-Xmx设为相同值，开发测试机JVM可以保留默认值 | -Xms1000M | 默认(MinHeapFreeRatio参数可以调整)空余堆内存小于40%时，JVM就会增大堆直到-Xmx的最大限制.
-Xmx | 最大堆大小 | 物理内存的1/4(<1GB)，最佳设值应该视物理内存大小及计算机内其他内存开销而定 | -Xms1000M | 默认(MaxHeapFreeRatio参数可以调整)空余堆内存大于70%时，JVM会减少堆直到 -Xms的最小限制
-Xmn | 年轻代大小(1.4or lator) | 不熟悉最好保留默认值 | 默认值 | 注意：此处的大小是（eden+ 2 survivor space)。与jmap -heap中显示的New gen是不同的。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小。增大年轻代后,将会减小年老代大小.此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8
-XX:PermSize | 设置持久代(perm gen)初始值 | 物理内存的1/64，不熟悉保留默认值 | 默认值 | $1
-XX:MaxPermSize | 设置持久代最大值 | 物理内存的1/4，不熟悉保留默认值 | 默认值 | $1
-Xss | 每个线程的堆栈大小 | JDK5.0 以后每个线程堆栈大小为1M,以前每个线程堆栈大小为256K. | 默认值 | 根据应用的线程所需内存大小进行调整。在相同物理内存下,减小这个值能生成更多的线程.但是操作系统对一个进程内的线程数还是有限制的,不能无限生成,经验值在3000~5000左右，一般小的应用， 如果栈不是很深， 应该是128k够用的 大的应用建议使用256k。这个选项对性能影响比较大，需要严格的测试。（校长）和threadstacksize选项解释很类似,官方文档似乎没有解释,在论坛中有这样一句话:"”-Xss is translated in a VM flag named ThreadStackSize”一般设置这个值就可以了。
-XX:ThreadStackSize | Thread Stack Size | 上面的-Xss不需要设置，如果要设置直接设置这个参数就可以 | 默认值 | (0 means use default stack size) [Sparc: 512; Solaris x86: 320 (was 256 prior in 5.0 and earlier); Sparc 64 bit: 1024; Linux amd64: 1024 (was 0 in 5.0 and earlier); all others 0.]
-XX:NewRatio | 年轻代(包括Eden和两个Survivor区)与年老代的比值(除去持久代) | Xms=Xmx并且设置了Xmn的情况下，该参数不需要进行设置。 | 默认值 | -XX:NewRatio=4表示年轻代与年老代所占比值为1:4,年轻代占整个堆栈的1/5。
-XX:SurvivorRatio | Eden区与Survivor区的大小比值 | | 默认值 | 设置为8,则两个Survivor区与一个Eden区的比值为2:8,一个Survivor区占整个年轻代的1/10
-XX:LargePageSizeInBytes | 内存页的大小 | =128m，不可设置过大， 会影响Perm的大小 | 默认值 | 128m
-XX:+UseFastAccessorMethods | 原始类型的快速优化(jdk 1.6 or later) | - | 默认值 | $1
-XX:+DisableExplicitGC | 关闭System.gc() | - | 默认值 | 这个参数需要严格的测试
-XX:MaxTenuringThreshold | 垃圾最大年龄，表示对象被移到老年代的年龄阈值的最大值 | 15 | 默认值 | 控制对象能经过几次GC才被转移到老年代。回收如果设置为0的话,则年轻代对象不经过Survivor区,直接进入年老代. 对于年老代比较多的应用,可以提高效率.如果将此值设置为一个较大值,则年轻代对象会在Survivor区进行多次复制,这样可以增加对象再年轻代的存活 时间,增加在年轻代即被回收的概率。该参数只有在串行GC时才有效。
-XX:+AggressiveOpts | 加快编译 | - | 默认值 | 启用该选项之后，需要考虑到性能的提升，同样也需要考虑到性能提升所带来的不稳定风险。
-XX:+UseBiasedLocking | 锁机制的性能改善 (Java 5 update 6 or later) | + | 默认值 | Java 5 HotSpot JDK需要明确的命令来启用这个特性，在使用-XX:+AggressiveOpts选项，有偏见的锁会Java 5中会被自动启用。在Java 6中是默认启用的。
-XX:TLABWasteTargetPercent | TLAB占eden区的百分比 | 1% | 默认值 | 无
-XX:+CollectGen0First | FullGC时是否先YGC | false | 默认值 | 无


-Xmx3550m：堆的最大值。设置JVM最大可用内存为3550M。
-Xms3550m：堆的最小值。设置JVM促使内存为3550m。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存。
-Xmn2g：分配给新生代的堆大小。设置年轻代大小为2G。整个JVM内存大小 = 年轻代大小 + 年老代大小 + 持久代大小。持久代一般固定大小为64m，所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。
-Xss128k：栈内存容量大小（线程堆栈）。设置每个线程的堆栈大小。JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。更具应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。

-XX:+HeapDumpOnOutOfMemoryError：让虚拟机在出现内存溢出异常时Dump出当前的内存堆转储快照以便事后分析。
-XX:+PrintGCDetails：收集器日志参数
-XX:+PrintReferenceGC：

-XX:PermSize：方法区大小即容量池容量
-XX:MaxPermSize：

-XX:MaxDirectMemorySize：DirectMemory容量，默认与Java堆最大值(-Xmx)一样

-XX:PretenureSizeThreshold：令大于这个设置值的对象直接在老年代分配
-XX:MaxTenuringThreshold：对象晋升老年代的年龄阈值


# 行为参数
http://blog.csdn.net/kl28978113/article/details/53031710