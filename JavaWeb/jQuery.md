---
title: jQuery
date: 2017-02-12 18:36:58
categories: JavaWeb
---

# jQuery 只有一个对象 （jQuery == $） 一定要搞清楚jQuery 与dom 对象之间的区别于联系
    dom对象：dom 对象 是浏览器自带对象，dom 对象只能调用dom 里面的属性和方法，
    不能调用jQuery 对象里面的属性和方法

    jQuery对象：jquery 对象是通过jQuery 包装页面上面的元素或者dom
    而产生的一个新的 对象，jquery 对象时jQuery 独有的，不能调用dom
    对象里面的属性和方法，jQuery 对象是一个数组。

    jQuery 对象与dom 对象时可以相互转换的，转换之后它们就可以相互调用了

## 三种方式
```html
$(document).ready(function(){
    $("#dombutton").click();
});

$(document).ready(function(){
    $("#dombutton").click();
});

jQuery().ready(function(){
    $("#dombutton").click();
});
```

## DOM转换成jQuery对象
```
//DOM获取
var username = document.getElementById("username");
alert(username.value);

//DOM对象转换成jQuery对象:$(DOM对象)
var $username = $(username);
alert($username.val());
```


## jQuery对象转换成DOM对象
```
//jQuery获取(定义jQuery变量,在其前面增加"$".不加是可以的,但是不建议)
var $username = $("#username");

//jQuery对象转换成DOM对象
//1 jQuery对象是一个数组对象
var username = $username[0];
alert(username.value);

//2 jQuery提供了get(index)方法
var username = $username.get(0);
alert(username.value);
```

$()：方法调用，根据正则去匹配


# 选择器
## 基本选择器
`#one`：id为one的元素
`.mini`：class为mini的元素
`div`：元素名为div的元素
`*`：所有元素
```javascript
$(document).ready(function(){
    //<input type="button" value="选择 id为 one 的元素." id="btn1"/> 
    $("#btn1").click(function(){
        $("#one").css("background","red");   
    });
    
    //<input type="button" value="选择 class 为 mini 的所有元素." id="btn2"/>
    $("#btn2").click(function(){   
        $(".mini").css("background","yellow");
    });
    
    //<input type="button" value="选择 元素名是 div 的所有元素." id="btn3"/>
    $("#btn3").click(function(){   
        $("div").css("background","green");
    });
    
    //<input type="button" value="选择 所有的元素." id="btn4"/>
    $("#btn4").click(function(){
        $("*").css("background","blue");   
    });
    
    //<input type="button" value="选择 所有的span元素和id为two的元素." id="btn5"/>
    $("#btn5").click(function(){
        //组合选择器..
        $("span,#two").css("background","blue");       
    });
});
```

## 层级选择器(只有当前的这个方法返回的是jQuery对象才能进行链式操作)
`body div`：body内的所有div元素
`body>div`：在body内,选择子元素是div的
`#one+div`：id为one 的下一个div元素
`#two~div`：id为two的元素后面的所有div兄弟元素（只有它后面的）
`$("#two").siblings("div")`：id为two的元素的所有div兄弟元素（前面后面都包括）

```html
<body>
        <div>
            <div></div>
        </div>

        <div></div>
</body>
```
```javascript
$(document).ready(function(){
        //<input type="button" value="选择 body内的所有div元素." id="btn1"/>
        $("#btn1").click(function(){
            $("body div").css("background","blue");               
        });
        
        //<input type="button" value="在body内,选择子元素是div的。" id="btn2"/>
        $("#btn2").click(function(){
            $("body>div").css("background","blue");
        });
        
        //<input type="button" value="选择 id为one 的下一个div元素." id="btn3"/>
        $("#btn3").click(function(){
            $("#one+div").css("background","blue");
        });
        
        //<input type="button" value="选择 id为two的元素后面的所有div兄弟元素." id="btn4"/>
        $("#btn4").click(function(){
            $("#two~div").css("background","blue");
        });
        
        //<input type="button" value="选择 id为two的元素的所有div兄弟元素." id="btn5"/>
        $("#btn5").click(function(){
            //只有当前的这个方法返回的是jQuery 对象才能进行链式操作...
            $("#two").siblings("div").css("background","blue");          
        });
});
```

