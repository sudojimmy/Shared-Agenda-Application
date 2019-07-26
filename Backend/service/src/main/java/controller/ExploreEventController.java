package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Event;
import types.ExploreEventRequest;
import types.ExploreEventResponse;
import utils.EventListUtils;
import utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

@RestController
public class ExploreEventController extends BaseController {

    @PostMapping("/exploreEvent")
    public ResponseEntity<ExploreEventResponse> handle(@RequestBody ExploreEventRequest request) {
        logger.info("ExploreEvent: " + request);

        ExceptionUtils.assertPropertyValid(request.getKeyword(), ApiConstant.EVENT_NAME_SUBSTRING);
        ExceptionUtils.assertPropertyValid(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        ArrayList<Event> eventList = EventListUtils.getAllEventList();

        // filter permission
        ArrayList<Event> eventListWithPermission = new ArrayList<Event>();
        for (Event event: eventList) {
            if (EventListUtils.checkEventPermission(request.getCallerId(), event)) {
                eventListWithPermission.add(event);
            }
        }

        //filter by keywork
        ArrayList<Event> finalEventList = new ArrayList<Event>();
        for (Event event: eventListWithPermission) {
            String eventname = event.getEventname();

            if (Pattern.compile
                    (Pattern.quote(request.getKeyword()), Pattern.CASE_INSENSITIVE)
                    .matcher(eventname).find()){
                finalEventList.add(event);
            }
        }


        return new ResponseEntity<>(new ExploreEventResponse()
                .withEventList(finalEventList),HttpStatus.OK);
    }
}
