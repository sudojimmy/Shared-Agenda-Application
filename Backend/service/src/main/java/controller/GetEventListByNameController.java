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

import java.util.ArrayList;

@RestController
public class GetEventListByNameController extends BaseController {

    @PostMapping("/getEventListByName")
    public ResponseEntity<GetEventListByNameResponse> handle(@RequestBody GetEventListByNameRequest request) {
        logger.info("GetEventListByName: " + request);

        assertPropertyValid(request.getEventname(), ApiConstant.EVENT_EVENT_NAME);

        ArrayList<Event> eventList = EventListUtils.getEventListByName(request.getEventname());
        return new ResponseEntity<>(new GetEventListByNameResponse().withEventList(eventList),HttpStatus.OK);
    }
}
