package utils;

import constant.ApiConstant;
import org.bson.Document;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Event;
import types.EventMessage;

import java.util.ArrayList;

import static controller.BaseController.dataStore;

public class EventMessageUtils {
    public static EventMessage createEventMessageToDatabase(String eventId, final String eventname, final String starterId,
                                                            final Event.Type type, final int start, final int count,
                                                            final String date, final String location, final Event.Repeat repeat,
                                                            final Event.State state, final String description, boolean isPublic) {
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

        String eventmessageId = new ObjectId().toString();
        EventMessage eventmessage = new EventMessage().withMessageId(eventmessageId).withEvent(p);
        dataStore.insertToCollection(eventmessage, DataStore.COLLECTION_EVENTMESSAGES);
        return eventmessage;
    }

}
