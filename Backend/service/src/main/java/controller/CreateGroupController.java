package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.CreateGroupRequest;
import types.CreateGroupResponse;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
public class CreateGroupController extends BaseController {

    @PostMapping("/createGroup")
    public ResponseEntity<CreateGroupResponse> handle(@RequestBody CreateGroupRequest request) {
        logger.info("CreateGroup: " + request);

        ExceptionUtils.assertPropertyValid(request.getName(), ApiConstant.GROUP_NAME);
        ExceptionUtils.assertPropertyValid(request.getOwnerId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getMembers(), ApiConstant.GROUP_MEMBERS);

        List<String> createMembers = request.getMembers();
        String owner = request.getOwnerId();

        if (!createMembers.contains(owner)) {
            createMembers.add(owner);
        }

        List<String> members = new ArrayList<>(
            new HashSet<>(createMembers));

        AccountUtils.checkAccountsExist(members);

        String calendarId = CalendarUtils.createCalendarToDatabase().getCalendarId();
        String voteQueueId = VoteQueueUtils.createVoteQueueToDatabase().getVoteQueueId();
        String groupId = GroupUtils.createGroupToDatabase(
                request.getName(),
                request.getDescription(),
                request.getOwnerId(),
                members,
                calendarId,
                voteQueueId);

        GroupQueueUtils.addGroupToMemebersGroupQueue(groupId, members);

        return new ResponseEntity<>(new CreateGroupResponse()
                .withGroupId(groupId)
                .withCalendarId(calendarId)
                .withVoteQueueId(voteQueueId),
            HttpStatus.CREATED);
    }
}
