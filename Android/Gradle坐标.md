# servlet
```xml
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

# Spring + SpringMVC
```xml
// ----------------------Core Container----------------------
// https://mvnrepository.com/artifact/org.springframework/spring-webmvc
compile group: 'org.springframework', name: 'spring-webmvc', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-core
compile group: 'org.springframework', name: 'spring-core', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-beans
compile group: 'org.springframework', name: 'spring-beans', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-context
compile group: 'org.springframework', name: 'spring-context', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-context-support
compile group: 'org.springframework', name: 'spring-context-support', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-expression
compile group: 'org.springframework', name: 'spring-expression', version: '4.3.8.RELEASE'

// ----------------------AOP and Instrumentation----------------------
// https://mvnrepository.com/artifact/org.springframework/spring-aop
compile group: 'org.springframework', name: 'spring-aop', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-aspects
compile group: 'org.springframework', name: 'spring-aspects', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-instrument
compile group: 'org.springframework', name: 'spring-instrument', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-instrument-tomcat
compile group: 'org.springframework', name: 'spring-instrument-tomcat', version: '4.3.8.RELEASE'

// ----------------------Messaging----------------------
// https://mvnrepository.com/artifact/org.springframework/spring-messaging
compile group: 'org.springframework', name: 'spring-messaging', version: '4.3.8.RELEASE'

// ----------------------Data Access/Integration----------------------
// https://mvnrepository.com/artifact/org.springframework/spring-jdbc
compile group: 'org.springframework', name: 'spring-jdbc', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-tx
compile group: 'org.springframework', name: 'spring-tx', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-orm
compile group: 'org.springframework', name: 'spring-orm', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-oxm
compile group: 'org.springframework', name: 'spring-oxm', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-jms
compile group: 'org.springframework', name: 'spring-jms', version: '4.3.8.RELEASE'

// ----------------------Web----------------------
// https://mvnrepository.com/artifact/org.springframework/spring-web
compile group: 'org.springframework', name: 'spring-web', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-webmvc
compile group: 'org.springframework', name: 'spring-webmvc', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-webmvc-portlet
compile group: 'org.springframework', name: 'spring-webmvc-portlet', version: '4.3.8.RELEASE'
// https://mvnrepository.com/artifact/org.springframework/spring-websocket
compile group: 'org.springframework', name: 'spring-websocket', version: '4.3.8.RELEASE'

// ----------------------Test----------------------
// https://mvnrepository.com/artifact/org.springframework/spring-test
compile group: 'org.springframework', name: 'spring-test', version: '4.3.8.RELEASE'
```

# Mybatis
```xml
// https://mvnrepository.com/artifact/org.mybatis/mybatis
compile group: 'org.mybatis', name: 'mybatis', version: '3.4.4'
// https://mvnrepository.com/artifact/org.mybatis/mybatis-spring
compile group: 'org.mybatis', name: 'mybatis-spring', version: '1.3.1'
// https://mvnrepository.com/artifact/mysql/mysql-connector-java
compile group: 'mysql', name: 'mysql-connector-java', version: '6.0.6'
```

# JUnit
```xml
// https://mvnrepository.com/artifact/junit/junit
compile group: 'junit', name: 'junit', version: '4.12'
```

# Log
```xml
// https://mvnrepository.com/artifact/commons-logging/commons-logging
compile group: 'commons-logging', name: 'commons-logging', version: '1.2'
// https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api
compile group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.8.2'
// https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
compile group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.8.2'
// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25'
// https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.25'
```

# 数据库连接池
```xml
// https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
compile group: 'org.apache.commons', name: 'commons-pool2', version: '2.4.2'

// https://mvnrepository.com/artifact/com.mchange/c3p0
compile group: 'com.mchange', name: 'c3p0', version: '0.9.5.2'

// https://mvnrepository.com/artifact/org.apache.commons/commons-dbcp2
compile group: 'org.apache.commons', name: 'commons-dbcp2', version: '2.1.1'

// https://mvnrepository.com/artifact/com.alibaba/druid
compile group: 'com.alibaba', name: 'druid', version: '1.0.29'
```

# Json
```xml
// https://mvnrepository.com/artifact/com.alibaba/fastjson
compile group: 'com.alibaba', name: 'fastjson', version: '1.2.31'

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
compile group: 'com.fasterxml.jackson.core', name: 'jackson-core', version: '2.8.8'
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
compile group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.8.8'
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
compile group: 'com.fasterxml.jackson.core', name: 'jackson-annotations', version: '2.8.8'
```

# Apache Commons
[Apache Commons工具集简介](http://zhoualine.iteye.com/blog/1770014)  
```xml
// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
compile group: 'org.apache.commons', name: 'commons-lang3', version: '3.5'
// https://mvnrepository.com/artifact/commons-io/commons-io
compile group: 'commons-io', name: 'commons-io', version: '2.5'
// https://mvnrepository.com/artifact/org.apache.commons/commons-collections4
compile group: 'org.apache.commons', name: 'commons-collections4', version: '4.1'
// https://mvnrepository.com/artifact/org.apache.commons/commons-math3
compile group: 'org.apache.commons', name: 'commons-math3', version: '3.6.1'
// https://mvnrepository.com/artifact/commons-beanutils/commons-beanutils
compile group: 'commons-beanutils', name: 'commons-beanutils', version: '1.9.3'
// https://mvnrepository.com/artifact/commons-dbutils/commons-dbutils
compile group: 'commons-dbutils', name: 'commons-dbutils', version: '1.6'
// https://mvnrepository.com/artifact/org.apache.commons/commons-pool2
compile group: 'org.apache.commons', name: 'commons-pool2', version: '2.4.2'
// https://mvnrepository.com/artifact/org.apache.commons/commons-dbcp2
compile group: 'org.apache.commons', name: 'commons-dbcp2', version: '2.1.1'
// https://mvnrepository.com/artifact/org.apache.commons/com.springsource.org.apache.commons.codec
compile group: 'org.apache.commons', name: 'com.springsource.org.apache.commons.codec', version: '1.6.0'
// https://mvnrepository.com/artifact/org.apache.commons/commons-digester3
compile group: 'org.apache.commons', name: 'commons-digester3', version: '3.2'
// https://mvnrepository.com/artifact/org.apache.commons/commons-exec
compile group: 'org.apache.commons', name: 'commons-exec', version: '1.3'
// https://mvnrepository.com/artifact/org.apache.commons/commons-configuration2
compile group: 'org.apache.commons', name: 'commons-configuration2', version: '2.1.1'
// https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload
compile group: 'commons-fileupload', name: 'commons-fileupload', version: '1.3.2'
// https://mvnrepository.com/artifact/org.apache.commons/commons-compress
compile group: 'org.apache.commons', name: 'commons-compress', version: '1.13'
// https://mvnrepository.com/artifact/org.apache.commons/commons-csv
compile group: 'org.apache.commons', name: 'commons-csv', version: '1.4'
// https://mvnrepository.com/artifact/org.apache.commons/commons-email
compile group: 'org.apache.commons', name: 'commons-email', version: '1.4'
```
