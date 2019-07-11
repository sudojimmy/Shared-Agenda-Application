package utils;

import static controller.BaseController.dataStore;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import constant.ApiConstant;
import store.DataStore;
import types.Account;

public class AccountUtils {
    // @return true if account already in DataBase and false otherwise
    public static boolean checkAccountExist(final String accountId) {
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, accountId);
        return dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS);
    }

    public static void checkAccountsExist(final List<String> accountIdList) {
        List<String> missingMembers = new ArrayList<String>();
        for (String member : accountIdList) {
            if (!checkAccountExist(member)) {
                missingMembers.add(member);                
            }
        }
        if (missingMembers.size()>0) {
            ExceptionUtils.objectNotFound(ApiConstant.ACCOUNT_ACCOUNT_ID);
        }
    }

    public static Account getAccount(final String accountId, final String propType) {
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, accountId);
        Account account = dataStore.findOneInCollection(document, DataStore.COLLECTION_ACCOUNTS);
        ExceptionUtils.assertDatabaseObjectFound(account, propType);
        return account;
    }

    public static ArrayList<Account> getAllAccountList() {

        return new ArrayList<>(dataStore.findAllCollection(DataStore.COLLECTION_ACCOUNTS));
    }
}
