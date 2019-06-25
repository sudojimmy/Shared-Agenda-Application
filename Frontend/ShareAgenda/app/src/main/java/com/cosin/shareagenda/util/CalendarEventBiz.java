package com.cosin.shareagenda.util;

import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.SimpleEventEntity;
import com.cosin.shareagenda.entity.WeekDayEventEntity;

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

}
