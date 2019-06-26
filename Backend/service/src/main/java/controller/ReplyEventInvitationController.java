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
        ExceptionUtils.assertPropertyValid(request.getDescription(), ApiConstant.REPLY_DESCRIPTION);

        // Step II: check restriction (conflict, or naming rules etc.)
        Message message = MessageUtils.getMessage(request.getMessageId());
        ExceptionUtils.assertDatabaseObjectFound(message, ApiConstant.MESSAGE_MESSAGE_ID);

        // Step III: update replyMessage
        EventMessage eventMessage = EventMessageUtils.getEventMessage(request.getMessageId());
        ReplyMessage replyMessage = ReplyMessageUtils.getReplyMessage(eventMessage.getReplyId());

        boolean updated = ReplyMessageUtils.updateReplyMessageInDatabase(
                replyMessage.getReplyId(),
                replyMessage.getSenderId(),
                replyMessage.getReceiverId(),
                request.getStatus(),
                request.getDescription());

        // Step IV: create response object
        return new ResponseEntity<>(new ReplyEventInvitationResponse().withReplyId(replyMessage.getReplyId()),
                HttpStatus.CREATED);
    }
}
