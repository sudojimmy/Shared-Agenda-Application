package controller;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.conversions.Bson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.UpdateAccountRequest;
import types.UpdateAccountResponse;
import utils.ExceptionUtils;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


@RestController
public class UpdateAccountController extends BaseController {

    @PostMapping("/updateAccount")
    public ResponseEntity<UpdateAccountResponse> handle(@RequestBody UpdateAccountRequest request) {
        logger.info("UpdateAccount: " + request);

        ExceptionUtils.assertPropertyValid(request.getNickname(), ApiConstant.ACCOUNT_NICKNAME);
        ExceptionUtils.assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        Bson filter = Filters.eq(ApiConstant.ACCOUNT_ACCOUNT_ID, request.getAccountId());
        Bson query = combine(
            set(ApiConstant.ACCOUNT_NICKNAME, request.getNickname()),
            set(ApiConstant.ACCOUNT_DESCRIPTION, request.getDescription()));

        if (!dataStore.updateInCollection(filter, query, DataStore.COLLECTION_ACCOUNTS)) {
            logger.error("Account Id Not Found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UpdateAccountResponse().withAccountId(request.getAccountId()),
            HttpStatus.OK);
    }
}
