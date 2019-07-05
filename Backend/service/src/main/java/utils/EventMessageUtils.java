package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.*;

import static controller.BaseController.dataStore;

public class EventMessageUtils {
    public static EventMessage createEventMessageToDatabase(final Event event) {

        String eventMessageId = new ObjectId().toString();

        EventMessage eventMessage = new EventMessage().withMessageId(eventMessageId).withEvent(event);
        dataStore.insertToCollection(eventMessage, DataStore.COLLECTION_EVENTMESSAGES);
        return eventMessage;
    }

    public static EventMessage getEventMessage(final String messageId) {
        Document document = new Document();
        document.put(ApiConstant.MESSAGE_MESSAGE_ID, messageId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_EVENTMESSAGES);
    }


    public static boolean deleteEventMessage(final String eventmessageId) {
        Bson filter = Filters.eq(ApiConstant.MESSAGE_MESSAGE_ID, eventmessageId);
        return dataStore.delete(filter, DataStore.COLLECTION_EVENTMESSAGES);
    }

    // @return true if EventMessage already in DataBase and false otherwise
    public static void checkEventMessageExist(final String messageId) {
        Document document = new Document();
        document.put(ApiConstant.MESSAGE_MESSAGE_ID, messageId);
        if(!dataStore.existInCollection(document, DataStore.COLLECTION_EVENTMESSAGES)) {
            ExceptionUtils.invalidProperty(ApiConstant.MESSAGE_MESSAGE_ID);
        }
    }
}
