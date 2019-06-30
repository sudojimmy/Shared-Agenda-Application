package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.GetMessageQueueRequest;
import types.GetMessageQueueResponse;
import types.Message;
import utils.ExceptionUtils;
import utils.MessageQueueUtils;

import java.util.ArrayList;

import static utils.AccountUtils.getAccount;

@RestController
public class GetMessageQueueController extends BaseController {

    @PostMapping("/getMessageQueue")
    public ResponseEntity<GetMessageQueueResponse> handle(@RequestBody GetMessageQueueRequest request) {
        logger.info("GetMessageQueue: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getMessageQueueId(), ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID);

        Account targetAccout = getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);
        if(!request.getMessageQueueId().equals(targetAccout.getMessageQueueId())){
            ExceptionUtils.invalidProperty("Can only get own's MessageQueue");
        }

        ArrayList<Message> messageList = MessageQueueUtils.getMessageList(request.getMessageQueueId());

        return new ResponseEntity<>(new GetMessageQueueResponse().withMessageList(messageList),HttpStatus.OK);
    }
}
