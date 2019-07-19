package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.AccountUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;
import utils.GroupUtils;


@RestController
public class CreateEventController extends BaseController {

    @PostMapping("/createEvent")
    public ResponseEntity<CreateEventResponse> handle(@RequestBody CreateEventRequest request) {
        logger.info("CreateEvent: " + request);

        // Step I: check parameters
        ExceptionUtils.assertPropsEqual(
                request.getEvent().getStarterId(),
                request.getCallerId(),
                ApiConstant.EVENT_STARTER_ID,
                ApiConstant.REPEAT_CALLER_ID);
        ExceptionUtils.assertEventValid(
                request.getEvent(),
                false,
                request.getCallerId());


        // Step II: check restriction (conflict, or naming rules etc.)
        Account account = AccountUtils.getAccount(request.getCallerId(), ApiConstant.EVENT_STARTER_ID);

        // Step III: write to Database
        String eventId = EventListUtils.createEventToDatabase(request.getEvent());

        Permission permission = request.getEvent().getPermission();
        if (permission.getType().equals(PermissionType.GROUP)) {
            String groupId = permission.getPermitToId();
            Group group = GroupUtils.getGroup(groupId);
            EventListUtils.addEventIdToCalendar(eventId, group.getCalendarId());
        }

        EventListUtils.addEventIdToCalendar(eventId, account.getCalendarId());

        // Step IV: create response object
        return new ResponseEntity<>(new CreateEventResponse().withEventId(eventId),
                HttpStatus.CREATED);
    }
}
