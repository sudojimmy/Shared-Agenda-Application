package utils;

import org.bson.types.ObjectId;
import store.DataStore;
import types.*;
import static controller.BaseController.dataStore;

public class VoteUtils {

    public static String createVoteToDatabase(final Vote vote) {
        createVoteToDatabase(vote, new ObjectId().toString());
        return vote.getVoteId();
    }

    private static void createVoteToDatabase(final Vote vote, final String voteId) {
        vote.setVoteId(voteId);
        dataStore.insertToCollection(vote, DataStore.COLLECTION_VOTES);
    }
}
