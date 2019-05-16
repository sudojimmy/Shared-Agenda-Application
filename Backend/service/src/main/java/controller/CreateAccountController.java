package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import types.Account;

/* USEFUL DOCUMENTS

    Spring REST server tutorial:
    https://spring.io/guides/gs/rest-service/

    Google Authentication:
    https://developers.google.com/identity/sign-in/android/start

 */
@RestController
public class CreateAccountController {
    Logger logger = LoggerFactory.getLogger(CreateAccountController.class);
    @PostMapping("/createAccount")
    public Account greeting(@RequestBody Account name) {
        logger.info("Account Creator Controller Called!");
        return new types.Account().withAccountId(name.getAccountId()).withName(name.getName());
    }
}