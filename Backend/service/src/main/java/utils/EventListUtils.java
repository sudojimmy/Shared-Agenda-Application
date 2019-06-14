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
    public static String createEventToDatabase(String eventId, final String eventname, final String starterId,
                                               final EventType type, final int start, final int count,
                                               final String date, final String location, final EventRepeat repeat,
                                               final EventState state, final String description, final boolean isPublic) {
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
                .withDescription(description)
                .with_public(isPublic);

        dataStore.insertToCollection(p, DataStore.COLLECTION_EVENTS);
        return eventId;
    }

    public static boolean updateEventInDatabase(String eventId, final String eventname, final String starterId,
                                               final EventType type, final int start, final int count,
                                               final String date, final String location, final EventRepeat repeat,
                                               final EventState state, final String description, final boolean isPublic) {

        Bson query = combine(
                set(ApiConstant.EVENT_EVENT_NAME, eventname),
                set(ApiConstant.EVENT_STARTER_ID, starterId),
                set(ApiConstant.EVENT_TYPE, type),
                set(ApiConstant.EVENT_DATE, date),
                set(ApiConstant.EVENT_START, start),
                set(ApiConstant.EVENT_COUNT, count),
                set(ApiConstant.EVENT_REPEAT, repeat),
                set(ApiConstant.EVENT_LOCATION, location),
                set(ApiConstant.EVENT_STATE, state),
                set(ApiConstant.EVENT_DESCRIPTION, description),
                set(ApiConstant.EVENT_PUBLIC, isPublic));


        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, eventId);
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
