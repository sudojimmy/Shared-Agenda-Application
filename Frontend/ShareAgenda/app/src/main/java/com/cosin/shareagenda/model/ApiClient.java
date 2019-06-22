package com.cosin.shareagenda.model;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import types.Account;
import types.GetAccountRequest;

import static com.cosin.shareagenda.config.SystemConfig.SHARED_AGENDA_API_URL;

public class ApiClient {
    public static final MediaType MEDOA_JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();
    static Gson gson = new Gson();

    public static Account getAccount(String accountId) {
        GetAccountRequest getAccountRequest = new GetAccountRequest().withAccountId(accountId);
        String body = post("getAccount", gson.toJson(getAccountRequest));
        return gson.fromJson(body, Account.class);
    }

    public static String post(String endpoint, String json) {
        try {
            return makePostRequest(endpoint, json);
        } catch (IOException e) {
            return null;
        }
    }

    private static String makePostRequest(String endpoint, String json) throws IOException {
        RequestBody body = RequestBody.create(MEDOA_JSON, json);
        Request request = new Request.Builder()
                .url(SHARED_AGENDA_API_URL + endpoint)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
