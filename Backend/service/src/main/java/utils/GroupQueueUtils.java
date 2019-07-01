package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Account;
import types.GroupQueue;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class GroupQueueUtils {
    public static GroupQueue createGroupQueueToDatabase() {
        String groupQueueId = new ObjectId().toString();
        GroupQueue groupQueue = new GroupQueue().withGroupQueueId(groupQueueId)
                .withGroupList(new ArrayList<>());
        dataStore.insertToCollection(groupQueue, DataStore.COLLECTION_GROUPQUEUES);
        return groupQueue;
    }

    public static GroupQueue getGroupQueue(final String GroupQueueId) {
        Document document = new Document();
        document.put(ApiConstant.GROUPQUEUE_GROUP_QUEUE_ID, GroupQueueId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_GROUPQUEUES);
    }

    public static ArrayList<String> getGroupList(final String groupQueueId){
        GroupQueue groupQueue = getGroupQueue(groupQueueId);
        return new ArrayList<>(groupQueue.getGroupList());
    }

    public static void addGroupToGroupQueue(final String groupId, final String groupQueueId) {
        GroupQueue groupQueue = getGroupQueue(groupQueueId);
        if (!groupQueue.getGroupList().contains(groupId)) {
            List<String> groupList = groupQueue.getGroupList();
            groupList.add(groupId);
            updateGroupListToGroupQueue(groupList,groupQueueId);
        }
    }

    public static void removeGroupFromGroupQueue(final String groupId, final String groupQueueId) {
        GroupQueue groupQueue = getGroupQueue(groupQueueId);
        if (groupQueue.getGroupList().contains(groupId)) {
            List<String> groupList = groupQueue.getGroupList();
            groupList.remove(groupId);
            updateGroupListToGroupQueue(groupList,groupQueueId);
        }
    }

    public static void updateGroupListToGroupQueue(final List<String> groupList, final String groupQueueId) {
    
        Bson filter = Filters.eq(ApiConstant.GROUPQUEUE_GROUP_QUEUE_ID, groupQueueId);
        Bson query = combine(
                set(ApiConstant.GROUPQUEUE_GROUP_LIST, groupList));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_GROUPQUEUES);
    }

    public static void addGroupToMemebersGroupQueue(final String groupId, final List<String> members) {
        for (String member : members) {
            Account account = AccountUtils.getAccount(member, ApiConstant.ACCOUNT_ACCOUNT_ID);
            String groupQueueId = account.getGroupQueueId();
            addGroupToGroupQueue(groupId, groupQueueId);
        }
    }

    public static void removeGroupFromMemebersGroupQueue(final String groupId, final List<String> members) {
        for (String member : members) {
            Account account = AccountUtils.getAccount(member, ApiConstant.ACCOUNT_ACCOUNT_ID);
            String groupQueueId = account.getGroupQueueId();
            removeGroupFromGroupQueue(groupId, groupQueueId);
        }
    }
}
