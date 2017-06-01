# 介绍
jersey 是基于Java的一个轻量级RESTful风格的Web Services框架。

[官网](https://jersey.java.net/)

# Jar包详解：
`jersey-client` 是jersey提供的客户端包，封装了一些客户端操作的类
`jersey-container-servlet` 是jersey的核心，服务端必备包
`jersey-media-moxy` 是定义了jersry支持的常用的数据格式，json，xml都包括其中
`jersey-media-multipart` 是jersey的上传文件的支持

```xml
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.core/jersey-client -->
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-client</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.containers/jersey-container-servlet -->
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.containers/jersey-container-servlet-core -->
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet-core</artifactId>
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
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-moxy -->
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-moxy</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-jaxb -->
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-jaxb</artifactId>
    <version>2.25.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-multipart -->
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-multipart</artifactId>
    <version>2.25.1</version>
</dependency>
```

# 参考资料
[jersey使用指南](http://www.jianshu.com/p/15c32cb52da1)  
