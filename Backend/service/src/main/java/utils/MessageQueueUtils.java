package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Message;
import types.MessageQueue;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
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
}
