package com.dg.eventapp.entity;

import java.io.Serializable;
import java.util.Locale;

public class EventEntity implements Serializable {
    private long eventId;
    private int eventType;
    private String eventName;
    private String location;
    private int quarter;
    private int count;
    private int hh;
    private int mm;

    public EventEntity(long eventId, int eventType, String eventName, String location, int quarter, int count) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventName = eventName;
        this.location = location;
        this.quarter = quarter;
        this.count = count;
        setTime();
    }

    public EventEntity(int quarter) {
        this.quarter = quarter;
        this.eventType = 0;
        this.count = 1;
        setTime();
    }

    private void setTime(){
        hh = quarter / 4;
        mm = quarter % 4;
    }

    public String getTime(){
        return String.format(Locale.ENGLISH,"%2d:%02d", hh, mm * 15);
    }

    public long getEventId() {
        return eventId;
    }

    public int getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public String getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getMm() {
        return mm;
    }
}
