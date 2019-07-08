package com.cosin.shareagenda.util;

import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.SimpleEventEntity;
import com.cosin.shareagenda.entity.WeekDayEventEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CalendarEventBiz {
    public static String getHolidays(Date date) {
        return "";
    }

    public static boolean haveHoliday(List<WeekDayEventEntity> weekEvents) {
        for (WeekDayEventEntity e : weekEvents) {
            if (e.isHoliday())
                return true;
        }
        return false;
    }

    public static boolean checkQuarterInEvent(int quarter, FriendEvent friendsEvents) {
        List<SimpleEventEntity> events = friendsEvents.getEvents();
        if (events == null || events.size() == 0)
            return false;
        Iterator<SimpleEventEntity> it = events.iterator();
        while (it.hasNext()) {
            SimpleEventEntity evt = it.next();
            if (quarter >= evt.getQuarter() && quarter < evt.getQuarter() + evt.getDuring())
                return true;
        }
        return false;
    }

    public static String checkQuarterInEventName(int quarter, FriendEvent friendsEvents) {
        List<SimpleEventEntity> events = friendsEvents.getEvents();
        if (events == null || events.size() == 0)
            return "";
        Iterator<SimpleEventEntity> it = events.iterator();
        while (it.hasNext()) {
            SimpleEventEntity evt = it.next();
            if (quarter >= evt.getQuarter() && quarter < evt.getQuarter() + evt.getDuring()) {
                if ( quarter == evt.getQuarter() + (int)(evt.getDuring() / 2.0f - 0.5f) ) {
                    return (evt.getName() + " ").substring(0,3);
                } else {
                    return "~";
                }
            }
        }
        return "";
    }

    private final static Calendar calendarInstance = Calendar.getInstance();

    public static String toDateString(final int year, final int month, final int dateOfMonth) {
        return year + "-" + month + "-" + dateOfMonth;
    }

    public static String toTimeString(final int hour, final int minute) {
        return hour + ":" + minute;
    }

    public static String getCurrentDateString() {
        return toDateString(getCurrentYear(), getCurrentMonth(), getCurrentDayOfMonth());
    }

    public static String getCurrentTimeString() {
        return toTimeString(getCurrentHour(), getCurrentMinute());
    }

    public static int getCurrentYear() {
        return calendarInstance.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return calendarInstance.get(Calendar.MONDAY);
    }

    public static int getCurrentDayOfMonth() {
        return calendarInstance.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        return calendarInstance.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        return calendarInstance.get(Calendar.MINUTE);
    }

}
