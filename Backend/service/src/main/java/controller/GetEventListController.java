package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Event;
import types.GetEventListRequest;
import types.GetEventListResponse;
import utils.EventListUtils;

import java.util.ArrayList;

@RestController
public class GetEventListController extends BaseController {

    @PostMapping("/getEventList")
    public ResponseEntity<GetEventListResponse> handle(@RequestBody GetEventListRequest request) {
        logger.info("GetEventList: " + request);

        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid event name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ArrayList<Event> eventList = EventListUtils.getEventListByName(request.getEventname());
        return new ResponseEntity<>(new GetEventListResponse().withEventList(eventList),HttpStatus.OK);
    }
}
