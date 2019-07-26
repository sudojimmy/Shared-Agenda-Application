package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Event;
import types.GetEventListByNameRequest;
import types.GetEventListByNameResponse;
import utils.EventListUtils;
import utils.ExceptionUtils;

import java.util.ArrayList;

@RestController
public class GetEventListByNameController extends BaseController {

    @PostMapping("/getEventListByName")
    public ResponseEntity<GetEventListByNameResponse> handle(@RequestBody GetEventListByNameRequest request) {
        logger.info("GetEventListByName: " + request);

        ExceptionUtils.assertPropertyValid(request.getEventname(), ApiConstant.EVENT_EVENT_NAME);
        ExceptionUtils.assertPropertyValid(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        ArrayList<Event> eventList = EventListUtils.getEventListByName(request.getEventname());

        ArrayList<Event> finalEventList = new ArrayList<Event>();
        for (Event event: eventList) {
            if (EventListUtils.checkEventPermission(request.getCallerId(), event)) {
                finalEventList.add(event);
            }
        }

        return new ResponseEntity<>(new GetEventListByNameResponse().withEventList(finalEventList),HttpStatus.OK);
    }
}
