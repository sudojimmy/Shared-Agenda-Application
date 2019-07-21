package com.cosin.shareagenda.api;

import android.net.Uri;

import com.cosin.shareagenda.access.net.NetLoader;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.model.Model;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.RequestBody;
import types.CreateAccountRequest;
import types.CreateEventRequest;
import types.CreateGroupRequest;
import types.CreateRelatedEventsRequest;
import types.DeleteEventRequest;
import types.DeleteFriendRequest;
import types.Event;
import types.ExploreAccountRequest;
import types.ExploreEventRequest;
import types.FriendInvitationRequest;
import types.GetAccountRequest;
import types.GetEventMonthlyRequest;
import types.GetFriendQueueRequest;
import types.GetGroupCalendarEventListRequest;
import types.GetGroupEventMonthlyRequest;
import types.GetGroupListRequest;
import types.GetGroupRequest;
import types.GetMessageQueueRequest;
import types.GetOccupiedTimeRequest;
import types.InviteEventRequest;
import types.JoinEventRequest;
import types.ReplyInvitationRequest;
import types.ReplyStatus;

import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_ACCOUNT;
import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_GROUP;
import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_RELATED_EVENTS;
import static com.cosin.shareagenda.api.ApiEndpoint.DELETE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.DELETE_FRIEND;
import static com.cosin.shareagenda.api.ApiEndpoint.EXPLORE_ACCOUNT;
import static com.cosin.shareagenda.api.ApiEndpoint.EXPLORE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_ACCOUNT;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_EVENT_MESSAGE_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_EVENT_MONTHLY;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_FRIEND_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_GROUP;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_GROUP_CALENDAR_EVENT_LIST;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_GROUP_EVENT_MONTHLY;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_GROUP_LIST;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_MESSAGE_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_NEW_FRIEND_MESSAGE_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_OCCUPIED_TIME;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_REPLY_MESSAGE_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.INVITE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.INVITE_FRIEND;
import static com.cosin.shareagenda.api.ApiEndpoint.JOIN_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.REPLY_FRIEND;
import static com.cosin.shareagenda.api.ApiEndpoint.REPLY_Event;

public class ApiClient extends BaseApiClient {

    public static void getAccount(String accountId, Callback callback) {
        GetAccountRequest getAccountRequest = new GetAccountRequest().withAccountId(accountId);
        makePostRequest(GET_ACCOUNT, gson.toJson(getAccountRequest), callback);
    }

    public static void createAccount(String nickname, String description, Callback callback) {
        Uri photoUrl = Model.model.getGoogleSignInAccount().getPhotoUrl();
        CreateAccountRequest createAccountRequest = new CreateAccountRequest()
                .withAccountId(getAccountId())
                .withNickname(nickname)
                .withDescription(description);
        if (photoUrl != null) {
            createAccountRequest.withProfileImageUrl(photoUrl.toString());
        }
        makePostRequest(CREATE_ACCOUNT, gson.toJson(createAccountRequest), callback);
    }

    public static void exploreAccount(String callerId, String keyword, Callback callback) {
        ExploreAccountRequest request =
                new ExploreAccountRequest()
                        .withCallerId(callerId)
                        .withKeyword(keyword);
        makePostRequest(EXPLORE_ACCOUNT, gson.toJson(request), callback);
    }

    public static void getGroupList(Callback callback) {
        GetGroupListRequest request = new GetGroupListRequest()
                .withAccountId(getAccountId());
        makePostRequest(GET_GROUP_LIST, gson.toJson(request), callback);
    }

    public static void getGroup(String groupId, Callback callback) {
        GetGroupRequest request = new GetGroupRequest()
                .withGroupId(groupId);
        makePostRequest(GET_GROUP, gson.toJson(request), callback);
    }

    public static void createGroup(String name, String description,
                                   ArrayList<String> members, Callback callback) {
        CreateGroupRequest request = new CreateGroupRequest()
                .withName(name)
                .withOwnerId(getAccountId())
                .withDescription(description)
                .withMembers(members);
        makePostRequest(CREATE_GROUP, gson.toJson(request), callback);
    }

    public static void getGroupEventMonthly(String groupId, int month, int year, Callback callback) {
        GetGroupEventMonthlyRequest request = new GetGroupEventMonthlyRequest()
                .withCallerId(getAccountId())
                .withGroupId(groupId)
                .withMonth(month)
                .withYear(year);
        makePostRequest(GET_GROUP_EVENT_MONTHLY, gson.toJson(request), callback);
    }

    public static void getEventMonthly(String targetAccountId, int month, int year, Callback callback) {
        GetEventMonthlyRequest request = new GetEventMonthlyRequest()
                .withCallerId(getAccountId())
                .withAccountId(targetAccountId)
                .withMonth(month)
                .withYear(year);
        makePostRequest(GET_EVENT_MONTHLY, gson.toJson(request), callback);
    }

    public static void getOccupiedTime(String targetAccountId, int month, int year, Callback callback) {
        GetOccupiedTimeRequest request = new GetOccupiedTimeRequest()
                .withAccountId(getAccountId())
                .withTargetAccountId(targetAccountId)
                .withMonth(month)
                .withYear(year);
        makePostRequest(GET_OCCUPIED_TIME, gson.toJson(request), callback);
    }

    public static void createEvent(Event event, Callback callback) {
        CreateEventRequest createEventRequest = new CreateEventRequest()
                .withCallerId(getAccountId())
                .withEvent(event);
        makePostRequest(CREATE_EVENT, gson.toJson(createEventRequest), callback);
    }


