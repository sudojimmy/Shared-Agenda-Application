package com.cosin.shareagenda.model;

import com.cosin.shareagenda.access.net.NetLoader;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import types.CreateAccountRequest;
import types.CreateGroupRequest;
import types.ExploreAccountRequest;
import types.FriendInvitationRequest;
import types.CreateEventRequest;
import types.Event;
import types.GetAccountRequest;
import types.GetEventMonthlyRequest;
import types.GetFriendQueueRequest;
import types.GetGroupListRequest;
import types.GetMessageQueueRequest;
import types.ReplyInvitationRequest;
import types.ReplyStatus;

import static com.cosin.shareagenda.model.ApiEndpoint.CREATE_ACCOUNT;
import static com.cosin.shareagenda.model.ApiEndpoint.CREATE_EVENT;
import static com.cosin.shareagenda.model.ApiEndpoint.CREATE_GROUP;
import static com.cosin.shareagenda.model.ApiEndpoint.EXPLORE_ACCOUNT;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_ACCOUNT;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_EVENT_MONTHLY;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_FRIEND_QUEUE;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_GROUP_LIST;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_MESSAGE_QUEUE;
import static com.cosin.shareagenda.model.ApiEndpoint.INVITE_FRIEND;
import static com.cosin.shareagenda.model.ApiEndpoint.REPLY_FRIEND;

public class ApiClient {
    public static final MediaType MEDOA_JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

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
        RequestBody body = RequestBody.create(MEDOA_JSON, json);
        new NetLoader(endpoint, body, getUserToken())
                .PostRequest(callback);
    }

    private static String getUserToken() {
        return Model.model.getGoogleSignInAccount().getIdToken();
    }
}
