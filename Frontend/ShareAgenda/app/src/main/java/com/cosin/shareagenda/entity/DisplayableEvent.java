package com.cosin.shareagenda.entity;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;
import com.cosin.shareagenda.util.CalendarEventBiz;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import types.Event;
import types.EventType;

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
        Integer color = colorMap.get(event.getType());
        if (color == null) { // permission issue
            setColor(Color.rgb(189, 189, 189));
        } else {
            setColor(color);
        }
    }

    // https://htmlcolorcodes.com/color-chart/material-design-color-chart/
    private static final Map<EventType, Integer> colorMap = Collections.unmodifiableMap(
            new HashMap<EventType, Integer>() {{
                put(EventType.APPOINTMENT, Color.rgb(239, 154, 154));
                put(EventType.ENTERTAINMENT, Color.rgb(251, 192, 45));
                put(EventType.OTHER, Color.rgb(161, 136, 127));
                put(EventType.WORK, Color.rgb(159, 168, 218));
                put(EventType.STUDY, Color.rgb(129, 199, 132));
            }});

    public Event getEvent() {
        return event;
    }
}
