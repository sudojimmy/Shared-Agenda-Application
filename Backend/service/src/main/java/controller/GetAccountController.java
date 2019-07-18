package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.GetAccountRequest;
import types.GetAccountResponse;
import utils.AccountUtils;
import utils.ExceptionUtils;

@RestController
public class GetAccountController extends BaseController {

    @PostMapping("/getAccount")
    public ResponseEntity<GetAccountResponse> handle(@RequestBody GetAccountRequest request) {
        logger.info("GetAccount: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        Account account = AccountUtils.getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);

        return new ResponseEntity<>(new GetAccountResponse()
                .withAccountId(account.getAccountId())
                .withCalendarId(account.getCalendarId())
                .withDescription(account.getDescription())
                .withNickname(account.getNickname())
                .withProfileImageUrl(account.getProfileImageUrl())
                .withMessageQueueId(account.getMessageQueueId())
                .withFriendQueueId(account.getFriendQueueId())
                .withGroupQueueId(account.getGroupQueueId()),
                HttpStatus.OK);
    }
}
