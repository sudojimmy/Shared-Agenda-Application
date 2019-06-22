package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Calendar;
import types.GetCalendarEventListRequest;
import types.GetCalendarEventListResponse;
import utils.CalendarUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;

@RestController
public class GetCalendarEventListController extends BaseController {

    @PostMapping("/getCalendarEventList")
    public ResponseEntity<GetCalendarEventListResponse> handle(@RequestBody GetCalendarEventListRequest request) {
        logger.info("getCalendarEventList: " + request);

        ExceptionUtils.assertPropertyValid(request.getCalendarId(), ApiConstant.CALENDAR_CALENDAR_ID);

        Calendar calendar = CalendarUtils.getCalendar(request.getCalendarId());
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        return new ResponseEntity<>(new GetCalendarEventListResponse()
            .withEventList(EventListUtils.getEventListFromCalendar(calendar)),HttpStatus.OK);
    }
}
