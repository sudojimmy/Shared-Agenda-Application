package com.cosin.shareagenda.config;

import android.app.Application;
import android.content.Context;

import com.cosin.shareagenda.entity.UserEntity;

public class AgendaApplication extends Application {
    private static Context context;
    private static AgendaApplication instance;
    private static UserEntity userInfo = null;
    private static String description;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();


    }

    public static AgendaApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public static UserEntity getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserEntity userInfo) {
        AgendaApplication.userInfo = userInfo;
    }

    public static String getUserDescription() {
        return description;
    }

    public static void setUserDescription(String desc) {
        AgendaApplication.description = desc;
    }

}
