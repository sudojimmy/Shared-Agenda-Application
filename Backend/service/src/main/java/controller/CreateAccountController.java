package controller;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Account;
import types.CreateAccountRequest;
import types.CreateAccountResponse;
import utils.AccountUtils;
import utils.CalendarUtils;


/* USEFUL DOCUMENTS

    Spring REST server tutorial:
    https://spring.io/guides/gs/rest-service/

    Google Authentication:
    https://developers.google.com/identity/sign-in/android/start

 */
@RestController
public class CreateAccountController extends BaseController {

    @PostMapping("/createAccount")
    public ResponseEntity<CreateAccountResponse> handle(@RequestBody CreateAccountRequest request) {
        logger.info("CreateAccount: " + request);

        // Step I: check parameters TODO move to parent class, need a better solution
        assertPropertyValid(request.getNickname(), ApiConstant.ACCOUNT_NICKNAME);
        assertPropertyValid(request.getAccountId(), ApiConstant.ACCOUNT_ACCOUNT_ID);

        // Step II: check restriction (conflict, or naming rules etc.)
        if (AccountUtils.checkAccountExist(request.getAccountId())) {
            logger.error("AccountId Already Existed!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        String calendarId = CalendarUtils.createCalendarToDatabase().getCalendarId();

        // Step III: write to Database
        Account p = new Account()
            .withNickname(request.getNickname())
            .withAccountId(request.getAccountId())
            .withDescription(request.getDescription())
            .withCalendarId(calendarId);
        dataStore.insertToCollection(p, DataStore.COLLECTION_ACCOUNTS);

        // Step IV: create response object
        return new ResponseEntity<>(new CreateAccountResponse()
            .withAccountId(request.getAccountId())
            .withCalendarId(calendarId),
            HttpStatus.CREATED);
    }
}
