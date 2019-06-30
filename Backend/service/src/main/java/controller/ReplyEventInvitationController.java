package controller;

import constant.ApiConstant;
import org.bson.types.ObjectId;
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
    public ResponseEntity<ReplyEventInvitationResponse> handle(@RequestBody ReplyEventInvitationRequest request) {
        logger.info("ReplyEventInvitation: " + request);

        // Step I: check parameters
        ExceptionUtils.assertPropertyValid(request.getMessageId(), ApiConstant.MESSAGE_MESSAGE_ID);
        ExceptionUtils.assertPropertyValid(request.getStatus(), ApiConstant.REPLY_STATUS);
        //ExceptionUtils.assertPropertyValid(request.getDescription(), ApiConstant.REPLY_DESCRIPTION);

        // Step III: update replyMessage
        EventMessage eventMessage = EventMessageUtils.getEventMessage(request.getMessageId());
        ExceptionUtils.assertDatabaseObjectFound(eventMessage, ApiConstant.MESSAGE_MESSAGE_ID);
        ReplyMessage replyMessage = ReplyMessageUtils.getReplyMessage(eventMessage.getReplyId());
        ExceptionUtils.assertDatabaseObjectFound(replyMessage, ApiConstant.MESSAGE_MESSAGE_ID);

        boolean updated = ReplyMessageUtils.updateReplyMessageInDatabase(
                replyMessage.getReplyId(),
                replyMessage.getSenderId(),
                replyMessage.getReceiverId(),
                request.getStatus(),
                request.getDescription());

        // remove the eventMessage from reply's sender messageQ
        Account replyUser = AccountUtils.getAccount(replyMessage.getSenderId(), ApiConstant.REPLY_SENDER_ID);
        MessageQueueUtils.deleteMessageFromMessageQueue(eventMessage.getMessageId(), replyUser.getMessageQueueId());


        if(request.getStatus().equals(ReplyStatus.ACCEPT)){
            // create Event Object to DB
            String eventId = EventListUtils.createEventToDatabase(eventMessage.getEvent());

            // add event to the invited account
            EventListUtils.addEventIdToCalendar(eventId, replyUser.getCalendarId());

            // add event to the inviting account
            Account inviteUser = AccountUtils.getAccount(replyMessage.getReceiverId(), ApiConstant.REPLY_RECEIVER_ID);
            EventListUtils.addEventIdToCalendar(eventId, inviteUser.getCalendarId());
        }


        // delete EventMessage Obj from DB
        EventMessageUtils.deleteEventMessage(eventMessage.getMessageId());


        // Step IV: create response object
        return new ResponseEntity<>(new ReplyEventInvitationResponse().withReplyId(replyMessage.getReplyId()),
                HttpStatus.CREATED);
    }
}
