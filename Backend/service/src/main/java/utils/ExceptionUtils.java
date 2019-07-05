package utils;

import constant.ApiConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import types.*;

public class ExceptionUtils {

    public static Logger exceptionLogger = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void assertEventValid(final Event event, boolean eventIdShouldPresent) {
        if (!eventIdShouldPresent && event.getEventId() != null) {
            String errMsg = String.format("Invalid! %s should not present!", ApiConstant.EVENT_EVENT_ID);
            raise(errMsg, HttpStatus.BAD_REQUEST);
        }

        if (eventIdShouldPresent && event.getEventId() == null) {
            String errMsg = String.format("Invalid! %s should present!", ApiConstant.EVENT_EVENT_ID);
            raise(errMsg, HttpStatus.BAD_REQUEST);
        }

        ExceptionUtils.assertPropertyValid(event.getEventname(), ApiConstant.EVENT_EVENT_NAME);
        ExceptionUtils.assertPropertyValid(event.getStarterId(), ApiConstant.EVENT_STARTER_ID);
        ExceptionUtils.assertPropertyValid(event.getType(), ApiConstant.EVENT_TYPE);
        ExceptionUtils.assertPropertyValid(event.getRepeat(), ApiConstant.EVENT_REPEAT);
        ExceptionUtils.assertPropertyValid(event.getState(), ApiConstant.EVENT_STATE);
        ExceptionUtils.assertPropertyValid(event.getStartTime(), ApiConstant.EVENT_START_TIME);
        ExceptionUtils.assertPropertyValid(event.getEndTime(), ApiConstant.EVENT_END_TIME);
    }

    public static void assertPropsEqual(final String prop1, final String prop2,
                                        final String propName1, final String propName2) {
        assertPropertyValid(prop1, propName1);
        assertPropertyValid(prop2, propName2);
        if (!prop1.equals(prop2)) {
            String errMsg = String.format("Invalid! %s and %s are NOT equal!", propName1, propName2);
            raise(errMsg, HttpStatus.BAD_REQUEST);
        }
    }

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
            raise(errMsg, HttpStatus.BAD_REQUEST);
        }
    }

    public static void assertNoFriendship(final Account account, final String friendId) {
        if (FriendQueueUtils.isFriend(account, friendId)) {
            String errMsg = String.format("Invalid! %s and %s are ALREADY friends!", account.getAccountId(), friendId);
            raise(errMsg, HttpStatus.BAD_REQUEST);
        }
    }

    public static void objectNotFound(final String propName) {
        String errMsg = String.format("%s Not Found!", propName);
        raise(errMsg, HttpStatus.NOT_FOUND);
    }

    public static void invalidProperty(final String propName) {
        String errMsg = String.format("Invalid %s!", propName);
        raise(errMsg, HttpStatus.BAD_REQUEST);
    }

    public static void assertNoPendingFriendInvitation(final Account receiver, final String senderId) {
        if (MessageQueueUtils.existFriendRequest(receiver.getMessageQueueId(), senderId)) {
            String errMsg = "Invalid! Existing Pending Friend Request!";
            raise(errMsg, HttpStatus.BAD_REQUEST);
        }
    }

    public static void raise(final String errMsg, final HttpStatus status) {
        exceptionLogger.error(errMsg);
        throw new ResponseStatusException(status, errMsg);
    }
}