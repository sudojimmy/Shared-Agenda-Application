package controller;

import constant.ApiConstant;
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
        assertPropertyValid(request.getEventname(), ApiConstant.EVENT_EVENT_NAME);
        assertPropertyValid(request.getStarterId(), ApiConstant.EVENT_STARTER_ID);
        assertPropertyValid(request.getType(), ApiConstant.EVENT_EVENT_NAME);

        // Step II: check restriction (conflict, or naming rules etc.)
        Account account = AccountUtils.getAccount(request.getStarterId());
        assertDatabaseObjectFound(account, ApiConstant.EVENT_STARTER_ID);

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
                request.getDescription(),
                request.isPublic()
        );

        EventListUtils.addEventIdToCalendar(eventId, account.getCalendarId());

        // Step IV: create response object
        return new ResponseEntity<>(new CreateEventResponse().withEventId(eventId),
                HttpStatus.CREATED);
    }
}
