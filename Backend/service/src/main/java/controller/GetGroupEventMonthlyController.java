package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.*;

import java.util.ArrayList;

@RestController
public class GetGroupEventMonthlyController extends BaseController {

    @PostMapping("/getGroupEventMonthly")
    public ResponseEntity<GetGroupEventMonthlyResponse> handle(@RequestBody GetGroupEventMonthlyRequest request) {
        logger.info("getGroupEventMonthly: " + request);

        ExceptionUtils.assertPropertyValid(request.getGroupId(), ApiConstant.GROUP_ID);
        ExceptionUtils.assertPropertyValid(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getYear(), ApiConstant.EVENT_MONTHLY_YEAR);
        ExceptionUtils.assertPropertyValid(request.getMonth(), ApiConstant.EVENT_MONTHLY_MONTH);
        if ((request.getMonth() > 12) || (request.getMonth() < 1)) {
            ExceptionUtils.invalidProperty("Month Value");
        }

        // caller
        Account callerAccount = AccountUtils.getAccount(request.getCallerId()
                , ApiConstant.ACCOUNT_ACCOUNT_ID);

        // group
        Group group = GroupUtils.getGroup(request.getGroupId());
        ArrayList<String> memberList = new ArrayList<>(group.getMembers());

        ArrayList<Event> eventList = new ArrayList<Event>();

        for(String accountId: memberList) {
           ArrayList<Event> oneList = (EventListUtils.getMonthlyEventByAccount(
                   accountId,
                   callerAccount.getAccountId(),
                   request.getYear(),
                   request.getMonth()));

           eventList.addAll(oneList);

        }

        return new ResponseEntity<>(new GetGroupEventMonthlyResponse()
                .withEventList(eventList),HttpStatus.OK);
    }
}
