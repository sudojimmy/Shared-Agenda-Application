package controller;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;

import constant.ApiConstant;
import utils.*;

@RestController
public class FriendInvitationController extends BaseController {

    @PostMapping("/inviteFriend")
    public ResponseEntity<FriendInvitationResponse> handle(@RequestBody FriendInvitationRequest request) {
        logger.info("InviteFriend: " + request);

        ExceptionUtils.assertPropertyValid(request.getSenderId(), ApiConstant.INVITATION_SENDER);
        ExceptionUtils.assertPropertyValid(request.getReceiverId(), ApiConstant.INVITATION_RECEIVER);

        if (request.getSenderId().equals(request.getReceiverId())) {
            String errMsg = "sender id and receiver id";
            ExceptionUtils.invalidProperty(errMsg);
        }

        Account sender = AccountUtils.getAccount(request.getSenderId(), ApiConstant.INVITATION_SENDER);
        ExceptionUtils.assertNoFriendship(sender, request.getReceiverId());
        Account receiver = AccountUtils.getAccount(request.getReceiverId(),ApiConstant.INVITATION_RECEIVER);
        ExceptionUtils.assertNoPendingFriendInvitation(receiver, sender.getAccountId());

        final String messageId = new ObjectId().toString();
        MessageQueueUtils.notifyAccounts(sender, receiver, MessageType.FRIEND, messageId);

        return new ResponseEntity<>(new FriendInvitationResponse().withMessageId(messageId),
                HttpStatus.OK);
    }
}
