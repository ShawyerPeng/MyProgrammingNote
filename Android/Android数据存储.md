# InternalStorageFile
```java
FileOutputStream fileOutputStream = openFileOutput("FILENAME", Context.MODE_PRIVATE);
fileOutputStream.write(mEditText.getText().toString().getBytes());
fileOutputStream.close();

StringBuilder stringBuilder = new StringBuilder();
try {
    InputStream inputStream = openFileInput(FILENAME);
    if ( inputStream != null ) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String newLine = null;
        while ((newLine = bufferedReader.readLine()) != null ) {
            stringBuilder.append(newLine+"\n");
        }
        inputStream.close();
    }
} catch (java.io.IOException e) {
    e.printStackTrace();
}
```

# ExternalStorageFile
`<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`

```java
public boolean isExternalStorageWritable() {
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
        return true;
    }
    return false;
}

public boolean isExternalStorageReadable() {
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
        return true;
    }
    return false;
}

public void writeFile(View view) {
    if (isExternalStorageWritable()) {
        try {
            File textFile = new File(Environment.getExternalStorageDirectory(), FILENAME);
            FileOutputStream fileOutputStream = new FileOutputStream(textFile);
            fileOutputStream.write(mEditText.getText().toString().getBytes());
            fileOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error writing file", Toast.LENGTH_LONG).show();
        }
    } else {
        Toast.makeText(this, "Cannot write to External Storage", Toast.LENGTH_LONG).show();
    }
}

public void readFile(View view) {
    if (isExternalStorageReadable()) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File textFile = new File(Environment.getExternalStorageDirectory(), FILENAME);
            FileInputStream fileInputStream = new FileInputStream(textFile);
            if (fileInputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String newLine = null;
                while ( (newLine = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(newLine+"\n");
                }
                fileInputStream.close();
            }
            mEditText.setText(stringBuilder);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading file", Toast.LENGTH_LONG).show();
        }
    } else {
        Toast.makeText(this, "Cannot read External Storage", Toast.LENGTH_LONG).show();
    }
}
```



# SharedPreference
```java
SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
editor.putString("name", mEditTextName.getText().toString());
editor.commit();

SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
String name = sharedPreferences.getString("name", null);
```

# SQLite
```java
public class DictionaryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dictionary.db";
    private static final String TABLE_DICTIONARY = "dictionary";

    private static final String FIELD_WORD = "word";
    private static final String FIELD_DEFINITION = "definition";
    private static final int DATABASE_VERSION = 1;

    // 构造函数
    DictionaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // OnCreate()
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_DICTIONARY +
                "(_id integer PRIMARY KEY," +
                FIELD_WORD + " TEXT, " +
                FIELD_DEFINITION + " TEXT);");
    }

    // onUpgrade()
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }

    public void saveRecord(String word, String definition) {
        long id = findWordID(word);
        if (id>0) {
            updateRecord(id, word,definition);
        } else {
            addRecord(word,definition);
        }
    }

    // add
    public long addRecord(String word, String definition) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_WORD, word);
        values.put(FIELD_DEFINITION, definition);
        return db.insert(TABLE_DICTIONARY, null, values);
    }

    // update
    public int updateRecord(long id, String word, String definition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put(FIELD_WORD, word);
        values.put(FIELD_DEFINITION, definition);
        return db.update(TABLE_DICTIONARY, values, "_id = ?", new String[]{String.valueOf(id)});
    }

    // delete
    public int deleteRecord(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_DICTIONARY, "_id = ?", new String[]{String.valueOf(id)});
    }

    // query
    public long findWordID(String word) {
        long returnVal = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_ DICTIONARY + " WHERE " + FIELD_WORD + " = ?", new String[]{word});
        Log.i("findWordID","getCount()="+cursor.getCount());
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getInt(0);
        }
        return returnVal;
    }

    public String getDefinition(long id) {
        String returnVal = "";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT definition FROM " + TABLE_ DICTIONARY + " WHERE _id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            returnVal = cursor.getString(0);
        }
        return returnVal;
    }

    public Cursor getWordList() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id, " + FIELD_WORD + " FROM " + TABLE_DICTIONARY + " ORDER BY " + FIELD_WORD + " ASC";
        return db.rawQuery(query, null);
    }
}
```

## DictionaryDatabase

