https://developer.android.com/studio/build/index.html

## 设置应用ID
```gradle
android {
    defaultConfig {
        applicationId "com.example.myapp"
        minSdkVersion 15
        targetSdkVersion 24
        versionCode 1
        versionName "1.0"
    }
    ...
}
```


## 构建类型
构建类型定义 Gradle 在构建和打包您的应用时使用的某些属性，通常针对开发生命周期的不同阶段进行配置。例如，调试构建类型支持调试选项，使用调试密钥签署 APK；而发布构建类型则可压缩、混淆 APK 以及使用发布密钥签署 APK 进行分发。您必须至少定义一个构建类型才能构建应用 - Android Studio 默认情况下会创建调试和发布构建类型。要开始为应用自定义打包设置，请学习如何配置构建类型。

## 产品风味
产品风味代表您可以发布给用户的不同应用版本，例如免费和付费的应用版本。您可以将产品风味自定义为使用不同的代码和资源，同时对所有应用版本共有的部分加以共享和重复利用。产品风味是可选项，并且您必须手动创建。要开始创建不同的应用版本，请学习如何配置产品风味。

## 构建变体
构建变体是构建类型与产品风味的交叉产物，是 Gradle 在构建应用时使用的配置。您可以利用构建变体在开发时构建产品风味的调试版本，或者构建已签署的产品风味发布版本进行分发。您并不直接配置构建变体，而是配置组成变体的构建类型和产品风味。创建附加构建类型或产品风味也会创建附加构建变体。要了解如何创建和管理构建变体，请阅读配置构建变体概览。

## 清单条目
您可以为构建变体配置中清单文件的一些属性指定值。这些构建值会替换清单文件中的现有值。如果您想为模块生成多个 APK，让每一个 APK 文件都具有不同的应用名称、最低 SDK 版本或目标 SDK 版本，便可运用这一技巧。存在多个清单时，Gradle 会合并清单设置。

## 依赖项
构建系统管理来自您的本地文件系统以及来自远程存储区的项目依赖项。这样一来，您就不必手动搜索、下载依赖项的二进制文件包以及将它们复制到项目目录内。要了解更多信息，请学习如何声明依赖项。

## 签署
构建系统让您能够在构建配置中指定签署设置，并可在构建过程中自动签署您的 APK。构建系统通过使用已知凭据的默认密钥和证书签署调试版本，以避免在构建时提示密码。除非您为此构建显式定义签署配置，否则，构建系统不会签署发布版本。如果您没有发布密钥，可以按签署您的应用中所述生成一个。

## ProGuard
构建系统让您能够为每个构建变体指定不同的 ProGuard 规则文件。构建系统可在构建过程中运行 ProGuard 对类进行压缩和混淆处理。

## APK 拆分
构建系统让您能够自动构建不同的 APK，并且每个 APK 只包含特定屏幕密度或应用二进制界面 (ABI) 所需的代码和资源。如需了解详细信息，请参阅配置 APK 拆分。

## 全局配置
```gradle
buildscript {
    repositories {
        jcenter()
        maven{url 'http://maven.aliyun.com/nexus/content/groups/public/'}
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.1'
    }
}

allprojects {
   repositories {
       jcenter()
       mavenCentral()
       mavenLocal()
       maven {
           url "http://repo.mycompany.com/maven2"
       }
   }
}
```

# 模块配置
```gradle
apply plugin: 'com.android.application'

android {
    compileSdkVersion 25
    buildToolsVersion "25.0.3"
    defaultConfig {
        applicationId "com.example.androidtemplate"
        minSdkVersion 15
        targetSdkVersion 25
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    productFlavors {        // 更改用于构建变体的应用ID
        free {
            applicationIdSuffix ".free"
        }
        pro {
            applicationIdSuffix ".pro"
        }
        dev {
            minSdkVersion 21
            versionNameSuffix "-dev"
            applicationIdSuffix '.dev'
        }
        prod {
        }
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    // Dependency on local binaries
    compile fileTree(dir: 'libs', include: ['*.jar'])

    // Dependency on a local library module
    compile project(":mylibrary")

    // androidTestCompile
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })

    // Adds a remote binary dependency only for local tests.
    testCompile 'junit:junit:4.12'

    // Dependency on a remote binary
    compile 'com.android.support:appcompat-v7:25.3.1'
    compile 'com.android.support.constraint:constraint-layout:1.0.2'
}
```
