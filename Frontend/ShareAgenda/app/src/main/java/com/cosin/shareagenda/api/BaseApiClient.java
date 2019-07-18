package com.cosin.shareagenda.api;

import com.google.gson.Gson;

import okhttp3.MediaType;

public class BaseApiClient {
    public static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    protected static Gson gson = new Gson();
}
