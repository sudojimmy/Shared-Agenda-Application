package controller;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Event;
import types.UpdateEventRequest;
import types.UpdateEventResponse;
import utils.EventListUtils;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

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

        Bson query = combine(
                set(ApiConstant.EVENT_EVENT_NAME, request.getEventname()),
                set(ApiConstant.EVENT_STARTER_ID, event.getStarterId()),
                set(ApiConstant.EVENT_TYPE, request.getType()),
                set(ApiConstant.EVENT_DATE, request.getDate()),
                set(ApiConstant.EVENT_START, request.getStart()),
                set(ApiConstant.EVENT_COUNT, request.getCount()),
                set(ApiConstant.EVENT_REPEAT, request.getRepeat()),
                set(ApiConstant.EVENT_LOCATION, request.getLocation()),
                set(ApiConstant.EVENT_STATE, request.getState()),
                set(ApiConstant.EVENT_DESCRIPTION, request.getDescription()),
                set(ApiConstant.EVENT_PUBLIC, request.isPublic()));


        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, request.getEventId());
        if (!dataStore.updateInCollection(filter, query, DataStore.COLLECTION_EVENTS)) {
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UpdateEventResponse().withEventId(request.getEventId()),
                HttpStatus.OK);
    }
}
