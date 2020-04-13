package com.onbonbx.demo;

import android.graphics.Typeface;
import android.util.Log;

import java.awt.Color;
import java.awt.Font;

import onbon.bx06.Bx6GControllerRS;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.area.DateTimeBxArea;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.TimeStyle;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.message.led.ReturnControllerStatus;
import onbon.bx06.message.led.ReturnPingStatus;
import onbon.bx06.series.Bx6M;
import onbon.bx06.utils.TextBinary;

public class BxG6Helper {



    private static final String TAG = "BxG6Helper";

    /**
     * 获取控制器的状态
     * @param ip 控制器 ip 地址
     */
    public static void getStatus(String ip) {

        Bx6GScreenClient screen = new Bx6GScreenClient("screen", new Bx6M());
        try {
            // 连接控制器
            if (!screen.connect(ip, 5005)) {
                System.out.println("connect failed");
                return;
            }

            // 获取控制器状态
            Bx6GScreen.Result<ReturnPingStatus> result = screen.ping();
            if(result.isOK()) {
                Log.d(TAG, "ping result: " + result);
                Log.d(TAG, "(screen width, height): " + result.reply.getScreenWidth() + ", " + result.reply.getScreenHeight());
            }
            else {
                Log.d(TAG, "ping result: " + "failed!");
            }

            // 断开链接
            screen.disconnect();

        } catch (Bx6GException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以节目的形式发送一段文本, 此处以 xw 为例
     * @param ip 控制器 ip 地址
     * @param text 要发送的文本
     */
    public static void sendTextArea(String ip, String text) {
        Bx6GScreenClient screen = new Bx6GScreenClient("screen", new Bx6M());
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx6GScreenProfile profile = screen.getProfile();

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

        } catch (Bx6GException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以节目形式发送一个时间区
     * 此时间区以 hh:mm:ss 格式显示
     * @param ip 控制器 Ip 地址
     */
    public static void sendTimeArea(String ip) {

        Bx6GScreenClient screen = new Bx6GScreenClient("screen", new Bx6M());
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx6GScreenProfile profile = screen.getProfile();

            // 创建一个节目
            ProgramBxFile program = new ProgramBxFile(0, profile);

            DateTimeBxArea timeArea = new DateTimeBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
            timeArea.setForeground(Color.red);
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

        } catch (Bx6GException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
