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

import java.util.ArrayList;

@RestController
public class GetCalendarEventListByDateController extends BaseController {

    @PostMapping("/getCalendarEventListByDate")
    public ResponseEntity<GetCalendarEventListByDateResponse> handle(@RequestBody GetCalendarEventListByDateRequest request) {
        logger.info("getCalendarEventListByDate: " + request);

        if (request.getCalendarId() == null || request.getCalendarId().isEmpty()) {
            logger.error("Invalid calendar Id!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Calendar calendar = CalendarUtils.getCalendar(request.getCalendarId());
        if (calendar == null) {
            logger.error("Calendar Id Not Exist!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Bson dateFilter = Filters.eq(ApiConstant.EVENT_DATE, request.getDate());
        ArrayList<Event> eventList = EventListUtils.getEventListFromCalendarWithCond(calendar, dateFilter);
        return new ResponseEntity<>(new GetCalendarEventListByDateResponse()
                .withEventList(eventList),HttpStatus.OK);
    }
}
