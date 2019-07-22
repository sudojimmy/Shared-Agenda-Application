package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.ExploreAccountRequest;
import types.ExploreAccountResponse;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.FriendQueueUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

@RestController
public class ExploreAccountController extends BaseController {

    @PostMapping("/exploreAccount")
    public ResponseEntity<ExploreAccountResponse> handle(@RequestBody ExploreAccountRequest request) {
        logger.info("ExploreAccount: " + request);

        ExceptionUtils.assertPropertyValid(request.getKeyword(), ApiConstant.ACCOUNT_NAME_SUBSTRING);
        Account callerAccount = AccountUtils.getAccount(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        ArrayList<Account> accountList = AccountUtils.getAllAccountList();

        ArrayList<ComparableAccount> comparableAccountList = new ArrayList<>();

        String matchString = request.getKeyword();

        //filter by keywork
        for (Account account: accountList) {
            String accountId = account.getAccountId();
            String accountname = account.getNickname();

            ComparableAccount comparableAccount;

            if (accountId.equals(matchString)) {
                // check ID fully match
                comparableAccount = new ComparableAccount(account, 1);
            } else if (accountname.equals(matchString)) {
                // check nickname fully match
                comparableAccount = new ComparableAccount(account, 2);
            } else {
                int diffLengthId = -1;
                int diffLengthName = -1;
                int diffLength = -1;

                if (Pattern.compile
                        (Pattern.quote(matchString), Pattern.CASE_INSENSITIVE)
                        .matcher(accountId).find()) {

                    diffLengthId = accountId.length() - matchString.length();

                }

                if (Pattern.compile
                        (Pattern.quote(matchString), Pattern.CASE_INSENSITIVE)
                        .matcher(accountname).find()) {

                    diffLengthName = accountname.length() - matchString.length();

                }

                if ((diffLengthId != -1) && (diffLengthName != -1)){
                    diffLength = Math.min(diffLengthId, diffLengthName);
                } else if (diffLengthId != -1) {
                    diffLength = diffLengthId;
                } else if (diffLengthName != 1){
                    diffLength = diffLengthName;
                }

                if (diffLength != -1){
                    comparableAccount = new ComparableAccount(account, diffLength + 2);
                } else {
                    comparableAccount = null;
                }

            }

            if (comparableAccount != null){
                comparableAccountList.add(comparableAccount);
            }
        }

        Collections.sort(comparableAccountList);

        ArrayList<Account> withFriendAccountList = new ArrayList<Account>();

        for(ComparableAccount comparableAccount: comparableAccountList){
            withFriendAccountList.add(comparableAccount.account);
        }

        // filer out existed friends

        ArrayList<Account> finalAccountList = new ArrayList<Account>();

        ArrayList<String> withFriendAccountIdList = new ArrayList<String>();
        for(Account account: withFriendAccountList){
            withFriendAccountIdList.add(account.getAccountId());
        }

        String friendQueueId = callerAccount.getFriendQueueId();
        ArrayList<String> friendList = FriendQueueUtils.getFriendList(friendQueueId);



        // filter the caller
        friendList.add(callerAccount.getAccountId());


        for(String accountId: withFriendAccountIdList){
            if (!friendList.contains(accountId)) {
                Account account = AccountUtils.getAccount(accountId, ApiConstant.ACCOUNT_ACCOUNT_ID);
                finalAccountList.add(account);
            }
        }


        return new ResponseEntity<>(new ExploreAccountResponse()
                .withAccountList(finalAccountList),HttpStatus.OK);
    }
}

