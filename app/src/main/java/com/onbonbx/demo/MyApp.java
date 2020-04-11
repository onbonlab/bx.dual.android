package com.onbonbx.demo;

import android.app.Application;
import android.util.Log;

import j2a.awt.AwtEnv;
import onbon.bx05.Bx5GEnv;
import onbon.bx06.Bx6GEnv;



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
