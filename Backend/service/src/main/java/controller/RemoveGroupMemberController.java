package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Group;
import types.RemoveGroupMemberRequest;
import types.RemoveGroupMemberResponse;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.GroupQueueUtils;
import utils.GroupUtils;

@RestController
public class RemoveGroupMemberController extends BaseController {

    @PostMapping("/group/removeMember")
    public ResponseEntity<RemoveGroupMemberResponse> handle(@RequestBody RemoveGroupMemberRequest request, HttpServletRequest servletRequest) {
        logger.info("RemoveGroupMember: " + request);
        // Todo add account check 
        String sessionId = getSessionAccountId(servletRequest);
        System.out.println(sessionId);

        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getMembers(), ApiConstant.GROUP_MEMBERS);
        ExceptionUtils.assertPropertyValid(request.getOwnerId(), ApiConstant.GROUP_OWNER_ID);

        GroupUtils.validateGroupOwner(request.getGroupId(), request.getOwnerId());
        GroupUtils.checkGroupExist(request.getGroupId());

        List<String> members = request.getMembers();
        AccountUtils.checkAccountsExist(members);

        Group group = GroupUtils.getGroup(request.getGroupId());
        List<String> existedMembers = group.getMembers();

        existedMembers.removeAll(members);

        List<String> updateMembers = new ArrayList<>(
            new HashSet<>(existedMembers));

        GroupUtils.updateGroupMembers(group.getGroupId(), updateMembers);

        GroupQueueUtils.removeGroupFromMemebersGroupQueue(group.getGroupId(), members);

        return new ResponseEntity<>(new RemoveGroupMemberResponse().withGroupId(request.getGroupId()),
            HttpStatus.OK);
    }
}
