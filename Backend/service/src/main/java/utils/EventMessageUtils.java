package utils;

import constant.ApiConstant;
import org.bson.Document;
import org.bson.types.ObjectId;
import store.DataStore;
import types.*;

import static controller.BaseController.dataStore;

public class EventMessageUtils {
    public static EventMessage createEventMessageToDatabase(String eventId,
                                                            final String eventname,
                                                            final String starterId,
                                                            final EventType type,
                                                            final int start,
                                                            final int count,
                                                            final String date,
                                                            final String location,
                                                            final EventRepeat repeat,
                                                            final EventState state,
                                                            final String description,
                                                            final String replyId,
                                                            final String senderId,
                                                            final String receiverId,
                                                            final ReplyStatus status,
                                                            final String replydescription) {
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

        ReplyMessageUtils.createReplyMessageToDatabase(replyId, senderId, receiverId, status, replydescription);
        EventMessage eventmessage = new EventMessage().withMessageId(eventmessageId).withReplyId(replyId).withEvent(p);
        dataStore.insertToCollection(eventmessage, DataStore.COLLECTION_EVENTMESSAGES);
        return eventmessage;
    }

    public static EventMessage getEventMessage(final String messageId) {
        Document document = new Document();
        document.put(ApiConstant.MESSAGE_MESSAGE_ID, messageId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_EVENTMESSAGES);
    }
}
