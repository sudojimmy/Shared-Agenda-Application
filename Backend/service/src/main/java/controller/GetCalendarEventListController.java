package controller;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Calendar;
import types.Event;
import types.GetCalendarEventListRequest;
import types.GetCalendarEventListResponse;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class GetCalendarEventListController extends BaseController {

    @PostMapping("/getCalendarEventList")
    public ResponseEntity<GetCalendarEventListResponse> handle(@RequestBody GetCalendarEventListRequest request) {
        logger.info("getCalendarEventList: " + request);

        if (request.getCalendarId() == null || request.getCalendarId().isEmpty()) {
            logger.error("Invalid calendar Id!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Document document = new Document();
        document.put(ApiConstant.CALENDAR_CALENDAR_ID, request.getCalendarId());
        Calendar calendar = dataStore.findOneInCollection(document, DataStore.COLLECTION_CALENDARS);

        if (calendar == null) {
            logger.error("Calendar Id Not Exist!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ArrayList<Bson>loFilters = new ArrayList<>();
        calendar.getEventList().forEach(e -> loFilters.add(Filters.eq(ApiConstant.Event_Event_ID, e)));

        Bson filter = Filters.or(loFilters);
        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        ArrayList<Event> eventList = new ArrayList<>(collection);

        return new ResponseEntity<>(new GetCalendarEventListResponse()
            .withEventList(new ArrayList<>(eventList)),HttpStatus.OK);
    }
}
