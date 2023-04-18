package com.example.radar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class update_activity extends AppCompatActivity {
    private Radar radar;
    private JSONObject res=null;
    private RequestBody requestBody=null;
    private String url="";
    EditText id;
    EditText ip;
    EditText pork;
    EditText wifi_name;
    EditText wifi_password;
    EditText gateway;
    EditText update_ip;
    EditText update_pork;
    private String get_server_url="http://192.168.124.120:8018/radar_app/get_serv_radar";
    private String set_server_url="http://192.168.124.120:8018/radar_app/set_option_param";
    private String get_progress_url="http://192.168.124.120:8018/radar_app/get_boundary";
    private String begin_update_url="http://192.168.124.120:8018/radar_app/ota_start";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        radar=new Radar();
        id=findViewById(R.id.edittext_id);
        ip=findViewById(R.id.edittext_ip);
        pork=findViewById(R.id.edittext_port);
        wifi_name=findViewById(R.id.edittext_route);
        wifi_password=findViewById(R.id.edittext_password);
        gateway=findViewById(R.id.edittext_gateway);
        update_ip=findViewById(R.id.edittext_update_network);
        update_pork=findViewById(R.id.edittext_update_pork);
    }
    public void Onclick(View view) throws IOException, InterruptedException {
        switch (view.getId()){
            case R.id.network_set:
                String s1=id.getText().toString();
                String s2=ip.getText().toString();
                String s3=pork.getText().toString();
                String s4=wifi_name.getText().toString();
                String s5=wifi_password.getText().toString();
                String s6=gateway.getText().toString();
                if(!s1.equals("") && !s2.equals("") &&!s3.equals("") &&!s4.equals("") &&!s5.equals("") &&!s6.equals("") ){
                    requestBody = new FormBody.Builder()
                            .add("radar_sn",MainActivity.radar_sn)
                            .add("deviceId",s1)
                            .add("dstUrl",s2)
                            .add("dstPort",s3)
                            .add("staSsid",s4)
                            .add("staPwd",s5)
                            .add("staGateway",s6)
                            .build();
                    res=radar.post(set_server_url,requestBody);
                    if(res!=null){
                        Toast.makeText(this, "设置服务器成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "设置服务器失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "您有未设置的服务器参数！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.network_readback:
                url=get_server_url+"?radar_sn="+MainActivity.radar_sn;
                res=radar.get(url);
                if(res!=null){
                    id.setText(res.getJSONObject("tOptionParam").getString("deviceId"));
                    ip.setText(res.getJSONObject("tOptionParam").getString("dstUrl"));
                    pork.setText(res.getJSONObject("tOptionParam").getString("dstPort"));
                    wifi_name.setText(res.getJSONObject("tOptionParam").getString("staSsid"));
                    wifi_password.setText(res.getJSONObject("tOptionParam").getString("staPwd"));
                    gateway.setText(res.getJSONObject("tOptionParam").getString("staGateway"));
                    Toast.makeText(this, "回读服务器参数成功！", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "回读服务器参数失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.begin_update:
                String up1=update_ip.getText().toString();
                String up2=update_pork.getText().toString();
                Log.e("升级设置",up1);
                if(!up1.equals("") && !up2.equals("") ){
                    Log.e("进入升级设置",up1);
                    requestBody = new FormBody.Builder()
                            .add("radar_sn",MainActivity.radar_sn)
                            .add("ip",up1)
                            .add("port",up2)
                            .build();
                    res=radar.post(begin_update_url,requestBody);
                    if(res!=null){
                        Toast.makeText(this, "升级成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "升级失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "您有未设置的升级参数！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.check_progress:
                url=get_progress_url+"?radar_sn="+MainActivity.radar_sn;
                res=radar.get(url);
                if(res!=null){
                    Toast.makeText(this, "当前进度为："+res.getString("otaStatus")+"%", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "查询失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.return_MainActivity1:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}