package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.CreateEventRequest;
import types.CreateEventResponse;
import utils.AccountUtils;
import utils.EventListUtils;


@RestController
public class CreateEventController extends BaseController {

    @PostMapping("/createEvent")
    public ResponseEntity<CreateEventResponse> handle(@RequestBody CreateEventRequest request) {
        logger.info("CreateEvent: " + request);

        // Step I: check parameters
        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid Eventname!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (request.getStarterId() == null || request.getStarterId().isEmpty()) {
            logger.error("Invalid StarterId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (request.getType() == null) {
            logger.error("Invalid Type!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Step II: check restriction (conflict, or naming rules etc.)
        Account account = AccountUtils.getAccount(request.getStarterId());
        if (account == null) {
            logger.error("StarterId is not Existed!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Step III: write to Database
        String eventId = EventListUtils.createEventToDatabase(
                null,
                request.getEventname(),
                request.getStarterId(),
                request.getType(),
                request.getStart(),
                request.getCount(),
                request.getDate(),
                request.getLocation(),
                request.getRepeat(),
                request.getState(),
                request.getDescription()
        );

        EventListUtils.addEventIdToCalendar(eventId, account.getCalendarId());

        // Step IV: create response object
        return new ResponseEntity<>(new CreateEventResponse().withEventId(eventId),
                HttpStatus.CREATED);
    }
}
