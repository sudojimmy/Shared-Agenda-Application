package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.GetGroupListRequest;
import types.GetGroupListResponse;
import types.Group;
import utils.AccountUtils;
import utils.ExceptionUtils;
import utils.GroupQueueUtils;
import utils.GroupUtils;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GetGroupListController extends BaseController {

    @PostMapping("/getGroupList")
    public ResponseEntity<GetGroupListResponse> handle(@RequestBody GetGroupListRequest request) {
        logger.info("getGroup: " + request);

        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        Account account = AccountUtils.getAccount(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);
        final List<Group> groupList = GroupQueueUtils
                .getGroupList(account.getGroupQueueId())
                .stream()
                .map(GroupUtils::getGroup)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new GetGroupListResponse().withGroupList(groupList),
            HttpStatus.OK);
    }
}
