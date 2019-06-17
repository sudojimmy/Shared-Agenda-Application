package com.cosin.shareagenda.activity;

import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.cosin.shareagenda.R;

public class RequestActivity extends MainTitleActivity {
    @Override
    protected  String titleName() {
        return getResources().getString(R.string.title_friends_calendar);
    }

    @Override
    protected void loadContentView() {
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_request, coordinatorLayout);
    }

    @Override
    protected void loadData() {
        //
    }

    @Override
    protected void initView() {
        super.initView();

        // init panel

    }
}