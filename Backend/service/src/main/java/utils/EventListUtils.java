package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import store.DataStore;
import types.Calendar;
import types.Event;

import java.util.ArrayList;
import java.util.Collection;

import static controller.BaseController.dataStore;

public class EventListUtils {

    public static ArrayList<Event> getEventListByName(String eventname) {
        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_NAME, eventname);
        return new ArrayList<>(dataStore.findManyInCollection(document, DataStore.COLLECTION_EVENTS));
    }

    public static ArrayList<Event> getEventListFromCalendar(Calendar calendar) {
        Bson filter = calendarToEventFilter(calendar);

        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        return new ArrayList<>(collection);
    }

    public static ArrayList<Event> getEventListFromCalendarWithCond(Calendar calendar, Bson cond) {
        Bson filter = Filters.and(calendarToEventFilter(calendar), cond);
        Collection<Event> collection = dataStore.findManyInCollection(filter, DataStore.COLLECTION_EVENTS);
        return new ArrayList<>(collection);
    }

    private static Bson calendarToEventFilter(Calendar calendar) {
        ArrayList<Bson>loFilters = new ArrayList<>();
        calendar.getEventList().forEach(e -> loFilters.add(Filters.eq(ApiConstant.EVENT_EVENT_ID, e)));
        return Filters.or(loFilters);
    }
}
