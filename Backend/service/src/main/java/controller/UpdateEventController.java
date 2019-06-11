package controller;

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

        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            logger.error("Invalid EventId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
            logger.error("Invalid AccountId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid Event Name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getType() == null) {
            logger.error("Invalid Event type!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // check startId is a valid accountId
        Event event = EventListUtils.getEventListById(request.getEventId());
        if (event == null) {
            logger.error("The event is not an existed event!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(!request.getAccountId().equals(event.getStarterId())){
            logger.error("Only Event owner can update event!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // TODO find a way to properly update
/*        Bson query = combine(
                set(ApiConstant.EVENT_EVENT_NAME, request.getEventname()),
                set(ApiConstant.EVENT_STARTER_ID, request.getStarterId()),
                set(ApiConstant.EVENT_TYPE, request.getType()),
                set(ApiConstant.EVENT_DATE, request.getDate()),
                set(ApiConstant.EVENT_START, request.getStart()),
                set(ApiConstant.EVENT_COUNT, request.getCount()),
                set(ApiConstant.EVENT_REPEAT, request.getRepeat()));
                set(ApiConstant.EVENT_LOCATION, request.getLocation()),
                set(ApiConstant.EVENT_STATE, request.getState()),
                set(ApiConstant.EVENT_DESCRIPTION, request.getDescription()));*/

        if (!EventListUtils.deleteEvent(request.getEventId())){
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EventListUtils.createEventToDatabase(
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
                request.getDescription()
        );

        // TODO find a way to properly update
        /*if (!dataStore.updateInCollection(filter, query, DataStore.COLLECTION_EVENTS)) {
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/

        return new ResponseEntity<>(new UpdateEventResponse().withEventId(request.getEventId()),
                HttpStatus.OK);
    }
}
