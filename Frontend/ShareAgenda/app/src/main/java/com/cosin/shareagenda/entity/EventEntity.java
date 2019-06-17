package com.cosin.shareagenda.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class EventEntity implements Serializable {
    private long id;
    private Date dei;
    private String name;
    private int type;
    private String location;
    private int quarter;
    private int count;
    private int h;
    private int m;

    public EventEntity(long id, int type, int quarter, int count, String name, String location) {
        this.id = id;
        this.type = type;
        this.quarter = quarter;
        this.count = count > 0 ? count : 1;
        this.name = name;
        this.location = location;
        setTime();
    }

    // empty event
    public EventEntity(int quarter) {
        this.quarter = quarter;
        this.type = 0;
        this.count = 1;
        setTime();
    }

    private void setTime() {
        h = quarter / 4;
        m = quarter % 4;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getQuarter() {
        return quarter;
    }

    public String getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public int getH() {
        return h;
    }

    public int getM() {
        return m;
    }

    public String getTime() {
        return String.format(Locale.ENGLISH,"%2d:%02d", h, m * 15);
    }

    public Date getDei() {
        return dei;
    }

    public void setDy(Date dei) {
        this.dei = dei;
    }

}
