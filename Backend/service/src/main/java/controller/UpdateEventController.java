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

// TODO find a way to properly update


@RestController
public class UpdateEventController extends BaseController {

    @PostMapping("/updateEvent")
    public ResponseEntity<UpdateEventResponse> handle(@RequestBody UpdateEventRequest request) {
        logger.info("UpdateEvent: " + request);

        assertPropertyValid(request.getEventId(), ApiConstant.EVENT_EVENT_ID);
        assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        assertPropertyValid(request.getEventname(), ApiConstant.EVENT_EVENT_NAME);
        assertPropertyValid(request.getType(), ApiConstant.EVENT_TYPE);

        // check startId is a valid accountId
        Event event = EventListUtils.getEventListById(request.getEventId());
        assertDatabaseObjectFound(event, ApiConstant.EVENT_TYPE);

        if(!request.getAccountId().equals(event.getStarterId())){
            logger.error("Only Event owner can update event!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean updated = EventListUtils.updateEventInDatabase(
                request.getEventId(),
                request.getEventname(),
                event.getStarterId(),
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

        if (!updated) {
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UpdateEventResponse().withEventId(request.getEventId()),
                HttpStatus.OK);
    }
}
