package controller;

import constant.ApiConstant;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.CreateEventRequest;
import types.CreateEventResponse;
import types.Event;

import static store.DataStore.COLLECTION_EVENTS;


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
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, request.getStarterId());
        if (!dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS)) {
            logger.error("StarterId is not Existed!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Step III: write to Database
        ObjectId groupId = new ObjectId();
        String id = groupId.toString();
        Event p = new Event()
                .withEventId(id)
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

        // Step IV: create response object
        return new ResponseEntity<>(new CreateEventResponse().withEventId(id),
                HttpStatus.CREATED);
    }
}
