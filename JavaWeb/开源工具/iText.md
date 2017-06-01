# 下载
```xml
<!-- https://mvnrepository.com/artifact/com.itextpdf/itext7-core -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.0.2</version>
</dependency>
```


# 代码示例
```java
// 创建 PdfWriter 对象
PdfWriter writer = new PdfWriter(DEST);
// 实例化文档对象
PdfDocument pdf = new PdfDocument(writer);
// 初始化document
Document document = new Document(pdf);
// 添加段落
document.add(new Paragraph("Hello World!"));
// 关闭document
document.close();
```

* Font
```java
// Create a PdfFont
PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
// Add a Paragraph
document.add(new Paragraph("iText is:").setFont(font));
```

* List
```java
// Create a List
List list = new List()
    .setSymbolIndent(12)
    .setListSymbol("\u2022")
    .setFont(font);
// Add ListItem objects
list.add(new ListItem("Never gonna give you up"))
    .add(new ListItem("Never gonna let you down"))
    .add(new ListItem("Never gonna run around and desert you"))
    .add(new ListItem("Never gonna make you cry"))
    .add(new ListItem("Never gonna say goodbye"))
    .add(new ListItem("Never gonna tell a lie and hurt you"));
// Add the list
document.add(list);
```

* Image
```java
Image fox = new Image(ImageDataFactory.create("src/main/resources/img/dog.bmp"));
Image dog = new Image(ImageDataFactory.create("src/main/resources/img/fox.bmp"));
Paragraph p = new Paragraph("The quick brown ")
            .add(fox)
            .add(" jumps over the lazy ")
            .add(dog);
document.add(p);
```

* Publishing a database  
```java
PdfWriter writer = new PdfWriter(dest);
PdfDocument pdf = new PdfDocument(writer);    
Document document = new Document(pdf, PageSize.A4.rotate());
document.setMargins(20, 20, 20, 20);
PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
Table table = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
table.setWidthPercent(100);
BufferedReader br = new BufferedReader(new FileReader("src/main/resources/data/united_states.csv"));
String line = br.readLine();
process(table, line, bold, true);
while ((line = br.readLine()) != null) {
    process(table, line, font, false);
}
br.close();
document.add(table);
document.close();
```

* 中文支持  

    ```java
    // 1. 推荐，自定义字体目录
    PdfFont font1 = PdfFontFactory.createFont("D:\\方正喵呜体.ttf", PdfEncodings.IDENTITY_H, false);
    document.add(new Paragraph("我是中文").setFont(font1));

    // 2. 内置的字体STSongStd-Light & UniGB-UCS2-H
    PdfFont font2 = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
    document.add(new Paragraph("我是中文").setFont(font2));

    // 3. 注册字体
    FontProgramFactory.registerFont("c:/windows/fonts/STXINGKA.TTF", "STXINGKA");
    PdfFont font3 = PdfFontFactory.createRegisteredFont("STXINGKA");
    document.add(new Paragraph("我是中文").setFont(font3));
    ```

* 服务器目录字体注册  
http://developers.itextpdf.com/examples/font-examples/itext7-why-my-font-not-applied-when-i-create-pdf-document
http://developers.itextpdf.com/content/best-itext-questions-stackoverview/font-examples/itext7-how-can-i-load-font-web-infresourcesfontsfoobarttf
```java
String path = getServletContext().getRealPath("/WEB-INF/resources/fonts/foobar.ttf");
PdfFontFactory.register(path);
```

* 字体大小  
```java
Text title1 = new Text("The Strange Case of ").setFontSize(12);
Text title2 = new Text("Dr. Jekyll and Mr. Hyde").setFontSize(16);
Text author = new Text("Robert Louis Stevenson");
Paragraph p = new Paragraph().setFontSize(8)
        .add(title1).add(title2).add(" by ").add(author);
document.add(p);
```

* web下载  
```java
// 设置response参数，可以打开下载页面  
response.reset();  
response.setContentType("application/pdf;charset=utf-8");    
response.addHeader("Content-Disposition","attachment;filename="+ new String( fileName.getBytes("gb2312"), "ISO8859-1"));
```

* 标题
```java
// 大标题
Paragraph p = new Paragraph("我是标题").setTextAlignment(TextAlignment.CENTER).setFontSize(18);
document.add(p);
```

# 完整代码
```java
String DEST = "C:\\Users\\PatrickYates\\Desktop\\HelloWorld.pdf";

// 创建 PdfWriter 对象
PdfWriter writer = new PdfWriter(DEST);
// 实例化文档对象
PdfDocument pdf = new PdfDocument(writer);
// 初始化document
Document document = new Document(pdf);
// 添加段落
document.add(new Paragraph("Hello World!"));

// Create a List
List list = new List()
        .setSymbolIndent(12)
        .setListSymbol("\u2022");
// Add ListItem objects
list.add(new ListItem("Never gonna give you up"))
        .add(new ListItem("Never gonna let you down"))
        .add(new ListItem("Never gonna run around and desert you"))
        .add(new ListItem("Never gonna make you cry"))
        .add(new ListItem("Never gonna say goodbye"))
        .add(new ListItem("Never gonna tell a lie and hurt you"));
// Add the list
document.add(list);

Text title1 = new Text("The Strange Case of ").setFontSize(12);
Text title2 = new Text("Dr. Jekyll and Mr. Hyde").setFontSize(16);
Text author = new Text("Robert Louis Stevenson");
Paragraph p = new Paragraph().setFontSize(8)
        .add(title1).add(title2).add(" by ").add(author);
document.add(p);

// 关闭document
document.close();
```
