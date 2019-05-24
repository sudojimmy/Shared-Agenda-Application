package controller;

import constant.ApiConstant;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Event;
import types.CreateEventRequest;
import types.CreateEventResponse;


// for Alice only
// ../gradlew clean build


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
        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            logger.error("Invalid EventId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Step II: check restriction (conflict, or naming rules etc.)
        Document document = new Document();
        document.put(ApiConstant.Event_Event_ID, request.getEventId());
        if (dataStore.existInCollection(document, DataStore.COLLECTION_EVENTS)) {
            logger.error("EventId Already Existed!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Step III: write to Database
        Event p = new Event().withEventname(request.getEventname()).withEventId(request.getEventId());
        dataStore.insertToCollection(p, DataStore.COLLECTION_EVENTS);

        // Step IV: create response object
        return new ResponseEntity<>(new CreateEventResponse().withEventId(request.getEventId()),
                HttpStatus.OK);
    }
}
