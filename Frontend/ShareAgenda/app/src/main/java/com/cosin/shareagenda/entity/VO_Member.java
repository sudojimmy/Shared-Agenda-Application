package com.cosin.shareagenda.entity;

public class VO_Member {
    private String  memberId;
    private boolean elected;

    public VO_Member(String  user) {
        this.memberId = user;
        this.elected = false;
    }

    public String  getMember() {
        return memberId;
    }

    public void setMember(String  memberId) {
        this.memberId = memberId;
    }

    public boolean isElected() {
        return elected;
    }

    public void setElected(boolean selected) {
        elected = selected;
    }
}
