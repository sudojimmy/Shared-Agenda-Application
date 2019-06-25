package com.cosin.shareagenda.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FriendEvent implements Serializable {
    private Date day;
    private String friendName;
    private List<SimpleEventEntity> events;

    public FriendEvent(Date day, String friendName, List<SimpleEventEntity> events) {
        this.day = day;
        this.friendName = friendName;
        this.events = events;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getDateDM() {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M");
        return sdf.format(day);
    }

    public List<SimpleEventEntity> getEvents() {
        return events;
    }

    public boolean isToday() {
        Calendar cal = Calendar.getInstance();
        Calendar todayC = Calendar.getInstance();
        todayC.setTime(new Date());
        cal.setTime(day);
        return todayC.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR) &&
                todayC.get(Calendar.YEAR) == cal.get(Calendar.YEAR);
    }

    public Date getDate() {
        return day;
    }
}
