package com.example.radar;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public class Radar {
    JSONObject get_result=null;
    JSONObject post_result=null;
    Boolean flag=true;
    public JSONObject get(String url) throws InterruptedException {
        OkHttpClient okHttpClient = new OkHttpClient();//一个全局的执行者，所有请求动作交由它执行。
        Request.Builder builder=new Request.Builder();//通过Builder来构造request。
        Request request=builder.get().url(url).build();//builder到build返回request
        Call call = okHttpClient.newCall(request);//将request传入
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                flag=false;
                Log.e("get请求失败，原因：",e.toString());

            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                get_result=JSONObject.parseObject(response.body().string());
                Log.e("get请求完成","2");
                flag=false;
//                JSONArray jsonArray = jsnobject.getJSONArray("radar_list");
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                String a=jsonObject.getString("radarSn");
            }
        });
        while (flag){
            Thread.currentThread().sleep(10);
            Log.e("请求未完成","仍在等待");
        }
        flag=false;
        Log.e("请求完成","1");
        return get_result;
    }

    public JSONObject post(String url, RequestBody data) throws IOException, InterruptedException {
        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder()
//                .add("city","广州")
//                .add("appkey","9878b9b510123e52a951fe2074d19be1")
//                .build();

        Request request = new Request.Builder()
                .addHeader("content-type", "application/json")
                .url(url)
                .post(data)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                flag=false;
                Log.e("post请求失败，原因：",e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                post_result=JSONObject.parseObject(response.body().string());
                Log.e("post请求完成","2");
                flag=false;

            }
        });

        while (flag){
            Thread.currentThread().sleep(10);
            Log.e("请求未完成","仍在等待");
        }
        flag=false;
        return post_result;

    }
}