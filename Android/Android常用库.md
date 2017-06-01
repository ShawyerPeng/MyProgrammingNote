OkHttp：`compile 'com.squareup.okhttp3:okhttp:3.7.0'`
`compile 'com.squareup.okio:okio:1.12.0'`

[android OkHttp学习以及使用例子](http://blog.csdn.net/lan12334321234/article/details/70049513)  

```java
public class CameraFragment extends Fragment implements View.OnClickListener {
    private static final int SUCCESS = 1;
    private static final int FALL = 2;
    private OnFragmentInteractionListener mListener;
    private final OkHttpClient client = new OkHttpClient();   // 创建OkHttpClient对象
    private View view;
    private Button btnOK;
    private ImageView imgShow;

    public CameraFragment() {
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //加载网络成功进行UI的更新,处理得到的图片资源
                case SUCCESS:
                    //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    imgShow.setImageBitmap(bitmap);
                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            private void execute() throws Exception {
                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    System.out.println(response.code());
                    System.out.println(response.body().string());
                }
            }
        }).start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera, container, false);

//        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/logo.png");
//        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
//        draweeView.setImageURI(uri);
        btnOK = (Button) view.findViewById(R.id.btnOK);
        imgShow = (ImageView) view.findViewById(R.id.imgShow);
        btnOK.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                //1.创建一个okhttpclient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
                Request request = new Request.Builder()
                        .url("http://g.hiphotos.baidu.com/zhidao/pic/item/1e30e924b899a901da2aece318950a7b0308f5cc.jpg")
                        .build();
                //3.创建一个Call对象，参数是request对象，发送请求
                Call call = okHttpClient.newCall(request);
                //4.异步请求，请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到从网上获取资源，转换成我们想要的类型
                        byte[] Picture_bt = response.body().bytes();
                        //通过handler更新UI
                        Message message = handler.obtainMessage();
                        message.obj = Picture_bt;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                    }
                });
                break;
        }
    }
}
```

## GET
Request是OkHttp中访问的请求，Builder是辅助类。Response即OkHttp中的响应。
```java
public class GetExample {
    OkHttpClient client = new OkHttpClient();   // 创建OkHttpClient对象

    String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();   // 创建一个Request

        try (Response response = client.newCall(request).execute()) {   // new call
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        System.out.println(response);
    }
}
```

## 异步GET
```java
public class GetExample {
    OkHttpClient client = new OkHttpClient();   // 创建OkHttpClient对象

    void run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();   // 创建一个Request

        Call call = client.newCall(request);

        call.enqueue(new Callback() {           //请求加入调度
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        example.run("https://raw.github.com/square/okhttp/master/README.md");
    }
}
```

## POST
使用Request的post方法来提交请求体RequestBody
```java
public class PostExample {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    public static void main(String[] args) throws IOException {
        PostExample example = new PostExample();
        String json = example.bowlingJson("Jesse", "Jake");
        String response = example.post("http://www.roundsapp.com/post", json);
        System.out.println(response);
    }
}
```

## POST提交键值对
```java

```

# Picasso
http://square.github.io/picasso/
`compile 'com.squareup.picasso:picasso:2.5.2'`

























中
