package com.cosin.shareagenda.access.net;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetLoader {
    private String url;
    private RequestBody requestBody;
    private String googleToken;

    public NetLoader(String url) {
        this.url = url;
    }

    public NetLoader(String baseUrl, String accessName) {
        this.url = baseUrl + accessName;
    }

    public NetLoader(String baseUrl, String accessName, RequestBody requestBody) {
        this.url = baseUrl + accessName;
        this.requestBody = requestBody;
    }

    public NetLoader(String baseUrl, String accessName, RequestBody requestBody, String googleToken) {
        this.url = baseUrl + accessName;
        this.requestBody = requestBody;
        this.googleToken = googleToken;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void PostRequest(Callback callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (googleToken != null) {
            requestBuilder.addHeader("google-token", googleToken);
        }

        Request request = requestBuilder.build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void GetRequest(Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
