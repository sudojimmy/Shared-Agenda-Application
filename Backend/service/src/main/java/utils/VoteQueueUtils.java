package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.VoteQueue;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class VoteQueueUtils {
    public static VoteQueue createVoteQueueToDatabase() {
        String VoteQueueId = new ObjectId().toString();
        VoteQueue VoteQueue = new VoteQueue().withVoteQueueId(VoteQueueId)
                .withVoteList(new ArrayList<>());
        dataStore.insertToCollection(VoteQueue, DataStore.COLLECTION_VOTEQUEUES);
        return VoteQueue;
    }

    public static VoteQueue getVoteQueue(final String VoteQueueId) {
        Document document = new Document();
        document.put(ApiConstant.VOTEQUEUE_VOTE_QUEUE_ID, VoteQueueId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_VOTEQUEUES);
    }

    public static ArrayList<String> getVoteList(final String VoteQueueId){
        VoteQueue VoteQueue = getVoteQueue(VoteQueueId);
        return new ArrayList<>(VoteQueue.getVoteList());
    }

    public static void addVoteToVoteQueue(final String VoteId, final String VoteQueueId) {
        VoteQueue VoteQueue = getVoteQueue(VoteQueueId);
        if (!VoteQueue.getVoteList().contains(VoteId)) {
            VoteQueue.getVoteList().add(VoteId);

            Bson filter = Filters.eq(ApiConstant.VOTEQUEUE_VOTE_QUEUE_ID, VoteQueueId);
            Bson query = combine(
                    set(ApiConstant.VOTEQUEUE_VOTE_LIST, VoteQueue.getVoteList()));

            dataStore.updateInCollection(filter, query, DataStore.COLLECTION_VOTEQUEUES);
        }
    }

    public static void removeVoteToVoteQueue(final String VoteId, final String VoteQueueId) {
        VoteQueue VoteQueue = getVoteQueue(VoteQueueId);
        if (VoteQueue.getVoteList().contains(VoteId)) {
            VoteQueue.getVoteList().remove(VoteId);

            Bson filter = Filters.eq(ApiConstant.VOTEQUEUE_VOTE_QUEUE_ID, VoteQueueId);
            Bson query = combine(
                    set(ApiConstant.VOTEQUEUE_VOTE_LIST, VoteQueue.getVoteList()));

            dataStore.updateInCollection(filter, query, DataStore.COLLECTION_VOTEQUEUES);
        }
    }
}
