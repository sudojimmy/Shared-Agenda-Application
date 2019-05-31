package controller;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Account;
import types.Calendar;
import types.CreateEventRequest;
import types.CreateEventResponse;
import types.Event;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


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
        Account account = dataStore.findOneInCollection(document, DataStore.COLLECTION_ACCOUNTS);
        if (account == null) {
            logger.error("StarterId is not Existed!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Step III: write to Database

        String eventId = new ObjectId().toString();

        addEventIdToCalendar(eventId, account.getCalendarId());

        Event p = new Event()
                .withEventId(eventId)
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
        return new ResponseEntity<>(new CreateEventResponse().withEventId(eventId),
                HttpStatus.CREATED);
    }

    private void addEventIdToCalendar(String eventId, String calendarId) {
        Document calendarDoc = new Document();
        calendarDoc.put(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Calendar calendar = dataStore.findOneInCollection(calendarDoc, DataStore.COLLECTION_CALENDARS);
        calendar.getEventList().add(eventId);

        Bson filter = Filters.eq(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Bson query = combine(
            set(ApiConstant.CALENDAR_EVENT_LIST, calendar.getEventList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_CALENDARS);
    }
}