## 基本过滤选择器
找到一堆页面元素，我们可以对这些元素加过滤条件，找到我们想要的这些元素，然后进行操作  
过滤条件前面添加 : 符号 
`div:first`：第一个div元素
`div:last`：最后一个div元素
`div:not('.one')`：class不为one的所有div元素
`div:even`：索引值为偶数的div元素
`div:odd`：索引值为奇数的div元素
`div:eq(3)`：索引值等于3的元素
`div:gt(3)`：索引值大于3的元素
`div:lt(3)`：索引值小于3的元素
`:header`：所有的标题元素
`:animated`：当前正在执行动画的所有元素

```javascript
$(document).ready(function(){
        //<input type="button" value="选择第一个div元素." id="btn1"/>
        $("#btn1").click(function(){	  	
            $("div:first").css("background","red");
        });
        
        //<input type="button" value="选择最后一个div元素." id="btn2"/>
        $("#btn2").click(function(){	  	
            $("div:last").css("background","green");
        });
        
        //<input type="button" value="选择class不为one的 所有div元素." id="btn3"/>
        $("#btn3").click(function(){		  	
            $("div:not('.one')").css("background","green");
        });
        
        //<input type="button" value="选择索引值为偶数 的div元素." id="btn4"/>
        $("#btn4").click(function(){
            $("div:even").css("background","green");
        });
        
        //<input type="button" value="选择索引值为奇数 的div元素." id="btn5"/>
        $("#btn5").click(function(){
            $("div:odd").css("background","green");
        });
        
        //<input type="button" value="选择索引值等于3的元素." id="btn6"/>
        $("#btn6").click(function(){
            $("div:eq(3)").css("background","green");		  	
        });
        
        //<input type="button" value="选择索引值大于3的元素." id="btn7"/>
        $("#btn7").click(function(){
            $("div:gt(3)").css("background","green");
        });
        
        //<input type="button" value="选择索引值小于3的元素." id="btn8"/>
        $("#btn8").click(function(){
            $("div:lt(3)").css("background","red");
        });
        
        //<input type="button" value="选择所有的标题元素." id="btn9"/>
        $("#btn9").click(function(){
            $(":header").css("background","red");
        });
        
        //<input type="button" value="选择当前正在执行动画的所有元素." id="btn10"/>
        function move(){
            $("#mover").slideToggle("slow",move);
        }
        move();
        
        $("#btn10").click(function(){				
            $(":animated").css("background","red");
        });	  
});
```

## 内容过滤选择器
`div:contains('di')`：含有文本“di”的div元素
`div:empty`：不包含子元素(或者文本元素)的div空元素
`div:has('.mini')`：含有class为mini元素的div元素
`div:parent`：含有子元素(或者文本元素)的div元素

```javascript
$(document).ready(function(){
        //<input type="button" value="选取含有文本“di”的div元素." id="btn1"/>
        $("#btn1").click(function(){
            $("div:contains('di')").css("background","red");
        });
        
        //<input type="button" value="选取不包含子元素(或者文本元素)的div空元素." id="btn2"/>
        $("#btn2").click(function(){
                $("div:empty").css("background","red");
        });
        
        //<input type="button" value="选取<含有!>class为mini元素的div元素." id="btn3"/>
        $("#btn3").click(function(){
            $("div:has('.mini')").css("background","red");		  	
        });
        
        //<input type="button" value="选取含有子元素(或者文本元素)的div元素." id="btn4"/>
        $("#btn4").click(function(){
            $("div:parent").css("background","red");		  		
        });		  
});
```

## 可见性过滤选择器
`div:visible`：所有可见的div元素
`div:hidden`：所有不可见的div元素
`input:hidden`：选取所有的文本隐藏域, 并打印它们的值
`#onediv>div`：选取onediv所有的div的, 并打印它们的值

