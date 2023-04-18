package com.example.radar;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JWebSocketClientService extends Service {
    public JWebSocketClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        URI uri = URI.create("ws://192.168.124.120:8018/ws/wscon/");
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                Log.e("JWebSClientService", unicodeDecode(message));
                sendMsg(unicodeDecode(message));
            }
        };
        try {
            client.connectBlocking();
//            String abc="123456789AB";
            String m="[{\"radarSn\":"+"\"" +MainActivity.radar_sn+"\""+"}]";
            client.send(m);
            Log.e("发送雷达序列号成功",m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendMsg(String msg) {

        Intent intent2 = new Intent("radar_change");

        intent2.putExtra("msg", msg);

        this.sendBroadcast(intent2);

    }
    public String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
