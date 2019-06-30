package controller;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.MessageUtils;
import utils.ExceptionUtils;
import utils.FriendQueueUtils;

@RestController
public class FriendInvitationController extends BaseController {

    @PostMapping("/inviteFriend")
    public ResponseEntity<FriendInvitationResponse> handle(@RequestBody FriendInvitationRequest request) {
        logger.info("InviteFriend: " + request);

        ExceptionUtils.assertPropertyValid(request.getSender(), ApiConstant.INVITATION_SENDER);
        ExceptionUtils.assertPropertyValid(request.getReceiver(), ApiConstant.INVITATION_RECEIVER);

        if (request.getSender().equals(request.getReceiver())) {
            String errMsg = "sender id and receiver id";
            ExceptionUtils.invalidProperty(errMsg);
        }

        Account sender = AccountUtils.getAccount(request.getSender(), ApiConstant.INVITATION_SENDER);
        Account receiver = AccountUtils.getAccount(request.getReceiver(),ApiConstant.INVITATION_RECEIVER);

        ReplyStatus replyStatus = ReplyStatus.valueOf("PENDING");
        String replyId = new ObjectId().toString();
//        Message message = new Message().withMessageId()
        if (request.getStatus().equals(ApiConstant.INVITATION_STATUS_ACCEPTED)) {
            FriendQueueUtils.addFriendToFriendQueue(sender.getAccountId(), receiver.getFriendQueueId());
            FriendQueueUtils.addFriendToFriendQueue(receiver.getAccountId(), sender.getFriendQueueId());
        } 
        MessageUtils.generateMessageToMessageQueue(request.getStatus(),receiver.getMessageQueueId(), request.getSender());



        return new ResponseEntity<>(new FriendInvitationResponse(),
        HttpStatus.OK);
    }
}
