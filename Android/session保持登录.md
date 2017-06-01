# `MainActivity.java`
```java
public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button)findViewById(R.id.login);
        logup = (Button)findViewById(R.id.logUp);
        cancel = (Button)findViewById(R.id.cancel_action);
        adminEdit =(EditText)findViewById(R.id.adminEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        checkBox = (CheckBox)findViewById(R.id.checkbox);

        // 
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        // 获取编辑器
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        // 初始化是否记住密码状态
        editor.putBoolean("isRemembered",false);
        // 提交修改
        editor.commit();
        if(sharedPreferences.getBoolean("isRemembered",true)&&sharedPreferences.getString("adminName",null)!=null){
            adminEdit.setText(sharedPreferences.getString("adminName",null));
        }
        adminSql = new AdminSql(mContext,"user.db",null,1);
    }
}
```

# `activity_main.xml`
```xml

```