package utils;

import constant.ApiConstant;
import org.bson.Document;
import store.DataStore;
import types.Group;

import static controller.BaseController.dataStore;

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
}
