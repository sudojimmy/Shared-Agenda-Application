package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.GetFriendQueueRequest;
import types.GetFriendQueueResponse;
import utils.ExceptionUtils;
import utils.FriendQueueUtils;

import java.util.ArrayList;

import static utils.AccountUtils.getAccount;

@RestController
public class GetFriendQueueController extends BaseController {

    @PostMapping("/getFriendQueue")
    public ResponseEntity<GetFriendQueueResponse> handle(@RequestBody GetFriendQueueRequest request) {
        logger.info("GetFriendQueue: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        Account account = getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);

        ArrayList<String> friendList = FriendQueueUtils.getFriendList(account.getFriendQueueId());

        return new ResponseEntity<>(new GetFriendQueueResponse().withFriendList(friendList),HttpStatus.OK);
    }
}
