package com.cosin.shareagenda.activity;

import android.content.Intent;

import com.cosin.shareagenda.entity.UserEntity;

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
        Intent intent = new Intent(this, CreateEventActivity.class);
        this.startActivity(intent);
    }
}
