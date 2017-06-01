# 下载图片
```java
public static void download(String src) throws IOException {
    // 构造URL
    URL url = new URL(src);
    // 打开连接
    URLConnection con = url.openConnection();
    //设置请求超时为5s
    con.setConnectTimeout(5*1000);
    // 输入流
    InputStream is = con.getInputStream();
    // 1K的数据缓冲
    byte[] bs = new byte[1024];
    // 读取到的数据长度
    int len;
    // 输出的文件流
    File sf = new File("/home/patrick/桌面");
    OutputStream os = new FileOutputStream(sf.getPath() + "/" + "avatar.jpg");
    // 开始读取
    while ((len = is.read(bs)) != -1) {
        os.write(bs, 0, len);
    }
    // 关闭所有连接
    os.close();
    is.close();
}
```

```java
public static void downImg(String src) {
    File file = new File("/home/patrick/桌面/yzm.jpg");
    InputStream input = null;
    FileOutputStream out = null;
    HttpGet httpGet = new HttpGet(src);
    try {
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        input = entity.getContent();
        int i;
        byte[] byt=new byte[1024];
        out=new FileOutputStream(file);
        while((i=input.read(byt))!=-1){
            out.write(byt);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            input.close(); 
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
```

# 完整代码
```java
private static CloseableHttpClient httpClient = HttpClients.createDefault();
private static BasicCookieStore cookieStore = new BasicCookieStore();
private static BasicClientCookie cookie;

public static void login(String num) throws IOException {
    cookie = new BasicClientCookie("JSESSIONID", "1234");
    cookie.setDomain("http://202.119.113.135");
    cookie.setPath("/");

    httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    
    HttpGet codeGet = new HttpGet("http://202.119.113.135/validateCodeAction.do");
    HttpPost loginPost = new HttpPost("http://202.119.113.135/loginAction.do");

    FileOutputStream out = null;
    InputStream in = null;
    HttpResponse codeResponse = httpClient.execute(codeGet);
    HttpEntity entity = codeResponse.getEntity();
    in = entity.getContent();

    File file = new File("/home/patrick/桌面/yzm.do");
    out = new FileOutputStream(file);
    byte[] byt = new byte[1024];
    int i;
    while ((i = in.read(byt)) != -1) {
        out.write(byt);
    }
    System.out.println("图片下载成功");
    in.close();
    out.close();

    //获取验证码
    String captcha;
    //输入验证码
    System.out.println("请输入验证码：");
    BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
    captcha = buff.readLine();
    
    // 创建参数队列
    List<NameValuePair> formParams = new ArrayList<NameValuePair>();
    formParams.add(new BasicNameValuePair("zjh", num));
    formParams.add(new BasicNameValuePair("mm", num));
    formParams.add(new BasicNameValuePair("v_yzm", captcha));
    try {
        loginPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
        CloseableHttpResponse postResponse = httpClient.execute(loginPost);
        try {
            HttpEntity postEntity = postResponse.getEntity();
            if (postEntity != null) {
                System.out.println("--------------------------------------");
                System.out.println("Response content: " + EntityUtils.toString(postEntity, "UTF-8"));
                System.out.println("--------------------------------------");
            }
            // 2. 进行登录后的操作
            HttpGet imgGet = new HttpGet("http://202.119.113.135/xjInfoAction.do?oper=img");
            CloseableHttpResponse imgResponse = httpClient.execute(imgGet);

            // 下载图片
            byte buffer[] = new byte[1024];
            HttpEntity imgEntity = imgResponse.getEntity();
            in = imgEntity.getContent();
            out = new FileOutputStream(new File("/home/patrick/桌面/hehe/myself.jpeg" + UUID.randomUUID()));
            int index = 0;
            while ((index = in.read(buffer)) != -1) {
                out.write(buffer, 0, index);
            }
            out.flush();

            imgResponse.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postResponse.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        httpClient.close();
    }
}
```

# Tesseract
```xml
<!-- https://mvnrepository.com/artifact/net.sourceforge.tess4j/tess4j -->
<dependency>
    <groupId>net.sourceforge.tess4j</groupId>
    <artifactId>tess4j</artifactId>
    <version>3.3.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/net.java.dev.jna/jna -->
<dependency>
    <groupId>net.java.dev.jna</groupId>
    <artifactId>jna</artifactId>
    <version>4.4.0</version>
</dependency>
```


# HttpClient
```xml
<!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpcomponents-client -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpcomponents-client</artifactId>
    <version>4.5.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpcore -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpcore</artifactId>
    <version>4.4.6</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpmime -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpmime</artifactId>
    <version>4.5.3</version>
</dependency>
```

```java

```

