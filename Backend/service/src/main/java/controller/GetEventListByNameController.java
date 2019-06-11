package controller;

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

        if (request.getEventname() == null || request.getEventname().isEmpty()) {
            logger.error("Invalid event name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ArrayList<Event> eventList = EventListUtils.getEventListByName(request.getEventname());
        return new ResponseEntity<>(new GetEventListByNameResponse().withEventList(eventList),HttpStatus.OK);
    }
}
