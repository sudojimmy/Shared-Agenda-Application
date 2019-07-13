package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.Calendar;
import types.GetCalendarEventListRequest;
import types.GetCalendarEventListResponse;
import utils.AccountUtils;
import utils.CalendarUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;

@RestController
public class GetCalendarEventListController extends BaseController {

    @PostMapping("/getCalendarEventList")
    public ResponseEntity<GetCalendarEventListResponse> handle(@RequestBody GetCalendarEventListRequest request) {
        logger.info("getCalendarEventList: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        Account account = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        String calendarId = account.getCalendarId();
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        return new ResponseEntity<>(new GetCalendarEventListResponse()
            .withEventList(EventListUtils.getEventListFromCalendar(calendar)),HttpStatus.OK);
    }
}
