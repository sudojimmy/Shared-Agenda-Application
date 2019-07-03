package com.cosin.shareagenda.util;

import android.content.Context;

import com.cosin.shareagenda.config.AgendaApplication;
import com.cosin.shareagenda.entity.UserEntity;
import com.cosin.shareagenda.model.Model;

import types.Account;

public class AppHelper {
    public static void checkApp() {
        // check env
        Account user = Model.model.getUser();
        if (user == null) {
            AgendaApplication.setUserInfo(new UserEntity("-1","Florie"));
            AgendaApplication.setUserDescription("Early bird gets the worm");
        } else {
            AgendaApplication.setUserInfo(new UserEntity(user.getAccountId(),user.getNickname()));
            AgendaApplication.setUserDescription(user.getDescription());
        }
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

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
