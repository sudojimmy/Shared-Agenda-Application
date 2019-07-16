package com.cosin.shareagenda.api.plugin.uwapi;

import com.cosin.shareagenda.access.net.NetLoader;
import com.cosin.shareagenda.api.BaseApiClient;

import okhttp3.Callback;
import okhttp3.HttpUrl;

public class UWaterlooApiClient extends BaseApiClient {
    public static final String SCHEMA = "https";
    public static final String HOST = "api.uwaterloo.ca";
    public static final String VERSION = "v2";
    public static final String API_KEY = "key";
    public static final String API_KEY_VALUE = "9905ad6eee99dfc86e0f30b6e98e3d46";

    public static final String TERMS = "terms";

    private static final String JSON_POSTFIX = ".json";
    public static final String SCHEDULE = "schedule" + JSON_POSTFIX;
    public static final String IMPORTANT_DATES = "importantdates" + JSON_POSTFIX;

    public static void getTermCourse(String term, String subject, String catalog_number, Callback callback) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(SCHEMA)
                .host(HOST)
                .addPathSegment(VERSION)
                .addPathSegment(TERMS)
                .addPathSegment(term)
                .addPathSegment(subject)
                .addPathSegment(catalog_number)
                .addPathSegment(SCHEDULE)
                .addQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        makePostRequest(httpUrl.toString(), callback);
    }

    public static void getTermInfo(String term, Callback callback) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(SCHEMA)
                .host(HOST)
                .addPathSegment(VERSION)
                .addPathSegment(TERMS)
                .addPathSegment(term)
                .addPathSegment(IMPORTANT_DATES)
                .addQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        makePostRequest(httpUrl.toString(), callback);
    }

    private static void makePostRequest(String url, Callback callback) {
        new NetLoader(url).GetRequest(callback);
    }
}