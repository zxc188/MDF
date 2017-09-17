package com.example.administrator.mdf;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.example.administrator.mdf.service.Music_Service;

public class MYapplication extends Application {
    public static Context context;
    public static String apikey;
    public static String token;
    public static String instanceUrl;
    public static Music_Service my_service;

    @Override
    public void onCreate() {
        super.onCreate();
        MYapplication.context = getApplicationContext();
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            apikey = bundle.getString("apiKey");
            instanceUrl=bundle.getString("instanceUrl");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(MYapplication.class.getSimpleName(), "读取配置文件出错", e);
        }

    }

    public static Context getAppContext(){
        return MYapplication.context;
    }

    public static Music_Service getMy_service() {
        return my_service;
    }

    public static void setMy_service(Music_Service my_service) {
        MYapplication.my_service = my_service;
    }

}
