package utils;

import constant.ApiConstant;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import store.DataStore;
import types.Group;

import static controller.BaseController.dataStore;

import java.util.List;

import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

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
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID,groupId);
        Group group = dataStore.findOneInCollection(document, DataStore.COLLECTION_GROUPS);
        if (!ownerId.equals(group.getOwnerId())) {
            ExceptionUtils.invalidProperty(ApiConstant.GROUP_OWNER_ID);
        }
    }

    public static String createGroupToDatabase(final String name, final String ownerId,
     final List<String> members) {
        ObjectId groupId = new ObjectId();
        String id = groupId.toString();
        Group p = new Group().withName(name).withGroupId(id)
        .withMembers(members).withOwnerId(ownerId);      
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
