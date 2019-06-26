package com.cosin.shareagenda.access.net;

import com.cosin.shareagenda.config.SystemConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetLoader {
    private String accesssName;
    private RequestBody requestBody;
    private String googleToken;

    public NetLoader(String accessName) {
        this.accesssName = SystemConfig.SHARED_AGENDA_API_URL + accessName;
    }

    public NetLoader(String accessName, RequestBody requestBody) {
        this.accesssName = SystemConfig.SHARED_AGENDA_API_URL + accessName;
        this.requestBody = requestBody;
    }

    public NetLoader(String accessName, RequestBody requestBody, String googleToken) {
        this.accesssName = SystemConfig.SHARED_AGENDA_API_URL + accessName;
        this.requestBody = requestBody;
        this.googleToken = googleToken;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void PostRequest(Callback callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(accesssName)
                .post(requestBody);
        if (googleToken != null) {
            requestBuilder.addHeader("google-token", googleToken);
        }

        Request request = requestBuilder.build();
        OkHttpClient client = new OkHttpClient();

        System.out.println("=========================================");
        System.out.println(request);
        System.out.println(googleToken);
        System.out.println();
        System.out.println("=========================================");
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void GetRequest(Callback callback) {
        Request request = new Request.Builder()
                .url(this.accesssName)
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
