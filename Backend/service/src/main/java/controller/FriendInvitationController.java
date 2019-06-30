package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.FriendInvitationResponse;
import types.Account;
import types.FriendInvitationRequest;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.MessageUtils;
import utils.ExceptionUtils;
import utils.FriendQueueUtils;

@RestController
public class FriendInvitationController extends BaseController {

    @PostMapping("/friendInvitation")
    public ResponseEntity<FriendInvitationResponse> handle(@RequestBody FriendInvitationRequest request) {
        logger.info("FriendInvitation: " + request);

        ExceptionUtils.assertPropertyValid(request.getSender(), ApiConstant.INVITATION_SENDER);
        ExceptionUtils.assertPropertyValid(request.getReceiver(), ApiConstant.INVITATION_RECEIVER);
        ExceptionUtils.assertPropertyValid(request.getStatus(), ApiConstant.INVITATION_STATUS);

        if (!(request.getStatus().equals(ApiConstant.INVITATION_STATUS_ACCEPTED) ||
        request.getStatus().equals(ApiConstant.INVITATION_STATUS_ACTIVE) ||
        request.getStatus().equals(ApiConstant.INVITATION_STATUS_DECLINED))
        ) {
            ExceptionUtils.invalidProperty(ApiConstant.INVITATION_STATUS);
        }
        
        if (request.getSender().equals(request.getReceiver())) {
            String errMsg = "sender id and receiver id";
            ExceptionUtils.invalidProperty(errMsg);
        }

        Account sender = AccountUtils.getAccount(request.getSender(), ApiConstant.INVITATION_SENDER);

        Account receiver = AccountUtils.getAccount(request.getReceiver(),ApiConstant.INVITATION_RECEIVER);

        if (request.getStatus().equals(ApiConstant.INVITATION_STATUS_ACCEPTED)) {
            FriendQueueUtils.addFriendToFriendQueue(sender.getAccountId(), receiver.getFriendQueueId());
            FriendQueueUtils.addFriendToFriendQueue(receiver.getAccountId(), sender.getFriendQueueId());
        } 
        MessageUtils.generateMessageToMessageQueue(request.getStatus(),receiver.getMessageQueueId());



        return new ResponseEntity<>(new FriendInvitationResponse(),
        HttpStatus.OK);
    }
}
