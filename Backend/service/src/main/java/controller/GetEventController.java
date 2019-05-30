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
import types.GetEventRequest;
import types.GetEventResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class GetEventController extends BaseController {

    @PostMapping("/getEvent")
    public ResponseEntity<GetEventResponse> handle(@RequestBody GetEventRequest request) {
        logger.info("GetEvent: " + request);

        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid event name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Document document = new Document();
        document.put(ApiConstant.Event_Event_NAME, request.getEventname());
        Collection<Event> eventList = dataStore.findMoreInCollection(document, DataStore.COLLECTION_EVENTS);
        if (eventList == null) {
            logger.error("No Event found according to this Event Name");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Event> list= new ArrayList<Event>();

        for (Iterator iterator = eventList.iterator(); iterator.hasNext();) {
            Event event = (Event)iterator.next();
            list.add(event);
        }

        return new ResponseEntity<>(new GetEventResponse().withEventList(list),HttpStatus.OK);
    }
}
