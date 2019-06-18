package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Group;
import types.GetGroupRequest;
import types.GetGroupResponse;
import utils.ExceptionUtils;
import utils.GroupUtils;

@RestController
public class GetGroupController extends BaseController {

    @PostMapping("/getGroup")
    public ResponseEntity<GetGroupResponse> handle(@RequestBody GetGroupRequest request) {
        logger.info("getGroup: " + request);

        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);

        if (!GroupUtils.checkGroupExist(request.getGroupId())) {
            ExceptionUtils.invalidProperty(ApiConstant.GROUP_ID);
        }

        Group group = GroupUtils.getGroup(request.getGroupId());
        ExceptionUtils.assertDatabaseObjectFound(group, ApiConstant.GROUP_ID);

        return new ResponseEntity<>(new GetGroupResponse()
            .withGroupId(group.getGroupId())
            .withName(group.getName())
            .withDescription(group.getDescription())
            .withMembers(group.getMembers())
            .withOwnerId(group.getOwnerId()),
            HttpStatus.OK);
    }
}
