  
package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Message;
import types.MessageQueue;
import types.*;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class MessageUtils {
    public static Message createMessageToDatabase(String messageId, final MessageType type) {
        Message message = new Message().withMessageId(messageId).withType(type);
        dataStore.insertToCollection(message, DataStore.COLLECTION_MESSAGES);
        return message;
    }

    public static void generateMessageToMessageQueue(final MessageType type, final String messageQueueId) {
        String messageId = new ObjectId().toString();
        createMessageToDatabase(messageId, type);
        addMessageIdToMessageQueue(messageId, messageQueueId);
    }

    public static Message getMessage(final String messageId) {
        Document document = new Document();
        document.put(ApiConstant.MESSAGE_MESSAGE_ID, messageId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_MESSAGES);
    }

    public static void addMessageIdToMessageQueue(final String messageId, final String messageQueueId) {
        MessageQueue messageQueue = MessageQueueUtils.getMessageQueue(messageQueueId);
        Message message = getMessage(messageId);
        messageQueue.getMessageList().add(message);

        Bson filter = Filters.eq(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
        Bson query = combine(
                set(ApiConstant.MESSAGEQUEUE_MESSAGE_LIST, messageQueue.getMessageList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_MESSAGEQUEUES);
    }

    public static void removeMessageIdFromMessageQueue(final String messageId, final String messageQueueId) {
        MessageQueue messageQueue = MessageQueueUtils.getMessageQueue(messageQueueId);
        Message message = getMessage(messageId);
        messageQueue.getMessageList().remove(message);

        Bson filter = Filters.eq(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
        Bson query = combine(
                set(ApiConstant.MESSAGEQUEUE_MESSAGE_LIST, messageQueue.getMessageList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_MESSAGEQUEUES);
    }
}