package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Account;
import types.FriendQueue;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class FriendQueueUtils {
    public static FriendQueue createFriendQueueToDatabase() {
        String friendQueueId = new ObjectId().toString();
        FriendQueue friendQueue = new FriendQueue().withFriendQueueId(friendQueueId)
                .withFriendList(new ArrayList<>());
        dataStore.insertToCollection(friendQueue, DataStore.COLLECTION_FRIENDQUEUES);
        return friendQueue;
    }

    public static FriendQueue getFriendQueue(final String friendQueueId) {
        Document document = new Document();
        document.put(ApiConstant.FRIENDQUEUE_FRIEND_QUEUE_ID, friendQueueId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_FRIENDQUEUES);
    }

    public static ArrayList<String> getFriendList(final String friendQueueId){
        FriendQueue friendQueue = getFriendQueue(friendQueueId);
        return new ArrayList<>(friendQueue.getFriendList());
    }

    public static void addFriendToFriendQueue(final String friendId, final String friendQueueId) {
        FriendQueue friendQueue = getFriendQueue(friendQueueId);
        if (!friendQueue.getFriendList().contains(friendId)) {
            friendQueue.getFriendList().add(friendId);

            Bson filter = Filters.eq(ApiConstant.FRIENDQUEUE_FRIEND_QUEUE_ID, friendQueueId);
            Bson query = combine(
                    set(ApiConstant.FRIENDQUEUE_FRIEND_LIST, friendQueue.getFriendList()));

            dataStore.updateInCollection(filter, query, DataStore.COLLECTION_FRIENDQUEUES);
        }
    }

    public static void removeFriendToFriendQueue(final String friendId, final String friendQueueId) {
        FriendQueue friendQueue = getFriendQueue(friendQueueId);
        if (friendQueue.getFriendList().contains(friendId)) {
            friendQueue.getFriendList().remove(friendId);

            Bson filter = Filters.eq(ApiConstant.FRIENDQUEUE_FRIEND_QUEUE_ID, friendQueueId);
            Bson query = combine(
                    set(ApiConstant.FRIENDQUEUE_FRIEND_LIST, friendQueue.getFriendList()));

            dataStore.updateInCollection(filter, query, DataStore.COLLECTION_FRIENDQUEUES);
        }
    }

    public static boolean isFriend(Account account, String friendId) {
        return getFriendList(account.getFriendQueueId()).contains(friendId);
    }
}
