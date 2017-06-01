# SQLiteOpenHelper

.getReadableDatabase()`
`getWritableDatabase()`
`onCreate(SQLiteDatabase db)`
`onOpen(SQLiteDatabase db)`
`onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)`

`isReadOnly()`

```java
public static final int VERSION_NUMBER = 1;

SQLiteDatabase database = new SQLiteOpenHelper(context, "ContactDatabase", null, VERSION_NUMBER);


SQLiteDatabase db = SQLiteOpenHelper.getWritableDatabase();



```


# SQLiteDatabase
`insert(String table, String nullColumnHack, ContentValues values)`
`query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)`
`rawQuery(String sql, String[] selectionArgs)`
`delete(String table, String whereClause, String[] whereArgs)`
`update(String table, ContentValues values, String whereClause, String[] whereArgs)`

```java
SQLiteDatabase db = sqlHelper.getWritableDatabase();

```

# 