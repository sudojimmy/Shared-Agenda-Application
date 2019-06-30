package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.AccountUtils;
import utils.EventMessageUtils;
import utils.ExceptionUtils;
import utils.MessageUtils;


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
        Account accountSender = AccountUtils.getAccount(request.getSenderId());
        Account account = AccountUtils.getAccount(request.getReceiverId());

        // Step III: write to Database
        CreateEventRequest ER = request.getEvent();

        if(!request.getSenderId().equals(ER.getStarterId())){
            ExceptionUtils.invalidProperty("sendId need equal to starterId to invite");
        }

        // TODO check friendship

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
                ER.getDescription(),
                ER.isPublic()).getMessageId();

        MessageUtils.createMessageToDatabase(messageId, ApiConstant.MESSAGE_TYPE_EVENT);
        String messageQueueId = account.getMessageQueueId();

        MessageUtils.addMessageIdToMessageQueue(messageId, messageQueueId);

        // Step IV: create response object
        return new ResponseEntity<>(new InviteEventResponse().withMessageId(messageId),
                HttpStatus.CREATED);
    }
}
