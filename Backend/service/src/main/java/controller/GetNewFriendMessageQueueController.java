package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.MessageQueueUtils;

import java.util.List;
import java.util.stream.Collectors;

import static utils.AccountUtils.getAccount;

@RestController
public class GetNewFriendMessageQueueController extends BaseController {

    @PostMapping("/getNewFriendMessageQueue")
    public ResponseEntity<GetNewFriendMessageQueueResponse> handle(@RequestBody GetMessageQueueRequest request) {
        logger.info("GetNewFriendMessageQueue: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getMessageQueueId(), ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID);

        Account targetAccout = getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);
        if(!request.getMessageQueueId().equals(targetAccout.getMessageQueueId())){
            ExceptionUtils.invalidProperty("Can only get own's MessageQueue");
        }

        final List<FriendMessage> accountList = MessageQueueUtils
                .getMessageList(request.getMessageQueueId())
                .stream()
                .filter((Message m) -> m.getType().equals(MessageType.FRIEND))
                .map((Message m) -> new FriendMessage()
                        .withAccount(AccountUtils.getAccount(m.getSenderId()))
                        .withMessageId(m.getMessageId()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new GetNewFriendMessageQueueResponse().withMessageList(accountList),HttpStatus.OK);
    }
}
