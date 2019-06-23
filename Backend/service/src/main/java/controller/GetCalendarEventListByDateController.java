package controller;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;

import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Calendar;
import types.Event;
import types.GetCalendarEventListByDateRequest;
import types.GetCalendarEventListByDateResponse;
import utils.CalendarUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;

import java.util.ArrayList;

@RestController
public class GetCalendarEventListByDateController extends BaseController {

    @PostMapping("/getCalendarEventListByDate")
    public ResponseEntity<GetCalendarEventListByDateResponse> handle(@RequestBody GetCalendarEventListByDateRequest request) {
        logger.info("getCalendarEventListByDate: " + request);

        ExceptionUtils.assertPropertyValid(request.getCalendarId(), ApiConstant.CALENDAR_CALENDAR_ID);
        ExceptionUtils.assertPropertyValid(request.getDate(), ApiConstant.EVENT_DATE);

        Calendar calendar = CalendarUtils.getCalendar(request.getCalendarId());
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        Bson dateFilter = Filters.eq(ApiConstant.EVENT_DATE, request.getDate());
        ArrayList<Event> eventList = EventListUtils.getEventListFromCalendarWithCond(calendar, dateFilter);
        return new ResponseEntity<>(new GetCalendarEventListByDateResponse()
                .withEventList(eventList),HttpStatus.OK);
    }
}
