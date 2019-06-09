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
import types.*;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@RestController
public class DeleteEventController extends BaseController {

    @PostMapping("/deleteEvent")
    public ResponseEntity<DeleteEventResponse> handle(@RequestBody DeleteEventRequest request) {
        logger.info("DeleteEvent: " + request);

        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            logger.error("Invalid EventId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
            logger.error("Invalid AccountId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // check accountId is a valid accountId
        Document document1 = new Document();
        document1.put(ApiConstant.ACCOUNT_ACCOUNT_ID, request.getAccountId());
        Account account = dataStore.findOneInCollection(document1, DataStore.COLLECTION_ACCOUNTS);
        if (account == null) {
            logger.error("accountId(caller) is not an existed accountId!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_ID, request.getEventId());
        Event event = dataStore.findOneInCollection(document, DataStore.COLLECTION_EVENTS);
        if (event  == null) {
            logger.error("Event Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(request.getAccountId().equals(event.getStarterId())){
            // delete the event from DB
            Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, request.getEventId());
            if (!dataStore.delete(filter, DataStore.COLLECTION_EVENTS)){
                logger.error("Event Id Not Found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else{
            // only remove the event from accountId's calendar
            deleteEventIdFromCalendar(event.getEventId(), account.getCalendarId());
        }

        return new ResponseEntity<>(new DeleteEventResponse()
                .withEventId(event.getEventId()),
                HttpStatus.OK);
    }

    private void deleteEventIdFromCalendar(String eventId, String calendarId) {
        Document calendarDoc = new Document();
        calendarDoc.put(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Calendar calendar = dataStore.findOneInCollection(calendarDoc, DataStore.COLLECTION_CALENDARS);
        calendar.getEventList().remove(eventId);

        Bson filter = Filters.eq(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Bson query = combine(
                set(ApiConstant.CALENDAR_EVENT_LIST, calendar.getEventList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_CALENDARS);
    }
}