`response.getProtocolVersion()`：协议版本
`response.getStatusLine().getStatusCode()`：
`response.getStatusLine().getReasonPhrase()`：
`response.getStatusLine().toString()`：

`response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");`：添加消息头
`Header h1 = response.getFirstHeader("Set-Cookie");`：获取第一个消息头
`Header h2 = response.getLastHeader("Set-Cookie");`：获取最后一个消息头
`Header[] hs = response.getHeaders("Set-Cookie");`：获取所有消息头
或者使用HeaderIterator接口：
```java
HeaderIterator it = response.headerIterator("Set-Cookie");
while (it.hasNext()) {
    System.out.println(it.next());
}
```

## HTTP实体
`streamed`（流）: 内容通过流接收，或者实时生成（generated on the fly）。特别地，streamed包含了从HTTP响应接收到的实体。streamed实体一般是不可重复的。
`self-contained`（独立的）: 内容可能在内存中，或者通过与某一连接或其他实体独立的方式获得。self-contained实体一般是可重复的。这种类型的实体常常用来封装HTTP请求。
`wrapping`（封装）: 其内容从其他实体中获取。

当从HTTP响应读取内容时，为了连接管理，上面这些区分是很重要的。对于那些由一个应用程序创建的，并仅仅使用HttpClient发送的实体，streamed类实体和self-contained类实体之间的区别并不重要。这种情况下，streamed被认为是不可重复的，self-contained则是可重复的。


## GET
```java
// 1. 创建默认的httpClient实例
CloseableHttpClient httpClient = HttpClients.createDefault();
// 2. 创建HttpGet
HttpGet httpGet = new HttpGet("https://www.baidu.com");
// URIBuilder创建请求
URI uri = new URIBuilder()
        .setScheme("http")
        .setHost("www.google.com")
        .setPath("/search")
        .setParameter("q", "httpclient")
        .setParameter("btnG", "Google Search")
        .setParameter("aq", "f")
        .setParameter("oq", "")
        .build();
HttpGet httpGet1 = new HttpGet(uri);
// 打印请求的URI
System.out.println("executing request " + httpGet.getURI());

// 3. 执行get请求
CloseableHttpResponse response = httpClient.execute(httpGet1);
try {
    // 获取响应实体
    HttpEntity entity = response.getEntity();

    System.out.println("--------------------------------------");
    // 打印响应状态
    System.out.println(response.getStatusLine());
    // 打印状态码
    System.out.println("HTTP Status of response: " + response.getStatusLine().getStatusCode());
    if (entity != null) {
        // 打印响应内容长度
        System.out.println("Response content length: " + entity.getContentLength());
        // 打印响应内容
        System.out.println("Response content: " + EntityUtils.toString(entity));
    }
    System.out.println("------------------------------------");
} finally {
    response.close();
}
```

## POST
### Basic POST
```java
CloseableHttpClient httpClient = HttpClients.createDefault();
// 创建httpget
HttpPost httpPost = new HttpPost("https://www.baidu.com");

// 创建参数队列
List<NameValuePair> formParams = new ArrayList<NameValuePair>();
formParams.add(new BasicNameValuePair("username", "admin"));
formParams.add(new BasicNameValuePair("password", "123456"));
try {
    httpPost.setEntity(new UrlEncodedFormEntity(formParams,"UTF-8"));
    CloseableHttpResponse response = httpClient.execute(httpPost);
    try {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            System.out.println("--------------------------------------");
            System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
            System.out.println("--------------------------------------");
        }
    } finally {
        response.close();
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    // 关闭连接,释放资源
    try {
        httpClient.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### POST with JSON
```java
CloseableHttpClient client = HttpClients.createDefault();
HttpPost httpPost = new HttpPost("http://www.example.com");

String json = "{\"id\":1,\"name\":\"John\"}";
StringEntity entity = new StringEntity(json);
httpPost.setEntity(entity);
httpPost.setHeader("Accept", "application/json");
httpPost.setHeader("Content-type", "application/json");

CloseableHttpResponse response = client.execute(httpPost)
client.close();
```

### POST with the HttpClient Fluent API
```java
HttpResponse response = 
    Request.Post("http://www.example.com").bodyForm(
    Form.form().add("username", "John").add("password", "pass").build())
    .execute().returnResponse();
```

### POST Multipart Request
```java
CloseableHttpClient client = HttpClients.createDefault();
HttpPost httpPost = new HttpPost("http://www.example.com");

MultipartEntityBuilder builder = MultipartEntityBuilder.create();
builder.addTextBody("username", "John");
builder.addTextBody("password", "pass");
builder.addBinaryBody("file", new File("test.txt"), ContentType.APPLICATION_OCTET_STREAM, "file.ext");

