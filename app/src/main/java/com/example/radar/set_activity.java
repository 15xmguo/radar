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

public class set_activity extends AppCompatActivity {
    private Radar radar;
    private String url="";//过渡用的url
    private JSONObject res=null;
    private RequestBody requestBody=null;
    EditText delay;
    EditText right_boundary;
    EditText left_boundary;
    EditText upper_boundary;
    EditText lower_boundary;
    EditText mirco_move;
    private String get_delay_url="http://192.168.124.120:8018/radar_app/get_delay_param";
    private String set_delay_url="http://192.168.124.120:8018/radar_app/set_delay_param";
    private String get_boundary_url="http://192.168.124.120:8018/radar_app/get_boundary";
    private String set_boundary_url="http://192.168.124.120:8018/radar_app/set_boundary";
    private String get_move_url="http://192.168.124.120:8018/radar_app/get_micro_move";
    private String set_move_url="http://192.168.124.120:8018/radar_app/set_micro_move";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        radar=new Radar();
        delay=findViewById(R.id.edit_delay);
        right_boundary=findViewById(R.id.edit_right_boundary);
        left_boundary=findViewById(R.id.edit_left_boundary);
        upper_boundary=findViewById(R.id.edit_upper_boundary);
        lower_boundary=findViewById(R.id.edit_lower_boundary);
        mirco_move=findViewById(R.id.edit_detection);
    }
    public void Onclick(View view) throws InterruptedException, IOException {
        switch (view.getId()){
            case R.id.delay_set:
                String delay_content=delay.getText().toString();
                if(!delay_content.equals("")){
                     requestBody = new FormBody.Builder()
                            .add("radar_sn",MainActivity.radar_sn)
                            .add("seconds",delay_content)
                            .build();
                    res=radar.post(set_delay_url,requestBody);
                    if(res!=null){
                        Toast.makeText(this, "设置延迟成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "设置延迟失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "您未设置延迟参数！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delay_readback:
                url=get_delay_url+"?radar_sn="+MainActivity.radar_sn;
                res=radar.get(url);
                if(res!=null){
                    delay.setText(res.getJSONObject("delayParam").getString("seconds"));
                    Toast.makeText(this, "回读延迟成功！", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "回读延迟失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.boundary_set:
                String left=left_boundary.getText().toString();
                String right=right_boundary.getText().toString();
                String upper=upper_boundary.getText().toString();
                String lower=lower_boundary.getText().toString();
                if(!left.equals("") && !right.equals("") && !upper.equals("") && !lower.equals("")){
                    RequestBody requestBody = new FormBody.Builder()
                            .add("radar_sn",MainActivity.radar_sn)
                            .add("boundary_left",left)
                            .add("boundary_right",right)
                            .add("boundary_forward",upper)
                            .add("boundary_backward",lower)
                            .build();
                    res=radar.post(set_boundary_url,requestBody);
                    if(res!=null){
                        Toast.makeText(this, "设置延迟成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "设置延迟失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "您有未设置的边界参数！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.boundary_readback:
                url=get_boundary_url+"?radar_sn="+MainActivity.radar_sn;
                res=radar.get(url);
                if(res!=null){
                    left_boundary.setText(String.format("%.2f",res.getJSONObject("boundary").getFloat("boundary_left")));
                    right_boundary.setText(String.format("%.2f",res.getJSONObject("boundary").getString("boundary_right")));
                    upper_boundary.setText(String.format("%.2f",res.getJSONObject("boundary").getString("boundary_forward")));
                    lower_boundary.setText(String.format("%.2f",res.getJSONObject("boundary").getString("boundary_backward")));
                    Toast.makeText(this, "回读边界成功！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "回读边界失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.detection_set:
                String detection_content=mirco_move.getText().toString();
                if(!detection_content.equals("")){
                    requestBody = new FormBody.Builder()
                            .add("radar_sn",MainActivity.radar_sn)
                            .add("value",detection_content)
                            .build();
                    res=radar.post(set_move_url,requestBody);
                    if(res!=null){
                        Toast.makeText(this, "设置微动检测阈值成功！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "设置微动检测阈值，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "您未设置微动检测阈值参数！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.detection_readback:
                url=get_move_url+"?radar_sn="+MainActivity.radar_sn;
                res=radar.get(url);
                if(res!=null){
                    mirco_move.setText(res.getJSONObject("microMoveThre").getString("value"));
                    Toast.makeText(this, "回读微动阈值成功！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "回读微动阈值败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.return_MainActivity:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}