```javascript
$(document).ready(function(){
        //<input type="button" value=" 选取所有可见的div元素"  id="b1"/>
        $("#b1").click(function(){
                $("div:visible").css("background","red");
        });
        
        //<input type="button" value=" 选取所有不可见的元素, 利用 jQuery 中的 show() 方法将它们显示出来"  id="b2"/>
        $("#b2").click(function(){
            $("div:hidden").css("background","red").show(2000);
        });
        
        //<input type="button" value=" 选取所有的文本隐藏域, 并打印它们的值"  id="b3"/>
        $("#b3").click(function(){
                //  <!--文本隐藏域-->
			//	 <input type="hidden" value="hidden_1">
			//	 <input type="hidden" value="hidden_2">
			//	 <input type="hidden" value="hidden_3">
			//	 <input type="hidden" value="hidden_4">

            //var $input=$("input:hidden");
                
            //首先我们通过dom来遍历...
            //1.显示迭代
			//	for(var i=0;i<$input.length;i++){
			//		alert($input[i].value);
			//	}
            //2.隐式迭代
            $("input:hidden").each(function(index,dom){
                    //alert(index);
                    //alert(dom.value);					
            })			
        });
        
        //<input type="button" value=" 选取onediv所有的div的, 并打印它们的值"  id="b4"/>
        $("#b4").click(function(){
            $("#onediv>div").each(function(index,dom){			   	
                alert($(dom).text());
            })			
        });		  
});
```

## 属性过滤选择器
`div[title]`：含有属性title的div元素
`div[title=test]`：属性title值等于“test”的div元素
`div[title!=test]`：属性title值不等于“test”的div元素(没有属性title的也将被选中)
`div[title^=te]`：属性title值以“te”开始的div元素
`div[title$=est]`：属性title值以“est”结束的div元素
`div[title*=es]`：属性title值 含有“es”的div元素
`div[id][title*=es]`：组合属性选择器,首先选取有属性id的div元素，然后在结果中选取属性title值含有“es”的元素

```javascript
$(document).ready(function(){
        //<input type="button" value="选取含有 属性title 的div元素." id="btn1"/>
        $("#btn1").click(function(){
            $("div[title]").css("background","red");
            
        });
        
        //<input type="button" value="选取 属性title值等于“test”的div元素." id="btn2"/>
        $("#btn2").click(function(){
            $("div[title=test]").css("background","red");
        });
        
        //<input type="button" value="选取 属性title值不等于“test”的div元素(没有属性title的也将被选中)." id="btn3"/>
        $("#btn3").click(function(){
                $("div[title!=test]").css("background","red");
        });
        
        //<input type="button" value="选取 属性title值 以“te”开始 的div元素." id="btn4"/>
        $("#btn4").click(function(){
                $("div[title^=te]").css("background","red");
        });
        
        //<input type="button" value="选取 属性title值 以“est”结束 的div元素." id="btn5"/>
        $("#btn5").click(function(){
                $("div[title$=est]").css("background","red");
        });
        
        //<input type="button" value="选取 属性title值 含有“es”的div元素." id="btn6"/>
        $("#btn6").click(function(){
            $("div[title*=es]").css("background","red");
        });
        
        //<input type="button" value="组合属性选择器,首先选取有属性id的div元素，然后在结果中 选取属性title值 含有“es”的元素." id="btn7"/>
        $("#btn7").click(function(){
            $("div[id][title*=es]").css("background","red");
        });       
});
```

## 子元素过滤选择器
冒号前一定要加空格
`div[class=one] :nth-child(2)`：选取每个class为one的div,父元素下的第2个子元素
`div[class=one] :first-child`：选取每个class为one的div, 父元素下的第一个子元素
`div[class=one] :last-child`：选取每个class为one的div父元素下的最后一个子元素
`div[class=one] :only-child`：如果class为one的div父元素下的仅仅只有一个子元素，那么选中这个子元素

```javascript
$(document).ready(function(){
        //<input type="button" value="选取每个class为one的div,父元素下的第2个子元素." id="btn1"/>
        $("#btn1").click(function(){      
                //子元素过滤选择器特殊写法，: 前面加空格...  从1开始计算...
                $("div[class=one] :nth-child(2)").css("background","red");
        });
        
        //<input type="button" value="选取每个class为one的div, 父元素下的第一个子元素." id="btn2"/>
        $("#btn2").click(function(){
            //第一种写法
            //$("div[class=one] :nth-child(1)").css("background","red");
            //第二种写法..
            $("div[class=one] :first-child").css("background","red");
        });
        
        //<input type="button" value="选取每个class为one的div父元素下的最后一个子元素." id="btn3"/>
        $("#btn3").click(function(){        
                $("div[class=one] :last-child").css("background","red");
        });
        
        //<input type="button" value="如果class为one的div父元素下的仅仅只有一个子元素，那么选中这个子元素." id="btn4"/>
        $("#btn4").click(function(){
            $("div[class=one] :only-child").css("background","red");
        });       
});
```

