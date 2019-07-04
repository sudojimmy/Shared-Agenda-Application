package controller;

import constant.ApiConstant;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.GetGroupListRequest;
import types.GetGroupListResponse;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.GroupQueueUtils;

@RestController
public class GetGroupListController extends BaseController {

    @PostMapping("/getGroupList")
    public ResponseEntity<GetGroupListResponse> handle(@RequestBody GetGroupListRequest request) {
        logger.info("getGroup: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        Account account = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        ArrayList<String> groupList = GroupQueueUtils.getGroupList(account.getGroupQueueId());

        return new ResponseEntity<>(new GetGroupListResponse().withGroupList(groupList),
            HttpStatus.OK);
    }
}
