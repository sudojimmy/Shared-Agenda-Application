package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.Group;

import java.util.List;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class GroupUtils {
    // @return true if Group already in DataBase and false otherwise
    public static void checkGroupExist(final String groupId) {
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID, groupId);
        if(!dataStore.existInCollection(document, DataStore.COLLECTION_GROUPS)) {
            ExceptionUtils.invalidProperty(ApiConstant.GROUP_ID);
        }
    }

    public static Group getGroup(final String groupId) {
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID,groupId);
        Group group = dataStore.findOneInCollection(document, DataStore.COLLECTION_GROUPS);
        ExceptionUtils.assertDatabaseObjectFound(group, ApiConstant.GROUP_ID);
        return group;
    }

    public static void validateGroupOwner(final String groupId, final String ownerId){
        Group group = getGroup(groupId);
        if (!ownerId.equals(group.getOwnerId())) {
            ExceptionUtils.invalidProperty(ApiConstant.GROUP_OWNER_ID);
        }
    }

    public static boolean checkGroupMember(final String groupId, final String accountId){
        Group group = getGroup(groupId);
        return checkGroupMember(group, accountId);
    }

    public static boolean checkGroupMember(final Group group, final String accountId){
        List<String> groupList = group.getMembers();

        for(String groupMember: groupList){
            if (groupMember.equals(accountId)) {
                return true;
            }
        }

        return false;
    }

    public static String createGroupToDatabase(final String name,
                                               final String description,
                                               final String ownerId,
                                               final List<String> members,
                                               final  String calendarId,
                                               final String voteQueueId) {
        ObjectId groupId = new ObjectId();
        String id = groupId.toString();
        Group p = new Group()
                .withName(name)
                .withDescription(description)
                .withGroupId(id)
                .withMembers(members)
                .withOwnerId(ownerId)
                .withCalendarId(calendarId)
                .withVoteQueueId(voteQueueId);
        dataStore.insertToCollection(p, DataStore.COLLECTION_GROUPS);
        return id;
    }

    public static void updateGroupMembers(final String groupId, final List<String> members) {
        Group group = getGroup(groupId);
        ExceptionUtils.assertDatabaseObjectFound(group, ApiConstant.GROUP_ID);

        Bson filter = Filters.eq(ApiConstant.GROUP_ID, groupId);
        Bson query = combine(
            set(ApiConstant.GROUP_MEMBERS, members));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_GROUPS);
    }


}
