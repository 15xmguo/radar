package com.example.radar;

import android.app.Application;
//该类用于存储永久数据直到app关闭
public class Data extends Application {
    private String data;
    private static Data instance;
    private Data (){}

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }
    public String getdata(){
        return this.data;
    }
    public void setdata(String data){
        this.data= data;
}
}
