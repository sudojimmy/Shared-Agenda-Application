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
import types.GetEventListRequest;
import types.GetEventListResponse;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class GetEventListController extends BaseController {

    @PostMapping("/getEventList")
    public ResponseEntity<GetEventListResponse> handle(@RequestBody GetEventListRequest request) {
        logger.info("GetEventList: " + request);

        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid event name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_NAME, request.getEventname());
        Collection<Event> eventList = dataStore.findManyInCollection(document, DataStore.COLLECTION_EVENTS);

        return new ResponseEntity<>(new GetEventListResponse().withEventList(new ArrayList<>(eventList)),HttpStatus.OK);
    }
}
