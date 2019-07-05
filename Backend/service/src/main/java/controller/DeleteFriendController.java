package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;

import constant.ApiConstant;
import utils.*;

@RestController
public class DeleteFriendController extends BaseController {

    @PostMapping("/deleteFriend")
    public ResponseEntity<DeleteFriendResponse> handle(@RequestBody DeleteFriendRequest request) {
        logger.info("DeleteFriend: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getFriendId(), ApiConstant.FRIEND_FRIEND_ID);

        if (request.getAccountId().equals(request.getFriendId())) {
            String errMsg = "account id and friend id";
            ExceptionUtils.invalidProperty(errMsg);
        }

        Account account = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertFriendship(account, request.getFriendId());
        Account friend = AccountUtils.getAccount(request.getFriendId(),ApiConstant.FRIEND_FRIEND_ID);
        ExceptionUtils.assertFriendship(friend, account.getAccountId());

        FriendQueueUtils.removeFriendToFriendQueue(friend.getAccountId(), account.getFriendQueueId());
        FriendQueueUtils.removeFriendToFriendQueue(account.getAccountId(), friend.getFriendQueueId());

        return new ResponseEntity<>(new DeleteFriendResponse().withFriendId(friend.getAccountId()),
                HttpStatus.OK);
    }
}
