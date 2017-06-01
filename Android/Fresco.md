[MyApplication.java]
```java
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);
	}
}
```

[AndroidManifest.xml]
```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET" />
    <application
        android:label="@string/app_name"
        android:name=".MyApplication" >
    </application>
</manifest>
```

[xml布局文件]
```xml
<!-- 其他元素-->
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:fresco="http://schemas.android.com/apk/res-auto"
    android:layout_height="match_parent"
    android:layout_width="match_parent">

    <com.facebook.drawee.view.SimpleDraweeView
        android:id="@+id/my_image_view"
        android:layout_width="130dp"
        android:layout_height="130dp"
        fresco:placeholderImage="@drawable/my_drawable"
    />
```

加载图片
```java
Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/logo.png");
SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
draweeView.setImageURI(uri);
```




# DraweeView
继承于 View, 负责图片的显示。

# DraweeHierarchy
用于组织和维护最终绘制和呈现的 Drawable 对象，通过它来在Java代码中自定义图片的展示

# DraweeController
和 image loader 交互对Uri加载到的图片做一些额外的处理

# DraweeControllerBuilder


# Listeners
使用 ControllerListener 的一个场景就是设置一个 Listener监听图片的下载。


# URI
Uri uri = Uri.parse("res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher);


# 定制
```xml
<com.facebook.drawee.view.SimpleDraweeView
  android:id="@+id/my_image_view"
  android:layout_width="20dp"
  android:layout_height="20dp"
  fresco:fadeDuration="300"
  fresco:actualImageScaleType="focusCrop"

  fresco:placeholderImage="@color/wait_color"       占位图(Placeholder)
  fresco:placeholderImageScaleType="fitCenter"

  fresco:failureImage="@drawable/error"             设置加载失败占位图
  fresco:failureImageScaleType="centerInside"
  fresco:retryImage="@drawable/retrying"            点击重新加载图
  fresco:retryImageScaleType="centerCrop"

  fresco:progressBarImage="@drawable/progress_bar"  显示进度条
  fresco:progressBarImageScaleType="centerInside"
  fresco:progressBarAutoRotateInterval="1000"

  fresco:backgroundImage="@color/blue"              背景（最先绘制）
  fresco:overlayImage="@drawable/watermark"         设置叠加图(Overlay)
  fresco:pressedStateOverlayImage="@color/red"      设置按压状态下的叠加图

  fresco:roundAsCircle="false"
  fresco:roundedCornerRadius="1dp"
  fresco:roundTopLeft="true"
  fresco:roundTopRight="false"
  fresco:roundBottomLeft="false"
  fresco:roundBottomRight="true"
  fresco:roundWithOverlayColor="@color/corner_color"    背景固定成指定颜色
  fresco:roundingBorderWidth="2dp"
  fresco:roundingBorderColor="@color/border_color"

  android:id="@+id/my_image_view"
  android:layout_width="20dp"
  android:layout_height="wrap_content"
  fresco:viewAspectRatio="1.33"   固定宽高比
  
/>
```

或在代码中指定：`mSimpleDraweeView.setAspectRatio(1.33f);`
`mSimpleDraweeView.setImageURI(uri);`


# 在JAVA代码中使用Drawees(自定义DraweeHierarchy)
```java
List<Drawable> backgroundsList;
List<Drawable> overlaysList;
GenericDraweeHierarchyBuilder builder =
    new GenericDraweeHierarchyBuilder(getResources());
GenericDraweeHierarchy hierarchy = builder
    .setFadeDuration(300)
    .setPlaceholderImage(new MyCustomDrawable())
    .setBackgrounds(backgroundList)
    .setOverlays(overlaysList)
    .build();
mSimpleDraweeView.setHierarchy(hierarchy);



.setProgressBarImage(new ProgressBarDrawable())  //进度条

```


# ControllerBuilder
```java

```














