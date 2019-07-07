package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Event;
import types.UpdateEventRequest;
import types.UpdateEventResponse;
import utils.EventListUtils;
import utils.ExceptionUtils;

// TODO find a way to properly update


@RestController
public class UpdateEventController extends BaseController {

    @PostMapping("/updateEvent")
    public ResponseEntity<UpdateEventResponse> handle(@RequestBody UpdateEventRequest request) {
        logger.info("UpdateEvent: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertEventValid(request.getEvent(),
                true,
                request.getAccountId());

        Event oldEvent = EventListUtils.getEventListById(request.getEvent().getEventId());
        ExceptionUtils.assertDatabaseObjectFound(oldEvent, ApiConstant.EVENT_TYPE);

        Event event = request.getEvent();

        // permission check
        if(!request.getAccountId().equals(oldEvent.getStarterId())){
            ExceptionUtils.raise("Only Event owner can update oldEvent!", HttpStatus.BAD_REQUEST);
        }
        // cannot change starter id
        if(!event.getStarterId().equals(oldEvent.getStarterId())){
            ExceptionUtils.raise("Cannot modify starter Id!", HttpStatus.BAD_REQUEST);
        }

        boolean updated = EventListUtils.updateEventInDatabase(event);

        if (!updated) {
            ExceptionUtils.raise("Event Id Not Found!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new UpdateEventResponse().withEventId(request.getEvent().getEventId()),
                HttpStatus.OK);
    }
}
