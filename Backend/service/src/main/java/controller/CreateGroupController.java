package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Group;
import types.CreateGroupRequest;
import types.CreateGroupResponse;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.ExceptionUtils;

@RestController
public class CreateGroupController extends BaseController {

    @PostMapping("/createGroup")
    public ResponseEntity<CreateGroupResponse> handle(@RequestBody CreateGroupRequest request) {
        logger.info("CreateGroup: " + request);

        // Step I: check parameters TODO move to parent class, need a better solution
        ExceptionUtils.assertPropertyValid(request.getName(), ApiConstant.GROUP_NAME);
        ExceptionUtils.assertPropertyValid(request.getOwnerId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getMembers(), ApiConstant.GROUP_MEMBERS);


        // Step II: check restriction (conflict, or naming rules etc.)
        List<String> members = request.getMembers();
        String owner = request.getOwnerId();

        if (!members.contains(owner)) {
            members.add(owner);
        }
        Set<String> set = new HashSet<String>(members);

        members = new ArrayList<String>(set);

        for (String member : members) {
            if (!AccountUtils.checkAccountExist(member)) {
                ExceptionUtils.invalidProperty(ApiConstant.GROUP_MEMBERS);
            }
        }

        // Step III: write to Database
        ObjectId groupId = new ObjectId();
        String id = groupId.toString();
        Group p = new Group().withName(request.getName()).withGroupId(id)
        .withMembers(members).withOwnerId(owner);      
        dataStore.insertToCollection(p, DataStore.COLLECTION_GROUPS);

        // Step IV: create response object
        return new ResponseEntity<>(new CreateGroupResponse().withGroupId(id),
            HttpStatus.CREATED);
    }
}
