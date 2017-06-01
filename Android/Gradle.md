# Gradle下载
```
https://gradle.org/install
C:\Gradle\gradle-3.5\bin 添加到PATH变量中
```

```
wget https://services.gradle.org/distributions/gradle-3.5-bin.zip

mkdir /opt/gradle
unzip -d /opt/gradle gradle-3.5-bin.zip
ls /opt/gradle/gradle-3.5

export PATH=$PATH:/opt/gradle/gradle-3.5/bin
```

# Gradle基础
[ANDROID STUDIO系列教程四--GRADLE基础](http://stormzhang.com/devtools/2014/12/18/android-studio-tutorial4/)  
![](http://i2.muimg.com/567571/8cefcd83d0a26ef0.jpg)  
## `app/build.gradle`
app文件夹下这个Module的gradle配置文件，也可以算是整个项目最主要的gradle配置文件

```
// 声明构建的项目类型
apply plugin: 'com.android.application'     // 说明module的类型：com.android.application为程序，com.android.library为库

repositories {
    mavenCentral()
    flatDir {
        dirs 'libs'
    }
    flatDir {
        dirs '../third_party/AndroidSlidingUpPanel/libs/'
    }
    flatDir {
        dirs '../third_party/svg-android/libs/'
    }
}

android {
    compileSdkVersion 21        // 编译SDK的版本
    buildToolsVersion "21.1.1"  // Build Tools的版本

    defaultConfig {             // 默认配置
        applicationId "me.storm.ninegag"    // 应用程序的包名
        minSdkVersion 14                    // 支持的最低版本
        targetSdkVersion 21                 // 支持的目标版本
        versionCode 1                       // 版本号
        versionName "1.0.0"                 // 版本名
    }

    compileOptions {            // java版本
        sourceCompatibility JavaVersion.VERSION_1_7
        targetCompatibility JavaVersion.VERSION_1_7
    }

    signingConfigs {            // 签名配置
        debug {     // debug版签名配置
            storeFile file("../android/debug.keystore")
            storePassword "android"
            keyAlias "androiddebugkey"
            keyPassword "android"
        }
        release {   // 发布版签名配置
            storeFile file("../android/fk.keystore")    // 密钥文件路径
            storePassword "123"                         // 密钥文件密码
            keyAlias "fk"                               // key别名
            keyPassword "123"                           // key密码
        }
    }

    buildTypes {    // build类型
        debug {
            signingConfig  signingConfigs.debug
        }

        release {
            minifyEnabled false             // 是否进行混淆
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.txt'      // 指定混淆规则文件的位置
            signingConfig  signingConfigs.release
        }
    }

    lintOptions {
      abortOnError false        // 移除lint检查的error
    }

    aaptOptions {
        noCompress 'apk'
    }

    packagingOptions {
        exclude 'LICENSE.txt'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE-FIREBASE.txt'
        exclude 'META-INF/NOTICE'
    }

    testOptions {
        unitTests.returnDefaultValues = true
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: '*.jar')         // 编译libs目录下的所有jar包

    compile 'com.android.support:support-v4:21.0.2'         // 编译第三方库
    compile 'com.etsy.android.grid:library:1.0.5'
    compile 'com.alexvasilkov:foldable-layout:1.0.1'

    compile project(':extras:ShimmerAndroid')               // 编译附加的项目（extras目录下的ShimmerAndroid模块）
}
```
* buildToolsVersion这个需要你本地安装该版本才行，很多人导入新的第三方库，失败的原因之一是build version的版本不对，这个可以手动更改成你本地已有的版本或者打开 SDK Manager 去下载对应版本。
* applicationId代表应用的包名。
* proguardFiles这部分有两段，前一部分代表系统默认的android程序的混淆文件，该文件已经包含了基本的混淆声明，免去了我们很多事，这个文件的目录在 **/tools/proguard/proguard-android.txt** , 后一部分是我们项目里的自定义的混淆文件，目录就在 **app/proguard-rules.txt** , 如果你用Studio 1.0创建的新项目默认生成的文件名是 **proguard-rules.pro**, 这个名字没关系，在这个文件里你可以声明一些第三方依赖的一些混淆规则，由于是开源项目，9GAG里并未进行混淆，具体混淆的语法也不是本篇博客讨论的范围。最终混淆的结果是这两部分文件共同作用的。
* `compile project(‘:extras:ShimmerAndroid’)`这一行是因为9GAG中存在其他Module，总之你可以理解成Android Library，由于Gradle的普及以及远程仓库的完善，这种依赖渐渐的会变得非常不常见，但是你需要知道有这种依赖的。
* 以上文件里的内容只是基本配置，其实还有很多自定义部分，如自动打包debug，release，beta等环境，签名，多渠道打包等。

## `extras/ShimmerAndroid/build.gradle`
每一个Module都需要有一个gradle配置文件，语法都是一样，唯一不同的是开头声明的是`apply plugin: 'com.android.library'`

## `gradle/wrapper/gradle-wrapper.properties`
声明了gradle的目录与下载路径以及当前项目使用的gradle版本
```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip
```

## `build.gradle`
整个项目的gradle基础配置文件
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.1'
    }
}

allprojects {
    repositories {
        jcenter()
    }
}
```
内容主要包含了两个方面：一个是声明仓库的源，这里可以看到是指明的jcenter(), 之前版本则是mavenCentral(), jcenter可以理解成是一个新的中央远程仓库，兼容maven中心仓库，而且性能更优。另一个是声明了android gradle plugin的版本，android studio 1.0正式版必须要求支持gradle plugin 1.0的版本。

## `settings.gradle`
全局的项目配置文件，里面主要声明一些需要加入gradle的module
```
include ':android', ':server'
```
文件中的android, server都是module，如果还有其他module都需要按照如上格式加进去。





















s
