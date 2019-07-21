package com.cosin.shareagenda.entity;

import types.Account;

public class VO_Member {
    private Account member;
    private boolean elected;

    public VO_Member(Account user) {
        this.member = user;
        this.elected = false;
    }

    public Account getMember() {
        return member;
    }

    public void setMember(Account member) {
        this.member = member;
    }

    public boolean isElected() {
        return elected;
    }

    public void setElected(boolean selected) {
        elected = selected;
    }
}