## 表单对象属性过滤选择器
`$("input:enabled").val("");`：对表单内可用input赋值操作  
`$("input:disabled").val("卢雨");`：对表单内不可用input赋值操作  
`$("input:checked").size()`：获取多选框选中的个数  
`$("select>option:selected").each()`：获取下拉框选中的内容  

```javascript
$(document).ready(function(){
        //<button id="btn1">对表单内 可用input 赋值操作.</button>
        $("#btn1").click(function(){
            $("input:enabled").val("卢雨");
        });
        
        //<button id="btn2">对表单内 不可用input 赋值操作.</button>
        $("#btn2").click(function(){
            $("input:disabled").val("卢雨");
        });
        
        //<button id="btn3">获取多选框选中的个数.</button>
        $("#btn3").click(function(){
            alert($("input:checked").size());
        });
        
        //<button id="btn4">获取下拉框选中的内容.</button>
        $("#btn4").click(function(){
            //alert($("select>option:selected").length);           
            $("select>option:selected").each(function(index,dom){
                    //alert($(dom).text());
                    var title=$(dom).attr("title");
                    alert(title);						
            })			
        });		  
});
```

## 表单属性过滤选择器
`var $alltext = $("#form1 :text");`：拿到表单下的文本框
`var $allpassword= $("#form1 :password");`：拿到表单下的密码
`var $allradio= $("#form1 :radio");`：
`var $allcheckbox= $("#form1 :checkbox");`：

`var $allsubmit= $("#form1 :submit");`：
`var $allimage= $("#form1 :image");`：
`var $allreset= $("#form1 :reset");`：
`var $allbutton= $("#form1 :button");`：
`var $allfile= $("#form1 :file");`：
`var $allhidden= $("#form1 :hidden");`：
`var $allselect = $("#form1 select");`：
`var $alltextarea = $("#form1 textarea");`：
`var $AllInputs = $("#form1 :input");`：
`var $inputs = $("#form1 input");`：

```javascript
        $(document).ready(function(){
        var $alltext = $("#form1 :text");
        var $allpassword= $("#form1 :password");
        var $allradio= $("#form1 :radio");
        var $allcheckbox= $("#form1 :checkbox");

        var $allsubmit= $("#form1 :submit");
        var $allimage= $("#form1 :image");
        var $allreset= $("#form1 :reset");
        var $allbutton= $("#form1 :button"); // <input type=button />  和 <button ></button>都可以匹配
        var $allfile= $("#form1 :file");
        var $allhidden= $("#form1 :hidden"); // <input type="hidden" />和<div style="display:none">test</div>都可以匹配.
        var $allselect = $("#form1 select");
        var $alltextarea = $("#form1 textarea");
        var $AllInputs = $("#form1 :input");
        var $inputs = $("#form1 input");
        $("div").append(" 有" + $alltext.length + " 个（ :text 元素）<br/>")
                .append(" 有" + $allpassword.length + " 个（ :password 元素）<br/>")
                .append(" 有" + $allradio.length + " 个（ :radio 元素）<br/>")
                .append(" 有" + $allcheckbox.length + " 个（ :checkbox 元素）<br/>")
                .append(" 有" + $allsubmit.length + " 个（ :submit 元素）<br/>")
                .append(" 有" + $allimage.length + " 个（ :image 元素）<br/>")
                .append(" 有" + $allreset.length + " 个（ :reset 元素）<br/>")
                .append(" 有" + $allbutton.length + " 个（ :button 元素）<br/>")
                .append(" 有" + $allfile.length + " 个（ :file 元素）<br/>")
                .append(" 有" + $allhidden.length + " 个（ :hidden 元素）<br/>")
                .append(" 有" + $allselect.length + " 个（ select 元素）<br/>")
                .append(" 有" + $alltextarea.length + " 个（ textarea 元素）<br/>")
                .append(" 表单有 " + $inputs.length + " 个（input）元素。<br/>")
                .append(" 总共有 " + $AllInputs.length + " 个(:input)元素。<br/>")
                .css("color", "red")
        $("form").submit(function () { return false; }); // return false;不能提交.
});
```