HttpEntity multipart = builder.build();
httpPost.setEntity(multipart);

CloseableHttpResponse response = client.execute(httpPost);
client.close();
```

### Upload a File using HttpClient
```java
CloseableHttpClient client = HttpClients.createDefault();
HttpPost httpPost = new HttpPost("http://www.example.com");

MultipartEntityBuilder builder = MultipartEntityBuilder.create();
builder.addBinaryBody("file", new File("test.txt"),
    ContentType.APPLICATION_OCTET_STREAM, "file.ext");
HttpEntity multipart = builder.build();

httpPost.setEntity(multipart);

CloseableHttpResponse response = client.execute(httpPost);
client.close();
```

### Get File Upload Progress
继承`HttpEntityWrapper`
```java
CloseableHttpClient client = HttpClients.createDefault();
HttpPost httpPost = new HttpPost("http://www.example.com");

MultipartEntityBuilder builder = MultipartEntityBuilder.create();
builder.addBinaryBody("file", new File("test.txt"), ContentType.APPLICATION_OCTET_STREAM, "file.ext");
HttpEntity multipart = builder.build();

ProgressEntityWrapper.ProgressListener pListener = 
    new ProgressEntityWrapper.ProgressListener() {
    @Override
    public void progress(float percentage) {
        assertFalse(Float.compare(percentage, 100) > 0);
    }
};

httpPost.setEntity(new ProgressEntityWrapper(multipart, pListener));

CloseableHttpResponse response = client.execute(httpPost);
client.close();
```

## 创建实体内容
`StringEntity`，`ByteArrayEntity`，`InputStreamEntity`和`FileEntity`
```java

```

### HTML表单
`UrlEncodedFormEntity`
```java

```

### 内容分块
```java

