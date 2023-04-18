package com.example.radar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class radar_list_choose extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ArrayList<String> spinner_list = new ArrayList<String>();
    private Radar radar;
    private Spinner sp_dialog;
    String result="123456789AB";
    String get_sn_url="http://192.168.124.120:8018/radar_app/get_conn_radar_list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_list_choose);
}
    public void Onclick(View view) throws InterruptedException {
        switch (view.getId()){
            case R.id.get_radar_sn:
                radar=new Radar();
                JSONObject a=radar.get(get_sn_url);
                if(a!=null){//如果请求成功就分析数据
                    JSONObject dict;
                    JSONArray jsonArray = a.getJSONArray("radar_list");
                    for (int i=0; i< jsonArray.size();i++){
                        dict = jsonArray.getJSONObject(0);
                        String sn=dict.getString("radarSn");
                        spinner_list.add(sn);
                    }
                    sp_dialog = findViewById(R.id.sn_dropdown);
                    //声明一个下拉列表的数组适配器
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner, spinner_list);
                    sp_dialog.setAdapter(arrayAdapter);

                    //设置下拉列表标题
                    sp_dialog.setPrompt("请选择：");

                    //设置默认显示下拉第一项
                    sp_dialog.setSelection(0);
                    //给下拉框设置选择监听器
                    sp_dialog.setOnItemSelectedListener(this);
                }else {
                    Toast.makeText(this, "获取失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.begin:
                if(result==""){
                    Toast.makeText(this, "您未选择雷达sn！", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(this,MainActivity.class);
                    intent.putExtra("radar_sn",result);
                    startActivity(intent);

                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        result=spinner_list.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}