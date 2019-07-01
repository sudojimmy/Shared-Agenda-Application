  
package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
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
    public static void generateMessageToMessageQueue(final MessageType type, final String messageQueueId, String senderId) {
        String messageId = new ObjectId().toString();
        Message message = new Message().withMessageId(messageId).withType(type).withSenderId(senderId);
        addMessageIdToMessageQueue(message, messageQueueId);
    }

    public static void addMessageIdToMessageQueue(final Message message, final String messageQueueId) {
        MessageQueue messageQueue = MessageQueueUtils.getMessageQueue(messageQueueId);
        messageQueue.getMessageList().add(message);

        Bson filter = Filters.eq(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
        Bson query = combine(
                set(ApiConstant.MESSAGEQUEUE_MESSAGE_LIST, messageQueue.getMessageList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_MESSAGEQUEUES);
    }

//    public static void removeMessageIdFromMessageQueue(final String messageId, final String messageQueueId) {
//        MessageQueue messageQueue = MessageQueueUtils.getMessageQueue(messageQueueId);
//        Message message = getMessage(messageId);
//        messageQueue.getMessageList().remove(message);
//
//        Bson filter = Filters.eq(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
//        Bson query = combine(
//                set(ApiConstant.MESSAGEQUEUE_MESSAGE_LIST, messageQueue.getMessageList()));
//
//        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_MESSAGEQUEUES);
//    }
}