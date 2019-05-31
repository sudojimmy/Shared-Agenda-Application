package controller;

import constant.ApiConstant;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import store.DataStore;
import types.Account;
import types.Calendar;
import types.CreateAccountRequest;
import types.CreateAccountResponse;

import java.util.ArrayList;


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
        if (request.getNickname() == null || request.getNickname().isEmpty()) {
            logger.error("Invalid Nickname!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
            logger.error("Invalid AccountId!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Step II: check restriction (conflict, or naming rules etc.)
        Document document = new Document();
        document.put(ApiConstant.ACCOUNT_ACCOUNT_ID, request.getAccountId());
        if (dataStore.existInCollection(document, DataStore.COLLECTION_ACCOUNTS)) {
            logger.error("AccountId Already Existed!");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        String calendarId = new ObjectId().toString();
        Calendar calendar = new Calendar().withCalendarId(calendarId).withEventList(new ArrayList<>());
        dataStore.insertToCollection(calendar, DataStore.COLLECTION_CALENDARS);

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
