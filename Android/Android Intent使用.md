# 显式Intent(Explicit Intent)
## Action（动作）属性
Action是指Intent要完成的动作，是一个字符串常量。

| Constant | Target component | Action |
|---|---|---|
| ACTION_CALL | activity | Initiate a phone call.
| ACTION_EDIT | activity | Display data for the user to edit.
| ACTION_MAIN | activity | Start up as the initial activity of a task, with no data input and no returned output.
| ACTION_SYNC | activity | Synchronize data on a server with data on the mobile device.
| ACTION_BATTERY_LOW | broadcast receiver | A warning that the battery is low.
| ACTION_HEADSET_PLUG | broadcast receiver | A headset has been plugged into the device, or unplugged from it.
| ACTION_SCREEN_ON | broadcast receiver | The screen has been turned on.
| ACTION_TIMEZONE_CHANGED | broadcast receiver | The setting for the time zone has changed.



## Data（数据）属性
Intent的Data属性是执行动作的URI和MIME类型，不同的Action有不同的Data数据指定。比如：ACTION_EDIT Action应该和要编辑的文档URI Data匹配，ACTION_VIEW应用应该和要显示的URI匹配。

## Category（分类）属性
Intent中的Category属性是一个执行动作Action的附加信息。比如：CATEGORY_HOME则表示放回到Home界面，ALTERNATIVE_CATEGORY表示当前的Intent是一系列的可选动作中的一个。

| Constant | Meaning |
|---|---|
| CATEGORY_BROWSABLE | The target activity can be safely invoked by the browser to display data referenced by a link — for example, an image or an e-mail message. |
| CATEGORY_GADGET | The activity can be embedded inside of another activity that hosts gadgets. |
| CATEGORY_HOME | The activity displays the home screen, the first screen the user sees when the device is turned on or when the HOME key is pressed. |
| CATEGORY_LAUNCHER | The activity can be the initial activity of a task and is listed in the top-level application launcher. |
| CATEGORY_PREFERENCE | The target activity is a preference panel. |

回到Home界面的例子：
```java
public void onClick(View v) {     
    Intent intent = new Intent();                 
    intent.setAction(Intent.ACTION_MAIN);       // 添加Action属性                
    intent.addCategory(Intent.CATEGORY_HOME);   // 添加Category属性              
    startActivity(intent);                      // 启动Activity  
}  
```

## Type（类型）属性
Intent的Type属性显式指定Intent的数据类型（MIME）。一般Intent的数据类型能够根据数据本身进行判定，但是通过设置这个属性，可以强制采用显式指定的类型而不再进行推导。

## Compent（组件）属性
Intent的Compent属性指定Intent的的目标组件的类名称。通常 Android会根据Intent 中包含的其它属性的信息，比如action、data/type、category进行查找，最终找到一个与之匹配的目标组件。但是，如果 component这个属性有指定的话，将直接使用它指定的组件，而不再执行上述查找过程。指定了这个属性以后，Intent的其它所有属性都是可选的。

## Extra（扩展信）属性
Intent的Extra属性是添加一些组件的附加信息。比如，如果我们要通过一个Activity来发送一个Email，就可以通过Extra属性来添加subject和body。
```java
public void onClick(View v) {  
    Intent intent = new Intent();    
    intent.setClass(FirstActivity.this, SecondActivity.class);    //设置Intent的class属性，跳转到SecondActivity
    intent.putExtra("userName", etx.getText().toString());         //为intent添加额外的信息
    startActivity(intent);                                        //启动Activity
}  
```
```java
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_simple_intent_test);   // 设置当前的Activity的界面布局
    Intent intent = this.getIntent();                       // 获得Intent
    tv = (TextView)findViewById(R.id.TextView1);
    tv.setText(intent.getStringExtra("userName"));           // 从Intent获得额外信息，设置为TextView的文本
}
```


## 同一个应用程序中的Activity切换
从 AndroidManifest.xml修改的过程我们可以体会到， Intent 机制即使在程序内部且显式指定接收者，也还是需要在 AndroidManifest.xml 中声明 TestActivity。

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent">

    <Button
        android:text="测试Action属性"
        android:id="@+id/getBtn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</LinearLayout>
```

```java
public class MainActivity extends AppCompatActivity {
    private Button getBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBtn=(Button)findViewById(R.id.getBtn);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent();
                // intent.setAction(Intent.ACTION_MAIN);// 添加Action属性
                // intent.addCategory(Intent.CATEGORY_HOME);// 添加Category属性
                // startActivity(intent); // 启动Activity
                Intent myIntent = new Intent(MainActivity.this, NextActivity.class);
                myIntent.putExtra("key", value); // Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
```
或者这样写：
```java
public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    private Button getBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBtn=(Button)findViewById(R.id.getBtn);
        getBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(this, NextActivity.class);
        this.startActivity(myIntent);
    }
}
```

# Intent Filters
活动、服务、广播接收者为了告知系统能够处理哪些隐式intent，它们可以有一个或多个intent过滤器。每个过滤器描述组件的一种能力，即乐意接收的一组intent。实际上，它筛掉不想要的intents，也仅仅是不想要的隐式intents。一个显式intent总是能够传递到它的目标组件，不管它包含什么；不考虑过滤器。但是一个隐式intent，仅当它能够通过组件的过滤器之一才能够传递给它。



# 参考资料
[Android开发学习笔记：Intent的简介以及属性的详解](http://liangruijun.blog.51cto.com/3061169/634411)  
[Android开发实践：实战演练隐式Intent的用法](http://ticktick.blog.51cto.com/823160/1621957)  
[How to start new activity on button click](http://stackoverflow.com/questions/4186021/how-to-start-new-activity-on-button-click)  