# dom操作
页面上面的元素分为三种类型的节点：
1：元素节点 （9大选择器都是用来找元素节点）  
2：属性节点 （先找到元素节点，然后调用attr()）  
3：文本节点 (先找到元素节点然后调用text())  

节点的创建：元素节点的创建，属性节点，文本节点
## 查找节点
```html
<body>
    <ul>
        <li id="bj"></li>
        <li id="tj" name="tianjin">天津</li>   
    </ul>
</body>
<script type="text/javascript">
    //元素节点的查找
    //怎么查找元素节点的属性节点
    alert($("#tj").attr("name"));    
    //文本节点查找  text();
    alert($("#tj").text());
    //删除属性节点
    $("#tj").removeAttr("name");
    alert($("#tj").attr("name"));
    //设置属性节点
    $("#tj").attr("name","tianjin")
    alert($("#tj").attr("name"));
</script>
```

## 创建节点
```html
<body>
    <ul id="city">
        <li id="bj" name="beijing">北京</li>       
    </ul>
</body>
<script type="text/javascript">
    //通过jquery dom 创建<li id="tj" name="tianjin">天津</li>		
    //创建元素节点
    var $li=$("<li></li>");
    //设置属性节点
    $li.attr("id","tj");
    $li.attr("id","tj");
    //设置文本节点
    $li.text("天津");
    alert('a');
    //获取节点
    var $city=$("#city");
    //往节点里面追加创建好的节点..
    $city.append($li);
</script>
```

## 内部插入节点
```html
<body>
    <ul id="city">
        <li id="bj" name="beijing">北京</li>
        <li id="tj" name="tianjin">天津</li>
        <li id="cq" name="chongqing">重庆</li>
    </ul>
    <ul id="love">
        <li id="fk" name="fankong">反恐</li>
        <li id="xj" name="xingji">星际</li>
    </ul>   
    <div id="foo1">Hello1</div>    
</body>
<script type="text/javascript">	
    //append(content):向每个匹配的元素的内部的结尾处追加内容
    $("#bj").append($("#fk"));
    //appendTo(content):将每个匹配的元素追加到指定的元素中的内部结尾处
    $("#bj").appendTo($("#fk"));
    //prepend(content):向每个匹配的元素的内部的开始处插入内容
    $("#bj").prepend($("#fk"));
    //prependTo(content):将每个匹配的元素插入到指定的元素内部的开始处
    $("#bj").prependTo($("#fk"));
</script>
```

## 外部插入节点
```html
<body>
    <ul id="city">
        <li id="bj" name="beijing">北京</li>           
        <li id="tj" name="tianjin">天津</li>
        <li id="cq" name="chongqing">重庆</li>
    </ul>		
    <p  id="p3">I would like to say: p3</p>		 
    <p  id="p2">I would like to say: p2</p>	
    <p  id="p1">I would like to say: p1</p>      
</body>
<script type="text/javascript">
    //	在每个匹配的元素之后插入内容
    //	$("#bj").after($("#p3"));
    //  在每个匹配元素 的前面插入内容
    //	$("#bj").before($("#p3"));
    //	$("#bj").insertAfter($("#p3"));
	$("#bj").insertBefore($("#p3"));
</script>
```

## 删除节点
```html
<body>
    <ul id="city">
        <li id="bj" name="beijing">北京<p>海淀区</p></li>
        <li id="tj" name="tianjin">天津<p>河西区</p></li>
        <li id="cq" name="chongqing">重庆</li>
    </ul>
    <p class="hello">Hello</p> how are <p>you?</p> 
</body>
<script type="text/javascript">
    //$("#bj").remove();
    $("#city").remove();	
</script>
```

## 复制节点
```html
<body>
    <button>保存</button><br>	
    <p>段落</p>
</body>
<script type="text/javascript">
    //button增加事件
    $("button").click(function(){
        $("p").append($("button").clone(true));       
    });
    // clone 克隆某个元素的副本，但是这个副本不具备任何的行为，不具备任何的时间
    // 如果需要把时间也克隆过去，需要在clone(true));
</script>
```

