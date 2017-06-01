# GET
```java
public interface Request {
    //使用 RxJava 的方法,返回一个 Observable
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> RxContributors(@Path("owner") String owner, @Path("repo") String repo);
}
```

```java
Retrofit retrofit=new Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())         // 添加 json 转换器
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   // 添加 RxJava 适配器
        .build();

GitHub gitHub=retrofit.create(GitHub.class);
gitHub.RxContributors("square","retrofit")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Contributor>>() {
            @Override
            public void onCompleted() {
                Log.i("TAG","onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Contributor> contributors) {
               for (Contributor c:contributors){
                   Log.i("TAG","RxJava-->"+c.getLogin()+"  "+c.getId()+"  "+c.getContributions());
               }
            }
        });
```

# POST
```java
Retrofit retrofit3 = new Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())         //添加 json 转换器
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //添加 RxJava 适配器
        .build();

Github gitHub = retrofit3.create(Github.class);
gitHub.RxContributors("square","retrofit")
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .subscribe(new Subscriber<List<Contributor>>() {
            @Override
            public void onCompleted() {
                Log.i("TAG","onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Contributor> contributors) {
                for (Contributor c:contributors){
                    Log.i("TAG","RxJava-->" + c.getLogin()+"  "+c.getId()+"  "+c.getContributions());
                }
            }
        });
```
