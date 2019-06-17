package com.cosin.shareagenda.util;

import android.content.Context;

import com.cosin.shareagenda.config.AgendaApplication;
import com.cosin.shareagenda.entity.UserEntity;

public class AppHelper {
    public static void checkApp() {
        // check env
        UserEntity user = new UserEntity("1","Florie");
        AgendaApplication.setUserInfo(user);
    }

    private int dp2px(float dp) {
        Context context = AgendaApplication.getContext();
        final float density = context.getResources().getDisplayMetrics().density;
        return (int)(dp * density + 0.5f);
    }

    public static int sp2px(float spValue) {
        Context context = AgendaApplication.getContext();
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5f);
    }

    public static UserEntity getUserInfo() {
        return AgendaApplication.getUserInfo();
    }
}
