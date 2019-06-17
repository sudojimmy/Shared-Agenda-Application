package com.cosin.shareagenda.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FriendEvent implements Serializable {
    private String friendName;
    private String date;
    private List<FriendEventItem> events;

    public FriendEvent(String friendName, String date) {
        this.friendName = friendName;
        this.date = date;
    }

    public FriendEvent(String friendName, String date, List<FriendEventItem> events) {
        this.friendName = friendName;
        this.date = date;
        this.events = events;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getDate() {
        return date;
    }

    public List<FriendEventItem> getEvents() {
        return events;
    }

    public void setEvents(List<FriendEventItem> events) {
        this.events = events;
    }

    public boolean checkQuarterInEvent(int quarter) {
        if (events == null || events.size() == 0)
            return false;
        Iterator<FriendEventItem> it = events.iterator();
        while (it.hasNext()) {
            FriendEventItem evt = it.next();
            if (quarter >= evt.getQuarter() && quarter < evt.getQuarter() + evt.getDuring())
                return true;
        }
        return false;
    }
}
