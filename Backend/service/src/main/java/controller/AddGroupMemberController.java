package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Group;
import types.AddGroupMemberRequest;
import types.AddGroupMemberResponse;

import constant.ApiConstant;
import utils.AccountUtils;
import utils.GroupUtils;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@RestController
public class AddGroupMemberController extends BaseController {

    @PostMapping("/Group/addMember")
    public ResponseEntity<AddGroupMemberResponse> handle(@RequestBody AddGroupMemberRequest request) {
        logger.info("AddGroupMember: " + request);

        // Step I: check parameters TODO move to parent class, need a better solution
        assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);
        assertPropertyValid(request.getMembers(), ApiConstant.GROUP_MEMBERS);
        assertPropertyValid(request.getOwnerId(), ApiConstant.GROUP_OWNER_ID);

        // todo add permission check
        if (!GroupUtils.validateGroupOwner(request.getGroupId(), request.getOwnerId())){
            invalidProperty(ApiConstant.GROUP_OWNER_ID);
        }
        GroupUtils.checkGroupExist(request.getGroupId());

        List<String> addMembers = request.getMembers();
        List<String> missingMembers = new ArrayList<String>();
        for (String member : addMembers) {
            if (!AccountUtils.checkAccountExist(member)) {
                missingMembers.add(member);                
            }
        }

        if (missingMembers.size()>0) {
            return new ResponseEntity<>(new AddGroupMemberResponse().withGroupId(request.getGroupId())
            .withMembers(missingMembers),
            HttpStatus.NOT_FOUND);
        }

        Group group = GroupUtils.getGroup(request.getGroupId());
        assertDatabaseObjectFound(group, ApiConstant.GROUP_ID);
        List<String> existedMembers = group.getMembers();
        // Step III: write to Database
        existedMembers.addAll(addMembers);
        Set<String> set = new HashSet<String>(existedMembers);
        List<String> updateMembers = new ArrayList<String>(set); 

        Bson filter = Filters.eq(ApiConstant.GROUP_ID, request.getGroupId());
        Bson query = combine(
            set(ApiConstant.GROUP_MEMBERS, updateMembers));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_GROUPS);

        // Step IV: create response object
        return new ResponseEntity<>(new AddGroupMemberResponse().withGroupId(request.getGroupId()),
            HttpStatus.OK);
    }
}
