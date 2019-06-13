package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.CancelEventRequest;
import types.CancelEventResponse;
import types.Event;
import utils.AccountUtils;
import utils.EventListUtils;

@RestController
public class CancelEventController extends BaseController {

    @PostMapping("/cancelEvent")
    public ResponseEntity<CancelEventResponse> handle(@RequestBody CancelEventRequest request) {
        logger.info("CancelEvent: " + request);

        assertPropertyValid(request.getEventId(), ApiConstant.EVENT_EVENT_ID);
        assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        // check accountId is a valid accountId
        Account account = AccountUtils.getAccount(request.getAccountId());
        assertDatabaseObjectFound(account, ApiConstant.ACCOUNT_ACCOUNT_ID);

        Event event = EventListUtils.getEventListById(request.getEventId());
        assertDatabaseObjectFound(event, ApiConstant.EVENT_EVENT_ID);

        if(!request.getAccountId().equals(event.getStarterId())){
            // no permission to cancel the event
            invalidProperty("Only owner can cancel the event");
        }

        if (!EventListUtils.deleteEvent(request.getEventId())){
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EventListUtils.createEventToDatabase(
                request.getEventId(),
                event.getEventname(),
                event.getStarterId(),
                event.getType(),
                event.getStart(),
                event.getCount(),
                event.getDate(),
                event.getLocation(),
                event.getRepeat(),
                Event.State.CANCELLED,
                event.getDescription(),
                event.isPublic()
        );

        return new ResponseEntity<>(new CancelEventResponse()
                .withEventId(event.getEventId()),
                HttpStatus.OK);
    }

}
