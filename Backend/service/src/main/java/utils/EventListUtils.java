package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.*;
import types.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class EventListUtils {

    public static String createEventToDatabase(final Event event) {
        createEventToDatabase(event, new ObjectId().toString());
        return event.getEventId();
    }

    private static void createEventToDatabase(final Event event, final String eventId) {
        event.setEventId(eventId);
        dataStore.insertToCollection(event, DataStore.COLLECTION_EVENTS);
    }

    public static String createRelatedEventsToDatabase(final List<Event> events) {
        String eventId = null;
        for (Event e : events) {
            if (eventId == null) {
                eventId = createEventToDatabase(e);
            } else {
                createEventToDatabase(e, eventId);
            }
        }
        return eventId;
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
                set(ApiConstant.EVENT_PERMISSION, event.getPermission()));


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

    public static ArrayList<Event> getAllEventList() {
        return new ArrayList<>(dataStore.findAllCollection(DataStore.COLLECTION_EVENTS));
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

    private static boolean happened(final Event event, final String date) {
        try {
            Repeat repeat = event.getRepeat();
            EventRepeat eventRepeat = repeat.getType();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(repeat.getStartDate());
            Date endDate = sdf.parse(repeat.getEndDate());
            Date matchDate = sdf.parse(date);

            if (startDate.equals(matchDate)) {
                return true;
            }

            if(endDate.before(matchDate) || startDate.after(matchDate)){
                return false;
            }

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(startDate);
            int weekday1 = calendar.get(java.util.Calendar.DAY_OF_WEEK);
            int monthday1 = calendar.get(java.util.Calendar.DAY_OF_MONTH);
            int month1 = calendar.get(java.util.Calendar.MONTH);

            calendar.setTime(matchDate);
            int weekday2 = calendar.get(java.util.Calendar.DAY_OF_WEEK);
            int monthday2 = calendar.get(java.util.Calendar.DAY_OF_MONTH);
            int month2 = calendar.get(java.util.Calendar.MONTH);


            boolean result = true;
            switch (eventRepeat) {
                case DAY:
                    result = true;
                    break;
                case YEAR:
                    result = result && month1 == month2;
                    // flow
                case MONTH:
                    result = result && monthday1 == monthday2;
                    break;
                case WEEK:
                    result = result && weekday1 == weekday2;
                    break;
                default:
                    result = false;
            }
            return result;
        } catch (ParseException ex) {
            ex.printStackTrace();
            ExceptionUtils.invalidProperty("Date format");
            return false;
        }
    }

    public static ArrayList<Event> getEventListFromCalendarWithRepeat(final Calendar calendar, final String matchDate) {
        if (calendar.getEventList().isEmpty()) { return new ArrayList<>(); }

        ArrayList<Event> eventList = getEventListFromCalendar(calendar);
        List<Event> eventListRt = eventList.stream().filter((e) -> happened(e, matchDate)).collect(Collectors.toList());
        return (ArrayList<Event>) eventListRt;
    }

    private static Event modifiedEvent(Event event, String date){
        Repeat newrepeat = new Repeat()
                .withEndDate(date)
                .withStartDate(date)
                .withType(event.getRepeat().getType());

        Event newEvent = new Event()
                .withEventId(event.getEventId())
                .withEventname(event.getEventname())
                .withStarterId(event.getStarterId())
                .withType(event.getType())
                .withStartTime(event.getStartTime())
                .withEndTime(event.getEndTime())
                .withRepeat(newrepeat)
                .withLocation(event.getLocation())
                .withState(event.getState())
                .withDescription(event.getDescription())
                .withPermission(event.getPermission());

        return newEvent;

    }

    private static Event modifiedEventWithRepeat(Event event, String date){
        Repeat newrepeat = new Repeat()
                .withEndDate(date)
                .withStartDate(date)
                .withType(event.getRepeat().getType());

        return new Event()
                .withStartTime(event.getStartTime())
                .withEndTime(event.getEndTime())
                .withRepeat(newrepeat);
    }

    public static ArrayList<Event> getEventListFromCalendarWithDate(final ArrayList<Event> eventList,
                                                                    final String date) {
        ArrayList<Event> eventListRt = new ArrayList<>();

        for (Event event : eventList) {
            if (EventListUtils.happened(event, date)) {
                eventListRt.add(modifiedEventWithRepeat(event, date));
            }
        }

        return eventListRt;
    }

    public static ArrayList<Event> getEventListFromCalendarWithYearMonth(final Calendar calendar,
                                                                         final int year,
                                                                         final int month) {
        if (calendar.getEventList().isEmpty()) { return new ArrayList<>(); }

        ArrayList<Event> eventList = getEventListFromCalendar(calendar);

        // Get the number of days in that month
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int totalDayInMonth = yearMonthObject.lengthOfMonth();

        ArrayList<Event> eventListRt = new ArrayList<>();
        for(Event event: eventList){
            for(int i = 1; i < (totalDayInMonth + 1); i++){

                String date = constructDateByYearMonthDay(year, month, i);

                if (happened(event, date)) {
                    eventListRt.add(modifiedEvent(event, date));
                }
            }
        }
        return eventListRt;
    }

    public static String constructDateByYearMonthDay(final int year,
                                                     final int month,
                                                     final int day){
        String date;
        String monthString;

        // build month string
        if (month >= 10){
            monthString = Integer.toString(month);
        } else{
            monthString = "0" + month;
        }

        // build day string
        if (day >= 10) {
            date = year + "-" + monthString + "-" + day;
        } else {
            date = year + "-" + monthString + "-0" + day;
        }

        return date;
    }

    public static boolean deleteEvent(final String eventId) {
        Bson filter = Filters.eq(ApiConstant.EVENT_EVENT_ID, eventId);
        return dataStore.deleteAll(filter, DataStore.COLLECTION_EVENTS);
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

    public static boolean checkEventPermission(final String accountId, final Event event) {
        Permission permission = event.getPermission();
        if ((permission.getType() == PermissionType.PUBLIC)
                || (event.getStarterId().equals(accountId))) {
            return true;
        }

        if ((permission.getType() == PermissionType.ACCOUNT)
                && (permission.getPermitToId().equals(accountId))) {
            return true;
        }

        if (permission.getType() == PermissionType.GROUP) {
            return GroupUtils.checkGroupMember(permission.getPermitToId(), accountId);
        }

        return false;
    }

    public static ArrayList<Event> getMonthlyEventByAccount(String targetAccountId,
                                                            String callerAccountId,
                                                            int year,
                                                            int month){
        Account account = AccountUtils.getAccount(targetAccountId, ApiConstant.ACCOUNT_ACCOUNT_ID);
        String calendarId = account.getCalendarId();
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        ArrayList<Event> eventList = EventListUtils
                .getEventListFromCalendarWithYearMonth(
                        calendar,
                        year,
                        month);

        ArrayList<Event> finalEventList = new ArrayList<Event>();

        for (Event event: eventList) {
            if (!EventListUtils.checkEventPermission(callerAccountId, event)) {
                // if no permission

                Event displayEvent = new Event()
                        .withStartTime(event.getStartTime())
                        .withEndTime(event.getEndTime())
                        .withRepeat(event.getRepeat());

                finalEventList.add(displayEvent);
            } else {
                finalEventList.add(event);
            }
        }

        return finalEventList;

    }
}
