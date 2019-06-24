package com.cosin.shareagenda.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
    private String id;
    private String nickname;
    private String token;

    public UserEntity(String id) {
        this.id = id;
    }

    public UserEntity(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public UserEntity(String id, String nickname, String token) {
        this.id = id;
        this.nickname = nickname;
        this.token = token;
    }

    public String getId() {
        return id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
