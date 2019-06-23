package com.cosin.shareagenda.model;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import types.Account;
import types.CreateAccountRequest;
import types.CreateAccountResponse;
import types.GetAccountRequest;

import static com.cosin.shareagenda.config.SystemConfig.SHARED_AGENDA_API_URL;
import static com.cosin.shareagenda.model.ApiEndpoint.CREATE_ACCOUNT;
import static com.cosin.shareagenda.model.ApiEndpoint.GET_ACCOUNT;

public class ApiClient {
    public static final MediaType MEDOA_JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();
    static Gson gson = new Gson();

    public static Account getAccount(String accountId) throws ApiException {
        GetAccountRequest getAccountRequest = new GetAccountRequest().withAccountId(accountId);
        String body = post(GET_ACCOUNT, gson.toJson(getAccountRequest));
        return gson.fromJson(body, Account.class);
    }

    public static CreateAccountResponse createAccount(String nickname, String description) throws ApiException {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest()
                .withAccountId(getAccountId())
                .withNickname(nickname)
                .withDescription(description);
        String body = post(CREATE_ACCOUNT, gson.toJson(createAccountRequest));
        System.out.println(body);
        return gson.fromJson(body, CreateAccountResponse.class);
    }

    private static String getAccountId() {
        return Model.model.getGoogleSignInAccount().getEmail();
    }

    public static String post(String endpoint, String json) throws ApiException {
        try {
            return makePostRequest(endpoint, json);
        } catch (IOException e) {
            return null;
        }
    }

    private static String makePostRequest(String endpoint, String json) throws IOException, ApiException {
        RequestBody body = RequestBody.create(MEDOA_JSON, json);
        Request request = new Request.Builder()
                .url(SHARED_AGENDA_API_URL + endpoint)
                .post(body)
                .addHeader("google-token", getUserToken())
                .build();
        Response response = client.newCall(request).execute();
        int rc = response.code();
        if (!HttpStatusCodes.isSuccess(rc)) {
            throw new ApiException(response.message(), rc);
        }
        return response.body().string();
    }

    private static String getUserToken() {
        return Model.model.getGoogleSignInAccount().getIdToken();
    }
}
