package com.cosin.shareagenda.model;

import com.cosin.shareagenda.access.net.NetLoader;
import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import types.CreateAccountRequest;
import types.FriendInvitationRequest;
import types.GetAccountRequest;
import types.GetFriendQueueRequest;
import types.GetMessageQueueRequest;
import types.ReplyInvitationRequest;
import types.ReplyStatus;

import static com.cosin.shareagenda.model.ApiEndpoint.CREATE_ACCOUNT;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_ACCOUNT;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_FRIEND_QUEUE;
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

    private static void makePostRequest(String endpoint, String json, Callback callback) {
        RequestBody body = RequestBody.create(MEDOA_JSON, json);
        new NetLoader(endpoint, body, getUserToken())
                .PostRequest(callback);
    }

    private static String getUserToken() {
        return Model.model.getGoogleSignInAccount().getIdToken();
    }
}
