package com.cosin.shareagenda.entity;

import android.util.Log;

public class VO_Member {
    private UserEntity member;
    private boolean elected;

    public VO_Member(UserEntity user) {
        this.member = user;
        this.elected = false;
    }

    public UserEntity getMember() {
        return member;
    }

    public void setMember(UserEntity member) {
        this.member = member;
    }

    public boolean isElected() {
        return elected;
    }

    public void setElected(boolean selected) {
        elected = selected;
    }
}
