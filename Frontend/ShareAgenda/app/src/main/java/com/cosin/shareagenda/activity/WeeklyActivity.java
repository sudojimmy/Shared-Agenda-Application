package com.cosin.shareagenda.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.cosin.shareagenda.R;

public class WeeklyActivity extends MainTitleActivity {

    @Override
    protected  String titleName() {
        return getResources().getString(R.string.title_weekly_calendar);
    }

    @Override
    protected void loadContentView() {
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_weekly, coordinatorLayout);
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
