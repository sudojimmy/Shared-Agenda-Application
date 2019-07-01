package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Group;
import types.AddGroupMemberRequest;
import types.AddGroupMemberResponse;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.GroupQueueUtils;
import utils.GroupUtils;

@RestController
public class AddGroupMemberController extends BaseController {

    @PostMapping("/group/addMember")
    public ResponseEntity<AddGroupMemberResponse> handle(@RequestBody AddGroupMemberRequest request) {
        logger.info("AddGroupMember: " + request);

        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getMembers(), ApiConstant.GROUP_MEMBERS);
        ExceptionUtils.assertPropertyValid(request.getOwnerId(), ApiConstant.GROUP_OWNER_ID);

        // todo add permission check
        GroupUtils.checkGroupExist(request.getGroupId());
        GroupUtils.validateGroupOwner(request.getGroupId(), request.getOwnerId());

        AccountUtils.checkAccountsExist(request.getMembers());

        Group group = GroupUtils.getGroup(request.getGroupId());
        List<String> members = group.getMembers();
        // Step III: write to Database
        members.addAll(request.getMembers());
        List<String> updateMembers = new ArrayList<>(
            new HashSet<>(members));
        GroupUtils.updateGroupMembers(request.getGroupId(), updateMembers);
        GroupQueueUtils.addGroupToMemebersGroupQueue(request.getGroupId(),members);

        // Step IV: create response object
        return new ResponseEntity<>(new AddGroupMemberResponse().withGroupId(request.getGroupId()),
            HttpStatus.OK);
    }
}
