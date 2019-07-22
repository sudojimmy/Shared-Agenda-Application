package com.cosin.shareagenda.util;

import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.SimpleEventEntity;
import com.cosin.shareagenda.entity.WeekDayEventEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public static final String DETAILED_DATE_PATTERN = "YYYY-MM-dd  EEEE";
    public static final String DATE_PATTERN = "YYYY-MM-dd";
    public static final String DATE_FORMAT = "%d-%d-%d";
    public static final String TIME_FORMAT = "%d:%02d";

    public static String toDateString(final int year, final int month, final int dateOfMonth) {
        return String.format(DATE_FORMAT, year, (month + 1), dateOfMonth);
    }

    public static String toDateString(final Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(DATE_FORMAT, year, (month + 1), day);
    }

    public static String toTimeString(final int hour, final int minute) {
        return String.format(TIME_FORMAT, hour, minute);
    }

    public static String toTimeString(final Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format(TIME_FORMAT, hour, minute);
    }

    public static String calendarToDateString(Calendar calendar) {
        return toDateString(getCalendarYear(calendar),
                getCalendarMonth(calendar),
                getCalendarDayOfMonth(calendar));
    }

    public static String calendarToTimeString(Calendar calendar) {
        return toTimeString(getCalendarHour(calendar), getCalendarMinute(calendar));
    }

    public static String getCurrentDateString() {
        return toDateString(getCurrentYear(), getCurrentMonth(), getCurrentDayOfMonth());
    }

    public static String getCurrentDateStringDetailed() {
        SimpleDateFormat sdf = new SimpleDateFormat(DETAILED_DATE_PATTERN);
        return sdf.format(calendarInstance.getTime());
    }

    public static String getCalendarDateStringDetailed(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(DETAILED_DATE_PATTERN);
        return sdf.format(calendar.getTime());
    }

    public static String getCurrentTimeString() {
        return toTimeString(getCurrentHour(), getCurrentMinute());
    }

    public static int getCurrentYear() {
        return calendarInstance.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return calendarInstance.get(Calendar.MONTH); // java.util.Calendar Month start from 0
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

    public static int getCalendarYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int getCalendarMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    public static int getCalendarDayOfMonth(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCalendarHour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCalendarMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    // @date formatted in YYYY-MM-dd
    public static int getStringYear(String date) {
        return Integer.valueOf(date.split("-")[0]);
    }
    public static int getStringMonth(String date) {
        return Integer.valueOf(date.split("-")[1]);
    }
    public static int getStringDayOfMonth(String date) {
        return Integer.valueOf(date.split("-")[2]);
    }
    // @time formatted in HH:mm
    public static int getStringHour(String time) {
        return Integer.valueOf(time.split(":")[0]);
    }
    public static int getStringMinute(String time) {
        return Integer.valueOf(time.split(":")[1]);
    }

    public static String getNextHour(String time) {
        return toTimeString(getStringHour(time)+1, getStringMinute(time));
    }

    public static String getNextWeekDay(String date, int weekday) {
        Calendar cal = Calendar.getInstance();
        cal.set(getStringYear(date), getStringMonth(date)-1,getStringDayOfMonth(date));
        while (cal.get(Calendar.DAY_OF_WEEK) != weekday) {
            cal.add(Calendar.DATE, 1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(cal.getTime());
    }

    public static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static String to24hr(String time) throws ParseException {
        SimpleDateFormat inFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
        return outFormat.format(inFormat.parse(time));
    }
}
