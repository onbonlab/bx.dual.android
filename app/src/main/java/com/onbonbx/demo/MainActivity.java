package com.onbonbx.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ip = findViewById(R.id.ip);
        final EditText text = findViewById(R.id.txt_to_sent);

        // 获取控制器状态
        Button btnGetStatus = findViewById(R.id.btn_get_para);
        btnGetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //
                        BxG5Helper.getStatus(ip.getText().toString());
                    }
                }).start();
            }
        });

        // 发送一个文本区
        Button btnSendTxt = findViewById(R.id.btn_send_text);
        btnSendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BxG5Helper.sendTextArea(ip.getText().toString(), text.getText().toString());
                    }
                }).start();
            }
        });

        // 发送一个时间区
        Button btnSendTime = findViewById(R.id.btn_send_time);
        btnSendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BxG5Helper.sendTimeArea(ip.getText().toString());
                    }
                }).start();
            }
        });

        //
        // 六代卡测试
        Button btnChekStatusG6 = findViewById(R.id.btn_get_para_g6);
        btnChekStatusG6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BxG6Helper.getStatus(ip.getText().toString());
                    }
                }).start();
            }
        });

        // 发送一个文本节目
        Button btnSendTxtG6 = findViewById(R.id.btn_send_text_g6);
        btnSendTxtG6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BxG6Helper.sendTextArea(ip.getText().toString(), text.getText().toString());
                    }
                }).start();
            }
        });

        // 发送一个时间区节目
        Button btnSendTimeG6 = findViewById(R.id.btn_send_time_g6);
        btnSendTimeG6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BxG6Helper.sendTimeArea(ip.getText().toString());
                    }
                }).start();
            }
        });


        // 发送一个动态区
        Button btnSendDynamicAreaG6 = findViewById(R.id.btn_send_dynamic_g6);
        btnSendDynamicAreaG6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BxG6Helper.sendDynamicAreaFor6Q(ip.getText().toString());
                    }
                }).start();
            }
        });
    }






}
