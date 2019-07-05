package com.cosin.shareagenda.entity;

import java.io.Serializable;

public class UserEntity implements Serializable, ContactEntity {
    private String id;
    private String nickname;
    //private String token;

    public UserEntity(String id) {
        this.id = id;
    }

    public UserEntity(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
