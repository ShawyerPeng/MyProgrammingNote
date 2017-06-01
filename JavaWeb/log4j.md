1. 优先级
`FATAL`：
`ERROR`：严重错误，主要是程序的错误
`WARN`：一般警告，比如session丢失
`INFO`：一般要显示的信息，比如登录登出
`DEBUG`：程序的调试信息

2. Appender
`org.apache.log4j.ConsoleAppender`：控制台
`org.apache.log4j.FileAppender`：文件
`org.apache.log4j.DailyRollingFileAppender`：每天产生一个日志文件
`org.apache.log4j.RollingFileAppender`：文件大小到达指定尺寸的时候产生一个新的文件
`org.apache.log4j.WriterAppender`：将日志信息以流格式发送到任意指定的地方
`org.apache.log4j.SocketAppender`：Socket
`org.apache.log4j.NtEventLogAppender`：NT的Event Log
`org.apache.log4j.JMSAppender`：电子邮件

3. 日志输出格式layout
`org.apache.log4j.HTMLLayout`：以HTML表格形式布局 
`org.apache.log4j.PatternLayout`：可以灵活地指定布局模式 
`org.apache.log4j.SimpleLayout`：包含日志信息的级别和信息字符串
`org.apache.log4j.TTCCLayout`：包含日志产生的时间、线程、类别等信息

4. 打印参数
`%m`：输出代码中指定的消息，产生的日志具体信息
`%p`：输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL  
`%r`：输出自应用启动到输出该log信息耗费的毫秒数  
`%c`：输出所属的类目，通常就是所在类的全名  
`%t`：输出产生该日志事件的线程名  
`%n`：输出一个回车换行符，Windows平台为“/r/n”，Unix平台为“/n”  
`%d`：输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921  
`%l`：输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)
`%L`: 输出代码中的行号
`%x`：输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像Java servlets这样的多客户多线程的应用中
`-X`：X信息输出时左对齐
`%F`：输出日志消息产生时所在的文件名称

 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
 1)`%20c`：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
 2)`%-20c`:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
 3)`%.30c`:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
 4)`%20.30c`:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边较远输出的字符截掉。


```shell
# Global logging configuration 根Logger
log4j.rootLogger=DEBUG, stdout, D, E
# Console output 日志信息输出目的地Appender
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n

### 输出DEBUG级别以上的日志到D://logs/debug.log ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = D://logs/debug.log    # File指定消息输出到debug.log文件
log4j.appender.D.MaxFileSize = 500KB        # MaxFileSize指定在达到该大小时，将原来内容移到debug.log.1文件
log4j.appender.D.MaxBackupIndex = 10        # MaxBackupIndex指定可以产生的滚动文件的最大数
log4j.appender.D.Append = true              # Append指定将消息增加到指定文件中，false将覆盖内容
log4j.appender.D.ImmediateFlush = true      # ImmediateFlush指定所有的消息都会被立即输出
log4j.appender.D.Threshold = DEBUG          # Threshold指定日志消息的输出最低层次
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

### 输出ERROR级别以上的日志到D://logs/error.log ###
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File = D://logs/error.log
log4j.appender.E.Append = true
log4j.appender.E.Threshold = ERROR
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
```

# 代码中使用
```java
public class Log4JTest extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Log4JTest.class);

    public void init() {
        /* 找到在 web.xml 中指定的 log4j.properties 文件并读取配置信息 */
        String prefix =  getServletContext().getRealPath("/");
        String file = getInitParameter("log4j");
        System.out.println("................log4j start");
        if(file != null) {
            PropertyConfigurator.configure(prefix+file);
        }
    }

    public static void main(String[] args) {
        // BasicConfigurator.configure ()： 自动快速地使用缺省Log4j环境。  
        // DOMConfigurator.configure ( String filename ) ：读取XML形式的配置文件
        // 读取使用Java的特性文件编写的配置文件
        PropertyConfigurator.configure("D:/Code/conf/log4j.properties");
        logger.debug("debug");
        logger.error("error");
    }
}
```

# Spring中使用Log4J
在web.xml配置文件中配置Log4j监听器和log4j.properties文件
```xml
<context-param>
    <param-name>log4jConfigLocation</param-name>
    <param-value>classpath:/config/log4j.properties</param-value>
</context-param>
<context-param>
    <param-name>log4jRefreshInterval</param-name>
    <param-value>60000</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
</listener>
```