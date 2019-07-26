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
import utils.ExceptionUtils;

@RestController
public class CancelEventController extends BaseController {

    @PostMapping("/cancelEvent")
    public ResponseEntity<CancelEventResponse> handle(@RequestBody CancelEventRequest request) {
        logger.info("CancelEvent: " + request);

        ExceptionUtils.assertPropertyValid(request.getEventId(), ApiConstant.EVENT_EVENT_ID);
        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        // check accountId is a valid accountId
        AccountUtils.getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);

        Event event = EventListUtils.getEventListById(request.getEventId());
        ExceptionUtils.assertDatabaseObjectFound(event, ApiConstant.EVENT_EVENT_ID);

        if(!request.getAccountId().equals(event.getStarterId())){
            // no permission to cancel the event
            ExceptionUtils.invalidProperty("Only owner can cancel the event");
        }
        event.setState(EventState.CANCELLED);
        EventListUtils.updateEventInDatabase(event);

        return new ResponseEntity<>(new CancelEventResponse()
                .withEventId(event.getEventId()),
                HttpStatus.OK);
    }

}
