package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.EventMessage;
import types.GetEventMessageRequest;
import types.GetEventMessageResponse;
import utils.ExceptionUtils;
import utils.EventMessageUtils;

@RestController
public class GetEventMessageController extends BaseController {

    @PostMapping("/getEventMessage")
    public ResponseEntity<GetEventMessageResponse> handle(@RequestBody GetEventMessageRequest request) {
        logger.info("getEventMessage: " + request);

        ExceptionUtils.assertPropertyValid(request.getMessageId(), ApiConstant.MESSAGE_MESSAGE_ID);

        EventMessageUtils.checkEventMessageExist(request.getMessageId());

        EventMessage eventMessage = EventMessageUtils.getEventMessage(request.getMessageId());

        return new ResponseEntity<>(new GetEventMessageResponse()
                .withEventMessage(eventMessage), HttpStatus.OK);
    }
}
