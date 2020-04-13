package com.onbonbx.demo;

import android.graphics.Typeface;
import android.util.Log;

import java.awt.Color;
import java.awt.Font;

import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.Bx5GScreenProfile;
import onbon.bx05.area.DateTimeBxArea;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.TimeStyle;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.message.led.ReturnPingStatus;
import onbon.bx05.series.Bx5G;
import onbon.bx05.utils.TextBinary;

public class BxG5Helper {

    private static final String TAG = "BxG5Helper";

    /**
     * 获取控制器的状态
     * @param ip 控制器 ip 地址
     */
    public static void getStatus(String ip) {

        Bx5GScreenClient screen = new Bx5GScreenClient("screen", new Bx5G());
        try {
            // 连接控制器
            if (!screen.connect(ip, 5005)) {
                System.out.println("connect failed");
                return;
            }

            // 获取控制器状态
            Bx5GScreen.Result<ReturnPingStatus> result = screen.ping();
            if(result.isOK()) {
                Log.d(TAG, "ping result: " + result);
                Log.d(TAG, "(screen width, height): " + result.reply.getScreenWidth() + ", " + result.reply.getScreenHeight());
            }
            else {
                Log.d(TAG, "ping result: " + "failed!");
            }

            // 断开链接
            screen.disconnect();

        } catch (Bx5GException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 以节目的形式发送一段文本, 此处以 xw 为例
     * @param ip 控制器 ip 地址
     * @param text 要发送的文本
     */
    public static void sendTextArea(String ip, String text) {
        Bx5GScreenClient screen = new Bx5GScreenClient("screen");
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx5GScreenProfile profile = screen.getProfile();

            // 创建一个节目
            ProgramBxFile program = new ProgramBxFile(0, profile);
            // 创建一个区域
            TextCaptionBxArea area = new TextCaptionBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
            // 创建一个文本页
            TextBxPage tp = new TextBxPage(text);
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

            // 将节目写入控制器
            screen.writeProgram(program);

            // 断开链接
            screen.disconnect();

        } catch (Bx5GException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 以节目形式发送一个时间区
     * 此时间区以 hh:mm:ss 格式显示
     * @param ip 控制器 Ip 地址
     */
    public static void sendTimeArea(String ip) {
        Bx5GScreenClient screen = new Bx5GScreenClient("screen");
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx5GScreenProfile profile = screen.getProfile();

            // 创建一个节目
            ProgramBxFile program = new ProgramBxFile(0, profile);

            DateTimeBxArea timeArea = new DateTimeBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
            timeArea.setColor(Color.red);
            //timeArea.setFont(new Font("consolas", Font.PLAIN, timeModel.getFontSize()));

            timeArea.setMultiline(true);
            // 设置字体
            Typeface typeface = Typeface.MONOSPACE;
            Font font = new Font(typeface, 12);
            timeArea.setFont(font);

            timeArea.setDateStyle(null);
            timeArea.setTimeStyle(TimeStyle.HH_MM_SS_1);
            timeArea.setWeekStyle(null);

            // 将 area 添加到 program 中
            program.addArea(timeArea);

            // 将节目写入控制器
            screen.writeProgram(program);

            // 断开链接
            screen.disconnect();

        } catch (Bx5GException e) {
            e.printStackTrace();
            return;
        }
    }
}
