package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.DeleteEventRequest;
import types.DeleteEventResponse;
import types.Event;
import utils.AccountUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;

@RestController
public class DeleteEventController extends BaseController {

    @PostMapping("/deleteEvent")
    public ResponseEntity<DeleteEventResponse> handle(@RequestBody DeleteEventRequest request) {
        logger.info("DeleteEvent: " + request);

        ExceptionUtils.assertPropertyValid(request.getEventId(), ApiConstant.EVENT_EVENT_ID);
        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        // check accountId is a valid accountId
        Account account = AccountUtils.getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);

        Event event = EventListUtils.getEventListById(request.getEventId());
        ExceptionUtils.assertDatabaseObjectFound(event, ApiConstant.EVENT_EVENT_ID);

        if(request.getAccountId().equals(event.getStarterId())){
            // delete the event from DB
            if(!EventListUtils.deleteEvent(request.getEventId())) {
                logger.error("Event Id Not Found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        // remove the event from accountId's calendar
        EventListUtils.deleteEventIdFromCalendar(event.getEventId(), account.getCalendarId());

        return new ResponseEntity<>(new DeleteEventResponse()
                .withEventId(event.getEventId()),
                HttpStatus.OK);
    }

}
