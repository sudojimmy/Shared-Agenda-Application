package utils;

import constant.ApiConstant;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.DataStore;
import types.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static controller.BaseController.dataStore;

public class ExceptionUtils {

    public static Logger exceptionLogger = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void assertEventValid(final Event event,
                                        boolean eventIdShouldPresent,
                                        final String accountId) {
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
        // assert repeat obj
        Repeat repeat = event.getRepeat();
        ExceptionUtils.assertPropertyValid(repeat.getType(), ApiConstant.REPEAT_TYPE);
        ExceptionUtils.assertPropertyValid(repeat.getStartDate(), ApiConstant.REPEAT_START_DATE);
        ExceptionUtils.assertPropertyValid(repeat.getEndDate(), ApiConstant.REPEAT_END_DATE);
        isValidDateTime(event.getRepeat().getStartDate(), true);
        isValidDateTime(event.getRepeat().getEndDate(), true);
        if (repeat.getType().equals(EventRepeat.ONCE)) {
            DateTimeOrder(repeat.getStartDate(), repeat.getEndDate(), true, false);
        } else {
            DateTimeOrder(repeat.getStartDate(), repeat.getEndDate(), true, true);
        }

        ExceptionUtils.assertPropertyValid(event.getState(), ApiConstant.EVENT_STATE);

        ExceptionUtils.assertPropertyValid(event.getStartTime(), ApiConstant.EVENT_START_TIME);
        isValidDateTime(event.getStartTime(), false);
        ExceptionUtils.assertPropertyValid(event.getEndTime(), ApiConstant.EVENT_END_TIME);
        isValidDateTime(event.getEndTime(), false);
        DateTimeOrder(event.getStartTime(), event.getEndTime(), false, true);

        ExceptionUtils.assertPropertyValid(event.getPermission(), ApiConstant.EVENT_PERMISSION);
        Permission permission = event.getPermission();
        if (permission.getType() == PermissionType.ACCOUNT) {
            assertAccountExist(permission.getPermitToId());
        } else if (permission.getType() == PermissionType.GROUP) {
            assertGroupExist(permission.getPermitToId());
            // event creater must be the group member
            if (!GroupUtils.checkGroupMember(permission.getPermitToId(), accountId)){
                invalidProperty("Event creater, only group members can create group Event");
            }
        }
    }


    // order: prop1 < prop2
    // order == false: prop1 == prop2
    private static void DateTimeOrder(String prop1, String prop2, boolean isDate, boolean order) {
        SimpleDateFormat dateFormat = null;
        if (isDate) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("HH:mm");
        }
        try {
            Date date1 = dateFormat.parse(prop1);
            Date date2 = dateFormat.parse(prop2);

            if (order) {
                if (!date1.before(date2)) {
                    if (isDate) {
                        raise("start date need before end date", HttpStatus.BAD_REQUEST);
                    } else {
                        raise("start time need before end time", HttpStatus.BAD_REQUEST);
                    }
                }
            } else {
                if (!date1.equals(date2)) {
                    if (isDate) {
                        raise("start date need equal end date", HttpStatus.BAD_REQUEST);
                    } else {
                        raise("start time need equal end time", HttpStatus.BAD_REQUEST);
                    }
                }
            }
        } catch(ParseException ex) {
            ex.printStackTrace();
        }
    }

    private static void isValidDateTime(String prop, boolean isDate) {
        SimpleDateFormat dateFormat = null;
        if(isDate){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else{
            dateFormat = new SimpleDateFormat("HH:mm");
        }
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(prop.trim());
        } catch (ParseException pe) {
            if(isDate) {
                raise("Not a valid Date Format", HttpStatus.BAD_REQUEST);
            } else {
                raise("Not a valid Time Format", HttpStatus.BAD_REQUEST);
            }
        }
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

    public static void assertAccountExist(final String accountId) {
        if(!AccountUtils.checkAccountExist(accountId)){
            raise("This accountId does not exist!",
                    HttpStatus.NOT_FOUND);
        }
    }

    public static void assertEventExist(final String eventId) {
        Document document = new Document();
        document.put(ApiConstant.EVENT_EVENT_ID, eventId);
        if(!dataStore.existInCollection(document, DataStore.COLLECTION_EVENTS)){
            raise("This eventId does not exist!",
                    HttpStatus.NOT_FOUND);
        }
    }

    public static void assertGroupExist(final String groupId) {
        Document document = new Document();
        document.put(ApiConstant.GROUP_ID, groupId);
        if(!dataStore.existInCollection(document, DataStore.COLLECTION_GROUPS)){
            raise("This groupId does not exist!",
                    HttpStatus.NOT_FOUND);
        }
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