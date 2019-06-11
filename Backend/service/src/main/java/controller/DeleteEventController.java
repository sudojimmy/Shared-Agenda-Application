package controller;

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

@RestController
public class DeleteEventController extends BaseController {

    @PostMapping("/deleteEvent")
    public ResponseEntity<DeleteEventResponse> handle(@RequestBody DeleteEventRequest request) {
        logger.info("DeleteEvent: " + request);

        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            logger.error("Invalid EventId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
            logger.error("Invalid AccountId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // check accountId is a valid accountId
        Account account = AccountUtils.getAccount(request.getAccountId());
        if (account == null) {
            logger.error("accountId(caller) is not an existed accountId!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Event event = EventListUtils.getEventListById(request.getEventId());
        if (event  == null) {
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
