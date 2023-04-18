package com.example.radar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;



public class MainActivity extends AppCompatActivity {
public static String radar_sn="123456789AB";
private Radar radar;
private String url="";//过渡用的url
private  JSONObject res=null;
private String get_radar_version_url="http://192.168.124.120:8018/radar_app/get_soft_version";
private String get_wake_url="http://192.168.124.120:8018/radar_app/a_wake";
private String get_sleep_url="http://192.168.124.120:8018/radar_app/do_sleep";
private StringBuffer radar_info=new StringBuffer("");
private  String receive_text="";
 private  Recevie r;
    TextView sn;
    TextView id;
    TextView wifi_version;
    TextView radar_version;
    TextView radar_type;
    TextView radar_ip;
    TextView radar_mac;
    TextView display;
    Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("oncreate方法","执行");
        Log.e("a","a");
        //获取雷达sn
//        Intent intent_sn = getIntent();
//        radar_sn = intent_sn.getStringExtra("radar_sn");
        radar=new Radar();
        data=Data.getInstance();
        display=findViewById(R.id.display_text);
        display.setText(radar_info.toString());
        sn=findViewById(R.id.sn);
        id=findViewById(R.id.id);
        wifi_version=findViewById(R.id.wifi_version);
        radar_version=findViewById(R.id.radar_version);
        radar_type=findViewById(R.id.radar_type);
        radar_ip=findViewById(R.id.radar_ip);
        radar_mac=findViewById(R.id.radar_mac);
        Intent mIntent=new Intent(MainActivity.this,JWebSocketClientService.class) ;
        startService(mIntent);
        r=new Recevie();
        IntentFilter filter = new IntentFilter();
        filter.addAction("radar_change");
        registerReceiver(r, filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(r);
    }
    public void Onclick(View view) throws InterruptedException {
        switch (view.getId()){
            case R.id.get_version:
                url=get_radar_version_url+"?radar_sn="+radar_sn;
                res=radar.get(url);
                if(res!=null){
                    sn.setText("雷达sn："+res.getString("radarSn"));
                    id.setText("设备ID："+res.getString("deviceId"));
                    wifi_version.setText("wifi软件版本号："+res.getString("versionWifi"));
                    radar_version.setText("雷达软件版本号："+res.getString("versionRadar"));
                    radar_type.setText("雷达类型："+res.getString("radarHardType"));
                    radar_ip.setText("雷达本地ip地址："+res.getString("radarIp"));
                    radar_mac.setText("雷达mac地址："+res.getString("radarMac"));
                    Toast.makeText(this, "获取软件信息成功！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "获取软件信息失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.wake:
                url=get_wake_url+"?radar_sn="+radar_sn;
                JSONObject res1=radar.get(url);
                if(res1!=null){
                    Log.e("唤醒结果",res1.getString("aWake"));
                    if(res1.getString("aWake")=="true"){
                        Toast.makeText(this, "唤醒成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "唤醒失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "唤醒失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.sleep:
                url=get_sleep_url+"?radar_sn="+radar_sn;
                res=radar.get(url);
                if(res!=null){
                    if(res.getString("aWake")=="false"){
                        Toast.makeText(this, "休眠成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "休眠失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "休眠失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.clear:
                Log.e("清除","1");
                display.setText("");
                break;
            case R.id.set:
                Intent intent1=new Intent(this,set_activity.class);
                startActivity(intent1);
                break;
            case R.id.update:
                Intent intent2=new Intent(this,update_activity.class);
                startActivity(intent2);
                break;
        }
    }
    class Recevie extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            receive_text = intent.getStringExtra("msg");
            JSONObject j1=JSONObject.parseObject(receive_text);
            receive_text= j1.getString("res");
            if(receive_text!=null || !receive_text.equals("")){//如果广播接受到了信息且不为空
                radar_info.append(receive_text+"\n"+"----------------------------------------------------------------\n");
                data.setdata(radar_info.toString());
                display.setText(data.getdata());
                Log.e("收到雷达信息",data.getdata());
            }
        }
    }


}