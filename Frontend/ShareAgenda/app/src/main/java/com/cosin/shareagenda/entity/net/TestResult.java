package com.cosin.shareagenda.entity.net;

import com.cosin.shareagenda.entity.UserEntity;

public class TestResult extends BaseResult {
    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
