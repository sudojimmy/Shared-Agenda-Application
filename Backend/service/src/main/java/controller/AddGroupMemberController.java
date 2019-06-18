package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.client.model.Filters;

import org.bson.BSON;
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
import utils.ExceptionUtils;
import utils.GroupUtils;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@RestController
public class AddGroupMemberController extends BaseController {

    @PostMapping("/group/addMember")
    public ResponseEntity<AddGroupMemberResponse> handle(@RequestBody AddGroupMemberRequest request) {
        logger.info("AddGroupMember: " + request);

        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getMembers(), ApiConstant.GROUP_MEMBERS);
        ExceptionUtils.assertPropertyValid(request.getOwnerId(), ApiConstant.GROUP_OWNER_ID);

        // todo add permission check
        if (!GroupUtils.validateGroupOwner(request.getGroupId(), request.getOwnerId())) {
            ExceptionUtils.invalidProperty(ApiConstant.GROUP_OWNER_ID);
        }
        if (!GroupUtils.checkGroupExist(request.getGroupId())) {
            ExceptionUtils.invalidProperty(ApiConstant.GROUP_ID);
        }

        List<String> missingMembers = AccountUtils.checkAccountsExist(request.getMembers());
        if (missingMembers.size() > 0) {
            return new ResponseEntity<>(
                    new AddGroupMemberResponse().withGroupId(request.getGroupId()).withMembers(missingMembers),
                    HttpStatus.NOT_FOUND);
        }

        Group group = GroupUtils.getGroup(request.getGroupId());
        ExceptionUtils.assertDatabaseObjectFound(group, ApiConstant.GROUP_ID);
        List<String> existedMembers = group.getMembers();
        // Step III: write to Database
        existedMembers.addAll(addMembers);
        Set<String> set = new HashSet<String>(existedMembers);
        List<String> updateMembers = new ArrayList<String>(set);

        BSON filter = Filters.eq(ApiConstant.GROUP_ID, request.getGroupId());
        Bson query = combine(
            set(ApiConstant.GROUP_MEMBERS, updateMembers));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_GROUPS);

        // Step IV: create response object
        return new ResponseEntity<>(new AddGroupMemberResponse().withGroupId(request.getGroupId()),
            HttpStatus.OK);
    }
}
