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
public class ReplyEventInvitationController extends BaseController {

    @PostMapping("/replyEvent")
    public ResponseEntity<ReplyInvitationResponse> handle(@RequestBody ReplyInvitationRequest request) {
        logger.info("ReplyEventInvitation: " + request);

        // Step I: check parameters
        ExceptionUtils.assertPropertyValid(request.getMessageId(), ApiConstant.MESSAGE_MESSAGE_ID);
        ExceptionUtils.assertPropertyValid(request.getStatus(), ApiConstant.REPLY_STATUS);
        //ExceptionUtils.assertPropertyValid(request.getDescription(), ApiConstant.REPLY_DESCRIPTION);

        Account account = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        Message message = MessageQueueUtils.getMessageFromMessageQueueId(request.getMessageId(), account.getMessageQueueId());

        // Step III: update replyMessage
        EventMessage eventMessage = EventMessageUtils.getEventMessage(request.getMessageId());
        ExceptionUtils.assertDatabaseObjectFound(eventMessage, ApiConstant.MESSAGE_MESSAGE_ID);
        ReplyMessage replyMessage = ReplyMessageUtils.getReplyMessage(message.getReplyId());
        ExceptionUtils.assertDatabaseObjectFound(replyMessage, ApiConstant.MESSAGE_MESSAGE_ID);
        replyMessage.setStatus(request.getStatus()); // update replyMessage for future reference

        ReplyMessageUtils.updateReplyMessageInDatabase(
                replyMessage.getReplyId(),
                replyMessage.getSenderId(),
                replyMessage.getReceiverId(),
                request.getStatus(),
                request.getDescription());

        // remove the eventMessage from reply's sender messageQ
        MessageQueueUtils.deleteMessageFromMessageQueue(eventMessage.getMessageId(), account.getMessageQueueId());


        if(request.getStatus().equals(ReplyStatus.ACCEPT)){
            // create Event Object to DB
            String eventId = EventListUtils.createEventToDatabase(eventMessage.getEvent());

            // add event to the invited account
            EventListUtils.addEventIdToCalendar(eventId, account.getCalendarId());

            // add event to the inviting account
            Account inviteUser = AccountUtils.getAccount(replyMessage.getReceiverId(), ApiConstant.REPLY_RECEIVER_ID);
            EventListUtils.addEventIdToCalendar(eventId, inviteUser.getCalendarId());
        }


        // delete EventMessage Obj from DB
        EventMessageUtils.deleteEventMessage(eventMessage.getMessageId());
        PushNotificationUtils.getInstance()
                .pushEventNotification(replyMessage, eventMessage.getEvent().getEventname());


        // Step IV: create response object
        return new ResponseEntity<>(new ReplyInvitationResponse().withReplyId(replyMessage.getReplyId()),
                HttpStatus.CREATED);
    }
}
