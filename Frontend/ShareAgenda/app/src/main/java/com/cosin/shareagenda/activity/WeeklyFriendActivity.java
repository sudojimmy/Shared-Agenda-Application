package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.dialog.DialogReceiver;
import com.cosin.shareagenda.dialog.SendEventRequestDialog;
import com.cosin.shareagenda.entity.UserEntity;
import com.cosin.shareagenda.util.GenData;
import com.cosin.shareagenda.view.ItemViewListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeeklyFriendActivity extends WeeklyActivity {

    @Override
    protected String titleName() {
        return user.getNickname();
    }

    @Override
    protected void setUser() {
        Intent intent = getIntent();
        user =(UserEntity) intent.getSerializableExtra("user");
    }

    @Override
    public void receive(Object ret) {
        // go create event and send request
        Intent intent = new Intent(this, CreateEventAcitivty.class);
        this.startActivity(intent);
    }
}
