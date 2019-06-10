package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;
import types.GetAccountRequest;
import types.GetAccountResponse;
import utils.AccountUtils;

@RestController
public class GetAccountController extends BaseController {

    @PostMapping("/getAccount")
    public ResponseEntity<GetAccountResponse> handle(@RequestBody GetAccountRequest request) {
        logger.info("GetAccount: " + request);

        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
            logger.error("Invalid AccountId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account account = AccountUtils.getAccount(request.getAccountId());
        if (account  == null) {
            logger.error("Account Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new GetAccountResponse()
            .withAccountId(account .getAccountId())
            .withDescription(account.getDescription())
            .withNickname(account.getNickname()),
            HttpStatus.OK);
    }
}
