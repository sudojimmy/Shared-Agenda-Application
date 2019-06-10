package utils;

import constant.ApiConstant;
import org.bson.Document;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Calendar;

import java.util.ArrayList;

import static controller.BaseController.dataStore;

public class CalendarUtils {
    public static Calendar createCalendarToDatabase() {
        String calendarId = new ObjectId().toString();
        Calendar calendar = new Calendar().withCalendarId(calendarId).withEventList(new ArrayList<>());
        dataStore.insertToCollection(calendar, DataStore.COLLECTION_CALENDARS);
        return calendar;
    }

    public static Calendar getCalendar(String calendarId) {
        Document document = new Document();
        document.put(ApiConstant.CALENDAR_CALENDAR_ID, calendarId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_CALENDARS);
    }
}
