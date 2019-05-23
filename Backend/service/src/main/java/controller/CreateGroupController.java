package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
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

@RestController
public class CreateGroupController extends BaseController {

    @PostMapping("/createGroup")
    public ResponseEntity<CreateGroupResponse> handle(@RequestBody CreateGroupRequest request) {
        logger.info("CreateGroup: " + request);

        // Step I: check parameters TODO move to parent class, need a better solution
        if (request.getName() == null || request.getName().isEmpty()) {
            logger.error("Invalid name!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getMembers() == null || request.getMembers().isEmpty()) {
            logger.error("Invalid members!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.getOwnerid()== null || request.getOwnerid().isEmpty()) {
            logger.error("Invalid ownerid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Step II: check restriction (conflict, or naming rules etc.)
        List<String> members = request.getMembers();
        String owner = request.getOwnerid();

        if (!members.contains(owner)) {
            members.add(owner);
        }
        Set<String> set = new HashSet<String>(members);

        members = new ArrayList<String>(set);

        for (String member : members) {
            Document document = new Document();
            document.put("accountId", member);
            if (!dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS)) {
                logger.error("memberId is not Existed!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        // Step III: write to Database
        ObjectId groupId = new ObjectId();
        String id = groupId.toString();
        Group p = new Group().withName(request.getName()).with_id(id)
        .withMembers(members);      
        dataStore.insertToCollection(p, DataStore.COLLECTION_GROUPS);

        // Step IV: create response object
        return new ResponseEntity<>(new CreateGroupResponse().withGroupId(id),
            HttpStatus.OK);
    }
}