```

### 上传文件
三个传值方法：`addPart`、`addBinaryBody`、`addTextBody`
```java
entityBuilder.addBinaryBody("file",new byte[]{},ContentType.DEFAULT_BINARY,"file.jpg");
entityBuilder.addPart("owner","111");
entityBuilder.addTextBody("paramter1", "aaa");
```

```java
CloseableHttpClient httpClient = HttpClients.createDefault();
try {
    HttpPost httpPost = new HttpPost("http://localhost:8080/upload");

    FileBody bin = new FileBody(new File("D:\\image\\img.jpg"));
    StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
    HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();
    httpPost.setEntity(reqEntity);

    System.out.println("executing request " + httpPost.getRequestLine());
    CloseableHttpResponse response = httpClient.execute(httpPost);
    try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
        }
        EntityUtils.consume(resEntity);
    } finally {
        response.close();
    }
} catch (ClientProtocolException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    try {
        httpclient.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### 接收文件
```java
//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
 //判断是否多文件上传
 if(multipartResolver.isMultipart(request)){
     //将request变成多部分request
     MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
     Map<String,Object> map=multiRequest.getParameterMap();
     //获取multiRequest 中所有的文件名
     Iterator<String> iter=multiRequest.getFileNames();
     //遍历所有文件
     while(iter.hasNext()){
        MultipartFile file=multiRequest.getFile(iter.next().toString());
        //to do
     }
 }
```

## 发送https请求
```java
KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));
// 加载keyStore d:\\tomcat.keystore
trustStore.load(instream, "123456".toCharArray());
instream.close();

// 相信自己的CA和所有自签名的证书
SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
//SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
// 只允许使用TLSv1协议
SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

// 创建连接
CloseableHttpClient client = HttpClients.custom()
        .setSSLContext(sslContext)
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
        .setSSLSocketFactory(sslsf)
        .build();
HttpGet httpGet = new HttpGet("");
httpGet.setHeader("Accept", "application/xml");

HttpResponse response = client.execute(httpGet);
```

## 代理
```java
HttpHost proxy = new HttpHost("localhost", 1080);
DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
CloseableHttpClient httpClient = HttpClients.custom()
        .setRoutePlanner(routePlanner)
        .build();
HttpGet httpGet = new HttpGet("https://www.google.com");
CloseableHttpResponse response = httpClient.execute(httpGet);
System.out.println(response.toString());
```


## Cookie
```java
BasicCookieStore cookieStore = new BasicCookieStore();
BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
cookie.setDomain("http://115.159.188.200:8001");
cookie.setPath("/");
cookieStore.addCookie(cookie);
CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

final HttpGet httpGet = new HttpGet("http://115.159.188.200:8001/do_login");

CloseableHttpResponse response = client.execute(httpGet);
```

```java
BasicCookieStore cookieStore = new BasicCookieStore();
BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
cookie.setDomain("http://115.159.188.200:8001");
cookie.setPath("/");
cookieStore.addCookie(cookie);

RequestConfig globalConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.DEFAULT)
        .build();

CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();

RequestConfig localConfig = RequestConfig.copy(globalConfig)
        .setCookieSpec(CookieSpecs.STANDARD_STRICT)
        .build();

final HttpGet httpGet = new HttpGet("http://115.159.188.200:8001/showImg/");
httpGet.setConfig(localConfig);
CloseableHttpResponse response = client.execute(httpGet);
System.out.println(EntityUtils.toString(response.getEntity()));
```

### Cookie specifications
Standard strict: State management policy compliant with the syntax and semantics of the wellbehaved
profile defined by RFC 6265, section 4.
• Standard: RFC 2965 HTTP状态管理规范
• Netscape draft (obsolete): 尽量不要使用，除非一定要保证兼容很旧的代码
• RFC 2965 (obsolete): HTTP状态管理规范
• RFC 2109 (obsolete): 
• Browser compatibility (obsolete): 这种方式，尽量模仿常用的浏览器，如IE和firefox
• Default: 
• Ignore cookies:忽略所有Cookie

### Choosing cookie policy
```java

```

### Custom cookie policy
```java

```

### Cookie persistence
```java

```

### HTTP state management and execution context
```java

```

### Configure Cookie Management on the HttpClient（全局设置）
```java
BasicCookieStore cookieStore = new BasicCookieStore();
BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
cookie.setDomain(".github.com");
cookie.setPath("/");
cookieStore.addCookie(cookie);
HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

final HttpGet request = new HttpGet("http://www.github.com");

response = client.execute(request);
assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
```

### Set the Cookie on the Request（仅限单个请求设置）
```java
BasicCookieStore cookieStore = new BasicCookieStore();
BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
cookie.setDomain(".github.com");
cookie.setPath("/");
cookieStore.addCookie(cookie);
instance = HttpClientBuilder.create().build();

HttpGet request = new HttpGet("http://www.github.com");

HttpContext localContext = new BasicHttpContext();
localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
// localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore); // before 4.3
response = instance.execute(request, localContext);
```

### 使用Cookie登录
```java
// 创建BasicCookieStore
BasicCookieStore cookieStore = new BasicCookieStore();
BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "1234");
cookie.setDomain("http://115.159.188.200:8001");
cookie.setPath("/");
cookieStore.addCookie(cookie);

/** RequestConfig globalConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.DEFAULT)
        .build();
CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build(); **/
CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

// 1. 登录
HttpPost httpPost = new HttpPost("http://115.159.188.200:8001/do_login/");
// 创建参数队列
List<NameValuePair> formParams = new ArrayList<NameValuePair>();
formParams.add(new BasicNameValuePair("name", "admin"));
formParams.add(new BasicNameValuePair("pwd", "9876543210"));
try {
    httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
    CloseableHttpResponse response = httpClient.execute(httpPost);
    try {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            System.out.println("--------------------------------------");
            System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
            System.out.println("--------------------------------------");
            // 查看保存的Cookies
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("Local cookie: " + cookies.get(i));
            }
        }
        // 2. 进行登录后的操作
        HttpGet httpGet = new HttpGet("http://115.159.188.200:8001/showImg/");
        CloseableHttpResponse response2 = httpClient.execute(httpGet);
        System.out.println(EntityUtils.toString(response2.getEntity()));
    } finally {
        response.close();
        response2.close();
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    // 关闭连接,释放资源
    try {
        httpClient.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
``：
































# 参考资料
[HttpClient 4 Tutorial](http://www.baeldung.com/httpclient-guide)  
[httpclient-tutorial.pdf](http://hc.apache.org/httpcomponents-client-ga/tutorial/pdf/httpclient-tutorial.pdf)  
[httpclient4.4中文教程（中级篇）](https://www.gitbook.com/book/kmg343/httpcl-ient4-4-no2/details)  
[HttpClient 4.3教程](http://www.yeetrack.com/?p=779)  
[Android Cookie rejected的解决方法](http://blog.csdn.net/sniffer12345/article/details/7496180)  
[Java 爬虫如何抓取、请求中文域名网站？](https://www.zhihu.com/question/20403264)  
http://liangbizhi.github.io/httpclient-4-3-x-chapter-1/
http://www.voidcn.com/blog/gjb724332682/article/p-6051924.html
https://www.ibm.com/developerworks/cn/opensource/os-httpclient/
http://blog.csdn.net/wangpeng047/article/details/19624529
http://blog.csdn.net/dianacody/article/details/39717825
http://www.open-open.com/68.htm
http://www.cnblogs.com/linux007/p/5782720.html
http://www.baeldung.com/httpclient-status-code
https://www.kancloud.cn/digest/spiders/117829
