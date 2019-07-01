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
public class InviteEventController extends BaseController {

    @PostMapping("/inviteEvent")
    public ResponseEntity<InviteEventResponse> handle(@RequestBody InviteEventRequest request) {
        logger.info("InviteEvent: " + request);

        // Step I: check parameters
        ExceptionUtils.assertPropertyValid(request.getSenderId(), ApiConstant.EVENT_SENDER_ID);
        ExceptionUtils.assertPropertyValid(request.getReceiverId(), ApiConstant.EVENT_RECEIVER_ID);
        ExceptionUtils.assertPropertyValid(request.getEvent(), ApiConstant.EVENT_EVENT);

        // Step II: check restriction (conflict, or naming rules etc.)
        Account accountSender = AccountUtils.getAccount(request.getSenderId(), ApiConstant.EVENT_SENDER_ID);
        Account account = AccountUtils.getAccount(request.getReceiverId(), ApiConstant.EVENT_RECEIVER_ID);

        // Step III: write to Database
        CreateEventRequest ER = request.getEvent();

        if(!request.getSenderId().equals(ER.getStarterId())){
            ExceptionUtils.invalidProperty("sendId need equal to starterId to invite");
        }

        ExceptionUtils.assertFriendship(accountSender, account.getAccountId());

        String replyId = ReplyMessageUtils.createReplyMessageToDatabase(
                MessageType.EVENT,
                request.getReceiverId(),    // The sender/receiver in reply msg is reversed comparing to original msg
                request.getSenderId(),
                ReplyStatus.valueOf("PENDING"),
                "");            // currently not support to add description

        // Note: reply's receiver and sender is opposite way of invitation
        String messageId = EventMessageUtils.createEventMessageToDatabase(
                null,
                ER.getEventname(),
                ER.getStarterId(),
                ER.getType(),
                ER.getStart(),
                ER.getCount(),
                ER.getDate(),
                ER.getLocation(),
                ER.getRepeat(),
                ER.getState(),
                ER.getDescription())
                .getMessageId();

        Message message = new Message()
                .withMessageId(messageId)
                .withType(MessageType.EVENT)
                .withSenderId(request.getSenderId())
                .withReplyId(replyId); // only receiver's msg contains replyId

        Message reply = new Message()
                .withMessageId(replyId)
                .withType(MessageType.RESPONSE)
                .withSenderId(request.getReceiverId());

        String messageQueueId = account.getMessageQueueId();
        String messageQueueIdSender = accountSender.getMessageQueueId();

        // add the Message(eventMessage) to messageQueue
        MessageUtils.addMessageIdToMessageQueue(message, messageQueueId);

        // add the Message(replyMessage) to messageQueue
        MessageUtils.addMessageIdToMessageQueue(reply, messageQueueIdSender);

        // Step IV: create response object
        return new ResponseEntity<>(new InviteEventResponse().withMessageId(messageId),
                HttpStatus.CREATED);
    }
}
