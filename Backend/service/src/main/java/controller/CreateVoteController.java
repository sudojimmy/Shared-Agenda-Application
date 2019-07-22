package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.EventListUtils;
import utils.ExceptionUtils;
import utils.GroupUtils;
import utils.VoteQueueUtils;
import utils.VoteUtils;


@RestController
public class CreateVoteController extends BaseController {

    @PostMapping("/createVote")
    public ResponseEntity<CreateVoteResponse> handle(@RequestBody CreateVoteRequest request) {
        logger.info("CreateVote: " + request);
        //todo add datetime vote in the future 
        // Step I: check parameters
        ExceptionUtils.assertPropertyValid(request.getVote().getEventId(), ApiConstant.EVENT_EVENT_ID);
        ExceptionUtils.assertPropertyValid(request.getVote().getGroupId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getVote().getLocations(), ApiConstant.EVENT_LOCATION);
        ExceptionUtils.assertPropertyValid(request.getVote().getVoters(), ApiConstant.VOTE_VOTERS);
        // ExceptionUtils.assertPropertyValid(request.getVote().getDatetimes(), ApiConstant.EVENT_DATE);


        // Step II: check restriction (conflict, or naming rules etc.)
        Event event = EventListUtils.getEventListById(request.getVote().getEventId());
        Group group = GroupUtils.getGroup(request.getVote().getGroupId());
        ExceptionUtils.assertDatabaseObjectFound(event, ApiConstant.EVENT_EVENT_ID);
        ExceptionUtils.assertDatabaseObjectFound(group, ApiConstant.GROUP_ID);

        // Step III: write to Database
        EventListUtils.checkEventPermission(request.getAccountId(), event);

        String voteId = VoteUtils.createVoteToDatabase(request.getVote());
        VoteQueueUtils.addVoteToVoteQueue(voteId, group.getVoteQueueId());

        // Step IV: create response object
        return new ResponseEntity<>(new CreateVoteResponse().withVoteId(voteId),
                HttpStatus.CREATED);
    }
}
