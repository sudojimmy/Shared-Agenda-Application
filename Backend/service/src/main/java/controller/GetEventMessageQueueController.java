package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.EventMessageUtils;
import utils.ExceptionUtils;
import utils.MessageQueueUtils;

import java.util.List;
import java.util.stream.Collectors;

import static utils.AccountUtils.getAccount;

@RestController
public class GetEventMessageQueueController extends BaseController {

    @PostMapping("/getEventMessageQueue")
    public ResponseEntity<GetEventMessageQueueResponse> handle(@RequestBody GetMessageQueueRequest request) {
        logger.info("GetEventMessageQueue: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getMessageQueueId(), ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID);

        Account targetAccout = getAccount(request.getAccountId(),ApiConstant.ACCOUNT_ACCOUNT_ID);
        if(!request.getMessageQueueId().equals(targetAccout.getMessageQueueId())){
            ExceptionUtils.invalidProperty("Can only get own's MessageQueue");
        }

        final List<EventMessage> eventList = MessageQueueUtils
                .getMessageList(request.getMessageQueueId())
                .stream()
                .filter((Message m) -> m.getType().equals(MessageType.EVENT))
                .map(Message::getMessageId)
                .map(EventMessageUtils::getEventMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new GetEventMessageQueueResponse().withMessageList(eventList),HttpStatus.OK);
    }
}
