package controller;
import types.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ComparableEvent implements ComparableEventInterface {
    Event event;

    public ComparableEvent(Event passEvent){
        this.event = passEvent;
    }

    @Override
    public int compareTo(ComparableEvent comparableEvent) {
        Event event = comparableEvent.event;
        int hours;
        int min;
        int thishours;
        int thismin;

        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date = format.parse(event.getStartTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            hours = cal.get(Calendar.HOUR);
            min = cal.get(Calendar.MINUTE);



            Date thisdate = format.parse(this.event.getStartTime());
            cal.setTime(thisdate);
            thishours = cal.get(Calendar.HOUR);
            thismin = cal.get(Calendar.MINUTE);
        } catch (ParseException e) {
            // parsing failed
            return 0;
        }

        if (hours != thishours){
            return hours - thishours;
        } else {
            return min - thismin;
        }
    }

}

