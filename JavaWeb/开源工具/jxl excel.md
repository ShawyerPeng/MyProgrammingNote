# 使用
1. 创建工作薄Workbook
    ```java
    WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
    ```

2. 创建工作表Sheet
    ```java
    WritableSheet sheet = workbook.createSheet("第一页", 0);
    ```

3. 创建标签Label（表示col+1列，row+1行，标题内容是title）
    ```java
    Label label = new Label(col, row, title);
    ```

4. 将标签加入到工作表中
    ```java
    sheet.addCell(label);
    ```

5. 填充数据
    * 填充数字
        ```java
        jxl.write.Number num = new jxl.write.Number(1, 1, 250);
        sheet.addCell(num);
        ```

    * 填充格式化的数字
        ```java
        jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");
        jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat(nf);
        jxl.write.Number num = new jxl.write.Number(2, 1, 2.451, wcf);
        sheet.addCell(num);
        ```

    * 填充日期
        ```java
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String newdate = sdf.format(new Date());
        label = new Label(2, 2, newdate);
        sheet.addCell(label);
        ```

    * 填充文本
        ```java
        label = new Label(3, 3, "周星驰");
        sheet.addCell(label);
        ```

    * 填充boolean值
        ```java
        jxl.write.Boolean bool = new jxl.write.Boolean(4, 1, true);
        sheet.addCell(bool);
        ```

6. 设置WritableCellFormat
    ```java
    /************ 设置纵横打印（默认为纵打）、打印纸 ******************/
    // sheet.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,0,0);
    // sheet.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A4,0,0);
    // sheet.addRowPageBreak(12);
    jxl.SheetSettings sheetset = sheet.getSettings();
    sheetset.setProtected(false);

    // sheet.setColumnView(0,5);
    // sheet.setColumnView(1,12);

    /************** 设置单元格字体 ***************/
    WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
    WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
    WritableFont Font = new WritableFont(WritableFont.ARIAL, 10,WritableFont.NO_BOLD);

    /************** 以下设置几种格式的单元格 *************/
    // 用于正文居左
    WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
    wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
    wcf_left.setVerticalAlignment(VerticalAlignment.TOP); // 文字垂直对齐
    wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
    wcf_left.setWrap(false); // 文字是否换行

    // 用于正文居右
    WritableCellFormat wcf_right = new WritableCellFormat(NormalFont);
    wcf_right.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
    wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
    wcf_right.setAlignment(Alignment.RIGHT); // 文字水平对齐
    wcf_right.setWrap(false); // 文字是否换行

    // 用于正文居中
    WritableCellFormat wcf_center = new WritableCellFormat(Font);
    //            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
    wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
    wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
    wcf_center.setWrap(false); // 文字是否换行

    // 用于正文居中标题
    WritableCellFormat wcf_title = new WritableCellFormat(NormalFont);
    wcf_title.setBorder(Border.ALL, BorderLineStyle.THICK); // 线条
    wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
    wcf_title.setAlignment(Alignment.CENTRE); // 文字水平对齐
    wcf_title.setWrap(false); // 文字是否换行

    // 用于跨行
    WritableCellFormat wcf_merge = new WritableCellFormat(BoldFont);
    wcf_merge.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
    wcf_merge.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
    wcf_merge.setAlignment(Alignment.CENTRE); // 文字水平对齐
    wcf_merge.setWrap(true); // 文字是否换行

    //换行加居左
    WritableCellFormat wcf_merge_left = new WritableCellFormat(Font);
    //            wcf_merge.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
    wcf_merge.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
    wcf_merge.setAlignment(Alignment.LEFT); // 文字水平对齐
    wcf_merge.setWrap(true); // 文字是否换行
    ```

7. 写入文件
```java
workbook.write();
```

8. 关闭文件
```java
workbook.close();
```

9. 行列的批量操作
```java
// 获取所有的工作表
jxl.write.WritableSheet[] sheetList = workbook.getSheets();
// 获取第1列所有的单元格
jxl.Cell[] cellc = sheet.getColumn(0);
// 获取第1行所有的单元格
jxl.Cell[] cellr = sheet.getRow(0);
// 获取行数、列数
int row = s.getRows();
int col = s.getColumns();
// 获取第1行第1列的单元格
Cell c = sheet.getCell(0, 0);

// 获取单元格的值
String value = c.getContents();
```

# 从数据库导出excel
参考：[Java实现将Excel导入数据库和从数据库中导出为Excel](http://www.cnblogs.com/gaopeng527/p/4357570.html)

`DBhelper.java`
```java
import java.sql.*;

public class DBhelper {
    Connection conn = null;
    ResultSet rs = null;
    //连接数据库
    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/excel","root","123");
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            System.out.println("数据库驱动加载失败！");
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println("数据库连接失败！");
        }
    }

    //查询
    public ResultSet Search(String sql, String str[]){
        connect();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            if(str != null){
                for(int i=0;i<str.length-1;i++){
                    pst.setString(i+1, str[i]);
                }
                pst.setInt(str.length, Integer.parseInt(str[str.length-1]));
            }
            rs = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //增删修改
    public int AddU(String sql, String str[]){
        int a =0;
        connect();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            if(str != null){
                for(int i=0;i<str.length-1;i++){
                    pst.setString(i+1, str[i]);
                }
                pst.setInt(str.length, Integer.parseInt(str[str.length-1]));
            }
            a = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
}
```

`User.java`
```java
public class User {
    private int id;
    private String username;
    private String password;

    public User(){
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```


# 参考资料
[官方api文档](http://jexcelapi.sourceforge.net/resources/javadocs/2_6_10/docs/index.html)  
[Jxl使用总结](http://lavasoft.blog.51cto.com/62575/174244/)  
[jxl servlet示例](http://codsoul.iteye.com/blog/1219367)  
[Servlet 生成excel 并下载JXL方式](http://ganlangreen-163-com.iteye.com/blog/808379)  
[jxl导入/导出excel](http://www.cnblogs.com/linjiqin/p/3540266.html)  