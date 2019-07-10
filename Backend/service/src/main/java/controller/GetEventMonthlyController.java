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
import java.util.ArrayList;

@RestController
public class GetEventMonthlyController extends BaseController {

    @PostMapping("/getEventMonthly")
    public ResponseEntity<GetEventMonthlyResponse> handle(@RequestBody GetEventMonthlyRequest request) {
        logger.info("getCalendarEventMonthly: " + request);

        ExceptionUtils.assertPropertyValid(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getCalendarId(), ApiConstant.CALENDAR_CALENDAR_ID);
        ExceptionUtils.assertPropertyValid(request.getYear(), ApiConstant.EVENT_MONTHLY_YEAR);
        ExceptionUtils.assertPropertyValid(request.getMonth(), ApiConstant.EVENT_MONTHLY_MONTH);
        if ((request.getMonth() > 12) || (request.getMonth() < 1)) {
            ExceptionUtils.invalidProperty("Month Value");
        }

        AccountUtils.getAccount(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        Calendar calendar = CalendarUtils.getCalendar(request.getCalendarId());
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        ArrayList<Event> eventList = EventListUtils
                .getEventListFromCalendarWithYearMonth(
                        calendar,
                        request.getYear(),
                        request.getMonth());

        ArrayList<Event> finalEventList = new ArrayList<Event>();

        for (Event event: eventList) {
            if (!EventListUtils.checkEventPermission(request.getCallerId(), event)) {
                // if no permission
                Event displayEvent = new Event()
                        .withStartTime(event.getStartTime())
                        .withEndTime(event.getEndTime());
                finalEventList.add(displayEvent);
            } else {
                finalEventList.add(event);
            }
        }

        return new ResponseEntity<>(new GetEventMonthlyResponse()
                .withEventList(finalEventList),HttpStatus.OK);
    }
}
