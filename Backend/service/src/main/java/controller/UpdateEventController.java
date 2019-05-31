package controller;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.UpdateEventRequest;
import types.UpdateEventResponse;
import types.Event;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


@RestController
public class UpdateEventController extends BaseController {

    @PostMapping("/updateEvent")
    public ResponseEntity<UpdateEventResponse> handle(@RequestBody UpdateEventRequest request) {
        logger.info("UpdateEvent: " + request);

        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            logger.error("Invalid EventId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid Event Name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getStarterId() == null || request.getStarterId().isEmpty()) {
            logger.error("Invalid Starter(Account) Id!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // check startId is a valid accountId
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, request.getStarterId());
        if (!dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS)) {
            logger.error("StarterId is not an existed accountId!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (request.getType() == null) {
            logger.error("Invalid Event type!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, request.getEventId());

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

        if (!dataStore.delete(filter, DataStore.COLLECTION_EVENTS)){
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Event p = new Event()
                .withEventId(request.getEventId())
                .withEventname(request.getEventname())
                .withStarterId(request.getStarterId())
                .withType(request.getType())
                .withStart(request.getStart())
                .withCount(request.getCount())
                .withDate(request.getDate())
                .withLocation(request.getLocation())
                .withRepeat(request.getRepeat())
                .withState(request.getState())
                .withDescription(request.getDescription());
        dataStore.insertToCollection(p, DataStore.COLLECTION_EVENTS);

        // TODO find a way to properly update
        /*if (!dataStore.updateInCollection(filter, query, DataStore.COLLECTION_EVENTS)) {
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/

        return new ResponseEntity<>(new UpdateEventResponse().withEventId(request.getEventId()),
                HttpStatus.OK);
    }
}
