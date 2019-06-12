package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
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

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@RestController
public class AddGroupMemberController extends BaseController {

    @PostMapping("/group/addMember")
    public ResponseEntity<AddGroupMemberResponse> handle(@RequestBody AddGroupMemberRequest request) {
        logger.info("AddGroupMember: " + request);

        // Step I: check parameters TODO move to parent class, need a better solution
        if (request.getGroupId() == null || request.getGroupId().isEmpty()) {
            logger.error("Invalid group!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getMembers() == null || request.getMembers().isEmpty()) {
            logger.error("Invalid members!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // todo add permission check
        // if (request.getOwnerid()== null || request.getOwnerid().isEmpty()) {
        //     logger.error("Invalid ownerid");
        //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // }

        // Step II: check restriction (conflict, or naming rules etc.)
        // check if group exist

        List<String> addMembers = request.getMembers();
        List<String> missingMembers = new ArrayList<String>();
        for (String member : addMembers) {
            Document document = new Document();
            document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, member);
            if (!dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS)) {
                missingMembers.add(member);
            }
        }

        if (missingMembers.size()>0) {
            return new ResponseEntity<>(new AddGroupMemberResponse().withGroupId(request.getGroupId())
            .withMembers(missingMembers),
            HttpStatus.NOT_FOUND);
        }

        Document doc = new Document();
        doc.put(ApiConstant.GROUP_ID, request.getGroupId());
        if (!dataStore.existInCollection(doc, DataStore.COLLECTION_GROUPS)) {
            logger.error("groupId does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Group group = dataStore.findOneInCollection(doc, DataStore.COLLECTION_GROUPS);
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
