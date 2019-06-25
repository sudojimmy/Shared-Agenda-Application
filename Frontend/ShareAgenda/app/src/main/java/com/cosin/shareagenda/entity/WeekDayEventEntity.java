package com.cosin.shareagenda.entity;

import com.cosin.shareagenda.util.CalendarEventBiz;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekDayEventEntity implements Serializable {
    private Date date;
    private String dayName;
    private String day;
    private String holiday;
    private boolean isToday;
    private boolean isHoliday;
    private boolean isSundaySaturday;
    private List<SimpleEventEntity> events;

    public WeekDayEventEntity(Date date, List<SimpleEventEntity> events) {
        this.date = date;
        this.isToday = date.compareTo(new Date()) == 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        String[] weekDay = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
        this.dayName = weekDay[cal.get(Calendar.DAY_OF_WEEK) - 1];
        this.isSundaySaturday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY;

        this.holiday = CalendarEventBiz.getHolidays(date);
        this.isHoliday = !this.holiday.isEmpty();

        this.events = events;
    }

    public Date getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getDayName() {
        return dayName;
    }

    public String getHoliday() {
        return holiday;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean isSundaySaturday() {
        return isSundaySaturday;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public List<SimpleEventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<SimpleEventEntity> events) {
        this.events = events;
    }
}
