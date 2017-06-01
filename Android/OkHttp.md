```java
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        Button button = (Button) findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        // 新建CookieJar
                        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                        // 登录POST
                        RequestBody requestBodyPost = new FormBody.Builder().add("name", "admin").add("pwd", "9876543210").build();
                        Request requestPost = new Request.Builder().url("http://115.159.188.200:8001/do_login/").post(requestBodyPost).build();
                        okHttpClient.newCall(requestPost).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("MainActivity", "请求失败！");
                            }
                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                try {
                                    System.out.println(response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        // 登陆后GET
                        Request requestGet = new Request.Builder().url("http://115.159.188.200:8001/showImg/").build();
                        Response response = null;
                        try {
                            response = okHttpClient.newCall(requestGet).execute();
                            System.out.println(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


//                        String postBody = "{ \"name\":\"admin\",\"pwd\":\"9876543210\" }";
//                        // { "name":"admin","pwd":"9876543210" }
//                        Request request = new Request.Builder()
//                                .url("http://115.159.188.200:8001/login/")
//                                .post(RequestBody.create(JSON, postBody))
//                                .build();
//                        try {
//                            Response response = okHttpClient.newCall(request).execute();
//                            System.out.println(response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }).start();

            }
        });
```