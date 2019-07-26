package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.*;


@RestController
public class ReplyFriendInvitationController extends BaseController {

    @PostMapping("/replyFriend")
    public ResponseEntity<ReplyInvitationResponse> handle(@RequestBody ReplyInvitationRequest request) {
        logger.info("ReplyFriendInvitation: " + request);

        // Step I: check parameters
        ExceptionUtils.assertPropertyValid(request.getMessageId(), ApiConstant.MESSAGE_MESSAGE_ID);
        ExceptionUtils.assertPropertyValid(request.getStatus(), ApiConstant.REPLY_STATUS);
        //ExceptionUtils.assertPropertyValid(request.getDescription(), ApiConstant.REPLY_DESCRIPTION);

        Account sender = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID); // who send reply msg
        Message message = MessageQueueUtils.getMessageFromMessageQueueId(request.getMessageId(), sender.getMessageQueueId());

        // Step III: update replyMessage
        ReplyMessage replyMessage = ReplyMessageUtils.getReplyMessage(message.getReplyId());
        ExceptionUtils.assertDatabaseObjectFound(replyMessage, ApiConstant.MESSAGE_MESSAGE_ID);
        replyMessage.setStatus(request.getStatus());

        ReplyMessageUtils.updateReplyMessageInDatabase(
                replyMessage.getReplyId(),
                replyMessage.getSenderId(),
                replyMessage.getReceiverId(),
                request.getStatus(),
                request.getDescription());

        // remove the eventMessage from reply's sender messageQ
        MessageQueueUtils.deleteMessageFromMessageQueue(message.getMessageId(), sender.getMessageQueueId());

        if(request.getStatus().equals(ReplyStatus.ACCEPT)){
            // add friend to queue
            Account receiver = AccountUtils.getAccount(replyMessage.getReceiverId(), ApiConstant.REPLY_SENDER_ID);
            FriendQueueUtils.addFriendToFriendQueue(receiver.getAccountId(), sender.getFriendQueueId());
            FriendQueueUtils.addFriendToFriendQueue(sender.getAccountId(), receiver.getFriendQueueId());
        }
        PushNotificationUtils.getInstance().pushFriendNotification(replyMessage);

        // Step IV: create response object
        return new ResponseEntity<>(new ReplyInvitationResponse().withReplyId(replyMessage.getReplyId()),
                HttpStatus.CREATED);
    }
}
