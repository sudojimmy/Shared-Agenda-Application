package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.*;

import java.util.ArrayList;
import java.util.Collection;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class EventListUtils {

    public static String createEventToDatabase(final Event event) {
        event.setEventId(new ObjectId().toString());
        dataStore.insertToCollection(event, DataStore.COLLECTION_EVENTS);
        return event.getEventId();
    }

    public static boolean updateEventInDatabase(final Event event) {

        Bson query = combine(
                set(ApiConstant.EVENT_EVENT_NAME, event.getEventname()),
                set(ApiConstant.EVENT_STARTER_ID, event.getStarterId()),
                set(ApiConstant.EVENT_TYPE, event.getType()),
                set(ApiConstant.EVENT_REPEAT, event.getRepeat()),
                set(ApiConstant.EVENT_LOCATION, event.getLocation()),
                set(ApiConstant.EVENT_STATE, event.getState()),
                set(ApiConstant.EVENT_START_TIME, event.getStartTime()),
                set(ApiConstant.EVENT_END_TIME, event.getEndTime()),
                set(ApiConstant.EVENT_DESCRIPTION, event.getDescription()),
                set(ApiConstant.EVENT_PERMISSION, event.isPermit()));


        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, event.getEventId());
        return dataStore.updateInCollection(filter, query, DataStore.COLLECTION_EVENTS);
    }

    public static Event getEventListById(final String eventId) {
        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_ID, eventId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_EVENTS);
    }

    public static ArrayList<Event> getEventListByName(final String eventname) {
        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_NAME, eventname);
        return new ArrayList<>(dataStore.findManyInCollection(document, DataStore.COLLECTION_EVENTS));
    }

    public static ArrayList<Event> getEventListFromCalendar(final Calendar calendar) {
        if (calendar.getEventList().isEmpty()) { return new ArrayList<>(); }

        Bson filter = calendarToEventFilter(calendar);

        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        return new ArrayList<>(collection);
    }

    public static ArrayList<Event> getEventListFromCalendarWithCond(final Calendar calendar, final Bson cond) {
        if (calendar.getEventList().isEmpty()) { return new ArrayList<>(); }

        Bson filter = Filters.and(calendarToEventFilter(calendar), cond);
        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        return new ArrayList<>(collection);
    }

    public static boolean deleteEvent(final String eventId) {
        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, eventId);
        return dataStore.delete(filter, DataStore.COLLECTION_EVENTS);
    }

    public static void addEventIdToCalendar(final String eventId, final String calendarId) {
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        calendar.getEventList().add(eventId);

        Bson filter = Filters.eq(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Bson query = combine(
                set(ApiConstant.CALENDAR_EVENT_LIST, calendar.getEventList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_CALENDARS);
    }

    public static void deleteEventIdFromCalendar(final String eventId, final String calendarId) {
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        calendar.getEventList().remove(eventId);

        Bson filter = Filters.eq(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Bson query = combine(set(ApiConstant.CALENDAR_EVENT_LIST, calendar.getEventList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_CALENDARS);
    }

    private static Bson calendarToEventFilter(final Calendar calendar) {
        ArrayList<Bson>loFilters = new ArrayList<>();
        calendar.getEventList().forEach(e -> loFilters.add(Filters.eq(ApiConstant.EVENT_EVENT_ID, e)));
        return Filters.or(loFilters);
    }
}
