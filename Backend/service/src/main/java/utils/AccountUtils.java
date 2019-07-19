package utils;

import constant.ApiConstant;
import org.bson.Document;
import store.DataStore;
import types.Account;

import java.util.ArrayList;
import java.util.List;

import static controller.BaseController.dataStore;

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
        Account account = getAccount(accountId);
        ExceptionUtils.assertDatabaseObjectFound(account, propType);
        return account;
    }

    public static Account getAccount(final String accountId) {
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, accountId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_ACCOUNTS);
    }

    public static ArrayList<Account> getAllAccountList() {

        return new ArrayList<>(dataStore.findAllCollection(DataStore.COLLECTION_ACCOUNTS));
    }
}
