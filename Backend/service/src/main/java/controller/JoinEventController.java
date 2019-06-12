package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.AccountUtils;
import utils.EventListUtils;

@RestController
public class JoinEventController extends BaseController {

    @PostMapping("/joinEvent")
    public ResponseEntity<JoinEventResponse> handle(@RequestBody JoinEventRequest request) {
        logger.info("JoinEvent: " + request);

        assertPropertyValid(request.getEventId(), ApiConstant.EVENT_EVENT_ID);
        assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        // check accountId is a valid accountId
        Account account = AccountUtils.getAccount(request.getAccountId());
        assertDatabaseObjectFound(account, ApiConstant.ACCOUNT_ACCOUNT_ID);

        Event event = EventListUtils.getEventListById(request.getEventId());
        assertDatabaseObjectFound(event, ApiConstant.EVENT_EVENT_ID);

        EventListUtils.addEventIdToCalendar(request.getEventId(), account.getCalendarId());

        return new ResponseEntity<>(new JoinEventResponse()
                .withEventId(event.getEventId()),
                HttpStatus.OK);
    }

}
