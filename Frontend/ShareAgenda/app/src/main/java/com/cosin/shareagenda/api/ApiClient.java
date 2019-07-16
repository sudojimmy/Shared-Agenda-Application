package com.cosin.shareagenda.api;

import com.cosin.shareagenda.access.net.NetLoader;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.model.Model;

import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.RequestBody;
import types.CreateAccountRequest;
import types.CreateEventRequest;
import types.CreateGroupRequest;
import types.DeleteEventRequest;
import types.Event;
import types.ExploreAccountRequest;
import types.ExploreEventRequest;
import types.FriendInvitationRequest;
import types.GetAccountRequest;
import types.GetEventMonthlyRequest;
import types.GetFriendQueueRequest;
import types.GetGroupEventMonthlyRequest;
import types.GetGroupListRequest;
import types.GetMessageQueueRequest;
import types.JoinEventRequest;
import types.ReplyInvitationRequest;
import types.ReplyStatus;

import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_ACCOUNT;
import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.CREATE_GROUP;
import static com.cosin.shareagenda.api.ApiEndpoint.DELETE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.EXPLORE_ACCOUNT;
import static com.cosin.shareagenda.api.ApiEndpoint.EXPLORE_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_ACCOUNT;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_EVENT_MONTHLY;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_FRIEND_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_GROUP_EVENT_MONTHLY;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_GROUP_LIST;
import static com.cosin.shareagenda.api.ApiEndpoint.GET_MESSAGE_QUEUE;
import static com.cosin.shareagenda.api.ApiEndpoint.INVITE_FRIEND;
import static com.cosin.shareagenda.api.ApiEndpoint.JOIN_EVENT;
import static com.cosin.shareagenda.api.ApiEndpoint.REPLY_FRIEND;

public class ApiClient extends BaseApiClient {

    public static void getAccount(String accountId, Callback callback) {
        GetAccountRequest getAccountRequest = new GetAccountRequest().withAccountId(accountId);
        makePostRequest(GET_ACCOUNT, gson.toJson(getAccountRequest), callback);
    }

    public static void createAccount(String nickname, String description, Callback callback) {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest()
                .withAccountId(getAccountId())
                .withNickname(nickname)
                .withDescription(description);
        makePostRequest(CREATE_ACCOUNT, gson.toJson(createAccountRequest), callback);
    }

    public static void exploreAccount(String keyword, Callback callback) {
        ExploreAccountRequest getAccountRequest = new ExploreAccountRequest().withKeyword(keyword);
        makePostRequest(EXPLORE_ACCOUNT, gson.toJson(getAccountRequest), callback);
    }

    public static void getGroupList(Callback callback) {
        GetGroupListRequest createAccountRequest = new GetGroupListRequest()
                .withAccountId(getAccountId());
        makePostRequest(GET_GROUP_LIST, gson.toJson(createAccountRequest), callback);
    }

    public static void createGroup(String name, String description,
                                   ArrayList<String> members, Callback callback) {
        CreateGroupRequest createAccountRequest = new CreateGroupRequest()
                .withName(name)
                .withOwnerId(getAccountId())
                .withDescription(description)
                .withMembers(members);
        makePostRequest(CREATE_GROUP, gson.toJson(createAccountRequest), callback);
    }

    public static void getGroupEventMonthly(String groupId, int month, int year, Callback callback) {
        GetGroupEventMonthlyRequest createEventRequest = new GetGroupEventMonthlyRequest()
                .withCallerId(getAccountId())
                .withGroupId(groupId)
                .withMonth(month)
                .withYear(year);
        makePostRequest(GET_GROUP_EVENT_MONTHLY, gson.toJson(createEventRequest), callback);
    }

    public static void getEventMonthly(String targetAccountId, int month, int year, Callback callback) {
        GetEventMonthlyRequest createEventRequest = new GetEventMonthlyRequest()
                .withCallerId(getAccountId())
                .withAccountId(targetAccountId)
                .withMonth(month)
                .withYear(year);
        makePostRequest(GET_EVENT_MONTHLY, gson.toJson(createEventRequest), callback);
    }

    public static void createEvent(Event event, Callback callback) {
        CreateEventRequest createEventRequest = new CreateEventRequest()
                .withCallerId(getAccountId())
                .withEvent(event);
        makePostRequest(CREATE_EVENT, gson.toJson(createEventRequest), callback);
    }

    public static void deleteEvent(String eventId, Callback callback) {
        DeleteEventRequest createEventRequest = new DeleteEventRequest()
                .withAccountId(getAccountId())
                .withEventId(eventId);
        makePostRequest(DELETE_EVENT, gson.toJson(createEventRequest), callback);
    }

    public static void exploreEvent(String keyword, Callback callback) {
        ExploreEventRequest getAccountRequest = new ExploreEventRequest()
                .withKeyword(keyword)
                .withCallerId(getAccountId());
        makePostRequest(EXPLORE_EVENT, gson.toJson(getAccountRequest), callback);
    }

    public static void joinEvent(String eventId, Callback callback) {
        JoinEventRequest getAccountRequest = new JoinEventRequest()
                .withEventId(eventId)
                .withAccountId(getAccountId());
        makePostRequest(JOIN_EVENT, gson.toJson(getAccountRequest), callback);
    }

    public static void getMessageQueue(Callback callback) {
        GetMessageQueueRequest createEventRequest = new GetMessageQueueRequest()
                .withAccountId(getAccountId())
                .withMessageQueueId(Model.model.getUser().getMessageQueueId());
        makePostRequest(GET_MESSAGE_QUEUE, gson.toJson(createEventRequest), callback);
    }

    public static void getFriendQueue(Callback callback) {
        GetFriendQueueRequest createEventRequest = new GetFriendQueueRequest()
                .withAccountId(getAccountId());
        makePostRequest(GET_FRIEND_QUEUE, gson.toJson(createEventRequest), callback);
    }

    public static void inviteFriend(String friendId, Callback callback) {
        FriendInvitationRequest createEventRequest = new FriendInvitationRequest()
                .withSenderId(getAccountId())
                .withReceiverId(friendId);
        makePostRequest(INVITE_FRIEND, gson.toJson(createEventRequest), callback);
    }

    public static void replyFriend(String msgId, ReplyStatus status, Callback callback) {
        ReplyInvitationRequest createEventRequest = new ReplyInvitationRequest()
                .withAccountId(getAccountId())
                .withMessageId(msgId)
                .withStatus(status);
        makePostRequest(REPLY_FRIEND, gson.toJson(createEventRequest), callback);
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
