package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.*;

import static utils.ExceptionUtils.invalidProperty;

@RestController
public class GetGroupCalendarEventListController extends BaseController {

    @PostMapping("/getGroupCalendarEventList")
    public ResponseEntity<GetGroupCalendarEventListResponse> handle(@RequestBody GetGroupCalendarEventListRequest request) {
        logger.info("GetGroupCalendarEventList: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);

        Group group = GroupUtils.getGroup(request.getGroupId());
        if (!GroupUtils.checkGroupMember(group, request.getAccountId())) {
            invalidProperty("AccountId, only group members can check group Event");
        }
        String calendarId = group.getCalendarId();
        Calendar calendar = CalendarUtils.getCalendar(calendarId);
        ExceptionUtils.assertDatabaseObjectFound(calendar, ApiConstant.CALENDAR_CALENDAR_ID);

        return new ResponseEntity<>(new GetGroupCalendarEventListResponse()
            .withEventList(EventListUtils.getEventListFromCalendar(calendar)),HttpStatus.OK);
    }
}
