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

        String replyId = ReplyMessageUtils.createReplyMessageToDatabase(
                MessageType.FRIEND,
                request.getReceiverId(),    // The sender/receiver in reply msg is reversed comparing to original msg
                request.getSenderId(),
                ReplyStatus.valueOf("PENDING"),
                "");            // currently not support to add description

        Message message = new Message()
                .withMessageId(new ObjectId().toString())
                .withType(MessageType.FRIEND)
                .withSenderId(request.getSenderId())
                .withReplyId(replyId); // only receiver's msg contains replyId

        Message reply = new Message()
                .withMessageId(replyId)
                .withType(MessageType.RESPONSE)
                .withSenderId(request.getReceiverId());

        String messageQueueId = receiver.getMessageQueueId();
        String messageQueueIdSender = sender.getMessageQueueId();

        // add the Message(eventMessage) to messageQueue
        MessageUtils.addMessageIdToMessageQueue(message, messageQueueId);

        // add the Message(replyMessage) to messageQueue
        MessageUtils.addMessageIdToMessageQueue(reply, messageQueueIdSender);

        return new ResponseEntity<>(new FriendInvitationResponse().withMessageId(message.getMessageId()),
        HttpStatus.OK);
    }
}
