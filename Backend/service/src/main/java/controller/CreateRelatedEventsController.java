package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.CreateRelatedEventsRequest;
import types.CreateRelatedEventsResponse;
import types.Event;
import utils.AccountUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;


// TODO: Known error: related event update will cause problem. Might want to set the event un-updatable later
@RestController
public class CreateRelatedEventsController extends BaseController {

    @PostMapping("/createRelatedEvents")
    public ResponseEntity<CreateRelatedEventsResponse> handle(@RequestBody CreateRelatedEventsRequest request) {
        logger.info("createRelatedEvents: " + request);

        // Step I: check parameters
        for (Event e : request.getEvents()) {
            ExceptionUtils.assertPropsEqual(
                    e.getStarterId(),
                    request.getCallerId(),
                    ApiConstant.EVENT_STARTER_ID,
                    ApiConstant.REPEAT_CALLER_ID);
            ExceptionUtils.assertEventValid(e,false, request.getCallerId());
        }


        // Step II: check restriction (conflict, or naming rules etc.)
        Account account = AccountUtils.getAccount(request.getCallerId(), ApiConstant.EVENT_STARTER_ID);

        // Step III: write to Database
        String eventId = EventListUtils.createRelatedEventsToDatabase(request.getEvents());

        EventListUtils.addEventIdToCalendar(eventId, account.getCalendarId());

        // Step IV: create response object
        return new ResponseEntity<>(new CreateRelatedEventsResponse().withEventId(eventId),
                HttpStatus.CREATED);
    }
}
