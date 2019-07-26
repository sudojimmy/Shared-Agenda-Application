package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.*;
import utils.AccountUtils;
import utils.EventListUtils;
import utils.ExceptionUtils;
import java.util.ArrayList;

@RestController
public class GetEventMonthlyController extends BaseController {

    @PostMapping("/getEventMonthly")
    public ResponseEntity<GetEventMonthlyResponse> handle(@RequestBody GetEventMonthlyRequest request) {
        logger.info("getCalendarEventMonthly: " + request);

        ExceptionUtils.assertPropertyValid(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ExceptionUtils.assertPropertyValid(request.getYear(), ApiConstant.EVENT_MONTHLY_YEAR);
        ExceptionUtils.assertPropertyValid(request.getMonth(), ApiConstant.EVENT_MONTHLY_MONTH);
        if ((request.getMonth() > 12) || (request.getMonth() < 1)) {
            ExceptionUtils.invalidProperty("Month Value");
        }

        // caller account
        Account callerAccount =
                AccountUtils.getAccount(request.getCallerId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        // target account(calendar)
        ArrayList<Event> finalEventList = (EventListUtils.getMonthlyEventByAccount(
                request.getAccountId(),
                callerAccount.getAccountId(),
                request.getYear(),
                request.getMonth()));

        return new ResponseEntity<>(new GetEventMonthlyResponse()
                .withEventList(finalEventList),HttpStatus.OK);
    }
}