## 替换节点
```html
<html>
    <p>段落</p>
    <p>段落</p>
    <p>段落</p>
    <button>保存</button>
</html>
<script type="text/javascript">
		// 将所有匹配的元素都替换为指定的 HTML 或 DOM 元素
        // $("button").replaceWith($("p"));
		$("button").replaceAll($("p"));
</script>
```

## 样式操作
```html
<head>
    <style type="text/css">
        .one{
            width: 200px;
            height: 140px;
            margin: 40px;
            background: red;
            border: #000 1px solid;
            float:left;
            font-size: 17px;
            font-family:Roman;
        }

        div,span{
            width: 140px;
            height: 140px;
            margin: 20px;
            background: #9999CC;
            border: #000 1px solid;
            float:left;
            font-size: 17px;
            font-family:Roman;
        }
        
        div.mini{
            width: 30px;
            height: 30px;
            background: #CC66FF;
            border: #000 1px solid;
            font-size: 12px;
            font-family:Roman;
        }
    </style>
</head>

<script type="text/javascript">
//<input type="button" value="采用属性增加样式"  id="b1"/>
	$("#b1").click(function(){
		  $("#mover").css("background","red");		
	});
	
//<input type="button" value=" addClass"  id="b2"/>
	$("#b2").click(function(){
			$("#mover").addClass("mini");		
	});

//<input type="button" value="removeClass"  id="b3"/>
	$("#b3").click(function(){
		/*
		 * removeClass():
		 * 	* 不传参数:删除所有样式
		 * 	* 传递参数:删除指定样式
		 */		
		  $("#mover").removeClass();
		//$("#mover").removeClass();
	});

//<input type="button" value=" 切换样式"  id="b4"/>
	$("#b4").click(function(){		
		$("#mover").toggleClass("one");
	});

//<input type="button" value=" hasClass"  id="b5"/>
	$("#b5").click(function(){
		//hasClass():判断某个元素是否含有某个指定样式
		$("#mover").addClass("mini");
		alert($("#mover").hasClass("mini"));		
	});
</script>
```

## 遍历节点
```html
<body>
    <ul id="city">
        <li id="bj" name="beijing">北京</li>
        <li id="tj" name="tianjin">天津</li>
        <li id="nj" name="nanjing">南京</li>
    </ul>   
</body>
<script type="text/javascript">
    // 该方法只考虑子元素而不考虑任何后代元素.
    //	alert($("#city").children().length);
    //	取得相邻元素的下一个同级元素
    //	alert($("#bj").next().text());

    //	alert($("#tj").prev().text());

    //	alert($("#bj").siblings().length);

    //find
    //选获取元素通过find 找到对应的标签的子元素的集合...
        alert($("#city").find("li").length);
</script>
```

## 包裹节点
```html
<body>
    <strong title="jQuery">jQuery</strong>
    <strong title="jQuery">jQuery</strong>
</body>
<script type="text/javascript">
    //jQuery代码如下：
    // $("strong").wrap("<b></b>");
    
    $("strong").wrapAll("<b></b>");
    <br><<strong title="jQuery">jQuery</strong>
    <strong title="jQuery">jQuery</strong></br>
</script>
```

## 总结
```
* DOM操作：jQuery中封装DOM(对比原生DOM的使用方法)
    * 查找节点：
        * 元素节点：所有的选择器
        * 文本节点：text()
        * 属性节点：
            * attr()
            * removeAttr()
    * 创建节点；
        * 元素节点：$(HTML代码)
        * 文本节点：text()
        * 属性节点：attr()
    * 插入：内部与外部
    * 删除节点：
        * remove()：删除自身节点及后代节点
        * empty()：删除后代节点，自身节点保留（清空）
    * 复制节点：
        * jQuery的clone(Boolean)：表示是否复制事件。
        * js的cloneNode(Boolean)：表示是否复制后代节点。
    * 替换节点：
        * replaceWith()：前面的元素是被替换元素;后面的元素是替换元素
        * replaceAll()：就是颠倒了的replaceWith()
    * html()方法：直接操作HTML代码（等价于innerHTML属性）
    * 遍历节点：
        * 子节点：children()
        * 父节点：parent()
        * 上一个兄弟节点：prev()
        * 下一个兄弟节点：next()
        * 所有兄弟节点：siblings()
        * find()：通用遍历方法
    * 包裹节点：
        * wrap()：分别包裹
        * wrapAll()：一起包裹
        * wrapInner()：包裹内部
```



