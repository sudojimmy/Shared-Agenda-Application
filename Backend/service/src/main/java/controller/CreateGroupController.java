package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.CreateGroupRequest;
import types.CreateGroupResponse;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.GroupQueueUtils;
import utils.GroupUtils;

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

        String groupId = GroupUtils.createGroupToDatabase(request.getName(),request.getOwnerId(),members);

        GroupQueueUtils.addGroupToMemebersGroupQueue(groupId, members);

        return new ResponseEntity<>(new CreateGroupResponse().withGroupId(groupId),
            HttpStatus.CREATED);
    }
}
