package controller;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;

import org.bson.conversions.Bson;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class GetEventListByDateController extends BaseController {

    @PostMapping("/getEventListByDate")
    public ResponseEntity<GetEventListByDateResponse> handle(@RequestBody GetEventListByDateRequest request) {
        logger.info("getCalendarEventListByDate: " + request);

        ExceptionUtils.assertPropertyValid(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getCalendarId(), ApiConstant.CALENDAR_CALENDAR_ID);
        ExceptionUtils.assertPropertyValid(request.getDate(), ApiConstant.EVENT_DATE);

        AccountUtils.getAccount(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        Calendar calendar = CalendarUtils.getCalendar(request.getCalendarId());
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        ArrayList<Event> eventList = EventListUtils
                .getEventListFromCalendarWithRepeat(calendar, request.getDate());

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

        return new ResponseEntity<>(new GetEventListByDateResponse()
                .withEventList(finalEventList),HttpStatus.OK);
    }
}
