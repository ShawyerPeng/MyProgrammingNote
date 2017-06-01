```java
1. 创建类继承`SQLiteOpenHelper`
public class UserDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE [Book] (" + "[name] varchar(10) not null,"
                                                    + "[author] varchar(200)," + "[price] varchar(5) )";

    public UserDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
```

2. CRUD
```java
public class SQLiteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserDbHelper mDbHelper = new UserDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        SQLiteDatabase db2 = mDbHelper.getReadableDatabase();

        // 增
        ContentValues values = new ContentValues();
        values.put("name", "The Da Vinci Code");
        values.put("author", "Dan Brown");
        values.put("price", 16.96);
        long newRowId = db.insert("Book", null, values);
        values.clear();

        // 删
        db.delete("Book", "price > ?", new String[]{"5.2"});
        
        // 改
        values.put("price", 10.99);
        db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
        values.clear();

        // 查
        // select 列的列表 from 表的列表 where 条件语句 group by 分组属性 having 分组条件 order by 排序列 asc|desc limit m, n; 
        // query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        Cursor cursor = db2.query(true, "student", new String[]{"_id","name","age"}, "_id>?", new String[]{"1"}, null, null, "age desc", "1,5");
        Cursor cursor = db.rawQuery("", args);
        Cursor cursor = db2.query("Book", null, null, null, null, null, null);
        //遍历Cursor对象，指针位移，判断是否有下一个元素
        while (cursor.moveToNext()) {
            //根据列名找到序号，根据序号再查找出来数据
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String author = cursor.getString(cursor.getColumnIndex("author"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            Log.e("SQLiteActivity", name + "  " + author + "  " + price);
        }
        values.clear();
    }
}
```

Cursor：
* `close()`：关闭游标，释放资源
* `copyStringToBuffer(int columnIndex,CharArrayBuffer buffer)`：在缓冲区中检索请求的列的文本，将其存储
* `getColumnCount()`：返回所有列的行数
* `getColumnIndex(String columnName)`：返回指定的列，如果不存在那么返回-1
* `getColumnIndexOrThrow(String columnName)`：从0开始返回指定列的名称，如果不存在将抛出异常
* `getColumnName(int columnIndex)`：从给定的索引返回列名
* `getColumnNames()`：返回一个字符串数组的列名
* `moveToFirst()`：将游标移动到第一条
* `moveToLast()`：将游标移动到最后一条
* `move(int offset)`：将游标移动到指定ID
* `moveToNext()`：将游标移动到下一条
* `moveToPrevious()`：将游标移动到上一条
* `getCount()`：得到游标总记录条数
* `isFirst()`：判断当前游标是否为第一条数据
