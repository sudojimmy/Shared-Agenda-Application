package utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import types.Account;

public class ExceptionUtils {

    public static Logger exceptionLogger = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void assertPropertyValid(final String prop, final String propName) {
        if (prop == null || prop.isEmpty()) { ExceptionUtils.invalidProperty(propName); }
    }

    public static void assertPropertyValid(final Object prop, final String propName) {
        if (prop == null) { ExceptionUtils.invalidProperty(propName); }
    }

    public static void assertDatabaseObjectFound(final Object o, final String propName) {
        if (o == null) { ExceptionUtils.objectNotFound(propName); }
    }

    public static void assertFriendship(final Account account, final String friendId) {
        if (!FriendQueueUtils.isFriend(account, friendId)) {
            String errMsg = String.format("Invalid! %s and %s are NOT friends!", account.getAccountId(), friendId);
            exceptionLogger.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
    }

    public static void assertNoFriendship(final Account account, final String friendId) {
        if (FriendQueueUtils.isFriend(account, friendId)) {
            String errMsg = String.format("Invalid! %s and %s are ALREADY friends!", account.getAccountId(), friendId);
            exceptionLogger.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
    }

    public static void objectNotFound(final String propName) {
        String errMsg = String.format("%s Not Found!", propName);
        exceptionLogger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errMsg);
    }

    public static void invalidProperty(final String propName) {
        String errMsg = String.format("Invalid %s!", propName);
        exceptionLogger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
    }

    public static void assertNoPendingFriendInvitation(final Account receiver, final String senderId) {
        if (MessageQueueUtils.existFriendRequest(receiver.getMessageQueueId(), senderId)) {
            String errMsg = "Invalid! Existing Pending Friend Request!";
            exceptionLogger.error(errMsg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
        }
    }
}