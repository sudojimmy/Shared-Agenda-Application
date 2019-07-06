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

        // add permit to accountId
        Permission permission = request.getEvent().getPermission();
        if (permission.getType() != PermissionType.ACCOUNT){
            ExceptionUtils.invalidProperty("Permission Type");
        }
        permission.setPermitToId(request.getReceiverId());
        request.getEvent().setPermission(permission);

        ExceptionUtils.assertEventValid(request.getEvent(), false, "");

        // Step II: check restriction (conflict, or naming rules etc.)
        Account sender = AccountUtils.getAccount(request.getSenderId(), ApiConstant.EVENT_SENDER_ID);
        Account receiver = AccountUtils.getAccount(request.getReceiverId(), ApiConstant.EVENT_RECEIVER_ID);
        ExceptionUtils.assertFriendship(sender, receiver.getAccountId());

        Event event = request.getEvent();
        if(!request.getSenderId().equals(event.getStarterId())){
            ExceptionUtils.invalidProperty("sendId need equal to starterId to invite");
        }

        // Note: reply's receiver and sender is opposite way of invitation
        String messageId = EventMessageUtils
                .createEventMessageToDatabase(event)
                .getMessageId();

        MessageQueueUtils.notifyAccounts(sender, receiver, MessageType.EVENT, messageId);

        // Step IV: create response object
        return new ResponseEntity<>(new InviteEventResponse().withMessageId(messageId),
                HttpStatus.CREATED);
    }
}
