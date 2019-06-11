package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Calendar;
import types.Event;

import java.util.ArrayList;
import java.util.Collection;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class EventListUtils {
    public static String createEventToDatabase(String eventId, final String eventname, final String starterId,
                                               final Event.Type type, final int start, final int count,
                                               final String date, final String location, final Event.Repeat repeat,
                                               final Event.State state, final String description) {
        if (eventId == null) {
            eventId = new ObjectId().toString();
        }

        Event p = new Event()
                .withEventId(eventId)
                .withEventname(eventname)
                .withStarterId(starterId)
                .withType(type)
                .withStart(start)
                .withCount(count)
                .withDate(date)
                .withLocation(location)
                .withRepeat(repeat)
                .withState(state)
                .withDescription(description);

        dataStore.insertToCollection(p, DataStore.COLLECTION_EVENTS);
        return eventId;
    }

    public static Event getEventListById(String eventId) {
        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_ID, eventId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_EVENTS);
    }

    public static ArrayList<Event> getEventListByName(String eventname) {
        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_NAME, eventname);
        return new ArrayList<>(dataStore.findManyInCollection(document, DataStore.COLLECTION_EVENTS));
    }

    public static ArrayList<Event> getEventListFromCalendar(Calendar calendar) {
        if (calendar.getEventList().isEmpty()) { return new ArrayList<>(); }

        Bson filter = calendarToEventFilter(calendar);

        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        return new ArrayList<>(collection);
    }

    public static ArrayList<Event> getEventListFromCalendarWithCond(Calendar calendar, Bson cond) {
        if (calendar.getEventList().isEmpty()) { return new ArrayList<>(); }

        Bson filter = Filters.and(calendarToEventFilter(calendar), cond);
        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        return new ArrayList<>(collection);
    }

    public static boolean deleteEvent(String eventId) {
        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, eventId);
        return dataStore.delete(filter, DataStore.COLLECTION_EVENTS);
    }

    public static void addEventIdToCalendar(String eventId, String calendarId) {
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        calendar.getEventList().add(eventId);

        Bson filter = Filters.eq(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Bson query = combine(
                set(ApiConstant.CALENDAR_EVENT_LIST, calendar.getEventList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_CALENDARS);
    }

    public static void deleteEventIdFromCalendar(String eventId, String calendarId) {
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        calendar.getEventList().remove(eventId);

        Bson filter = Filters.eq(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        Bson query = combine(set(ApiConstant.CALENDAR_EVENT_LIST, calendar.getEventList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_CALENDARS);
    }

    private static Bson calendarToEventFilter(Calendar calendar) {
        ArrayList<Bson>loFilters = new ArrayList<>();
        calendar.getEventList().forEach(e -> loFilters.add(Filters.eq(ApiConstant.EVENT_EVENT_ID, e)));
        return Filters.or(loFilters);
    }
}
