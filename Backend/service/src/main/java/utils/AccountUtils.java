package utils;

import constant.ApiConstant;
import org.bson.Document;
import store.DataStore;
import types.Account;

import static controller.BaseController.dataStore;

public class AccountUtils {
    // @return true if account already in DataBase and false otherwise
    public static boolean checkAccountExist(final String accountId) {
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, accountId);
        return dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS);
    }

    public static Account getAccount(final String accountId) {
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, accountId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_ACCOUNTS);
    }
}
