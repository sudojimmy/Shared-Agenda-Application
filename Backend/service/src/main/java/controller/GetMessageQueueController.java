package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.GetMessageQueueRequest;
import types.GetMessageQueueResponse;
import types.Message;
import utils.MessageQueueUtils;

import java.util.ArrayList;

@RestController
public class GetMessageQueueController extends BaseController {

    @PostMapping("/getMessageQueue")
    public ResponseEntity<GetMessageQueueResponse> handle(@RequestBody GetMessageQueueRequest request) {
        logger.info("GetMessageQueue: " + request);

        assertPropertyValid(request.getMessageQueueId(), ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID);

        ArrayList<Message> messageList = MessageQueueUtils.getMessageList(request.getMessageQueueId());

        return new ResponseEntity<>(new GetMessageQueueResponse().withMessageList(messageList),HttpStatus.OK);
    }
}
