# 下载
```
compile 'com.squareup.retrofit2:retrofit:2.3.0'
compile 'io.reactivex.rxjava2:rxjava:2.1.0'
compile 'com.google.code.gson:gson:2.7'

// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-scalars
compile group: 'com.squareup.retrofit2', name: 'converter-scalars', version: '2.3.0'
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
compile group: 'com.squareup.retrofit2', name: 'converter-gson', version: '2.3.0'
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-jackson
compile group: 'com.squareup.retrofit2', name: 'converter-jackson', version: '2.3.0'
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-simplexml
compile group: 'com.squareup.retrofit2', name: 'converter-simplexml', version: '2.3.0'
// https://mvnrepository.com/artifact/com.squareup.retrofit2/adapter-rxjava
compile group: 'com.squareup.retrofit2', name: 'adapter-rxjava', version: '2.3.0'
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-protobuf
compile group: 'com.squareup.retrofit2', name: 'converter-protobuf', version: '2.3.0'
// https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-moshi
compile group: 'com.squareup.retrofit2', name: 'converter-moshi', version: '2.3.0'
```

# GET
`Request.java`
```java
public interface Request {
    @GET("/rest/F7KioBIcVfCKso857V1HsHJJm873ywcdEWw2yfftuReXfm2ni45LMOu9WBcv8oJM/info.json/12345/degrees")
    Call<String> get();
}
```

`MainActivity.java`
```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://www.zipcodeapi.com")
        // 增加返回值为String的支持
        .addConverterFactory(ScalarsConverterFactory.create())
        // 增加返回值为Gson的支持(以实体类返回)
        .addConverterFactory(ScalarsConverterFactory.create())
        // 增加返回值为Observable<T>的支持
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

Request request = retrofit.create(Request.class);               // 这里采用的是Java的动态代理模式
Call<String> call = request.get();   // 传入我们请求的键值对的值

call.enqueue(new Callback<String>() {
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Log.e("===","return:" + response.body());
    }
    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e("===","失败");
    }
});
```

# POST
`Request.java`
```java
public interface Request {
    @FormUrlEncoded
    @POST("/do_login/")
    Call<String> login(@Field("name") String name, @Field("pwd") String pwd);
}
```

`MainActivity.java`
```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://115.159.188.200:8001")
        // 增加返回值为String的支持
        .addConverterFactory(ScalarsConverterFactory.create())
        // 增加返回值为Gson的支持(以实体类返回)
        .addConverterFactory(ScalarsConverterFactory.create())
        // 增加返回值为Observable<T>的支持
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

Request request = retrofit.create(Request.class);               // 这里采用的是Java的动态代理模式
Call<String> call = request.login("admin","9876543210");   // 传入我们请求的键值对的值

call.enqueue(new Callback<String>() {
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Log.e("===","return:" + response.body());
    }
    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e("===","失败");
    }
});
```

# 
