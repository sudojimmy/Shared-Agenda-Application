package controller;

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

@RestController
public class GetCalendarEventListController extends BaseController {

    @PostMapping("/getCalendarEventList")
    public ResponseEntity<GetCalendarEventListResponse> handle(@RequestBody GetCalendarEventListRequest request) {
        logger.info("getCalendarEventList: " + request);

        if (request.getCalendarId() == null || request.getCalendarId().isEmpty()) {
            logger.error("Invalid calendar Id!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Calendar calendar = CalendarUtils.getCalendar(request.getCalendarId());
        if (calendar == null) {
            logger.error("Calendar Id Not Exist!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new GetCalendarEventListResponse()
            .withEventList(EventListUtils.getEventListFromCalendar(calendar)),HttpStatus.OK);
    }
}
