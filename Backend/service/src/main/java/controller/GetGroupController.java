package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.Group;
import types.GetGroupRequest;
import types.GetGroupResponse;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.GroupUtils;

import java.util.ArrayList;

@RestController
public class GetGroupController extends BaseController {

    @PostMapping("/getGroup")
    public ResponseEntity<GetGroupResponse> handle(@RequestBody GetGroupRequest request) {
        logger.info("getGroup: " + request);

        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);

        Group group = GroupUtils.getGroup(request.getGroupId());

        ArrayList<Account> memberList = new ArrayList<Account>();

        for(String accountId: group.getMembers()) {
            Account account = AccountUtils.getAccount(accountId, ApiConstant.ACCOUNT_ACCOUNT_ID);
            memberList.add(account);
        }

        return new ResponseEntity<>(new GetGroupResponse()
                .withGroupId(group.getGroupId())
                .withName(group.getName())
                .withDescription(group.getDescription())
                .withMembers(memberList)
                .withOwnerId(group.getOwnerId())
                .withCalendarId(group.getCalendarId())
                .withVoteQueueId(group.getVoteQueueId()),
            HttpStatus.OK);
    }
}
