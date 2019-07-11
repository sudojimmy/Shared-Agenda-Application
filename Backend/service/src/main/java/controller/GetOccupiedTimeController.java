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

import java.time.YearMonth;
import java.util.ArrayList;
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

            ArrayList<Event> dailyEventListMerge = new ArrayList<Event>();

            // merge 2 daily eventlist of a1 & a2 to one eventlist
            ArrayList<Event> dailyEventList1
                    = EventListUtils.getEventListFromCalendarWithdate(eventList1, date);
            ArrayList<Event> dailyEventList2
                    = EventListUtils.getEventListFromCalendarWithdate(eventList2, date);

            List<Event> eventListbeforeMerge =
                    Stream.concat(dailyEventList1.stream(), dailyEventList2.stream())
                    .collect(Collectors.toList());



            List<ComparableEvent> eventListbeforeMergeComparable = new ArrayList<ComparableEvent>();
            for(Event event: eventListbeforeMerge) {
                ComparableEvent comparableEvent = new ComparableEvent(event);
                eventListbeforeMergeComparable.add(comparableEvent);
            }



            // sort eventListbeforeMerge
            Collections.sort(eventListbeforeMergeComparable);

//            ArrayList<Event> temp = new ArrayList<Event>();
//
//            while (!eventListbeforeMerge.isEmpty()) {
//                for(int index = 0; ; index += 2) {
//                    if ((index+1) >= eventListbeforeMerge.size()){
//                        break;
//                    }
//
//                    //merge [index] and [index+1]
//
//                }
//            }

        }
















//
//        for (Event event: eventList) {
//            if (!EventListUtils.checkEventPermission(request.getCallerId(), event)) {
//                // if no permission
//                Event displayEvent = new Event()
//                        .withStartTime(event.getStartTime())
//                        .withEndTime(event.getEndTime());
//                finalEventList.add(displayEvent);
//            } else {
//                finalEventList.add(event);
//            }
//        }

        return new ResponseEntity<>(new GetOccupiedTimeResponse()
                .withEventList(finalEventList),HttpStatus.OK);
    }
}
