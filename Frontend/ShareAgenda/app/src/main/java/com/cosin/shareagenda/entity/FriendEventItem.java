package com.cosin.shareagenda.entity;

import java.io.Serializable;

public class FriendEventItem implements Serializable {
    private int quarter;
    private int during;

    public FriendEventItem(int quarter, int during) {
        this.quarter = quarter;
        this.during = during;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getDuring() {
        return during;
    }
}