    public static void createRelatedEvents(List<Event> events, Callback callback) {
        CreateRelatedEventsRequest createEventRequest = new CreateRelatedEventsRequest()
                .withCallerId(getAccountId())
                .withEvents(events);
        makePostRequest(CREATE_RELATED_EVENTS, gson.toJson(createEventRequest), callback);
    }

    public static void deleteEvent(String eventId, Callback callback) {
        DeleteEventRequest request = new DeleteEventRequest()
                .withAccountId(getAccountId())
                .withEventId(eventId);
        makePostRequest(DELETE_EVENT, gson.toJson(request), callback);
    }

    public static void exploreEvent(String keyword, Callback callback) {
        ExploreEventRequest request = new ExploreEventRequest()
                .withKeyword(keyword)
                .withCallerId(getAccountId());
        makePostRequest(EXPLORE_EVENT, gson.toJson(request), callback);
    }

    public static void joinEvent(String eventId, Callback callback) {
        JoinEventRequest request = new JoinEventRequest()
                .withEventId(eventId)
                .withAccountId(getAccountId());
        makePostRequest(JOIN_EVENT, gson.toJson(request), callback);
    }

    public static void inviteEvent(String friendId, Event event, Callback callback) {
        InviteEventRequest friendInvitationRequest = new InviteEventRequest()
                .withEvent(event)
                .withSenderId(getAccountId())
                .withReceiverId(friendId);
        makePostRequest(INVITE_EVENT, gson.toJson(friendInvitationRequest), callback);
    }

    public static void getMessageQueue(Callback callback) {
        GetMessageQueueRequest request = new GetMessageQueueRequest()
                .withAccountId(getAccountId())
                .withMessageQueueId(Model.model.getUser().getMessageQueueId());
        makePostRequest(GET_MESSAGE_QUEUE, gson.toJson(request), callback);
    }

    public static void getNewFriendMessageQueue(Callback callback) {
        GetMessageQueueRequest request = new GetMessageQueueRequest()
                .withAccountId(getAccountId())
                .withMessageQueueId(Model.model.getUser().getMessageQueueId());
        makePostRequest(GET_NEW_FRIEND_MESSAGE_QUEUE, gson.toJson(request), callback);
    }

    public static void getEventMessageQueue(Callback callback) {
        GetMessageQueueRequest request = new GetMessageQueueRequest()
                .withAccountId(getAccountId())
                .withMessageQueueId(Model.model.getUser().getMessageQueueId());
        makePostRequest(GET_EVENT_MESSAGE_QUEUE, gson.toJson(request), callback);
    }

    public static void getGroupCalendarEventList(String groupId, Callback callback) {
        GetGroupCalendarEventListRequest request = new GetGroupCalendarEventListRequest()
                .withAccountId(getAccountId())
                .withGroupId(groupId);
        makePostRequest(GET_GROUP_CALENDAR_EVENT_LIST, gson.toJson(request), callback);
    }

    public static void replyEvent(String msgId, ReplyStatus status, Callback callback) {
        ReplyInvitationRequest request = new ReplyInvitationRequest()
                .withAccountId(getAccountId())
                .withMessageId(msgId)
//                .withDescription()
                .withStatus(status);
        makePostRequest(REPLY_Event, gson.toJson(request), callback);
    }

    public static void getReplyMessageQueue(Callback callback) {
        GetMessageQueueRequest request = new GetMessageQueueRequest()
                .withAccountId(getAccountId())
                .withMessageQueueId(Model.model.getUser().getMessageQueueId());
        makePostRequest(GET_REPLY_MESSAGE_QUEUE, gson.toJson(request), callback);
    }

    public static void getFriendQueue(Callback callback) {
        GetFriendQueueRequest request = new GetFriendQueueRequest()
                .withAccountId(getAccountId());
        makePostRequest(GET_FRIEND_QUEUE, gson.toJson(request), callback);
    }

    public static void inviteFriend(String friendId, Callback callback) {
        FriendInvitationRequest friendInvitationRequest = new FriendInvitationRequest()
                .withSenderId(getAccountId())
                .withReceiverId(friendId);
        makePostRequest(INVITE_FRIEND, gson.toJson(friendInvitationRequest), callback);
    }

    public static void deleteFriend(String friendId, Callback callback) {
        DeleteFriendRequest deleteFriendRequest = new DeleteFriendRequest()
                .withAccountId(getAccountId())
                .withFriendId(friendId);
        makePostRequest(DELETE_FRIEND, gson.toJson(deleteFriendRequest), callback);
    }

    public static void replyFriend(String msgId, ReplyStatus status, Callback callback) {
        ReplyInvitationRequest request = new ReplyInvitationRequest()
                .withAccountId(getAccountId())
                .withMessageId(msgId)
                .withStatus(status);
        makePostRequest(REPLY_FRIEND, gson.toJson(request), callback);
    }

    private static String getAccountId() {
        return Model.model.getGoogleSignInAccount().getEmail();
    }

    private static String getCalendarId() {
        return Model.model.getUser().getCalendarId();
    }

    private static void makePostRequest(String endpoint, String json, Callback callback) {
        RequestBody body = RequestBody.create(MEDIA_JSON, json);
        new NetLoader(SystemConfig.SHARED_AGENDA_API_URL, endpoint, body, getUserToken())
                .PostRequest(callback);
    }

    private static String getUserToken() {
        return Model.model.getGoogleSignInAccount().getIdToken();
    }
}
