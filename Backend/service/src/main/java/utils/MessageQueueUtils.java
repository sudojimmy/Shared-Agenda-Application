package utils;

import constant.ApiConstant;
import org.bson.Document;
import org.bson.types.ObjectId;
import store.DataStore;
import types.MessageQueue;

import java.util.ArrayList;

import static controller.BaseController.dataStore;

public class MessageQueueUtils {
    public static MessageQueue createMessageQueueToDatabase() {
        String messageQueueId = new ObjectId().toString();
        MessageQueue messageQueue = new MessageQueue().withMessageQueueId(messageQueueId)
                .withMessageList(new ArrayList<>());
        dataStore.insertToCollection(messageQueue, DataStore.COLLECTION_MESSAGEQUEUES);
        return messageQueue;
    }

    public static MessageQueue getMessageQueue(final String messageQueueId) {
        Document document = new Document();
        document.put(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_MESSAGEQUEUES);
    }

    public static ArrayList<Message> getMessageList(final String messageQueueId){
        MessageQueue messageQueue = getMessageQueue(messageQueueId);
        return new ArrayList<>(messageQueue.getMessageList());
    }
}