# 绑定与解绑事件
```javascript
//页面加载完毕执行 
window.onload=function(){
}

//页面加载完毕执行 
$(function(){
})

$().ready(function(){
})
```

区别：`window.onload`与`$(function(){})`都是用来作于界面渲染完毕之后的初始化操作。

`window.onload`需要等待页面上面所有的元素都绘制完毕之后才执行，包含图片。  
`$(function(){})`页面上面所有的dom元素绘制完毕之后就执行，不包含图片。  









# getscript和getjson

# jQuery的异步提交表单
```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<html>
    <head>
        <title>jQuery异步提交表单</title>
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="this is my page">
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
        <script type="text/javascript">
                $(function(){
                    $("#formbutton").click(function(){
                        // 发送异步的ajax 请求			
                        
                        // 将表单里面的选项序列化成一个数组...
                        //var data=$("#form1").serialize();
                        var data=$("#form1").serializeArray();
                        
                        // 可以打印对象里面更加详细的信息				
                        console.info(data);
                        
                        for(var i=0;i<data.length;i++){
                            alert(data[i].name);
                            alert(data[i].value);
                        }
                        $.ajax({
                            url:"../../formServlet",	
                            //通过ajax方法提交的时候有两种数据格式(字符串/json)	 
                            data:data,
                            type:"POST",
                            success:function(data){							
                            }
                        })
                    })
                })
        </script>
    </head>
  
    <body>
        <form id="form1">
                用户名：<input type="text" name="username" id="username"><br><br>
                密&nbsp;&nbsp;码：<input type="password" name="password" id="password"><br><br>
                邮&nbsp;&nbsp;箱：<input type="text" name="email" id="email"><br><br>
                <input  type="button" value="异步提交表单" id="formbutton"/>
        </form>
    </body>
</html>
```

# jQuery的ajax load交互
```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<html>
    <head>
    <title>ajax 异步加载...</title>

    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <!--当用户第一次访问界面的时候我们不需要加载页面上面所有的资源,当用户想看的时候去加载...-->
    <script type="text/javascript" src="../../js/jquery-1.4.2.js"></script>
    <script type="text/javascript">
        $(window).scroll(function(){                   
                var t=document.documentElement.scrollTop;  
                if(t>0  && t<800){                       
                    loadImage("1");
                }                   
                if(t>800  && t<1600){                       
                    loadImage("2");
                }                    
                if(t>1600  && t<2400){                      
                    loadImage("3");
                }                    
                if(t>2400  && t<3200){
                    loadImage("4");                  
                }                 
                if(t>3200  && t<4000){                      
                    loadImage("5");
                }
                if(t>4000  && t<4800){
                    loadImage("6");                
                }                
                
                function loadImage(imageType){
                    $.ajax({
                        url:"../../imageServlet",
                        type:"POST",
                        data:{
                            imageType:imageType
                        },
                        success:function(data){
                            var area="#area_"+imageType;
                            var image="<img src='../../"+data+"'>";
                            $(area).html(image);
                        }
                    })                    
                }
        })
    </script>

    <style type="text/css">
            #message{
                height:4800px;
            }               
            #message div{
                height:800px;
                border:30px solid red;
            }      
    </style>
    </head>
  
    <body>
        <div id="message">
            <div id="area_1"></div>
            <div id="area_2"></div>
            <div id="area_3"></div>
            <div id="area_4"></div>
            <div id="area_5"></div>
            <div id="area_6"></div>
        </div>
    </body>
</html>
```

# jQuery的插件扩展
1.全局方法的插件
`$.validateUsername("")`
```javascript
<script type="text/javascript">
    $.extend({
        Constants:{
            baseURL:"http://localhost:8080/jQuery"
        },
        validateTelephone:function(tel){
            alert(tel);
            $Constants.baseURL
        }
    })
</script>
```

2.局部方法的插件
`$("#datagrid").datagrid({})`
```javascript
<script type="text/javascript">
    $.fn.extend({
        datagrid:function(obj){
            var columns=obj.columns;
            var tr="<tr>";
            ......
        }
    })
</script>
```

3.选择器插件
