# 下载
`compile 'com.mcxiaoke.volley:library:1.0.19'`

# 基本使用
```java
// 第一步
RequestQueue queue = Volley.newRequestQueue(this);
// 第二步
StringRequest request = new StringRequest(url, new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        Log.d(TAG, response);
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, error.toString());
    }
});
// 第三步
queue.add(request);
```


# 不带Cookie的不带参数的GET请求
```java
VolleyHelper.getRequest(requestQueue, "haha1", "http://www.zipcodeapi.com/rest/OjACA1TyoIpGRkIBimy0wR4o2W7wMz0BTBdtD712QrIHLqi3NQ3jI8HK5PhRoish/info.json/12345/degrees", new VolleyRequest() {
    @Override
    protected void onMyResponse(String s) {
        Log.i("Response：", "" + s);
    }

    @Override
    protected void onMyErrorResponse(VolleyError volleyError) {
        Log.i("Error：", "" + volleyError);
    }
});
```


# 不带Cookie的不带参数的POST请求
```java
 VolleyHelper.postRequest(requestQueue, "haha3", "http://baidu.com", new VolleyRequest() {
     @Override
     protected void onMyResponse(String s) {
         Log.i("Response：", "" + s);
     }

     @Override
     protected void onMyErrorResponse(VolleyError volleyError) {
         Log.i("Error：", "" + volleyError);
     }
 });
```

# **带Cookie的GET请求**
```java
VolleyHelper.getRequestWithCookie(requestQueue, "GetWithCookie", "http://115.159.188.200:8001/getPic/", Utils.getSettingNote(MainActivity.this, "cookie"), new VolleyRequest() {
    @Override
    protected void onMyResponse(String s) {
        Log.i("Response：", "" + s);
    }

    @Override
    protected void onMyErrorResponse(VolleyError volleyError) {
        Log.i("Error：", "" + volleyError);
    }
});
```

# **带Cookie的POST请求**
```java
// 获取Cookie的带参数post请求测试
Map<String, String> map = new HashMap<String, String>();
map.put("name", "admin");
map.put("pwd", "9876543210");

VolleyHelper.postRequestGetCookie(requestQueue, "Login", "http://115.159.188.200:8001/do_login/", map, new VolleyRequest() {
    @Override
    protected void onMyResponse(String s) {
        Log.i("Response：", "" + s);
    }

    @Override
    protected void onMyErrorResponse(VolleyError volleyError) {
        Log.i("Error：", "" + s);
    }
}, new CookieInterface() {
    @Override
    public void onResposeCookie(String cookie) {
        Log.i("Cookie：", "" + cookie);
        Utils.saveSettingNote(MainActivity.this, "cookie", cookie);
    }
});
```

# 网络请求显示在TextView上
```java
VolleyHelper.getRequest(requestQueue, "GitHub", "https://github.com/", new VolleyRequest() {
    @Override
    public void onMyResponse(String s) {
        webContent.setText(s);
    }

    @Override
    public void onMyErrorResponse(VolleyError volleyError) {
        Log.i("noco", volleyError.toString());
    }
});
```

# 展示图片
```java
VolleyHelper.displayImageWithCache(requestQueue,"https://avatars1.githubusercontent.com/u/26502308?v=3&s=40", imageView, R.mipmap.default_picture, R.mipmap.error_picture, VolleyHelper.DOUBLE_CACHE);
VolleyHelper.displayImage(requestQueue,"ImageLoader","http://upload-images.jianshu.io/upload_images/4043475-72b7868b0f671268.gif?imageMogr2/auto-orient/strip", imageView, R.mipmap.default_picture,R.mipmap.error_picture,0,0, Bitmap.Config.RGB_565);
```

# 完整代码
```java
package com.example.volleydemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView webContent;
    private ImageView imageView;

    private void assignViews() {
        webContent = (TextView) findViewById(R.id.web_content);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        //请求测试
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        VolleyHelper.displayImageWithCache(requestQueue,"https://avatars1.githubusercontent.com/u/26502308?v=3&s=40", imageView, R.mipmap.default_picture, R.mipmap.error_picture, VolleyHelper.DOUBLE_CACHE);
        VolleyHelper.displayImage(requestQueue,"ImageLoader","http://upload-images.jianshu.io/upload_images/4043475-72b7868b0f671268.gif?imageMogr2/auto-orient/strip", imageView, R.mipmap.default_picture,R.mipmap.error_picture,0,0, Bitmap.Config.RGB_565);

        //网络请求显示在TextView上
        VolleyHelper.getRequest(requestQueue, "baidu", "http://www.google.com/ncr", new VolleyRequest() {
            @Override
            public void onMyResponse(String s) {
                webContent.setText(s);
            }

            @Override
            public void onMyErrorResponse(VolleyError volleyError) {
                Log.i("noco", volleyError.toString());
            }
        });

        //获取Cookie的带参数post请求测试
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "admin");
        map.put("pwd", "9876543210");

        VolleyHelper.postRequestGetCookie(requestQueue, "ShareTo", "http://115.159.188.200:8001/do_login/", map, new VolleyRequest() {
            @Override
            protected void onMyResponse(String s) {
                Log.i("noco",""+s);
            }

            @Override
            protected void onMyErrorResponse(VolleyError volleyError) {

            }
        }, new CookieInterface() {
            @Override
            public void onResposeCookie(String cookie) {//不能进行UI更新
                Log.i("noco cookie",""+cookie);
                Utils.saveSettingNote(MainActivity.this, "cookie", cookie);

                //不带Cookie的get请求
                VolleyHelper.getRequest(requestQueue, "haha1", "http://www.zipcodeapi.com/rest/OjACA1TyoIpGRkIBimy0wR4o2W7wMz0BTBdtD712QrIHLqi3NQ3jI8HK5PhRoish/info.json/12345/degrees", new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco1", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco1", "" + volleyError);
                    }
                });

                //带Cookie的get请求
                VolleyHelper.getRequestWithCookie(requestQueue, "haha2", "http://115.159.188.200:8001/getPic/", Utils.getSettingNote(MainActivity.this, "cookie"), new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco2", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco2", "" + volleyError);
                    }
                });

                //不Cookie的且不带参数的post请求
                VolleyHelper.postRequest(requestQueue, "haha3", "http://115.28.49.92:8082/port/get_banner_port.ashx", new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco3", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco3", "" + volleyError);
                    }
                });

                //带Cookie且不带参数post请求
                VolleyHelper.postRequestWithCookie(requestQueue, "haha4", "http://115.28.49.92:8082/port/get_banner_port.ashx", Utils.getSettingNote(MainActivity.this, "cookie"), new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco4", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco4", "" + volleyError);
                    }
                });
            }
        });
    }
}
```
