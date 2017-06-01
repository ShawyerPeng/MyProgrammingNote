# 创建 Fragment 类
创建 Fragment 时，必须重写 `onCreateView()` 回调方法来定义布局。
```java
public class ArticleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // 拉伸该 Fragment 的布局
        return inflater.inflate(R.layout.article_view, container, false);
    }
}
```


# 用 XML 将 Fragment 添加到 Activity
每个 Fragment 实例都必须与一个 FragmentActivity 关联。我们可以在 Activity 的 XML 布局文件中逐个定义 Fragment 来实现这种关联。
`res/layout/news_articles.xml`
```xml
<fragment android:name="com.example.android.fragments.HeadlinesFragment"
            android:id="@+id/headlines_fragment"
            android:layout_weight="1"
            android:layout_width="0dp"
            android:layout_height="match_parent" />

<fragment android:name="com.example.android.fragments.ArticleFragment"
            android:id="@+id/article_fragment"
            android:layout_weight="2"
            android:layout_width="0dp"
            android:layout_height="match_parent" />
```
然后将这个布局文件用到 Activity 中。
`MainActivity.java`
```java
public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);
    }
}
```

# 建立灵活动态的 UI
利用 FragmentManager 类提供的方法，你可以在运行时添加、移除和替换 Activity 中的 Fragment，以便为用户提供一种动态体验。
## 在运行时向 Activity 添加 Fragment
你可以在 Activity 运行时向其添加 Fragment，而不用像 上一课 中介绍的那样，使用 <fragment> 元素在布局文件中为 Activity 定义 Fragment。如果你打算在 Activity 运行周期内更改 Fragment，就必须这样做。

如果 Activity 中的 Fragment 可以移除和替换，你应在调用 Activity 的 onCreate() 方法期间为 Activity 添加初始 Fragment(s)。

必须在布局中为 Fragment 提供 View 容器，以便保存 Fragment 的布局。

要用一个 Fragment 替换另一个 Fragment，Activity 的布局中需要包含一个作为 Fragment 容器的空 FrameLayout。
`res/layout/news_articles.xml`
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/fragment_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

在 Activity 中，用 Support Library API 调用 getSupportFragmentManager() 以获取 FragmentManager，然后调用 beginTransaction() 创建 FragmentTransaction，然后调用 add() 添加 Fragment。
你可以使用同一个 FragmentTransaction 对 Activity 执行多 Fragment 事务。当你准备好进行更改时，必须调用 commit()。


为上述布局添加 Fragment：
```java
public class MainActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        // 确认 Activity 使用的布局版本包含 fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // 不过，如果我们要从先前的状态还原，则无需执行任何操作而应返回，否则
            // 就会得到重叠的 Fragment。
            if (savedInstanceState != null) {
                return;
            }

            // 创建一个要放入 Activity 布局中的新 Fragment
            HeadlinesFragment firstFragment = new HeadlinesFragment();

            // 如果此 Activity 是通过 Intent 发出的特殊指令来启动的，
            // 请将该 Intent 的 extras 以参数形式传递给该 Fragment
            firstFragment.setArguments(getIntent().getExtras());

            // 将该 Fragment 添加到“fragment_container” FrameLayout 中
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }
}
```

## 用一个 Fragment 替换另一个 Fragment
调用 replace() 方法，而非 add()
```java
// 创建 Fragment 并为其添加一个参数，用来指定应显示的文章
ArticleFragment newFragment = new ArticleFragment();
Bundle args = new Bundle();
args.putInt(ArticleFragment.ARG_POSITION, position);
newFragment.setArguments(args);

FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// 将 fragment_container View 中的内容替换为此 Fragment，
// 然后将该事务添加到返回堆栈，以便用户可以向后导航
transaction.replace(R.id.fragment_container, newFragment);
transaction.addToBackStack(null);

// 执行事务
transaction.commit();
```

# 与其他 Fragment 交互
为了让 Fragment 与包含它的 Activity 进行交互，可以在 Fragment 类中定义一个接口，并在 Activity 中实现。该 Fragment 在它的 onAttach() 方法生命周期中获取该接口的实现，然后调用接口的方法，以便与 Activity 进行交互。
## 定义接口








# `android.support.v4.app.Fragment`和`android.app.Fragment`区别
`android.app.Fragment` 兼容的最低版本是android:minSdkVersion="11" 即3.0版  
`android.support.v4.app.Fragment` 兼容的最低版本是android:minSdkVersion="4" 即1.6版  
## `android.support.v4.app.Fragment`
```java
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CameraFragment.OnFragmentInteractionListener {
    private FragmentManager fragmentManager = null;

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Fragment cameraFragment = new CameraFragment();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, cameraFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
```

## `android.app.Fragment`
```java
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CameraFragment.OnFragmentInteractionListener {
    private FragmentManager fragmentManager = null;

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Fragment cameraFragment = new CameraFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, cameraFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
```