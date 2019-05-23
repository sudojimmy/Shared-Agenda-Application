package controller;

import constant.ApiConstant;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Account;
import types.GetAccountRequest;
import types.GetAccountResponse;

@RestController
public class GetAccountController extends BaseController {

    @PostMapping("/getAccount")
    public ResponseEntity<GetAccountResponse> handle(@RequestBody GetAccountRequest request) {
        logger.info("GetAccount: " + request);

        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
            logger.error("Invalid AccountId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, request.getAccountId());
        Account account = dataStore.findOneInCollection(document, DataStore.COLLECTION_ACCOUNTS);
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
