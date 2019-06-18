package utils;

import constant.ApiConstant;

import org.bson.Document;
import org.bson.conversions.Bson;

import store.DataStore;
import types.Group;

import static controller.BaseController.dataStore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class GroupUtils {
    // @return true if Group already in DataBase and false otherwise
    public static boolean checkGroupExist(final String groupId) {
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID, groupId);
        return dataStore.existInCollection(document, DataStore.COLLECTION_GROUPS);
    }

    public static Group getGroup(final String groupId) {
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID,groupId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_GROUPS);
    }

    public static boolean validateGroupOwner(final String groupId, final String ownerId){
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID,groupId);
        Group group = dataStore.findOneInCollection(document, DataStore.COLLECTION_GROUPS);
        return ownerId.equals(group.getOwnerId());
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
