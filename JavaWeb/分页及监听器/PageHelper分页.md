前些天按照视频里讲的做一个分页功能，可是不知道什么原因在页面就是不显示数据。昨天碰巧发现了一个分页插件，只需要一些设置就可以完成分页功能，非常非常方便。不过由于是新手，其中遇到了很多很多麻烦，不过幸好得到大神的帮助，最终完成了功能，非常感谢他十分耐心地帮我，给你一个么么哒(づ￣ 3￣)づ  
没想到做一个小小的分页功能也有这么多的不懂的地方，感觉要学的东西太多太多，加油努力学习吧~

首先给出项目Github地址：[Mybatis通用分页插件](https://github.com/pagehelper/Mybatis-PageHelper)  
然后按步骤给出各部分代码（其他无关代码和配置文件省略）

# 一、SqlMapConfig.xml  
此处要导入两个jar包：`pagehelper-5.0.0.jar`和`jsqlparser-0.9.5.jar`
```xml
<!-- 配置分页插件 -->
<plugins>
    <plugin interceptor="com.github.pagehelper.PageHelper">
        <!-- 指定数据库方言 -->
        <property name="dialect" value="mysql"/>
    </plugin>
</plugins>
```

# 二、po  
`Country.java`
```java
public class Country {
    private Integer id;
    private String countryname;
    private String countrycode;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCountryname() {
        return countryname;
    }
    public void setCountryname(String countryname) {
        this.countryname = countryname == null ? null : countryname.trim();
    }
    public String getCountrycode() {
        return countrycode;
    }
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode == null ? null : countrycode.trim();
    }
    @Override
    public String toString() {
        // return (this.getId()+" "+this.getCountryname()+" "+this.getCountrycode());
        return "Country [id=" + id +", countryname=" + countryname + ", countrycode=" + countrycode + "]";
    }
}
```

`EasyUIDataGridResult.java`
```java
public class EasyUIDataGridResult {
    // easyUI dataGrid 返回结果封装
    Long total;    // 总的记录数
    List<?> rows;  // 数据集

    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List<?> getRows() {
        return rows;
    }
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
```

# 三、service  
`CountryService.java`
```java
public interface CountryService {
    public EasyUIDataGridResult getCountryList(int page, int rows);
}
```

`CountryServiceImpl.java`
```java
@Service
public class CountryServiceImpl implements CountryService{
    @Autowired
    private CountryMapper countryMapper;

    public EasyUIDataGridResult getCountryList(int page, int rows) {

        //分页处理
        PageHelper.startPage(page, rows);

        //查询结果
        CountryExample example = new CountryExample();
        List<Country> list = countryMapper.selectByExample(example);

        //获取分页信息
        PageInfo<Country> info = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        long total = info.getTotal();

        //封装分页信息
        List<Country> row = info.getList();
        result.setRows(row);
        result.setTotal(total);
        return result;
    }
}
```

# 四、controller
`CountryController.java`
```java
@Controller
public class CountryController {
    @Autowired
    private CountryService countryService;

    @RequestMapping("/country/list")
    @ResponseBody
    private EasyUIDataGridResult getItemList(Integer page, Integer rows){
        EasyUIDataGridResult countrylists = countryService.getCountryList(page,rows);
        return countrylists;
    }
}
```

# 五、springmvc.xml
在原来的基础上增加json convertors，以便将List集合转换成json对象，可以使用fastjson或者jackson库，二者择其一就好
注意Jar的版本，我选择的是`jackson-core-2.8.7.jar` `jackson-databind-2.8.7.jar` `jackson-annotations-2.8.7.jar`，不同的版本可能会导致一些异常
```xml
<mvc:annotation-driven>
    <mvc:message-converters>
        <bean class="org.springframework.http.converter.StringHttpMessageConverter"/>
        <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/>
    </mvc:message-converters>
</mvc:annotation-driven>
```

# 六、jsp
`countryList.jsp`
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>列表分页展示</title>
        <!--必须引入jQuery和easyUI库-->
        <link rel="stylesheet" type="text/css" href="../js/jquery-easyui-1.4.1/themes/default/easyui.css" />
        <link rel="stylesheet" type="text/css" href="../js/jquery-easyui-1.4.1/themes/icon.css" />
        <link rel="stylesheet" type="text/css" href="../css/taotao.css" />
        <script type="text/javascript" src="../js/jquery-easyui-1.4.1/jquery.min.js"></script>
        <script type="text/javascript" src="../js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="../js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
    </head>

    <body>
        <table id="dg"></table>
    </body>
    
    <script>
        $(document).ready(function () {
            $('#dg').datagrid({
                url:'/country/list',
                pagination: true,
                columns:[[
                    {field:'id',title:'id',width:100},
                    {field:'countryname',title:'国家名称',width:100,align:'center'},
                    {field:'countrycode',title:'国家代码',width:100,align:'center'}
                ]]
            });

        });
    </script>
</html>
```

最后成果如下所示~  
![](http://p1.bqimg.com/567571/7a076f28928e7b8c.gif)