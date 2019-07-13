package com.cosin.shareagenda.entity;

import com.alamkanak.weekview.WeekViewEvent;
import com.cosin.shareagenda.util.CalendarEventBiz;

import java.util.Calendar;

import types.Event;

public class DisplayableEvent extends WeekViewEvent {
    private Event event;

    public DisplayableEvent(final Event event) {
        String startTimeStr = event.getStartTime();
        String startDateStr = event.getRepeat().getStartDate();
        String endTimeStr = event.getEndTime();
        String endDateStr = event.getRepeat().getEndDate();
        Calendar startTime = Calendar.getInstance();
        startTime.set(
                CalendarEventBiz.getStringYear(startDateStr),
                CalendarEventBiz.getStringMonth(startDateStr)-1,
                CalendarEventBiz.getStringDayOfMonth(startDateStr),
                CalendarEventBiz.getStringHour(startTimeStr),
                CalendarEventBiz.getStringMinute(startTimeStr));

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(
                CalendarEventBiz.getStringYear(endDateStr),
                CalendarEventBiz.getStringMonth(endDateStr)-1,
                CalendarEventBiz.getStringDayOfMonth(endDateStr),
                CalendarEventBiz.getStringHour(endTimeStr),
                CalendarEventBiz.getStringMinute(endTimeStr));
        setIdentifier(event.getEventId());
        setName(event.getEventname());
        setStartTime(startTime);
        setEndTime(endTime);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
