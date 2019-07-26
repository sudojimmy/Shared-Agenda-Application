package com.cosin.shareagenda.api.plugin.uwapi;

import com.cosin.shareagenda.model.Model;
import com.cosin.shareagenda.util.CalendarEventBiz;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import types.Event;
import types.EventRepeat;
import types.EventState;
import types.EventType;
import types.Permission;
import types.PermissionType;
import types.Repeat;
import types.UWClass;
import types.UWCourse;
import types.UWDate;
import types.UWLocation;

public class UWEventReader {

    // 1 + last2dig year + term start month. ex. 1195 => 1 + 2019 + May(Spring Term)
    // month start from 0
    private static String toTerm(int year, int month) {
        month = (month / 4) * 4 + 1;
        return "1" + (year % 100) + month;
    }

    public static String defaultTerm() {
        return toTerm(CalendarEventBiz.getCurrentYear(), CalendarEventBiz.getCurrentMonth());
    }

    public static ArrayList<Event> courseToEvents(UWCourse uwCourse, String termStartDate, String termEndDate) {
        ArrayList<Event> events = new ArrayList<>();
        for (UWClass uwClass : uwCourse.getClasses()) {
            events.addAll(classToEvents(
                    uwClass,
                    readTitle(uwCourse),
                    uwCourse.getTitle(),
                    termStartDate,
                    termEndDate
            ));
        }
        return events;
    }

    public static Event examToEvent(UWExamExploreInfo examExploreInfo, String termStartDate, String termEndDate) {
        return new Event()
                .withStarterId(Model.model.getUser().getAccountId())    // starter always user
                .withState(EventState.ACTIVE)                           // state always active
                .withEventname(examExploreInfo.getTitle())
                .withDescription(examExploreInfo.getDescription())
                .withLocation(examExploreInfo.getExam().getLocation())
                .withType(EventType.STUDY)
                .withRepeat(new Repeat()
                        .withType(EventRepeat.ONCE)
                        .withStartDate(examExploreInfo.getExam().getDate())
                        .withEndDate(examExploreInfo.getExam().getDate()))
                .withStartTime(readExamTime(examExploreInfo.getExam().getStart_time()))
                .withEndTime(readExamTime(examExploreInfo.getExam().getEnd_time()))
                .withPermission(new Permission().withType(PermissionType.PUBLIC));
    }

    private static String readExamTime(String time) {
        try {
            return CalendarEventBiz.to24hr(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static ArrayList<Event> classToEvents(UWClass uwClass, String title, String description,
                                                  String termStartDate, String termEndDate) {
        ArrayList<Event> events = new ArrayList<>();
        for (String weekday : readWeekDays(uwClass.getDate().getWeekdays())) {
            String startDate = CalendarEventBiz.getNextWeekDay(termStartDate, getWeekDay(weekday));
            events.add(createEvent(uwClass, title, description, startDate, termEndDate));
        }
        return events;
    }

    private static int getWeekDay(String weekday) {
        switch (weekday) {
            case "M":
                return Calendar.MONDAY;
            case "T":
                return Calendar.TUESDAY;
            case "W":
                return Calendar.WEDNESDAY;
            case "Th":
                return Calendar.THURSDAY;
            case "F":
                return Calendar.FRIDAY;
            case "S":
                return Calendar.SATURDAY;
            case "Su":
                return Calendar.SUNDAY;
        }
        return -1; // TODO exception
    }

    private static Event createEvent(UWClass uwClass, String title, String description,
                                     String startDate, String endDate) {
        startDate = readDate(uwClass.getDate().getStart_date(), startDate);
        endDate = readDate(uwClass.getDate().getEnd_date(), endDate);
        return new Event()
                .withStarterId(Model.model.getUser().getAccountId())    // starter always user
                .withState(EventState.ACTIVE)                           // state always active
                .withEventname(title)
                .withDescription(description)
                .withLocation(readLocation(uwClass.getLocation()))
                .withType(EventType.STUDY)
                .withRepeat(new Repeat()
                        .withType(startDate.equals(endDate) ? EventRepeat.ONCE : EventRepeat.WEEK)
                        .withStartDate(startDate)
                        .withEndDate(endDate))
                .withStartTime(uwClass.getDate().getStart_time())
                .withEndTime(uwClass.getDate().getEnd_time())
                .withPermission(new Permission().withType(PermissionType.PUBLIC));
    }

    private static String readDate(String endDate, String defaultDate) {
        if (endDate == null) {
            return defaultDate;
        }

        int year = CalendarEventBiz.getStringYear(defaultDate);
        System.out.println(endDate);
        System.out.println(defaultDate);
        System.out.println(year);
        String[] date = endDate.split("/");
        String month = date[0];
        String day = date[1];
        return year + "-" + month + "-" + day;
    }

    private static String readLocation(UWLocation uwLocation) {
        return uwLocation.getBuilding() + " " + uwLocation.getRoom();
    }

    // ex. CS446 Lec 001
    public static String readTitle(UWCourse uwCourse) {
        return uwCourse.getSubject() + uwCourse.getCatalog_number() + " " + uwCourse.getSection();
    }

    // ex. CS446 Lec 001
    public static String readDescription(UWCourse uwCourse) {
        StringBuilder description = new StringBuilder();
        for (UWClass uwClass : uwCourse.getClasses()) {
            UWDate date = uwClass.getDate();
            description.append(String.format("%s %s-%s\n",
                    date.getWeekdays(), date.getStart_time(), date.getEnd_time()));
        }
        return description.toString();
    }

    // split weekday by uppercase. ex. TTh => [T, Th]
    private static String [] readWeekDays(String weekday) {
        return weekday.split("(?<=.)(?=\\p{Lu})");
    }
}
