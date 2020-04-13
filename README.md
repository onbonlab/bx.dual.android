# bx.dual.android 

此工程为仰邦五代/六代单双色控制器 ANDRODI开发包与DEMO，你可以在此工程中的 libs 文件夹中获取最新的 SDK。

更详细的使用说明，可以参考以下文档：

https://doc.onbonbx.com/



## 使用说明

**步骤1:**  导入 sdk

将所有库文件拷贝至 libs 文件夹，并引入工程，如下所示：

在 module 的 build.gradle 中添加如下代码：

```gradle
implementation files('libs/bx05-0.5.0-SNAPSHOT.jar')
implementation files('libs/bx05.message-0.5.0-SNAPSHOT.jar')
implementation files('libs/bx06-0.6.0-SNAPSHOT.jar')
implementation files('libs/bx06.message-0.6.0-SNAPSHOT.jar')
implementation files('libs/log4j-1.2.14.jar')
implementation files('libs/simple-xml-2.7.1.jar')
implementation files('libs/uia-comm-0.3.3.jar')
implementation files('libs/uia-utils-0.2.0.jar')
implementation files('libs/uia-message-0.6.0.jar')

implementation(name: 'java.awt4a-0.1-release', ext: 'aar')
```

在 project 的 build.gradle 中添加如下代码：

```gradle
allprojects {
    repositories {
        google()
        jcenter()

        flatDir {
            dirs 'libs'
        }
    }
}
```

**步骤2:** sdk 初始化

SDK 初始化在整个 APP 中只能调用一次，因此，我们将其放在 application 的 onCreate() 接口调用。如下所示：

```java
public class MyApp extends Application {

    private static final String TAG="MyApp";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // java.awt for android 初始化
            AwtEnv.link(this);
            // 是否启动抗锯齿
            AwtEnv.configPaintAntiAliasFlag(false);

            // 初始化五代
            Bx5GEnv.initial();

            // 建立 BX6G API 運行環境。
            Bx6GEnv.initial();
            Log.d(TAG, "sdk 6 version:" + Bx6GEnv.VER_INFO);
        }
        catch (Exception ex) {
            Log.d(TAG, "sdk init error");
        }
    }
}
```

**步骤3：** 在 manifest.xml 中添加相关权限

对于网络通讯，必需先在 manifest 文件中申请相关网络访问的权限，否则会出现通讯失败。

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.onbonbx.demo">
    <!-- 允许联网 -->
    <!-- 获取GSM（2g）、WCDMA（联通3g）等网络状态的信息 -->
    <uses-permission android:name="android.permission.INTERNET" />
    <!-- 获取wifi网络状态的信息 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- 获取wifi网络状态的信息 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!-- 保持CPU 运转，屏幕和键盘灯有可能是关闭的,用于文件上传和下载 -->
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />

    <application
        android:name=".MyApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

**步骤4：** 在线程中调用相关接口

在 Android 系统中，网络通讯不能放在主线程中调用，而必需另起线程。通常如下：

```java
new Thread(new Runnable() {
    @Override
    public void run() {

        Bx5GScreenClient screen = new Bx5GScreenClient("screen");
        try {
            // 连接控制器
            screen.connect(ip.getText().toString(), 5005);

            // 获取控制器状态
            screen.ping();

            // 断开链接
            screen.disconnect();

        } catch (Bx5GException e) {
            e.printStackTrace();
            return;
        }
    }
}).start();
```