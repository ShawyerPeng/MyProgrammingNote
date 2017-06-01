# 定义
网络服务。 程序编写的功能，可以通过网络发布。
一种跨编程语言和跨操作系统平台的远程调用技术。
XML+XSD,SOAP和WSDL就是构成WebService平台的三大技术。
SOAP提供了标准的RPC方法来调用Web Service。SOAP协议 = HTTP协议 + XML数据格式
Rest-ful WebService 

作用是在两个独立的程序中，互相调用对方的程序方法。 
它是一种接口开放调用的手段，或者说是程序与程序之间的通用的调用协议。 

举个例子，比如在Windows Server服务器上有个C#.Net开发的应用A，在Linux上有个Java语言开发的应用B，B应用要调用A应用，或者是互相调用。用于查看对方的业务数据。
天气预报接口。无数的应用需要获取天气预报信息；这些应用可能是各种平台，各种技术实现；而气象局的项目，估计也就一两种，要对外提供天气预报信息，这个时候，如何解决呢？

# Restful与webService区别


JAX-WS（JSR-224）：Java API for XML-Based Web Services，基于SOAP
JAX-RS（JSR-339）：Java API for RESTful Web Services