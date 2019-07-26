package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.AccountUtils;
import utils.CalendarUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;

import static utils.EventListUtils.getEventListFromCalendar;

@RestController
public class GetOccupiedTimeController extends BaseController{

    @PostMapping("/getOccupiedTime")
    public ResponseEntity<GetOccupiedTimeResponse> handle(@RequestBody GetOccupiedTimeRequest request) {
        logger.info("getCalendarOccupiedTime: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getTargetAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getYear(), ApiConstant.EVENT_MONTHLY_YEAR);
        ExceptionUtils.assertPropertyValid(request.getMonth(), ApiConstant.EVENT_MONTHLY_MONTH);
        if ((request.getMonth() > 12) || (request.getMonth() < 1)) {
            ExceptionUtils.invalidProperty("Month Value");
        }

        Account account1 = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        Account account2 = AccountUtils.getAccount(request.getTargetAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        types.Calendar calendar1 = CalendarUtils.getCalendar(account1.getCalendarId());
        types.Calendar calendar2 = CalendarUtils.getCalendar(account2.getCalendarId());

        ArrayList<Event> eventList1 = getEventListFromCalendar(calendar1);
        ArrayList<Event> eventList2 = getEventListFromCalendar(calendar2);


        if (eventList1.isEmpty()) { return new ResponseEntity<>(new GetOccupiedTimeResponse()
                .withEventList(eventList2),HttpStatus.OK); }
        if (eventList2.isEmpty()) { return new ResponseEntity<>(new GetOccupiedTimeResponse()
                .withEventList(eventList1),HttpStatus.OK); }


        // Get the number of days in that month
        YearMonth yearMonthObject = YearMonth.of(request.getYear(), request.getMonth());
        int totalDayInMonth = yearMonthObject.lengthOfMonth();

        ArrayList<Event> finalEventList = new ArrayList<Event>();

        for(int i = 1; i < (totalDayInMonth + 1); i++){

            String date = EventListUtils.constructDateByYearMonthDay(request.getYear(), request.getMonth(), i);

            // merge 2 daily eventlist of a1 & a2 to one eventlist
            ArrayList<Event> dailyEventList1
                    = EventListUtils.getEventListFromCalendarWithDate(eventList1, date);
            ArrayList<Event> dailyEventList2
                    = EventListUtils.getEventListFromCalendarWithDate(eventList2, date);

            List<Event> eventListbeforeMerge =
                    Stream.concat(dailyEventList1.stream(), dailyEventList2.stream())
                    .collect(Collectors.toList());



            List<ComparableEvent> eventListbeforeMergeComparable = new ArrayList<ComparableEvent>();
            for(Event event: eventListbeforeMerge) {
                ComparableEvent comparableEvent = new ComparableEvent(event);
                eventListbeforeMergeComparable.add(comparableEvent);
            }



            // sort eventListbeforeMerge
            // sort in decreasing order
            Collections.sort(eventListbeforeMergeComparable);

            Stack<Event> eventsListToMergeStack = new Stack<Event>();


            //eventListbeforeMerge = new ArrayList<Event>();
            for(ComparableEvent comparableEvent: eventListbeforeMergeComparable) {
                Event event = comparableEvent.event;
                eventsListToMergeStack.push(event);
                //eventListbeforeMerge.add(event);
            }

            while (!eventsListToMergeStack.isEmpty()) {

                if (eventsListToMergeStack.size() == 1) {
                    finalEventList.add(eventsListToMergeStack.pop());
                    break;
                }

                Event event1 = eventsListToMergeStack.pop();
                Event event2 = eventsListToMergeStack.pop();

                //merge [index] and [index+1]

                int hours1;
                int min1;
                int hours2;
                int min2;
                int hours2End;
                int min2End;

                try {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    Date time1 = format.parse(event1.getEndTime());

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(time1);
                    hours1 = cal.get(Calendar.HOUR);
                    min1 = cal.get(Calendar.MINUTE);

                    Date time2 = format.parse(event2.getStartTime());
                    cal.setTime(time2);
                    hours2 = cal.get(Calendar.HOUR);
                    min2 = cal.get(Calendar.MINUTE);

                    Date time2End = format.parse(event2.getEndTime());
                    cal.setTime(time2End);
                    hours2End = cal.get(Calendar.HOUR);
                    min2End = cal.get(Calendar.MINUTE);


                } catch (ParseException e) {
                    // parsing failed
                    return new ResponseEntity<>(new GetOccupiedTimeResponse()
                            .withEventList(new ArrayList<Event>()), HttpStatus.BAD_REQUEST);
                }

                Repeat repeat = new Repeat()
                        .withStartDate(date)
                        .withEndDate(date)
                        .withType(EventRepeat.ONCE);

                // h1,m1: event1 endtime
                if ((hours1 == hours2End) && (min1 == min2End)) {
                    // end at same time
                    eventsListToMergeStack.push(event1);

                } else if ((hours1 < hours2)
                        || ((hours1 == hours2) && (min1 < min2))) {
                    // [s1] [e1] [s2] [e2]

                    // add e1 to rt list
                    event1.withRepeat(repeat);
                    finalEventList.add(event1);

                    // add back e2 to stack
                    eventsListToMergeStack.push(event2);

                } else {

                    Event event3 = new Event().withStartTime(event1.getStartTime()).withRepeat(repeat);


                    if ((hours1 < hours2End) || (hours1 == hours2End) && (min1 < min2End)){
                        // [s1] [s2] [e1] [e2]
                        event3.withEndTime(event2.getEndTime());
                    } else {
                        // [s1] [s2] [e2] [e1]
                        event3.withEndTime(event1.getEndTime());
                    }

                    eventsListToMergeStack.push(event3);

                }

            }

        }


        return new ResponseEntity<>(new GetOccupiedTimeResponse()
                .withEventList(finalEventList),HttpStatus.OK);
    }
}


