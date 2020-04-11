package com.onbonbx.demo;

import android.graphics.Typeface;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.awt.Color;
import java.awt.Font;

import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.Bx5GScreenProfile;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.message.area.TextCaptionArea;
import onbon.bx05.utils.TextBinary;

public class MainActivity extends AppCompatActivity {

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

                        Bx5GScreenClient screen = new Bx5GScreenClient("screen");
                        try {
                            // 连接控制器
                            screen.connect(ip.getText().toString(), 5005);

                            //
                            Bx5GScreenProfile profile = screen.getProfile();

                            // 创建一个节目
                            ProgramBxFile program = new ProgramBxFile(0, profile);
                            // 创建一个区域
                            TextCaptionBxArea area = new TextCaptionBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
                            // 创建一个文本页
                            TextBxPage tp = new TextBxPage(text.getText().toString());
                            tp.setForeground(Color.red);
                            tp.setHorizontalAlignment(TextBinary.Alignment.CENTER);
                            tp.setVerticalAlignment(TextBinary.Alignment.CENTER);

                            // 设置字体
                            Typeface typeface = Typeface.MONOSPACE;
                            Font font = new Font(typeface, 12);
                            tp.setFont(font);

                            // 将数据页添加到 area 中
                            area.addPage(tp);
                            // 将 area 添加到 program 中
                            program.addArea(area);

                            // 断开链接
                            screen.disconnect();

                        } catch (Bx5GException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }).start();
            }
        });

        // 发送一个时间区
        Button btnSendTime = findViewById(R.id.btn_send_time);
        btnSendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
