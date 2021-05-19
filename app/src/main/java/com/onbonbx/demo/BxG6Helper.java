package com.onbonbx.demo;

import android.graphics.Typeface;
import android.util.Log;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import onbon.bx05.Bx5GScreen;
import onbon.bx06.Bx6GControllerRS;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.area.DateTimeBxArea;
import onbon.bx06.area.DynamicBxArea;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.TimeStyle;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.message.global.ACK;
import onbon.bx06.message.led.ReturnControllerStatus;
import onbon.bx06.message.led.ReturnPingStatus;
import onbon.bx06.series.Bx6M;
import onbon.bx06.series.Bx6Q;
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
        Bx6GScreenClient screen = new Bx6GScreenClient("screen", new Bx6Q());
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx6GScreenProfile profile = screen.getProfile();

            // 创建一个节目
            ProgramBxFile program = new ProgramBxFile(0, profile);
            // 创建一个区域
            TextCaptionBxArea area = new TextCaptionBxArea(0, 0, profile.getWidth(), 16, profile);
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

        Bx6GScreenClient screen = new Bx6GScreenClient("screen", new Bx6Q());
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx6GScreenProfile profile = screen.getProfile();

            // 创建一个节目
            ProgramBxFile program = new ProgramBxFile(0, profile);

            DateTimeBxArea timeArea = new DateTimeBxArea(0, 16, profile.getWidth(), 16, profile);
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

    /**
     * 为 6Q 发送一个动态区
     * @param ip 控制器 Ip 地址
     */
    public static void sendDynamicAreaFor6Q(String ip) {

        Bx6GScreenClient screen = new Bx6GScreenClient("screen", new Bx6Q());
        try {
            // 连接控制器
            screen.connect(ip, 5005);

            //
            Bx6GScreenProfile profile = screen.getProfile();

            // 六代控制器动态区
            DynamicBxAreaRule rule = new DynamicBxAreaRule();

            // 设定动态区ID，此处ID为0，多个动态区ID不能相同
            rule.setId(0);

            // 设定异步节目停止播放，仅播放动态区
            // 0:动态区与异步节目一起播放
            // 1:异步节目停止播放，仅播放动态区
            // 2:当播放完节目编号最高的异步节目后播放该动态区
            rule.setImmediatePlay((byte) 1);

            // 设定动态区循环播放
            // 0:循环播放
            // 1:显示完成后静置显示最后一页数据
            // 2:循环显示，超过设定时间后仍未更新时不再显示
            // 3:循环显示，超过设定时间后仍未更新时显示Logo信息
            // 4:显示完成最后一页后就不再显示
            rule.setRunMode((byte) 0);
            DynamicBxArea area = new DynamicBxArea(0, 0, 128, 32, screen.getProfile());
            //area.setBackgroundFlag(true);
            TextBxPage page = new TextBxPage("动态区abc");
            area.addPage(page);
            Bx6GScreen.Result<ACK> result = screen.writeDynamic(rule, area);
            if(result.isNACK()) {
                Log.d(TAG, "sendDynamicAreaFor6Q: dynamic area");
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
}
